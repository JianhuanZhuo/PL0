package compiler.lexer.accidence;

/**
 * <h1>词法单元</h1><br>
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

	// 标识符的组成
	/**
	 * <pre>
	 * <identifier>				-> <identifier-nondigit>
	 * <identifier>				-> <identifier><identifier-nondigit>
	 * <identifier>				-> <identifier><digit>
	 * <identifier-nondigit>	-> <nondigit>
	 * <identifier-nondigit>	-> <universal-character-name>
	 * <identifier-nondigit>	-> <other implementation-defined characters>
	 * <nondigit>				-> _[a-zA-Z]
	 * <digit>					-> [0-9]
	 * </pre>
	 */

	/**
	 * <pre>
	 * <constant> 				-> <integer-constant>
	 * <constant> 				-> <floating-constant>
	 * <constant> 				-> <enumeration-constant>
	 * <constant> 				-> <character-constant>
	 * <integer-constant>		-> <decimal-constant><integer-suffix>
	 * <integer-constant>		-> <decimal-constant>
	 * <integer-constant>		-> <octal-constant><integer-suffix>
	 * <integer-constant>		-> <octal-constant>
	 * <integer-constant>		-> <hexadecimal-constant><integer-suffix>
	 * <integer-constant>		-> <hexadecimal-constant>
	 * <decimal-constant>		-> <nonzero-digit>
	 * <decimal-constant>		-> <decimal-constant><digit>
	 * <octal-constant>			-> 0
	 * <octal-constant>			-> <octal-constant><octal-digit>
	 * <hexadecimal-constant>	-> <hexadecimal-prefix><hexadecimal-digit>
	 * <hexadecimal-constant>	-> <hexadecimal-constant><hexadecimal-digit>
	 * <hexadecimal-prefix>		-> 0x
	 * <hexadecimal-prefix>		-> 0X
	 * <nonzero-digit>			-> [1-9]
	 * <octal-digit>			-> [0-7]
	 * <hexadecimal-digit>		-> [0-9][a-f][A-F]
	 * <integer-suffix>			-> <unsigned-suffix><long-suffix>
	 * <integer-suffix>			-> <unsigned-suffix>
	 * <integer-suffix>			-> <unsigned-suffix><long-suffix>
	 * <integer-suffix>			-> <unsigned-suffix><long-long-suffix>
	 * <integer-suffix>			-> <long-suffix>
	 * <integer-suffix>			-> <long-suffix><unsigned-suffix>
	 * <integer-suffix>			-> <long-long-suffix>
	 * <integer-suffix>			-> <long-long-suffix><unsigned-suffix>
	 * <unsigned-suffix>		-> u
	 * <unsigned-suffix>		-> U
	 * <long-suffix>			-> l
	 * <long-suffix>			-> L
	 * <long-long-suffix>		-> ll
	 * <long-long-suffix>		-> LL
	 * 
	 * </pre>
	 */
}
