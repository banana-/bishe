/**
 * 
 */
package org.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

import org.logical.*;
import org.swingconsole.*;
import org.bean.*;
/**
 * @author renyu
 *
 */
public class VariableShow extends JFrame {

	//分别存放数组参数，基本参数，局部变量的面板，和存放这些面板的面板center
	JPanel arrayParams,basicParams,localVariables, center;
	//在最下面显示提示信息
	JTextField prompt;
	//存储基本参数的<变量名，对应组件>映射，以便实时更新
	HashMap<String, JButton> basicParamsJButtonMap = null;
	//存储数组参数的<数组名，对应数组的组件列表>映射，以便实时更新
	HashMap<String, ArrayList<JButton>> arrayParamsListMap = null;
	
	//存储上一次测试数据
	HashMap<String, Integer> preBasicParamsMap = null;
	HashMap<String, int[]> preArrayParamsMap = null;
	
	//存储局部变量的<变量名，对应组件>映射，以便实时更新
	HashMap<String, JButton> localVariablesJButtonMap = null;
	//对BubbleSort.c文件分析的策略
	BubbleSortAnalysis bsa = null;
	String source;
	//程序逻辑图的头节点
	Step head = null;
	Step p = null;
	//存储上一次变化的按钮引用
	JButton pre = new JButton();
	//函数对象
	Function function = null;
	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public VariableShow(String title) throws HeadlessException {
		super(title);
		source = "c_file/add.c";
		init(source);
		//设置布局
		this.setLayout(new BorderLayout());
		//获取控制按钮组并添加到框架的上部
		this.add(this.getNorthPanel(), BorderLayout.NORTH);
		//获取提示框并添加到框架下部
		this.add(this.getSouthPanel(), BorderLayout.SOUTH);
		//获取变量信息，并把相应组件放入中心面板
		this.add(this.getCenterPanel(function), BorderLayout.CENTER);
	}
	
	public void init(String source){
		//获取处理过的源代码
		StringBuffer code = Lex.getCode(source);
		//构建策略对象
		bsa = new BubbleSortAnalysis(code);
		//从策略中获取函数对象，每个策略包括它分析过后产生的函数对象和程序流图头节点
		function = bsa.getFunction();
		//从相应策略中获取程序流图头节点
		this.head = bsa.getHead();
		p = head.getNext();
		//初始化普通形参对应的按钮映射
		this.basicParamsJButtonMap = new HashMap<String, JButton>();
		//初始化数组形参对应的列表映射，列表中存放着相应的数组中每个元素对应的按钮，按钮上显示数组相应索引处的值
		this.arrayParamsListMap = new HashMap<String, ArrayList<JButton>>();
		//初始化局部变量对应的列表映射
		this.localVariablesJButtonMap = new HashMap<String, JButton>();
		
		this.preBasicParamsMap = new HashMap<String, Integer>();
		this.preArrayParamsMap = new HashMap<String, int[]>();
	}
	
	public void initPanel(){
		p = head.getNext();
		center.removeAll();
		center.invalidate();
		
		basicParams = new JPanel();
		setBasicParams(basicParams,function);
		center.add(basicParams);
		
		arrayParams = new JPanel();
		setArrayParams(arrayParams,function);
		center.add(arrayParams);
		
		localVariables = new JPanel();
		setLocalVariables(localVariables, function);
		center.add(localVariables);
		
		center.validate();
	}
	/**
	 * 程序执行下一步
	 * @param p 函数逻辑图要执行的节点
	 */
	public Step execute(Step p){
		Step next = null;
		ExpressionType type = p.getType();
		String expression = p.getExpression();
		System.out.println("expression:" + expression);
		if(type == ExpressionType.ASSIGN){
			String var = expression.substring(0,expression.indexOf('='));
			String exp = expression.substring(expression.indexOf('=') + 1, expression.length());
			int val = Calculate.getValueOfExpression(exp, this.function);
			setValue(var,val,this.function);
			next = p.getNext();
		}else{
			int result = Calculate.getValueOfExpression(expression, this.function);
			if(result == 1){
				next = p.getNextY();
				this.prompt.setText(expression + "? YES");
			}else{
				next = p.getNextN();
				this.prompt.setText(expression + "? NO");
			}
		}
		return next;
	}
	

