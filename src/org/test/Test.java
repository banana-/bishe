package org.test;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import org.bean.Function;
import org.logical.BubbleSortAnalysis;
import org.logical.ExpressionType;
import org.logical.Lex;
import org.logical.Step;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Test {
	enum Location {NORTH,SOUTH};
	ArrayList<Step> array = new ArrayList<Step>();
	
	public Test() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void test(){

	}
	
	public void output(Function function){
		ArrayList<String> basicParamsList;
		HashMap<String, Integer> basicParamsMap;
		basicParamsList = function.getBasicParamsList();
		basicParamsMap = function.getBasicParamsMap();
		for(int i = 0; i < basicParamsList.size(); i ++){
			Integer value = basicParamsMap.get(basicParamsList.get(i));
			if(value != null){
				System.out.println(value.toString());
			}else{
				System.out.println("null");
			}
		}
	}
	
	public void change(ArrayList<Step> array){
		Step p = array.get(0);
		p.setNext(new Step(ExpressionType.ASSIGN,"hello"));
		
	}
	public void judgeShape(Shape shape){
		String str = shape.getClass().toString();
		if(str.indexOf("Polygon") != -1){
			System.out.println("Polygon");
		}else{
			System.out.println(str);
		}
	}
	public Location getLocation(){
		return Location.NORTH;
	}
	
	public static void replaceVar(StringBuffer expression){
		int begin = 0; 
		int end = 0;
		char ch;
		
		while(end < expression.length()){
			for(;end < expression.length() && !Lex.isOperator(expression.charAt(end)); end ++);
			String var = null;
			var = expression.substring(begin, end);
			int value = 1;
			expression.replace(begin, end, String.valueOf(value));
			begin = end + 1;
			end = begin;
		}
	}
	
	
	/**
	 * @param args
	 * @throws EvaluationException 
	 */
	public static void main(String[] args) throws EvaluationException {
		/*Evaluator eva = new Evaluator();
		String result = eva.evaluate("5 > 1 && 1 > 2");
		int re = Double.valueOf(result).intValue();
		System.out.println(re);*/
		
		/*char[] ch = {'a','b'};
		String str = "fdsvafdls";
		System.out.println(str.indexOf(ch[1]));*/
		/*int[] test ={2,3}, test2={1,2};
		System.out.println(test[0]+test2[0]);*/
		/*Shape shape = new Rectangle();
		Shape shape1 = new Polygon();
		Test test = new Test();
		test.judgeShape(shape);*/
		/*Test test = new Test();
		test.test(test.getLocation());*/
		/*ArrayList<String> array = new ArrayList<String>();
		array.add("a");
		array.add("b");
		System.out.println(array.get(array.size()));*/
		/*
		StringBuffer test = new StringBuffer("}");
		if((test.toString()).equals("}")){
			System.out.println("YES");
		}else{
			System.out.println("NO");
		}*/
		/*	
	    Test test = new Test();
		test.test();
		*/
		
		/*StringBuffer str = new StringBuffer("124");
		System.out.println(str);
		str.substring(0, 3);
		System.out.println(str);*/
		/*StringBuffer str = new StringBuffer("n>=12");
		Test test = new Test();
		test.replaceVar(str);
		System.out.println(str.toString());*/
		/*StringBuffer str = new StringBuffer("hello");
		StringBuffer test =null;
		StringBuffer test1 = null;
		test = str;
		test.append(" nihao");
		System.out.println(str);
		String test2 = "nihao";*/
		/*int[] test = new int[10];
		for(int i = 0; i < 10; i ++){
			test[i] = i + 1;
		}
		int[] test1 = test.clone();
		test1[1] = 0;
		System.out.println(test[1]);*/
	}

}
