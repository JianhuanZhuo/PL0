package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import compiler.lexer.automata.Symbol;

public class GrammarItemList_G2 {

	private List<GrammarItem_G2> grammarItemList;
	private Symbol startSymbol;

	public GrammarItemList_G2() {
		grammarItemList = new ArrayList<>();
	}

	public void add(GrammarItem_G2 e) {
		grammarItemList.add(e);
	}

	public GrammarItem_G2[] toArray() {
		return grammarItemList.toArray(new GrammarItem_G2[grammarItemList.size()]);
	}

	public Iterator<GrammarItem_G2> iterator() {
		return grammarItemList.iterator();
	}

	public String toString() {
		String res = "";
		res += "start with : " + startSymbol.getName() + "\n";
		for (GrammarItem_G2 g : grammarItemList) {
			res += g;
		}
		return res;
	}

	public Symbol getStartSymbol() {
		return startSymbol;
	}

	public void setStartSymbol(Symbol s) {
		startSymbol = s;
	}

	/**
	 * 删除指定字符串为文法左部非终结符名的文法
	 * <hr>
	 * 注意，该删除不会检查是否存在其他非终结符引用该非终结符的
	 * 
	 * @param name
	 *            文法左部的非终结符名
	 * @return 删除计数，返回删除的文法个数，若不存在以参数字符串为左部的文法，则返回0
	 */
	public int removeVN(String name) {
		int removeCount = 0;
		for (int i = 0; i < grammarItemList.size(); i++) {
			if (grammarItemList.get(i).getLeft().getName().equals(name)) {
				grammarItemList.remove(i);
				i--;
				removeCount++;
			}
		}
		return removeCount;
	}

	/**
	 * 使用文法左部的符号名检索文法，得到具有相同符号名的文法集合
	 * 
	 * @param n
	 *            欲检索的符号名
	 * @return 具有相同符号名的文法集合
	 */
	public GrammarItem_G2[] findItemWithLeftName(String n) {
		List<GrammarItem_G2> resItems = new ArrayList<>();
		for (GrammarItem_G2 g : grammarItemList) {
			Symbol target = new Symbol(n, true);
			if (0 == g.getLeft().compareTo(target)) {
				resItems.add(g);
			}
		}
		if (resItems.isEmpty()) {
			return null;
		} else {
			return resItems.toArray(new GrammarItem_G2[resItems.size()]);
		}
	}

