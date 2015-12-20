package compiler.lexer.accidence;

/**
 * <h1>词法单元</h1><br>
 * 
 * Token，作为Lexer阶段的输出
 * 
 * <ol>
 * <li>词法单元：一个</li>
 * <li>模式：词法单元的词素可能具备的<b>形式描述</b></li>
 * <li>词素：即词法单元的一个<b>实例</b>，它是源程序中的一个<b>字符序列</b>，它和某个词法单元的模式相匹配，能被词法器识别</li>
 * <ol>
 * <table>
 * <tr>
 * <th>词法单元</th>
 * <th>非正式描述/模式</th>
 * <th>词素示例</th>
 * </tr>
 * <tr>
 * <td>if</td>
 * <td>字符i和f的组合</td>
 * <td>if</td>
 * </tr>
 * <tr>
 * <td>number</td>
 * <td>任意数字常量</td>
 * <td>123,33,5,0</td>
 * </tr>
 * </table>
 * 
 * <br>
 * 
 * 词法单元分为五种：
 * <ol>
 * <li>keyword</li>
 * <li>identifier</li>
 * <li>constant</li>
 * <li>string-literal</li>
 * <li>punctuator</li>
 * </ol>
 * 
 * @author keepf
 *
 */
public class Token {

	//类型
	
}