	/**
	 * 在function对象里更新变量var的值，var或为普通变量， 或为数组变量
	 * @param var 变量名
	 * @param val 变量值
	 * @param fun 函数对象
	 */
	public void setValue(String var, int val, Function fun){
		HashMap<String, Integer> basicMap = fun.getBasicParamsMap();
		HashMap<String, Integer> localMap = fun.getLocalVariablesMap();
		HashMap<String, int[]> arrayMap = fun.getArrayParamsMap();
		JButton changeButton;
		String show;
		//如果var为数组变量
		if(var.indexOf('[') != -1){
			int first,second;
			first = var.indexOf('[');
			second = var.indexOf(']');
			String name = var.substring(0, first);
			int index = Calculate.getValueOfExpression(var.substring(first + 1, second), fun);
			int[] array = arrayMap.get(name);
			array[index] = val;
			show = name + "["+index+"]"+" = " + val;
			changeButton = this.arrayParamsListMap.get(name).get(index);
		}else{//如果var为普通变量
			if(basicMap.containsKey(var)){
				basicMap.put(var,val);
				changeButton = this.basicParamsJButtonMap.get(var);
			}else{
				localMap.put(var, val);
				changeButton = this.localVariablesJButtonMap.get(var);
			}
			show = var + " = " + val;
		}	
		changePrompt(changeButton, show);
		this.prompt.setText(var+"的值变为："+val);
		
	}
	
	public void changePrompt(JButton changeButton, String show){
		changeButton.setText(show);
		pre.setBackground(changeButton.getBackground());
		changeButton.setBackground(Color.RED);
		pre = changeButton;	
	}
	
