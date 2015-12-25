package com.e23.compiler;


public class Symbol {


    public static final int SYMBOL_NUMBER = 35;       // 单词的总数
    public static final int NUMBER_MAX = 1000000;     // PL/0中允许的无符号整数的最大值

    public static final SymbolSet<Integer> STAT_BEG_LIST 		= new SymbolSet<Integer>(4);
    public static final SymbolSet<Integer> DECL_BEG_LIST 		= new SymbolSet<Integer>(3);
    public static final SymbolSet<Integer> FACT_BEG_LIST 		= new SymbolSet<Integer>(3);
    public static final SymbolSet<Integer> PROC_DECL_END_List 	= new SymbolSet<Integer>(6);
    public static final SymbolSet<Integer> PROC_ALL_END_List 	= new SymbolSet<Integer>(7);
    public static final SymbolSet<Integer> NULL_List 			= new SymbolSet<Integer>(0);
    public static final SymbolSet<Integer> EXPRE_List 			= new SymbolSet<Integer>(6);
    public static final SymbolSet<Integer> BLOCK_END_LIST		= new SymbolSet<Integer>(8);

    static
    {
    	//TODO 声明起始符号集合
    	DECL_BEG_LIST.add(Symbol.CONST_SYMBOL);
    	DECL_BEG_LIST.add(Symbol.VAR_SYMBOL);
    	DECL_BEG_LIST.add(Symbol.PROCEDURE_SYMBOL);
    	
    	//TODO 语句起始符号集合
    	STAT_BEG_LIST.add(Symbol.BEGIN_SYMBOL);
    	STAT_BEG_LIST.add(Symbol.CALL_SYMBOL);
    	STAT_BEG_LIST.add(Symbol.IF_SYMBOL);
    	STAT_BEG_LIST.add(Symbol.WHILE_SYMBOL);
    	
    	//TODO 因子起始符号集合
    	FACT_BEG_LIST.add(Symbol.IDENTIFIER);
    	FACT_BEG_LIST.add(Symbol.NUMBER);
    	FACT_BEG_LIST.add(Symbol.LEFT_PARENTHESIS);
    	
    	//TODO 过程说明符号集合
    	PROC_ALL_END_List.addAll(Symbol.STAT_BEG_LIST);
    	PROC_ALL_END_List.add(Symbol.IDENTIFIER);
    	
    	//TODO 过程结尾符号集合
    	PROC_DECL_END_List.addAll(Symbol.PROC_ALL_END_List);
    	PROC_DECL_END_List.add(Symbol.PROCEDURE_SYMBOL);
    	
    	//TODO 比较操作符集合
    	EXPRE_List.add(Symbol.EQUAL);
    	EXPRE_List.add(Symbol.NOT_EQUAL);
		EXPRE_List.add(Symbol.LESS);
    	EXPRE_List.add(Symbol.GREATER);
		EXPRE_List.add(Symbol.LESS_OR_EQUAL);
		EXPRE_List.add(Symbol.GREATER_OR_EQUAL);

		//TODO 分程序结束符号集合
		BLOCK_END_LIST.addAll(Symbol.DECL_BEG_LIST);
		BLOCK_END_LIST.addAll(Symbol.STAT_BEG_LIST);
		BLOCK_END_LIST.add(Symbol.PERIOD);
    }
    
    
    /**------------------------------------
     * 分界符或操作符
     * . , ; = := + - ( ) * / <> < <= > > >=
     * -------------------------------------
     */
    public static final int NUL = 0;                  // NULL
    public static final int PLUS = 1;                 // 加号+
    public static final int MINUS = 2;                // 减号-
    public static final int MULTIPLY = 3;             // 乘号*
    public static final int DIVIDE = 4;               // 除号/
    public static final int EQUAL = 5;                // 等于号=(equal)
    public static final int NOT_EQUAL = 6;            // 不等于<>(not equal)
    public static final int LESS = 7;                 // 小于<(less)
    public static final int GREATER_OR_EQUAL = 8;     // 大于等于>=(greater or equal)
    public static final int GREATER = 9;              // 大于>(greater)
    public static final int LESS_OR_EQUAL = 10;       // 小于等于<=(less or equal)
    public static final int LEFT_PARENTHESIS = 11;    // 左括号(
    public static final int RIGHT_PARENTHESIS = 12;   // 右括号 )
    public static final int COMMA = 13;               // 逗号,
    public static final int SEMICOLON = 14;           // 分号;
    public static final int PERIOD = 15;              // 句号.
    public static final int BECOMES = 16;             // 赋值符号 :=


