package compiler.lexer.gammar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import compiler.lexer.automata.Symbol;

/**
 * <h1>LL(1)文法类</h1><br>
 * LL1文法是对
 * 
 * @author keepf
 *
 */
public class GrammarItemList_LL1 extends GrammarItemList_G2 {

	public static final int END_OF_GRAMMAR = 0x19;

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
	private Map<Symbol, Boolean> emptySet = new HashMap<Symbol, Boolean>();

	// first集
	private Map<Symbol, FirstSet> firstS = new HashMap<Symbol, FirstSet>();

	// Follow集
	private Map<Symbol, FollowSet> followS = new HashMap<Symbol, FollowSet>();

	// Select集
	Map<GrammarItem_G2, SelectSet> selectS = new HashMap<GrammarItem_G2, SelectSet>();

	// 起始符号

	// 推出空计算，其结果放在emptySet变量中
	public void calcEmptySet() {
		// 初始化一个用于计算空集的临时表列
		GrammarItemList_G2 gramTempList = new GrammarItemList_G2(this);

		// 步骤1： 定义推出空的标志一维数组，null表示未定，false表示否，true表示是
		Set<Symbol> vNSet = gramTempList.getVNSet();
		for (Symbol vN : vNSet) {
			getEmptySet().put(vN, null);
		}

		// 将右部为空的产生式，置为true，并删除所有产生式
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			if (g.rightNull()) {
				getEmptySet().put(g.getLeft(), true);
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
			if (getEmptySet().get(vN) == null) {
				if (gramTempList.getVNListNum(vN) == 0) {
					getEmptySet().put(vN, false);
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
				if (getEmptySet().get(s) == null || getEmptySet().get(s) == false) {
					isNull = false;
					break;
				}
			}
			// 右部非终结符全部可推出空
			if (isNull) {
				getEmptySet().put(g.getLeft(), true);
				gramTempList.removeVN(g.getLeft().getName());
				// TODO 重新检索
				// 步骤4：重复步骤3直到
				iterator = gramTempList.iterator();
			}

			// 右部非终结符存在“否”的
			for (Symbol s : rList) {
				if (getEmptySet().get(s) != null && getEmptySet().get(s) == false) {
					gramTempList.remove(g);
					if (gramTempList.getVNListNum(g.getLeft()) == 0) {
						getEmptySet().put(g.getLeft(), false);
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
			// System.out.println("VN : " + vN);
			if (getEmptySet().get(vN) != null && getEmptySet().get(vN)) {
				f.add(nullFirstSet);
			}
			getFirstS().put(vN, f);
		}
		// 添加空符号的first集
		getFirstS().put(nullSym, nullFirstSet);

		// TODO 对于非终结符，其First集是其本身
		Set<Symbol> NTSet = getVTSet();
		for (Symbol s : NTSet) {
			getFirstS().put(s, new FirstSet(s));
		}
		// TODO 对于首符号为终结符的，该符号加入First集中
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			// TODO 这需要用到take右部符号的操作，由于复制只达到文法项集合级别的浅复制，所以需要进行一次深复制。
			g = g.copy();
			// 添加左部的符号直到非空
			Symbol firstRight;
			while ((firstRight = g.takeRightFirstSymbol()) != null) {
				// TODO getIsVN便过滤了为\\N的情况，
				if (firstRight.getIsVN()) {
					getFirstS().get(g.getLeft()).add(getFirstS().get(firstRight));
					// 无法推出空，则结束该产生式的扫描
					Boolean ableToNULL = getEmptySet().get(firstRight);
					try {
						if (ableToNULL == null || !ableToNULL) {
							break;
						}
					} catch (Exception e) {
						throw new RuntimeException("this VN symbol " + firstRight + " can't look up empty!");
					}
				} else {
					FirstSet t = getFirstS().get(g.getLeft());
					FirstSet r = getFirstS().get(firstRight);
					if (r == null) {
						throw new RuntimeException("No good! " + firstRight);
					}
					if (t == null) {
						throw new RuntimeException("No good! " + g.getLeft());
					}
					try {
						t.add(r);
					} catch (Exception e) {
						// TODO: handle exception
						throw new RuntimeException("No good! " + t);
					}
					// 扫描到终结符，则结束该产生是的扫描
					break;
				}
			}
		} // end of for(iterator)
	}

	// Follow集计算
	public void calcFollowSet() {
		// 初始化一个用于计算空集的临时表列
		GrammarItemList_G2 gramTempList = new GrammarItemList_G2(this);
		Symbol start = gramTempList.getStartSymbol();

		Set<Symbol> vNSet = gramTempList.getVNSet();
		for (Symbol s : vNSet) {
			getFollowS().put(s, new FollowSet(s));
		}
		// 添加起始符号到#的指向
		getFollowS().get(start).add(new FirstSet(new Symbol("\\#")));
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();
			// TODO 这需要用到take右部符号的操作，由于复制只达到文法项集合级别的浅复制，所以需要进行一次深复制。
			g = g.copy();

			Symbol t = g.takeRightFirstSymbol();
			Symbol r = g.getRightFirstSymbol();
			Symbol l = g.getLeft();
			while (null != t) {
				if (null == r) {
					// 尾部处理
					if (null != t && t.getIsVN() && !t.equals(new Symbol("\\N"))) {
						// 在末尾的那个非终结符的Follow集中添加该产生式的Follow集
						getFollowS().get(t).add(getFollowS().get(l));
					}
				} else if (t.getIsVN()) {
					// 在前一个符号的Follow集中添加下一个符号First集
					getFollowS().get(t).add(getFirstS().get(r));
					// 可推出空的拼接处理
					if (t.getIsVN() && r.getIsVN() && getEmptySet().get(r) != null && getEmptySet().get(r) == true) {
						// 在前一符号的Follow集中添加下一个符号的Follow集
						getFollowS().get(t).add(getFollowS().get(r));
					}
				}
				t = g.takeRightFirstSymbol();
				r = g.getRightFirstSymbol();
			} // end of while(null != t)
		}
	}

	/**
	 * <h2>select集计算</h2>
	 * 
	 * 单个公式为：
	 */
	public void calcSelect() {
		// 初始化一个用于计算空集的临时表列
		GrammarItemList_G2 gramTempList = new GrammarItemList_G2(this);

		// 针对每个产生式生成一个select集
		for (Iterator<GrammarItem_G2> iterator = gramTempList.iterator(); iterator.hasNext();) {
			GrammarItem_G2 g = (GrammarItem_G2) iterator.next();

			// TODO 添加一个选择集
			SelectSet ss = new SelectSet(g);
			selectS.put(g, ss);

			List<Symbol> rList = g.getRightList();
			int i;
			if (g.rightNull()) {
				// TODO 直接跳出
				i = rList.size();
			} else {
				for (i = 0; i < rList.size(); i++) {
					Symbol s = rList.get(i);
					// TODO 加入其去空的First集
					ss.add(getFirstS().get(s).getFirstSetNoNull());
					// TODO 非空符号跳出
					if (getEmptySet().get(s) == null || getEmptySet().get(s) != true) {
						break;
					}
				}
			}
			// TODO 跳出检查
			if (rList.size() == i) {
				// 添加Follow集
				ss.add(getFollowS().get(g.getLeft()).getFollowSet());
			}
		}
		

		/**
		 * DEBUG用的
		 */
		System.out.println("\nSelect set:");
		for (Entry<GrammarItem_G2, SelectSet> e : selectS.entrySet()) {
			System.out.println(e.getValue());
		}
		System.out.println("check intersertion ：" + calcIntersertion());
	}

	/**
	 * 计算Select集的交集，检查是否存在冲突，<br />
	 * 即检查同一左部的产生式的Select集间是否存在交集
	 * 
	 * @return 无冲突返回true，存在冲突返回false
	 */
	public boolean calcIntersertion() {
		// TODO 用于计算交集的临时set集合，并初始化
		Map<Symbol, Set<Symbol>> intersertion = new HashMap<>();
		Set<Symbol> vNSet = getVNSet();
		for (Symbol vN : vNSet) {
			intersertion.put(vN, new HashSet<Symbol>());
		}

		for (Entry<GrammarItem_G2, SelectSet> e : selectS.entrySet()) {
			Set<Symbol> ss = intersertion.get(e.getKey().getLeft());
			int len = ss.size();
			ss.addAll(e.getValue().getSelectSet());
			if (ss.size() < (len + e.getValue().getSelectSet().size())) {
				// 存在交集，返回false
				System.out.println("this not interserction : " + e.getKey());
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查是否符合LL1文法类型
	 * 
	 * @return 符合返回true，否则返回false
	 */
	public boolean meetLL1() {
		calcEmptySet();
		calcFirstSet();
		calcFollowSet();
		calcSelect();
		return calcIntersertion();
	}

	public Map<GrammarItem_G2, SelectSet> getSelectS() {
		return selectS;
	}

	// 预测分析表

	// 非LL(1)文法到LL(1)文法的等价变换

	// 提取左公共因子
	//

	public static void main(String[] args) {

		String[] gs = CFGFormlize.loadGrammarsFile("Token_LL1_correct");
		CFGrammar constantG = new CFGrammar("token");
		// String[] gs = CFGFormlize.loadGrammarsFile("LL1_Test");
		// CFGrammar constantG = new CFGrammar("S");
		gs = CFGFormlize.formalize(gs);

		for (int i = 0; i < gs.length; i++) {
			System.out.println(gs[i]);
		}
		constantG.add(gs);
		System.out.println("constantG : " + constantG);
		System.out.println(constantG.getItemList());
		GrammarItemList_LL1 gl = new GrammarItemList_LL1(constantG.getItemList());
		// gl.refactor();
		// System.out.println(gl);
		gl.calcEmptySet();
		System.out.println("Empty set:");
		for (Entry<Symbol, Boolean> e : gl.getEmptySet().entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}

		gl.calcFirstSet();
		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.getFirstS().entrySet()) {
			System.out.println(f.getValue());
		}

		System.out.println("\nFirst set:");
		for (Entry<Symbol, FirstSet> f : gl.getFirstS().entrySet()) {
			System.out.println(f.getKey());
			FirstSet fs = f.getValue();
			for (Symbol s : fs.getFirstSet()) {
				System.out.println("\t" + s);
			}
		}

		System.out.println("\nFollow set:");
		gl.calcFollowSet();
		for (Entry<Symbol, FollowSet> e : gl.getFollowS().entrySet()) {
			System.out.println(e.getValue());
		}

		System.out.println("\nSelect set:");
		gl.calcSelect();
		for (Entry<GrammarItem_G2, SelectSet> e : gl.selectS.entrySet()) {
			System.out.println(e.getValue());
		}
		System.out.println("check intersertion ：" + gl.calcIntersertion());
	}

	public Map<Symbol, Boolean> getEmptySet() {
		return emptySet;
	}

	public void setEmptySet(Map<Symbol, Boolean> emptySet) {
		this.emptySet = emptySet;
	}

	public Map<Symbol, FirstSet> getFirstS() {
		return firstS;
	}

	public void setFirstS(Map<Symbol, FirstSet> firstS) {
		this.firstS = firstS;
	}

	public Map<Symbol, FollowSet> getFollowS() {
		return followS;
	}

	public void setFollowS(Map<Symbol, FollowSet> followS) {
		this.followS = followS;
	}
}