	/**
	 * 设置控制按钮并加入监听器
	 * @return 最上面的控制面板
	 */
	public JPanel getNorthPanel() {
		//监视程序执行过程的操作控制按钮，分别是：下一步，重新开始，打开文件
		JButton nextStep,reStart,openFile,showGraphic;
		
		JPanel panel = new JPanel();		
		nextStep = new JButton("下一步");
		reStart = new JButton("重新开始");	
		openFile = new JButton("打开新文件");
		showGraphic = new JButton("显示程序流程图");
		
		panel.setLayout(new FlowLayout());
		
		nextStep.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(p == null){
					JOptionPane.showMessageDialog(null,"this is an end","title",JOptionPane.OK_OPTION);
				}else{
					p = execute(p);
				}
			}
		});
		
		reStart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				reStartInit(function);
			}
		});
		
		openFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser choose = new JFileChooser();
				int rVal = choose.showOpenDialog(VariableShow.this);
				if(rVal == JFileChooser.APPROVE_OPTION){
					source = choose.getSelectedFile().getAbsolutePath();
					String type = source.substring(source.lastIndexOf('.'));
					if(type.equals(".c")){
						init(source);
					}else{
						JOptionPane.showMessageDialog(null, "please open the c file", "title", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		JButton inputParams = new JButton("输入参数值：");
		inputParams.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				bsa.inputParams();
				updateButton(function);
				initPanel();
				saveTestData(function);
			}
		});
				
		showGraphic.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SwingConsole.run(new ShowGraphic(source), 1000, 700, JFrame.DISPOSE_ON_CLOSE);
			}
		});
		panel.add(openFile);
		panel.add(showGraphic);
		panel.add(inputParams);
		panel.add(reStart);
		panel.add(nextStep);
		
		TitledBorder title = new TitledBorder("控制按钮");
		panel.setBorder(title);
		
		return panel;
	}
	
	/**
	 * 保存这次的测试数据，以免再次输入(深克隆)
	 * @param fun 函数对象
	 */
	public void saveTestData(Function fun){
		ArrayList<String> tempBasicParamsList = fun.getBasicParamsList();
		ArrayList<String> tempArrayParamsList = fun.getArrayParamsList();
		HashMap<String, Integer> tempBasicParamsMap = fun.getBasicParamsMap();
		HashMap<String, int[]> tempArrayParamsMap = fun.getArrayParamsMap();
		for(int i = 0; i < tempBasicParamsList.size(); i ++){
			String name = tempBasicParamsList.get(i);
			Integer value = tempBasicParamsMap.get(name);
			this.preBasicParamsMap.put(name, new Integer(value));
		}
		
		for(int i = 0; i < tempArrayParamsList.size(); i ++){
			String name = tempArrayParamsList.get(i);
			int[] values = tempArrayParamsMap.get(name);
			int[] temp = values.clone();
			this.preArrayParamsMap.put(name, temp);
		}
	}
	
	
	public void reStartInit(Function fun){
		getTestData(fun);
		updateText(fun);
		initPanel();
	}
	
	/**
	 * 获取这次测测试数据，初始化数据
	 * @param fun 函数对象
	 */
	public void getTestData(Function fun){
		ArrayList<String> tempBasicParamsList = fun.getBasicParamsList();
		ArrayList<String> tempArrayParamsList = fun.getArrayParamsList();
		HashMap<String, Integer> tempBasicParamsMap = fun.getBasicParamsMap();
		HashMap<String, int[]> tempArrayParamsMap = fun.getArrayParamsMap();
		for(int i = 0; i < tempBasicParamsList.size(); i ++){
			String name = tempBasicParamsList.get(i);
			Integer value = this.preBasicParamsMap.get(name);
			tempBasicParamsMap.put(name, new Integer(value));
		}
		
		for(int i = 0; i < tempArrayParamsList.size(); i ++){
			String name = tempArrayParamsList.get(i);
			int[] values = this.preArrayParamsMap.get(name);
			tempArrayParamsMap.put(name, values.clone());
		}
		
	}
	
	/**
	 * 设置中心的面板，包括基本参数和数组参数的显示
	 * @param function 函数对象
	 * @return 返回相应的面板
	 */
	public JPanel getCenterPanel(Function function){
		center = new JPanel();
		center.setLayout(new GridLayout(3,1));
		
		return center;
	}
	
	/**
	 * 为基本变量设置初始值
	 * @param panel 为该面板初试化
	 * @param fun 函数对象
	 */
	private void setBasicParams(JPanel panel, Function fun){
		ArrayList<String> basicParams =  fun.getBasicParamsList();	
		for(int i = 0; i < basicParams.size(); i ++){
			String name = basicParams.get(i);
			panel.add(this.basicParamsJButtonMap.get(name));
		}
		TitledBorder title = new TitledBorder("基本形参：");
		panel.setBorder(title);
	}
	
	/**
	 * 为数组参数面板初始化
	 * @param arrayParams 数组参数面板
	 * @param fun 函数对象
	 */
	private void setArrayParams(JPanel arrayParams, Function fun){
		ArrayList<String> arrayParamsList = fun.getArrayParamsList();
		int size = arrayParamsList.size();
		arrayParams.setLayout(new GridLayout(size,1));
		for(int i = 0; i < size; i ++){
			JPanel panel = new JPanel();
			String arrayName = arrayParamsList.get(i);
			ArrayList<JButton> buttons = this.arrayParamsListMap.get(arrayName);
			for(int j = 0; j < buttons.size(); j ++){
				panel.add(buttons.get(j));
			}
			TitledBorder title = new TitledBorder(arrayName);
			panel.setBorder(title);
			arrayParams.add(panel);
		}
		TitledBorder title = new TitledBorder("数组形参：");
		arrayParams.setBorder(title);
	}
	
	/**
	 * 初始化局部变量面板
	 * @param local 局部变量面板
	 * @param fun 函数对象
	 */
	private void setLocalVariables(JPanel local, Function fun){
		ArrayList<String> localNameList = fun.getLocalVariablesList();
		for(int i = 0; i < localNameList.size(); i ++){
			String name = localNameList.get(i);
			local.add(this.localVariablesJButtonMap.get(name));
		}
		TitledBorder title = new TitledBorder("局部变量：");
		local.setBorder(title);
	}
	
	/**
	 * 更新面板显示的普通形参和数组形参（基于函数对象）
	 * @param function 函数对象
	 */
	public void updateText(Function function){
		ArrayList<String> basicParamsList = null;
		HashMap<String, Integer> basicParamsMap = null;
		//基本形参列表
		basicParamsList = function.getBasicParamsList();
		//基本形参映射
		basicParamsMap = function.getBasicParamsMap();
		
		//更新基本形参按钮映射
		for(int i = 0; i < basicParamsList.size(); i ++){
			String name = basicParamsList.get(i);
			Integer value = basicParamsMap.get(name);
			this.basicParamsJButtonMap.get(name).setText(name + " = " + value.toString());
		}
		
		//更新数组形参按钮映射
		ArrayList<String> arrayParamsList = function.getArrayParamsList();
		HashMap<String, int[]> arrayParamsMap = function.getArrayParamsMap();
		for(int i = 0; i < arrayParamsList.size(); i ++){
			String name = arrayParamsList.get(i);
			int[] values = arrayParamsMap.get(name);
			ArrayList<JButton> button = this.arrayParamsListMap.get(name);
			for(int j = 0; j < values.length; j ++){
				button.get(j).setText(name + "[" + j + "]=" + values[j]);
			}
		}
	}
	
	/**
	 * 更新按钮（根据函数对象）
	 * @param function 函数对象
	 */
	public void updateButton(Function function){
		//基本形参列表
		ArrayList<String> basicParamsList = function.getBasicParamsList();
		//基本形参映射
		HashMap<String, Integer> basicParamsMap = function.getBasicParamsMap();
		//数组形参列表
		ArrayList<String> arrayParamsList = function.getArrayParamsList();
		//数组形参映射
		HashMap<String, int[]> arrayParamsMap = function.getArrayParamsMap();
		
		//更新基本形参按钮
		for(int i = 0; i < basicParamsList.size(); i ++){
			String name = basicParamsList.get(i);
			Integer value = basicParamsMap.get(name);
			this.basicParamsJButtonMap.put(name, new JButton(name + "=" + value.toString()) );
		}
		
		//更新数组形参按钮
		for(int i = 0; i < arrayParamsList.size(); i ++){
			String name = arrayParamsList.get(i);
			int[] values = arrayParamsMap.get(name);
			ArrayList<JButton> buttons = new ArrayList<JButton>();
			for(int j = 0; j < values.length; j ++){
				buttons.add(new JButton(name + "[" + j + "]=" + values[j]));
			}
			this.arrayParamsListMap.put(name, buttons);
		}
		
		//更新局部变量按钮
		ArrayList<String> localVariablesList = function.getLocalVariablesList();
		for(int i = 0; i < localVariablesList.size(); i ++){
			String name = localVariablesList.get(i);
			this.localVariablesJButtonMap.put(name, new JButton(name + "=?" ));
		}
	}
	
	/**
	 * 初始化下面的提示面板
	 * @return
	 */
	public JPanel getSouthPanel(){
		JPanel south = new JPanel();
		prompt = new JTextField(90);
		prompt.setEditable(false);
		prompt.setText("this is a prompt");
		south.add(prompt);
		
		TitledBorder title = new TitledBorder("提示信息：");
		south.setBorder(title);
		return south;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingConsole.run(new VariableShow("函数执行过程跟踪"), 1000, 700);
	}

}
