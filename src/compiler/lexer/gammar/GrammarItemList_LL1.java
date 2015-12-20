package compiler.lexer.gammar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import compiler.lexer.automata.Symbol;

/**
 * <h1>LL(1)文法类</h1><br>
 * LL1文法是对
 * 
 * @author keepf
 *
 */
public class GrammarItemList_LL1 extends GrammarItemList_G2 {

	public GrammarItemList_LL1() {
	}

	/**
	 * 使用一个GrammarItemList_G2父类初始化，也就是拓展这个父类的能力
	 * 
	 * @param g
	 *            父类
	 */
	public GrammarItemList_LL1(GrammarItemList_G2 g) {
		super(g);
	}

	// 推出空
	Map<Symbol, Boolean> emptySet = new HashMap<Symbol, Boolean>();

	// first集
	Map<Symbol, FirstSet> firstS = new HashMap<Symbol, FirstSet>();

	// 起始符号

	// 推出空计算，其结果放在emptySet变量中
	public void calcEmptySet() {
		// 初始化一个用于计算空集的临时表列
		GrammarItemList_G2 gramTempList = new GrammarItemList_G2(this);

		// 步骤1： 定义推出空的标志一维数组，null表示未定，false表示否，true表示是
		Set<Symbol> vNSet = gramTempList.getVNSet();
		for (Symbol vN : vNSet) {
			emptySet.put(vN, null);
		}

		// 将右部为空的产生式，置为true，并删除所有产生式
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			if (g.rightNull()) {
				emptySet.put(g.getLeft(), true);
				gramTempList.removeVN(g.getLeft().getName());
				// TODO 重新检索
				iterator = gramTempList.iterator();
			}
		}

		// 步骤2：删除所有右部含有终结符的产生式，
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			if (g.hasTermial()) {
				gramTempList.remove(g);
				// TODO 重新检索
				iterator = gramTempList.iterator();
			}
		}

		// 若非终结符的所有产生式都被消失，则设置为false
		for (Symbol vN : vNSet) {
			if (emptySet.get(vN) == null) {
				if (gramTempList.getVNListNum(vN) == 0) {
					emptySet.put(vN, false);
				}
			}
		}

		// 步骤3：扫描产生式的右部
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			List<Symbol> rList = g.getRightList();
			// 产生式右部皆为可推出空的非终结符，则视为可推出空
			boolean isNull = true;
			for (Symbol s : rList) {
				if (emptySet.get(s) == null || emptySet.get(s) == false) {
					isNull = false;
					break;
				}
			}
			// 右部非终结符全部可推出空
			if (isNull) {
				emptySet.put(g.getLeft(), true);
				gramTempList.removeVN(g.getLeft().getName());
				// TODO 重新检索
				// 步骤4：重复步骤3直到
				iterator = gramTempList.iterator();
			}

			// 右部非终结符存在“否”的
			for (Symbol s : rList) {
				if (emptySet.get(s) != null && emptySet.get(s) == false) {
					gramTempList.remove(g);
					if (gramTempList.getVNListNum(g.getLeft()) == 0) {
						emptySet.put(g.getLeft(), false);
					}
					// TODO 重新检索
					// 步骤4：重复步骤3直到
					iterator = gramTempList.iterator();
					break;
				}
			}
		}

	}

	// First集计算
	public void calcFirstSet() {
		// 初始化一个用于计算空集的临时表列
		GrammarItemList_G2 gramTempList = new GrammarItemList_G2(this);
		Set<Symbol> vNSet = gramTempList.getVNSet();
		Symbol nullSym = new Symbol("\\N");
		FirstSet nullFirstSet = new FirstSet(nullSym);
		for (Symbol vN : vNSet) {
			FirstSet f = new FirstSet(vN);
			System.out.println("VN : " + vN);
			if (emptySet.get(vN)) {
				f.add(nullFirstSet);
			}
			firstS.put(vN, f);
		}
		// 添加空符号的first集
		firstS.put(nullSym, nullFirstSet);

		// TODO 对于非终结符，其First集是其本身
		Set<Symbol> NTSet = getNTSet();
		for (Symbol s : NTSet) {
			firstS.put(s, new FirstSet(s));
		}
		// TODO 对于首符号为终结符的，该符号加入First集中
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			g.getRightFirstSymbol();
			// 添加左部的符号直到非空
			Symbol firstRight;
			while ((firstRight = g.takeRightFirstSymbol()) != null) {
				// TODO getIsVN便过滤了为\\N的情况，
				if (firstRight.getIsVN()) {
					firstS.get(g.getLeft()).add(firstS.get(firstRight));
					// 无法推出空，则结束该产生式的扫描
					if (!emptySet.get(firstRight)) {
						break;
					}
				} else {
					firstS.get(g.getLeft()).add(firstS.get(firstRight));
					// 扫描到终结符，则结束该产生是的扫描
					break;
				}
			}
		} // end of for(iterator)

	}
	// Follow集计算
	public void calcFollowSet() {
		
	}

	// select集计算


	// 预测分析表

	// 非LL(1)文法到LL(1)文法的等价变换

	// 提取左公共因子
	//

	public static void main(String[] args) {

		String[] gs = CFGFormlize.loadGrammarsFile("LL1_Test");
		CFGrammar constantG = new CFGrammar("S");
		// String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1");
		// CFGrammar constantG = new CFGrammar("token");
		constantG.add(CFGFormlize.formalize(gs));
		// System.out.println(constantG);
		System.out.println(constantG.getItemList());
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());
		// gl.refactor();
		// System.out.println(gl);
		gl.calcEmptySet();
		System.out.println("Empty set:");
		for (Entry<Symbol, Boolean> e : gl.emptySet.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		gl.calcFirstSet();
		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.firstS.entrySet()) {
			System.out.println(f.getValue());
		}

		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.firstS.entrySet()) {
			System.out.println(f.getKey());
			FirstSet fs = f.getValue();
			for (Symbol s : fs.getFirstSet()) {
				System.out.println("\t" + s);
			}
		}
	}
}