    /**------------------------------------
     * 标识符 <标识符> ::= <字母>{<字母>|<数字>}
     * -------------------------------------
     */
    public static final int IDENTIFIER = 17;


    /**------------------------------------
     * 常数中只有无符号整数 <无符号整数> ::= <数字>{<数字>}
     * -------------------------------------
     */
    public static final int NUMBER = 18;


    /**------------------------------------
     * 保留字
     * const, var, procedure, odd,
     * if, then, else, while,
     * do, call, begin, end,
     * repeat, until, read, write
     * -------------------------------------
     */
    public static final int CONST_SYMBOL 		= 19;
    public static final int VAR_SYMBOL 			= 20;
    public static final int PROCEDURE_SYMBOL 	= 21;
    public static final int ODD_SYMBOL 			= 22;
    public static final int IF_SYMBOL 			= 23;
    public static final int THEN_SYMBOL 		= 24;
    public static final int ELSE_SYMBOL 		= 25;
    public static final int WHILE_SYMBOL 		= 26;
    public static final int DO_SYMBOL 			= 27;
    public static final int CALL_SYMBOL 		= 28;
    public static final int BEGIN_SYMBOL 		= 29;
    public static final int END_SYMBOL 			= 30;
    public static final int REPEAT_SYMBOL 		= 31;
    public static final int UNTIL_SYMBOL 		= 32;
    public static final int READ_SYMBOL 		= 33;
    public static final int WRITE_SYMBOL 		= 34;

    // 设置保留字名字，按照字母顺序，便于二分查找
    public static final String[] WORD = new String[] {  
            "begin", "call", "const", "do",
            "else", "end", "if", "odd",
            "procedure", "read", "repeat", "then",
            "until", "var", "while", "write"
    };

    // 保留字对应的符号值
    public static final int[] WORD_SYMBOL = new int[] {  
            BEGIN_SYMBOL, CALL_SYMBOL, CONST_SYMBOL, DO_SYMBOL,
            ELSE_SYMBOL, END_SYMBOL, IF_SYMBOL, ODD_SYMBOL,
            PROCEDURE_SYMBOL, READ_SYMBOL, REPEAT_SYMBOL, THEN_SYMBOL,
            UNTIL_SYMBOL, VAR_SYMBOL, WHILE_SYMBOL, WRITE_SYMBOL
    };

    private int symbolType;  // 符号的类型，即前面35种中的一种

    private int number = 0; // 如果符号是无符号整数，则记录其值

    private String name = ""; // 如果符号是标识符，则记录其名字

    public Symbol(int symbolType) {
        this.symbolType = symbolType;
    }


    /**
     * 检查Symbol是否为声明开始的符号
     *	@return Symbol为声明开始为true，反则false
     */
    public boolean isDeclBegSys() {
		if (CONST_SYMBOL == symbolType || VAR_SYMBOL == symbolType || PROCEDURE_SYMBOL == symbolType) {
			return true;
		}else {
			return false;
		}
	}
    
    /*------------------------------
     * Getter
     * -----------------------------
     */

    public String getName() {
        return name;
    }

    public int getSymbolType() {
        return symbolType;
    }

    public int getNumber() {
        return number;
    }



    /*------------------------------
     * Setter
     * -----------------------------
     */

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
		
    	return "symbolType="+symbolType+";\tnumber="+number+"; \tName="+name;
    	
    }
}
