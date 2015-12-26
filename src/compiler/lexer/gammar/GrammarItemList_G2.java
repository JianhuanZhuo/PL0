package compiler.lexer.gammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import compiler.lexer.automata.Symbol;

/**
 * <h1>2型文法的文法项表列类</h1>
 * 
 * @author keepf
 *
 */
public class GrammarItemList_G2 {

	protected List<GrammarItem_G2> grammarItemList;
	protected List<String> VNList;
	private Symbol startSymbol;

	public GrammarItemList_G2() {
		grammarItemList = new ArrayList<>();
	}

	/**
	 * 复制一个目标文法项列表
	 * 
	 * @param g
	 *            目标文法项列表
	 */
	public GrammarItemList_G2(GrammarItemList_G2 g) {
		// 创建等长容量
		grammarItemList = new ArrayList<>((Arrays.asList(new GrammarItem_G2[g.grammarItemList.size()])));
		// COPY
		Collections.copy(grammarItemList, g.grammarItemList);
		startSymbol = g.getStartSymbol();
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
			res += "\t" + g + "\n";
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
	 * 删除指定文法项
	 * 
	 * @param g
	 *            指定文法项
	 * @return 删除是否成功
	 */
	public boolean remove(GrammarItem_G2 g) {
		return grammarItemList.remove(g);
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
							// System.out.println(g.getLeft().getName() + " -> "
							// + s.getName());
						}
					}
				} // end of for(rightList)
			}
		} // end of for(GrammarItemList)
			// System.out.println();
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
		/****************************************************************************************
		 * 
		 * 
		 * 
		 * //TODO 单产生式的剃减，可能会破坏其语义
		 * 
		 * 
		 * 
		 * // TODO 只有单个产生式，将产生式直接替换到每一个应用的子式中(起始符号除外)，并删除该符号 // 将该变量使用于产生式计数
		 * int[] singleProduct = new int[dele.length]; for (GrammarItem_G2 g :
		 * grammarItemList) {
		 * singleProduct[VNNameList.indexOf(g.getLeft().getName())]++; } // for
		 * (String i : VNNameList) { // System.out.println(" ident " + i); // }
		 * // TODO i=1起计数，表示忽略起始符号 for (int i = 1; i < singleProduct.length;
		 * i++) { // System.out.println("count " + i + " " + VNNameList.get(i) +
		 * " = " // + singleProduct[i]); if (1 == singleProduct[i]) { // TODO
		 * 检查是否存在自我循环引用，存在自我引用则不适合替换 String singleName = VNNameList.get(i); //
		 * System.out.println(singleName); GrammarItem_G2 singleG =
		 * findItemWithLeftName(singleName)[0]; if (!singleG.selfRefer()) { //
		 * TODO 单产生式，进行替换 Symbol singleGleft = singleG.getLeft(); List
		 * <Symbol> singleGList = singleG.getRightList(); // TODO 删除产生式本身
		 * grammarItemList.remove(singleG); // TODO 替换其他所有的存在该非终结符的产生式 for
		 * (GrammarItem_G2 g : grammarItemList) { g.replace(singleGleft,
		 * singleGList); } } } }
		 *
		 * end of 单产生式的剃减，可能会破坏其语义
		 *
		 *
		 *************************************************************************/
		VNList = VNNameList;
		return true;
	}

	/**
	 * 以非终结符的符号名为索引，检索出所有的文法项，并作为文法项表列返回，若不存在文法项，则返回null
	 * 
	 * @param VNname
	 *            非终结符的符号名
	 * @return 文法项表或null，若不存在文法项，则返回null
	 */
	public GrammarItemList_G2 getVNList(String VNname) {
		GrammarItemList_G2 res = new GrammarItemList_G2();
		int grammarCount = 0;
		for (GrammarItem_G2 g : grammarItemList) {
			if (g.getLeft().getName().equals(VNname)) {
				res.add(g);
				grammarCount++;
			}
		}
		if (grammarCount == 0) {
			return null;
		} else {
			return res;
		}
	}

	/**
	 * 检索非终结符在文法项的产生式条目数
	 * 
	 * @param VNname
	 *            非终结符
	 * @return 以该符号名为左部的文法项数
	 */
	public int getVNListNum(Symbol VN) {
		int grammarCount = 0;
		for (GrammarItem_G2 g : grammarItemList) {
			if (g.getLeft().equals(VN)) {
				grammarCount++;
			}
		}
		return grammarCount;
	}

	/**
	 * 返回非终结符的符号名集合
	 * 
	 * @return 符号名集合
	 */
	public Set<Symbol> getVNSet() {
		Set<Symbol> VNSet = new HashSet<>();
		for (GrammarItem_G2 g : grammarItemList) {
			VNSet.add(g.getLeft());
		}
		return VNSet;
	}

	/**
	 * 获得该文法的终结符符号集合
	 * 
	 * @return 该文法的终结符符号集合
	 */
	public Set<Symbol> getVTSet() {
		Set<Symbol> VTNameSet = new HashSet<>();
		for (GrammarItem_G2 g : grammarItemList) {
			try {
				for (Symbol s : g.getRightList()) {
					if (!s.getIsVN()) {
						VTNameSet.add(s);
					}
				}
			} catch (NullPointerException e) {
				System.out.println(g);
				throw new RuntimeException("xx");
			}
		}
		return VTNameSet;
	}

	public static void main(String[] args) {

		String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1");
		CFGrammar constantG = new CFGrammar("token");
		constantG.add(CFGFormlize.formalize(gs));
		GrammarItemList_G2 gl = constantG.getItemList();
		gl.refactor();
		System.out.println(gl);
	}
}
