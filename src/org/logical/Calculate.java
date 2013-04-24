package org.logical;

import java.util.*;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.bean.*;


public class Calculate {

	public Calculate() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 计算表达式的值
	 * @param exp 表达式字符串
	 * @return 返回表达式的值
	 */
	public static int getValueOfExpression(String exp, Function fun){
		int result = 0;
		StringBuffer expression = new StringBuffer(exp);
		replaceVar(expression, fun);
		Evaluator eva = new Evaluator();
		try {
			System.out.println(expression);
			String resultStr = eva.evaluate(expression.toString());
			result = Double.valueOf(resultStr).intValue();
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 用相应的值替换表达式expression中的变量
	 * @param expression 要处理的表达式
	 * @param fun 函数对象
	 */
	public static void replaceVar(StringBuffer expression, Function fun){
		int begin = 0; 
		int end = 0;
		char ch;
		HashMap<String, Integer> varMap = fun.getBasicParamsMap();
		HashMap<String, int[]> arrayMap = fun.getArrayParamsMap();
		HashMap<String, Integer> localMap = fun.getLocalVariablesMap();
		System.out.println(expression);
		while(end < expression.length()){
			System.out.println("开始 begin = " +begin + "expression.length()" + expression.length() + 
					"charatend: " + expression.charAt(end));
			for(;end < expression.length() && !Lex.isOperator(expression.charAt(end)); end ++){
				ch = expression.charAt(end);
				System.out.println(ch);
				if(ch == '['){
					end = expression.indexOf("]", begin);
					end ++;
					break;
				}
			}
			
			String var = null;
			var = expression.substring(begin, end);
			System.out.println("begin = " +begin + " end = " + end + " var = " + var);
			int value = getValue(var, varMap, arrayMap, localMap, fun);
			System.out.println("value=" + value);
			String valueStr = String.valueOf(value);
			expression.replace(begin, end, valueStr);
			int distance = var.length() - valueStr.length();
			
			begin = end + 1 - distance;
			if(begin < expression.length()){
				if( expression.charAt(begin) == '='){
					begin ++;
				}
			}
			end = begin;
			System.out.println("begin = end =" + begin);
		}
	}

	
	public static int getValue(String var, HashMap<String, Integer> varMap, HashMap<String, int[]> arrayMap,
																HashMap<String, Integer> localMap, Function fun){
		System.out.println(var);	
		int value;
		int begin, end;
		begin = var.indexOf('[');
		end = var.indexOf(']');
		//如果var是数组变量
		if(begin != -1){
			String name = var.substring(0, begin);
			String indexVar = var.substring(begin + 1, end);
			System.out.println(indexVar);
			int index = getValueOfExpression(indexVar, fun);
			int[] array = arrayMap.get(name);
			value = array[index];
		}else{//如果var不是数组变量
			//var是常量
			if(Lex.isConstants(var)){
				value = Integer.parseInt(var);
			}else{//var是普通变量
				//var是形参
				if(varMap.containsKey(var)){
					value = varMap.get(var);
				}else{//var是局部变量
					value = localMap.get(var);
				}
			}
		}
		return value;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
