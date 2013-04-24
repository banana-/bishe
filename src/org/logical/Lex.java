//:org/logical/Lex.java
//analysis the source file.
package org.logical;
import java.util.*;
import java.io.*;

/**
*Analysis the word of source code.
*@author jiaorenyu
*/
public class Lex{
	private static String key[] = {
		"auto", "short", "int", "long", "float", "double", "char", "struct", "enum", "union",
		"typedef", "const", "unsigned", "extern", "register", "static", "volatile", "void", "if", "else",
		"switch", "case", "for", "do", "while", "goto", "continue", "break", "default", "sizeof", 
		"return", "main"
	};
	
	private static char[] cmpOperator = {'>', '<', '!'};
	private static char[] operator = {'+', '-', '*', '/'};
	
	public Lex(){}
	
	
	/**
	 * 判断字符是否为比较运算符
	 * @param ch 要判断的字符
	 * @return 如果是比较运算符返回true,否则返回false
	 */
	public static boolean isCmpOperator(char ch){
		for(int i = 0; i < cmpOperator.length; i ++){
			if(cmpOperator[i] == ch) return true;
		}
		return false;
	}
	
	/**
	 * 判断表达式是赋值式还是比较式
	 * @param expression 要判断的表达式
	 * @return 如果是比较式返回true，否则返回false
	 */
	public static boolean containCmpOperator(String expression){
		for(int i = 0; i < cmpOperator.length; i ++){
			if(expression.indexOf(cmpOperator[i]) != -1){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断字符是否为算术运算符
	 * @param ch 要判断的字符
	 * @return 如果是比较运算符返回true,否则返回false
	 */
	public static boolean isArithOperator(char ch){
		for(int i = 0; i < operator.length; i ++){
			if(operator[i] == ch) return true;
		}
		return false;
	}
	
	public static boolean isOperator(char ch){
		if( isCmpOperator(ch) || isArithOperator(ch)){
			return true;
		} 
		return false;
	}
	
	/**
	*Delete the blank character in file named source.
	*@param source
	*@throws IOException
	*@return void
	*/
	private static void preHandle(String source){
		Map<String, Integer> define = new HashMap<String, Integer>();
		BufferedReader in;
		BufferedWriter out;
		ArrayList<String> list = new ArrayList<String>(10);
		try{
		    in = new BufferedReader(new FileReader(new File(source)));
		    out = new BufferedWriter(new FileWriter(source+"~"));
			String str;
				
			while((str = in.readLine()) != null){
				str = deleteBlank(str);
				if(str.equals(" ") || str.length() == 0){
					continue;
				}
				if(str.charAt(0) == '#'){
					if(str.indexOf("define") != -1 || str.indexOf("DEFINE") != -1){
						createDefineTable(define, str, list);
						continue;
					}
				}
				if(str.indexOf("//") != -1 || str.indexOf("/*") != -1 ){
					deletePrompt(in, out, str);
					continue;
				}
				String str1 = deleteBlank(str);			
				StringBuffer temp = new StringBuffer(str1);
				replace(temp, define, list);
				str1 = temp.toString();
				out.write(str1, 0, str1.length() );
				out.flush();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		//for(int i = 0; i < list.size(); i ++){
		//	System.out.println("\nkey:" + list.get(i) + " value:" + define.get(list.get(i)));
		//}
	}
	/**
	*delete the prompt in the source program.
	*@param in the reader
	*@param out the writer
	*@param str 
	*@throws IOException
	*@return void
	*/
	private static void deletePrompt(BufferedReader in, BufferedWriter out, String str) throws IOException {
		if(str.indexOf("//") != -1){
			str = str.substring(0, str.indexOf("//"));
		}
		int index_1 = str.indexOf("/*"), index_2 =  str.indexOf("*/");
		if(index_1 != -1){
			if(index_2 > index_1 + 1){
				str = str.substring(0, index_1) + str.substring(index_2 + 2, str.length());
				str = deleteBlank(str);
				out.write(str, 0, str.length());
				out.flush();
			}else{
				str = in.readLine();
				while(str.indexOf("*/") == -1){
					str = in.readLine();
				}
				str = str.substring(str.indexOf("*/") + 2, str.length());
				str = deleteBlank(str);
				out.write(str, 0, str.length());
				out.flush();
			}
		}
	}
	/**
	*Replace the string which is recorded in the list by the value in the map define
	*@param temp 
	*@param define
	*@param list
	*/
	private static void replace(StringBuffer temp, Map<String, Integer> define, ArrayList<String> list){
		for(int i = 0; i < list.size(); i ++){
			String name = list.get(i);
			int index = temp.indexOf(name);
			if(index != -1){
				if(isAlnumUline(temp.charAt(index - 1)) || isAlnumUline(temp.charAt(index + name.length()))){
					continue;
				}else{
					temp.replace(index, index + name.length(), String.valueOf(define.get(name).intValue()));
				}
			}
		}
	}
	/**
	*
	*
	*/
	private static boolean isAlnumUline(char ch){
		if(ch > 'a' && ch < 'z' || ch > 'A' && ch < 'Z' || ch > '0' && ch < '9' || ch == '_'){
			return 	true;
		}
		return false;
	}
	/**
	*Create the relation between the String and Integer defined by define
	*@param define 常量替换表
	*@param str 对str中的常量替换
	*@param list 存储常量的名称
	*@return void 
	*/
	private static void createDefineTable(Map<String, Integer> define, String str, ArrayList<String> list){
		StringBuffer temp = new StringBuffer(deleteBlank(str));
		String key;
		Integer value;
		int first, second;
		first = temp.indexOf(" ");
		second = temp.indexOf(" ", first + 1);
		key = temp.substring(first + 1, second);
		value = Integer.valueOf(temp.substring(second + 1, temp.length() - 1));
		define.put(key, value);
		list.add(key);
	}
	/**
	*Delete the blank char in the str and return,
	*include "\t", " ".Because there is no "\n", so, 
	*you needn't think about it.
	*@param str
	*/
	private static String deleteBlank(String str){
		StringBuffer str1;
		StringBuffer result = new StringBuffer(50);
		str1 = new StringBuffer(str.trim() + " ");
		int index;
		while((index = str1.indexOf("\t")) != -1){
			str1.replace(index, index + 1, " ");
		}
		int i;
		for( i = 0; i < str1.length(); i ++){
			if(str1.charAt(i) != ' '){
				int blank_index = str1.indexOf(" ", i);
				result.append(str1.substring(i, blank_index) + " ");
				i = blank_index ; 
			} 
		} 
		return result.toString(); 
	}
	
	/**
	*判断一个单词是不是关键字
	*@param word 要判断的单词
	*@throws 无
	*@return boolean 如果是返回TRUE,否则返回FALSE
	*/
	public static boolean isKey(String word){
		for(int i = 0; i < key.length; i ++){
			if(key[i].equals(word)) return true;
		}
		return false;
	}
	
	/**
	*Judge weather ch is a character in alphabet.
	*@param ch
	*/
	private static boolean isAlpha(char ch){
		if( ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' ){
			return true;
		}else{
			return false;
		}
	}
	/**
	*Judge weather ch is a number between 0 and 9.
	*@param ch
	*/
	public static boolean isNum(char ch){
		if( ch >= '0' && ch <= '9' ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断一个字符串是否都是数字 
	 * @param var 要判断的字符串
	 * @return 如果都是数字返回true,否则返回false
	 */
	public static boolean isConstants(String var){
		for(int i = 0; i < var.length(); i ++){
			if( !isNum(var.charAt(i)) ){
				return false;
			}
		}
		return true;
	}
	
	/**
	*Judge weather ch is number or character.
	*@param ch
	*/
	private static boolean isAlNum(char ch){
		if( isAlpha( ch ) || isNum( ch ) ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获得下一个句子，该句子从code的开始处开始，以‘；’或‘）’结束
	 * @param code 以该句子开始的代码
	 * @return 代码开始的句子
	 */
	public static String getNextSentence(StringBuffer code){
		StringBuffer sentence = new StringBuffer();
		String word;
		word = Lex.getNextString(code);
		while( !word.equals(";") && !word.equals(")")){
			sentence.append(word);
			word = Lex.getNextString(code);
		}
		
		return sentence.toString();
	}
	
	/**
	*从预处理后的代码串中获取一个一个的单词。
	*@param code
	*/
	public static String getNextString(StringBuffer code){
		int i = 0;
		char ch;
		StringBuffer current = new StringBuffer();
		ch = code.charAt( i );
		if(isAlpha( ch )){
			for(i = 0; i < code.length() && (isAlNum( ch ) || ch == '_'); ch = code.charAt( ++i )){
				current.append(ch);
			}
			if(ch == ' '){ i ++;}
			code = code.delete(0, i);
		}else{
			if(isNum( ch )){
				for(i = 0; i < code.length() && isNum( ch ); ch = code.charAt( ++i )){
					current.append(ch);
				}	
				if(ch == ' '){ i ++;}
				code = code.delete(0, i);
			}else{

			switch( ch ){
				case '#': 	current.append(ch);		break;
				case '{':   current.append(ch); 	break;
				case '}':   current.append(ch); 	break;
				case '[':   current.append(ch); 	break;
				case ']':   current.append(ch); 	break;
				case '(':   current.append(ch);		break;
				case ')':   current.append(ch);		break;
				case ',':   current.append(ch);		break;
				case ';':   current.append(ch);		break;
				case '=':   current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '='){	current.append(ch); i ++;} break;
				case '>':   current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '='){	current.append(ch); i ++;} break;
				case '<': 	current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '='){	current.append(ch); i ++;} break;
				case '+':   current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '+'){	current.append(ch); i ++;} 
							if( ch == '='){ current.append(ch); i ++;} break;
				case '-':   current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '-'){	current.append(ch); i ++;} 
							if( ch == '='){ current.append(ch); i ++;} break;
				case '*':   current.append(ch); 
							ch = code.charAt( i + 1);
							if( ch == '='){ current.append(ch); i ++;} break;
				case '/':   current.append(ch); 
							ch = code.charAt( i + 1);
							if( ch == '='){ current.append(ch); i ++;} break;
				case '%':   current.append(ch);
							ch = code.charAt( i + 1);
							if( ch == '='|| ch == 'd' || ch == 'f'){ current.append(ch); i ++;} 
							if( ch == 'l'){ current.append(ch); i ++; ch = code.charAt(i + 1); 
											if( ch == 'd' || ch == 'f'){ current.append(ch); i ++;}
										  }break;
				case '&':	current.append(ch);
							ch = code.charAt( i + 1 );
							if( ch == '&' ){ current.append(ch); i ++; } break;
				case '|':	current.append(ch);
							ch = code.charAt( i + 1 );
							if( ch == '|' ){ current.append(ch); i ++; } break;
				case '!':	current.append(ch);
							ch = code.charAt( i + 1 );
							if( ch == '=' ){ current.append(ch); i ++; } break;
				case '"':	current.append(ch);break;
				default:    break;
			}
			i ++;
			ch = code.charAt(i);
			if(ch == ' '){ i ++;}
			code = code.delete(0, i);
		}
		}
		return current.toString();
	}
	/**
	*获取预处理后的代码串。
	*@param source
	*/
	public static StringBuffer getCode(String source){
		StringBuffer result = null;
		try{
			preHandle(source);
			BufferedReader in = new BufferedReader(new FileReader(source+"~"));
			result = new StringBuffer(in.readLine());
		}catch(FileNotFoundException e){
			System.out.println("file not found!");
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args){
		StringBuffer code = Lex.getCode("c_file/BubbleSort.c");
		CodeAnalysis ca = new CodeAnalysis(code);
		System.out.println(code.toString());
		Step head = ca.getHeadOfBubbleSortGraphics(code);
		System.out.println(code.toString());
		System.out.println(head.getExpression());
		Step p;
		p = head;
		for(int i = 0; i < 10; i ++){
			System.out.println(p.getExpression());
			if(p.getType() == ExpressionType.JUDGE){
				System.out.println("judge");
				p = p.getNextY();
			}else{
				
				p = p.getNext();
			}
		}
	}
	
}

