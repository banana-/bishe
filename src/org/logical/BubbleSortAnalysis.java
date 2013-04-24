package org.logical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;

import org.bean.*;


public class BubbleSortAnalysis {
	//函数对象
	private Function function = null;
	//程序逻辑图头节点
	private Step head = null;
	//函数名
	private String name = null;
	//返回类型
	private String returnType = null;
	//基本形参变量列表
	private ArrayList<String> basicParamsList = null;
	//基本形参健值对(这里假设所有的基本类型都是整型数)
	private HashMap<String, Integer> basicParamsMap = null;
	//整型数组名列表
	private ArrayList<String> arrayParamsList = null;
	//整型数组映射表
	private HashMap<String, int[]> arrayParamsMap = null;
	//局部变量列表
	private ArrayList<String> localVariablesList = null;
	//局部基本变量映射表
	private HashMap<String, Integer> localVariablesMap = null;
	//局部数组映射表
	private HashMap<String, int[]> localArraysMap = null;
	
	public BubbleSortAnalysis(StringBuffer code) {
		
		// TODO Auto-generated constructor stub
		CodeAnalysis ca = new CodeAnalysis(code);
		this.function = ca.getFunction();
		this.head = ca.getHead();
		init();
	}
	
	private void init(){
		this.name = function.getName();
		this.returnType = function.getReturnType();
		this.basicParamsList = function.getBasicParamsList();
		this.basicParamsMap = function.getBasicParamsMap();
		this.arrayParamsList = function.getArrayParamsList();
		this.arrayParamsMap = function.getArrayParamsMap();
		this.localVariablesList = function.getLocalVariablesList();
		this.localVariablesMap = function.getLocalVariablesMap();
	}
	
	/**
	 * 输入基本参数值和数组参数值并放到函数对象里
	 * @param arrayParamsListMap
	 */
	public void inputParams(){
		Scanner scann = new Scanner(System.in);
		for(int i = 0; i < basicParamsList.size(); i ++){
			String paramName = this.basicParamsList.get(i);
			System.out.print("请输入变量" + paramName + "的值:");
			this.basicParamsMap.put(paramName, scann.nextInt());
		}
		int n;
		for(int i = 0; i < arrayParamsList.size(); i ++){
			String paramName = this.arrayParamsList.get(i);
			System.out.print("你要为数组" + paramName + "输入多少个元素？:");
			n = scann.nextInt();
			int[] array = new int[n];
			System.out.print("请为该数组输入相应个数的元素：");
			for(int j = 0; j < n; j ++){
				array[j] = scann.nextInt();
			}
			this.arrayParamsMap.put(paramName, array);
		}
		
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
