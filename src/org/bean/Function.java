/**
*这是一个函数类，用来存储分析得到的函数对象
*:org/logical/Function.java
*@author jiaorenyu
*Created On 2013-03-25 09:17
*/
package org.bean;
import java.util.*;

/**
*函数对象
*/
public class Function{
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

	
	public Function(){}
	/**
	*初始化对象
	*@param name 函数名
	*@param reurnType 函数返回类型
	*@throws 无
	*/
	public Function(String name, String returnType){
		this.name = name;
		this.returnType = returnType;
		basicParamsList = new ArrayList<String>();
		basicParamsMap = new HashMap<String, Integer>();
		arrayParamsList = new ArrayList<String>();
		arrayParamsMap = new HashMap<String, int[]>();
		localVariablesMap = new HashMap<String, Integer>();
		localArraysMap = new HashMap<String, int[]>();
	}

	

	
	public void outputParams(){
		System.out.println("所有的形参变量及其值：");
		for(int i = 0; i < this.basicParamsList.size(); i ++){
			String paramName = this.basicParamsList.get(i);
			System.out.println(paramName + " = " + this.basicParamsMap.get(paramName));
		}
		
		for(int i = 0; i < this.arrayParamsList.size(); i ++){
			String paramName = this.arrayParamsList.get(i);
			int[] array;
			array = this.arrayParamsMap.get(paramName);
			System.out.print("数组" + paramName + "各元素的值：");
			for(int j = 0; j < array.length; j ++){
				System.out.print(array[j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * @return the localVariablesList
	 */
	public ArrayList<String> getLocalVariablesList() {
		return localVariablesList;
	}
	/**
	 * @param localVariablesList the localVariablesList to set
	 */
	public void setLocalVariablesList(ArrayList<String> localVariablesList) {
		this.localVariablesList = localVariablesList;
	}
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	/**
	 * @param arrayParamsMap the arrayParamsMap to set
	 */
	public void setArrayParamsMap(HashMap<String, int[]> arrayParamsMap) {
		this.arrayParamsMap = arrayParamsMap;
	}
	/**
	*返回函数名
	*@return String 函数名
	*/
	public String getName(){
		return name;
	}

	/**
	*返回函数返回类型
	*@return String 函数返回类型
	*/
	public String getReturnType(){
		return returnType;
	}

	/**
	*返回基本形参列表
	*@return ArrayList<String> 形参列表，ArrayList里存储这形参名
	*/
	public ArrayList<String> getBasicParamsList(){
		return basicParamsList;
	}

	/**
	*重置基本形参列表名
	*@param basicParamsList 形参列表
	*@return void
	*/
	public void setBasicParamsList(ArrayList<String> basicParamsList){
		this.basicParamsList = basicParamsList;
	}

	/**
	*返回基本形参键值映射表
	*@return basicParamsMap 基本形参键值对映射表
	*/
	public HashMap<String, Integer> getBasicParamsMap(){
		return basicParamsMap;
	}

	/**
	*重置基本形参键值对映射表
	*@param HashMap 基本形参键值对映射表
	*@return void
	*/
	public void setBasicParamsMap(HashMap<String, Integer> basicParamsMap){
		this.basicParamsMap = basicParamsMap;
	}

	/**
	*返回形参数组列表
	*@return ArrayList<String> 形参数组名列表
	*/
	public ArrayList<String> getArrayParamsList(){
		return arrayParamsList;
	}

	/**
	*重置形参数组列表
	*@param ArrayList<String> 形参数组列表
	*@return void
	*/
	public void setArrayParamsList(ArrayList<String> arrayParamsList){
		this.arrayParamsList = arrayParamsList;
	}

	/**
	*返回形参数组键值映射表
	*@return arrayParamsMap 形参数组键值映射表
	*/
	public HashMap<String, int[]> getArrayParamsMap(){
		return arrayParamsMap;
	}

	/**
	*重置形参数组键值映射表
	*@param arrayParamsMap 形参数组键值映射表
	*@return void
	*/
	public HashMap<String, int[]> setArrayParamsMap(){
		return arrayParamsMap;
	}

	/**
	*返回局部变量映射表
	*@return HashMap<String, Integer> 局部变量映射表
	*/
	public HashMap<String, Integer> getLocalVariablesMap(){
		return localVariablesMap;
	}

	/**
	*重置局部变量映射表
	*@param localVariablesMap 局部变量映射表
	*@return void
	*/
	public void setLocalVariablesMap(HashMap<String, Integer> localVariablesMap){
		this.localVariablesMap = localVariablesMap;
	}

	/**
	*返回局部数组映射表
	*@return HashMap<String, int[]> 局部数组映射表
	*/
	public HashMap<String, int[]> getLocalArraysMap(){
		return localArraysMap;
	}

	/**
	*重置局部数组映射表
	*@param localArraysMap 局部数组映射表
	*@return void
	*/
	public void setLocalArraysMap(HashMap<String, int[]> localArraysMap){
		this.localArraysMap = localArraysMap;
	}

	/**
	*
	*/
	public static void main(String[] args){
	
		
	}

}
