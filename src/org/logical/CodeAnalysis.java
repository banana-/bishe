/**
*这个类文件用于对代码进行分析，并转化成图形和文字表示
*文件名：org/logical/CodeAnalysis.java
*@author jiaorenyu
*Created On 2013-03-25
*/
package org.logical;
import java.util.*;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.bean.Function;
import org.logical.Lex;

public class CodeAnalysis{
	
	//函数名
	private String functionName = null;
	//返回类型
	private String returnType = null;
	//基本形参列表
	private ArrayList<String> basicParamsList = new ArrayList<String>();
	//数组列表
	private ArrayList<String> arrayParamsList = new ArrayList<String>();
	//局部变量列表
	private ArrayList<String> localVariablesList = new ArrayList<String>();
	//函数对象
	private Function function = null;
	private Step head = null;
	
	public CodeAnalysis(){
		
	}
	public CodeAnalysis(StringBuffer code){
		bubbleSortAnalysis(code);
		this.head = this.getHeadOfBubbleSortGraphics(code);
	}
	
	
	/**
	 * @return the head
	 */
	public Step getHead() {
		return head;
	}
	/**
	 * @param head the head to set
	 */
	public void setHead(Step head) {
		this.head = head;
	}
	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}


	/**
	 * @param function the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}


	/**
	*识别函数名，形参，局部变量
	*@param code 经过预处理的代码
	*@throws 无
	*@return JPanel
	*/
	public void bubbleSortAnalysis(StringBuffer code){
			String word;
			//获取第一个单词，默认为返回类型
			word = Lex.getNextString(code);
			returnType = word;
			//第二个默认为函数名
			functionName = Lex.getNextString(code);
			//处理形式参数
			getFormParameters(code);

			//扫描局部变量
			getLocalParameters(code);
			function = new Function(functionName, returnType);
			function.setArrayParamsList(this.arrayParamsList);
			function.setBasicParamsList(basicParamsList);
			function.setLocalVariablesList(localVariablesList);		
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public Step getHeadOfBubbleSortGraphics(StringBuffer code){
		//有向图的头指针
		Step Head = null;
		Head = new Step(ExpressionType.ASSIGN, "begin");
		//judge用来存储if和for的判断语句，end用来存储for循环的步长，但当遇到if时存储null
		Stack<Step> judge = new Stack<Step>();
		Stack<Step> end = new Stack<Step>();
		
		//brace用来存储大括号，遇到左括号存储进去，遇到右括号删除栈顶元素。key用来存储for,if,while
		Stack<String> brace,key;
		brace = new Stack<String>();
		key = new Stack<String>();
		//外来借口，每一个句子都对应一个或几个前驱，除了头节点。outList就存储这前驱节点引用
		ArrayList<Step> outList = new ArrayList<Step>();
		//开始时Head节点是下一个节点的前驱
		outList.add(Head);
		//word用来存储每一次获取的词，pre存储前一个词
		String word;
	
		//用来存储一个句子，包括一个语句或“}“，”{“
		StringBuffer sentence;
		
		do{
			word = Lex.getNextString(code);
			if(Lex.isKey(word)){
				keyDeal(word, code, judge, end, brace, key, outList);
			}else{
				sentence = new StringBuffer(word);
				if(!(sentence.toString()).equals("}")){
					sentence.append(Lex.getNextSentence(code));
				}
				//当遇到'}'
				if((sentence.toString()).equals("}")){
					
					//并且栈brace为空时结束循环
					if(brace.empty()){
						
						break;
					}else{
						endOfModuleDeal(judge,end,brace,key, 	outList);
					}
				}else{
					
					this.addNode(sentence.toString(), ExpressionType.ASSIGN, outList);
				}
				
			}
		}while(true);
		
		return Head;
	}
	
	/**
	 * 当遇到‘}’时，说明一个模块结束了，或为for，或为while,或为if,else。
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null,while存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	public void endOfModuleDeal(Stack<Step> judge, Stack<Step> end, Stack<String> brace,
					Stack<String> key, ArrayList<Step> outList){
		Step curJudge,curEnd;
		String curKey;
		curJudge = judge.pop();
		curEnd = end.pop();
		curKey = key.pop();
		brace.pop();
		if(curKey.equals("for")){
			endOfForModule(curJudge, curEnd, outList);
		}
		if(curKey.equals("while")){
			endOfWhileModule(curJudge, outList);
		}
		if(curKey.equals("if")){
			//确定是if关键词的结束后，1,要把出口中的节点全部推进栈中，以供到else的结束时使用
			//晴空出口节点列表，并加入if的判断语句
			endOfIfModule(curJudge,judge, end, brace, key, outList);
		}
		if(curKey.equals("else")){
			//确定关键词是else后，1,
			endOfElseModule(judge, end, brace, key, outList);
		}
	}
	
	
	/**
	 * 遇到是If结束的}时的处理方法
	 * @param curJudge if语句的判断节点
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null,while存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	private void endOfIfModule(Step curJudge, Stack<Step> judge, Stack<Step> end, Stack<String> brace,
					Stack<String> key, ArrayList<Step> outList){
		for(int i = 0; i < outList.size(); i ++){
			Step p = outList.get(i);
			end.push(p);
			judge.push(null);
			brace.push(null);
			key.push(null);
			
		}
		outList.clear();
		outList.add(curJudge);
	}
	/**
	 * 遇到是else结束模块的算法
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null,while存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	public void endOfElseModule(Stack<Step> judge, Stack<Step> end, Stack<String> brace,
					Stack<String> key, ArrayList<Step> outList){
		while(key.peek() == null){
			outList.add(end.pop());
			judge.pop();
			brace.pop();
			key.pop();
		}
	}
	
	/**
	 * 当key栈顶为while时的处理方法
	 * @param curJudge while的判断语句
	 * @param outList 出口节点表
	 */
	public void endOfWhileModule(Step curJudge, ArrayList<Step> outList){
		Step p;
		for(int i = 0; i < outList.size(); i ++){
			p = outList.get(i);
			if(p.getType() == ExpressionType.JUDGE){
				if(p.getNextY() == null){
					p.setNextY(curJudge);
				}else{
					p.setNextN(curJudge);
				}
			}else{
				p.setNext(curJudge);
			}
		}
		outList.clear();
		outList.add(curJudge);
	} 
	
	/**
	 * 当前key栈顶为for时的处理方法
	 * @param curJudge for的判断语句，用来形成下一个出口节点
	 * @param curEnd 当前的for步长
	 * @param outList 出口节点表
	 */
	public void endOfForModule(Step curJudge, Step curEnd, ArrayList<Step> outList){
		Step p;
		for(int i = 0; i < outList.size(); i ++){
			p = outList.get(i);
			if(p.getType() == ExpressionType.JUDGE){
				if(p.getNextY() == null){
					p.setNextY(curEnd);
				}else{
					p.setNextN(curEnd);
				}
			}else{
				p.setNext(curEnd);
			}
		}
		outList.clear();
		outList.add(curJudge);
	} 
	
	/**
	 *当遇到关键字时的各种处理方法
	 * @param word 对应关键字
	 * @param code 处理到关键字时的代码
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null,while存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	public void keyDeal(String word, StringBuffer code, Stack<Step> judge, Stack<Step> end, 
			Stack<String> brace, Stack<String> key, ArrayList<Step> outList){
		
		if(word.equals("for")){
			forDeal(code,judge,end,brace,key,outList);		
		}
		if(word.equals("if")){
			ifDeal(code,judge,end,brace,key,outList);
		}
		if(word.equals("while")){
			whileDeal(code,judge,end,brace,key,outList);
		}
		if(word.equals("else")){
			elseDeal(code,judge,end,brace,key,outList);
		}
	}
	
	/**
	 * 对遇到关键词for的处理，处理到左大括号
	 * @param code 处理到for时的代码
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	private void forDeal(StringBuffer code, Stack<Step> judge, Stack<Step> end, 
			Stack<String> brace, Stack<String> key, ArrayList<Step> outList){
		Step p, q;
		Lex.getNextString(code);
		String init = Lex.getNextSentence(code);
		addNode(init,ExpressionType.ASSIGN,outList);
		String judgeSentence = Lex.getNextSentence(code);	
		p = new Step(ExpressionType.JUDGE,judgeSentence);
		addNode(p,outList);
		judge.push(p);
		String endSentence = Lex.getNextSentence(code);
		q = new Step(ExpressionType.ASSIGN, endSentence);
		q.setNext(p);
		end.push(q);
		brace.push(Lex.getNextString(code));
		key.push("for");
	}
	
	
	
	/**
	 * 对遇到关键词while的处理，处理到左大括号
	 * @param code 处理到while时的代码
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null,while存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	private void whileDeal(StringBuffer code, Stack<Step> judge, Stack<Step> end, 
			Stack<String> brace, Stack<String> key, ArrayList<Step> outList){
		Step p;
		Lex.getNextString(code);
		String judgeSentence = Lex.getNextSentence(code);
		p = new Step(ExpressionType.JUDGE, judgeSentence);
		this.addNode(p, outList);
		judge.push(p);
		key.push("while");
		brace.push(Lex.getNextString(code));
		end.push(null);
		
	}
	/**
	 * 对遇到关键词if的处理，处理到左大括号
	 * @param code 处理到if时的代码
	 * @param judge 存储判断节点的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	private void ifDeal(StringBuffer code, Stack<Step> judge, Stack<Step> end, 
			Stack<String> brace, Stack<String> key, ArrayList<Step> outList){
		Step p;
		Lex.getNextString(code);
		String judgeSentence = Lex.getNextSentence(code);
		p = new Step(ExpressionType.JUDGE, judgeSentence);
		this.addNode(p, outList);
		judge.push(p);
		key.push("if");
		brace.push(Lex.getNextString(code));
		end.push(null);
	}
	
	/**
	 * 当处理到else时的处理方法
	 * @param code 处理到else的代码
	 * @param judge 存储判断语句的栈
	 * @param end 存储for循环步长语句的栈,遇到if存储null
	 * @param brace 存储左大括号的栈
	 * @param key 存储关键字的栈
	 * @param outList 存储出口的表
	 */
	private void elseDeal(StringBuffer code, Stack<Step> judge, Stack<Step> end, 
			Stack<String> brace, Stack<String> key, ArrayList<Step> outList){
		judge.push(null);
		brace.push(Lex.getNextString(code));
		end.push( outList.get(outList.size()-1) );
		key.push("else");
	}
	
	/**
	 * 加入节点操作，使outList中的所有节点都指向该节点,并删除表中所有节点，然后把该节点加入
	 * @param expression 该节点的表达式语句
	 * @param type 该节点的类型，或判断或赋值语句
	 * @param outList 出口列表
	 */
	public void addNode(String expression, ExpressionType type,ArrayList<Step> outList){
		Step newNode = new Step(type, expression);
		Step p;
		for(int i = 0; i < outList.size(); i ++){
			p = outList.get(i);
			if(p.getType() == ExpressionType.ASSIGN){
				p.setNext(newNode);
			}else{
				if(p.getNextY() == null){
					p.setNextY(newNode);
				}else{
					p.setNextN(newNode);
				}
			}
		}
		outList.clear();
		outList.add(newNode);
	}
	
	/**
	 * 加入newNode节点，使outList中的所有节点都指向该节点,并删除表中所有节点，然后把该节点加入
	 * @param newNode 新节点
	 * @param outList 出口列表
	 */
	public void addNode(Step newNode, ArrayList<Step> outList){	
		Step p;
		for(int i = 0; i < outList.size(); i ++){
			p = outList.get(i);
			if(p.getType() == ExpressionType.ASSIGN ){
				p.setNext(newNode);
			}else{
				if(p.getNextY() == null){
					p.setNextY(newNode);
				}else{
					p.setNextN(newNode);
				}
			}
		}
		outList.clear();
		outList.add(newNode);
	}

	/**
	*分析局部变量，并存到相应的列表中去,遇到";"结束,这里假定一次声明完所有的变量
	*@param code 预处理过的代码，并且第一个词为函数开始时的左大括号
	*@return void
	*/
	private void getLocalParameters(StringBuffer code){
		String word;
		while( !(word = Lex.getNextString(code) ).equals("int") ){}
		while( !(word = Lex.getNextString(code) ).equals(";")){
			if(!word.equals(",")){
					localVariablesList.add(word);
			}
		}
	}

	/**
	*分析形式参数并存到相应的列表中
	*@param code 预处理过的代码，并且第一个词是函数左括号
	*@return void
	*/
	private void getFormParameters(StringBuffer code){
		String word;
		//分析形式参数
		while( !(word = Lex.getNextString(code)).equals(")") ){
		//默认所有的变量都为整型，如果该参数为关键词，下一个就是变量，
			//然后就判断是数组还是基本变量
			if(isKey(word)){
				//把该变量保存到一个临时变量中
				String tagWord = Lex.getNextString(code);
				//预取下一个词，以判断该变量是数组名还是基本变量
				word = Lex.getNextString(code);
				//如果为")"那么该变量为形式参数中的最后一个且为基本变量
				if(word.equals(")")){
					//把这个基本变量添加到基本变量列表中并跳出循环
					basicParamsList.add(tagWord);
					break;
				}
				//如果为","那么该变量为基本变量且不是最后一个
				if(word.equals(",")){
					//把该变量添加到基本变量列表中，并继续
					basicParamsList.add(tagWord);
					continue;
				}else{
					//如果既不是")"又不是","那么一定是"[",所以该变量为数组名
					arrayParamsList.add(tagWord);
				}
			}	
		}
	}

	/**
	*判断一个单词是不是关键字
	*@param word 要判断的单词
	*@throws 无
	*@return boolean 如果是返回TRUE,否则返回FALSE
	*/
	private boolean isKey(String word){
		if(Lex.isKey(word)) return true;
		return false;
	}
	
	/**
	 * 给定一个表达式字符串，计算它的值, 使用jeval包计算
	 * @param expression
	 * @return
	 */
	public int calculateExpression(StringBuffer expression){
		int result = 0;
		Evaluator eva = new Evaluator();
		try{
			String temp = eva.evaluate(expression.toString());
			result = (int)Double.parseDouble(temp);
		}catch(EvaluationException e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 判断该字符串是否为常数
	 * @param word 要判断的字符串
	 * @return 如果是返回true,否则返回false
	 */
	public boolean isConstant(String word){
		for(int i = 0; i < word.length(); i ++){
			if( !isDigit(word.charAt(i)) ){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断该字符是否为数字
	 * @param ch 要判断的字符
	 * @return 如果是返回true, 否则返回false
	 */
	private boolean isDigit(char ch){
		if(Lex.isNum(ch)) return true;
		return false;
	}
	
	/**
	 * 判断word是否为基本参数变量
	 * @param word 要判断的字符串
	 * @return 如果是基本变量返回true,否则返回false
	 */
	public boolean isBasicParams(String word){
		if(function.getBasicParamsList().indexOf(word) != -1) return true;
		return false;
	}
	
	/**
	 * 判断word是否为数组参数变量
	 * @param word 要判断的字符串
	 * @return 如果是返回true, 否则返回false,那么它一定为局部变量（假设局部变量里没有数组）
	 */
	public boolean isArrayParams(String word){
		if(function.getArrayParamsList().indexOf(word) != -1) return true;
		return false;
	}
	
	public void output(){
		System.out.println("函数返回类型：" + function.getReturnType());
		System.out.println("函数名：" + function.getName());
		System.out.println("基本形参变量：");
		ArrayList<String> basic, array,local;
		basic = function.getBasicParamsList();
		for(int i = 0; i < basic.size(); i ++){
			System.out.print(basic.get(i) + " ");
		}
		System.out.println();
		System.out.println("数组形参变量：");
		array = function.getArrayParamsList();
		for(int i = 0; i < array.size(); i ++){
			System.out.print(array.get(i) + " ");
		}
		System.out.println();
		System.out.println("本地变量：");
		local = function.getLocalVariablesList();
		for(int i = 0; i < local.size(); i ++){
			System.out.print(local.get(i) + " ");
		}
	}
	
	public void setTest(){
		HashMap<String, Integer> basicParamsMap = new HashMap<String, Integer>();
		basicParamsMap.put("n", 10);
		this.function.setBasicParamsMap(basicParamsMap);
		
	}
	
	
	public static void main(String[] args){
		//分析函数测试
		/*String source = "c_file/BubbleSort.c";
		Lex lex = new Lex();
		StringBuffer code = lex.getCode(source);
		CodeAnalysis analysis = new CodeAnalysis(code);
		analysis.setTest();
		analysis.output();
		Function function = analysis.getFunction();
		//function.setReturnType("int");
		//analysis.output();
		*/
		
		CodeAnalysis analysis = new CodeAnalysis();
		System.out.println(analysis.calculateExpression(new StringBuffer("1>2")));
		//计算表达式测试
		/*String expression = "n - 1 ";
		analysis.calculateExpression(expression);
		*/
		
		//判断常数测试
		//System.out.println(analysis.isConstant("12896896487"));
		
		//判断变量测试
		//System.out.println(analysis.isArrayParams("temp"));
		/*function.inputParams();
		function.outputParams();*/
		
	}
}
