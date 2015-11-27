package compiler.lexer.automata;

/**
 * <h>自动机模型类</h><br>
 * 得到输入后将根据设定进行动作/输出 <br>
 * 映射规则：(a)->()
 * <hr>
 * <b>不带控制器的自动机</b><br>
 * 作为自动机的控制器，对自动机的启动，步进，快进，暂停等动作进行控制<br>
 * 在不设置FSC的情况下，将由自动机自身实现控制器功能
 * 
 * @author keepf
 *
 */
public abstract class Automata {

	public abstract boolean step(Symbol input);

	public abstract void restart();
}