	/**
	 * 重构文法项列表
	 */
	public boolean refactor() {
		// TODO 使用Set保存名字
		Set<String> VNNameSet = new HashSet<>();
		for (GrammarItem_G2 g : grammarItemList) {
			VNNameSet.add(g.getLeft().getName());
		}
		List<String> VNNameList = new ArrayList<>(VNNameSet);
		// TODO 调换使得起始符号在列表的首部
		int startSymbolIndex = VNNameList.indexOf(startSymbol.getName());
		VNNameList.set(startSymbolIndex, VNNameList.get(0));
		VNNameList.set(0, startSymbol.getName());

		// TODO 使用二维表表示推导关系
		boolean[][] rela = new boolean[VNNameList.size()][VNNameList.size() + 1];
		// TODO 检查无产生式的非终结符
		for (GrammarItem_G2 g : grammarItemList) {
			// TODO 该文法的左部所在的索引号
			int leftIndex = VNNameList.indexOf(g.getLeft().getName());
			// TODO 推出\N空情况
			if (g.getRightFirstSymbol().getName().equals("\\N")) {
				rela[leftIndex][VNNameList.size()] = true;
			} else {
				List<Symbol> rightList = g.getRightList();
				if (null == rightList) {
					System.err.println("ERROR, this grammar's right is empty!");
					System.err.println(g);
					return false;
				}
				// TODO 找到非终结符之间的推导关系
				for (Symbol s : rightList) {
					if (s.getIsVN()) {
						// TODO 该非终结符是无产生式的，则返回错误
						int NVindex = VNNameList.indexOf(s.getName());
						if (NVindex < 0) {
							System.err.println(g);
							System.err.println("nontermial : " + s.getName());
							System.err.println(
									"ERROR, this grammar's right has a nontermial that can't be index a product!");
							return false;
						} else {
							rela[leftIndex][NVindex] = true;
							System.out.println(g.getLeft().getName() + " -> " + s.getName());
						}
					}
				} // end of for(rightList)
			}
		} // end of for(GrammarItemList)
		System.out.println();
		// TODO 删除无法到达的产生式
		// TODO 首先，先找到哪些产生式的左部非终结符是无法到达的
		// 删除标志位，该标志告诉循环该行的VN已被删除
		boolean[] dele = new boolean[rela.length];
		// 修剪标志位，这用于循环，直到数组没有被修剪(删除)为止
		boolean trimFlag = true;
		while (trimFlag) {
			trimFlag = false;
			// TODO 由1开始检查，即忽略起始符号，起始符号可以不被引用
			for (int i = 1; i < dele.length; i++) {
				if (!dele[i]) {
					// TODO 未删除，进行检查
					boolean emptyFlag = true;
					for (int j = 0; j < dele.length; j++) {
						// TODO i==j 即自我引用，这种情况是不算的
						if (rela[j][i] && (i != j)) {
							emptyFlag = false;
						}
					}
					// TODO 存在没有被引用的非终结符
					if (emptyFlag) {
						// TODO 清空对其他非终结符的引用
						for (int j = 0; j < rela.length; j++) {
							rela[i][j] = false;
						}
						dele[i] = true;
						// TODO 进行了修剪，所以需要置位
						trimFlag = true;
					}
				}
			}
		} // end of while(trimFlag)

		// TODO 删除已找到的非终结符
		for (int i = 0; i < dele.length; i++) {
			if (dele[i]) {
				System.out.println("remove " + VNNameList.get(i));
				removeVN(VNNameList.get(i));
			}
		}
		// TODO 只有单个产生式，将产生式直接替换到每一个应用的子式中(起始符号除外)，并删除该符号
		// 将该变量使用于产生式计数
		int[] singleProduct = new int[dele.length];
		for (GrammarItem_G2 g : grammarItemList) {
			singleProduct[VNNameList.indexOf(g.getLeft().getName())]++;
		}
		for (String i : VNNameList) {
			System.out.println(" ident " + i);
		}
		// TODO i=1起计数，表示忽略起始符号
		for (int i = 1; i < singleProduct.length; i++) {
			System.out.println("count " + i + " " + VNNameList.get(i) + " = " + singleProduct[i]);
			if (1 == singleProduct[i]) {
				// TODO 检查是否存在自我循环引用，存在自我引用则不适合替换
				String singleName = VNNameList.get(i);
				System.out.println(singleName);
				GrammarItem_G2 singleG = findItemWithLeftName(singleName)[0];
				if (!singleG.selfRefer()) {
					// TODO 单产生式，进行替换
					Symbol singleGleft = singleG.getLeft();
					List<Symbol> singleGList = singleG.getRightList();
					// TODO 删除产生式本身
					grammarItemList.remove(singleG);
					// TODO 替换其他所有的存在该非终结符的产生式
					for (GrammarItem_G2 g : grammarItemList) {
						g.replace(singleGleft, singleGList);
					}
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		String[] gs = { "	   <digit> 					-> [0-9]",
				"	   <constant> 				-> <integer-constant>", "	   <conxnt> 				-> <intetant>",
				"	   <intetant> 				-> 13",
				// " <constant> -> <floating-constant>",
				// " <constant> -> <enumeration-constant>",
				// " <constant> -> <character-constant>",
				"	   <integer-constant>		-> <decimal-constant><integer-suffix>",
				"	   <integer-constant>		-> <decimal-constant>",
				"	   <integer-constant>		-> <octal-constant><integer-suffix>",
				"	   <integer-constant>		-> <octal-constant>",
				"	   <integer-constant>		-> <hexadecimal-constant><integer-suffix>",
				"	   <integer-constant>		-> <hexadecimal-constant>",
				"	   <decimal-constant>		-> <nonzero-digit>",
				"	   <decimal-constant>		-> <decimal-constant><digit>", "	   <octal-constant>			-> 0",
				"	   <octal-constant>			-> <octal-constant><octal-digit>",
				"	   <hexadecimal-constant>	-> <hexadecimal-prefix><hexadecimal-digit>",
				"	   <hexadecimal-constant>	-> <hexadecimal-constant><hexadecimal-digit>", "<x>-><k>", "<k>->0",
				"	   <hexadecimal-prefix>		-> (<x>)x", "	   <hexadecimal-prefix>		-> 0X",
				"	   <nonzero-digit>			-> [1-9]", "	   <octal-digit>			-> [0-7]",
				"	   <hexadecimal-digit>		-> [0-9]|[a-f]|[A-F]",
				"	   <integer-suffix>			-> <unsigned-suffix><long-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix><long-suffix>",
				"	   <integer-suffix>			-> <unsigned-suffix><long-long-suffix>",
				"	   <integer-suffix>			-> <long-suffix>",
				"	   <integer-suffix>			-> <long-suffix><unsigned-suffix>",
				"	   <integer-suffix>			-> <long-long-suffix>",
				"	   <integer-suffix>			-> <long-long-suffix><unsigned-suffix>",
				"	   <unsigned-suffix>		-> u", "	   <unsigned-suffix>		-> U",
				"	   <long-suffix>			-> l", "	   <long-suffix>			-> L",
				"	   <long-long-suffix>		-> ll", "	   <long-long-suffix>		-> (LL)" };
		CFGrammar constantG = new CFGrammar("constant");
		constantG.add(CFGFormlize.formalize(gs));
		GrammarItemList_G2 gl = constantG.getItemList();
		gl.refactor();
		System.out.println(gl);
	}
}
