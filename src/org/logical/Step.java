package org.logical;

public class Step {
	
	//步骤类型，赋值或比较
	private ExpressionType type;
	//表达式
	private String expression;
	//如果是判断语句，那么nextY表示为真时要转向的节点，nextN表示为假时要转向的节点
	private Step nextY;
	private Step nextN;
	//如果不是判断语句那么next表示下一个节点
	private Step next;
	
	public Step(String expression) {
		// TODO Auto-generated constructor stub
		this.expression = expression;
		this.type = judgeType(expression);
		this.next = null;
		this.nextN = null;
		this.nextY = null;
	}
	
	public Step(ExpressionType type, String expression) {
		// TODO Auto-generated constructor stub
		this.expression = expression;
		this.type = type;
		this.next = null;
		this.nextN = null;
		this.nextY = null;
	}
	
	/**
	 * 判断表达式是比较式还是赋值式
	 * @param expression 要判断的表达式
	 * @return ExpressionType 返回表达式类型
	 */
	public ExpressionType judgeType(String expression){
		ExpressionType t ;
		if(Lex.containCmpOperator(expression)){
			t = ExpressionType.JUDGE;
		}else{
			t = ExpressionType.ASSIGN;
		}
		return t;
	}
	/**
	 * @return the type
	 */
	public ExpressionType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(ExpressionType type) {
		this.type = type;
	}


	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}


	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}


	/**
	 * @return the nextY
	 */
	public Step getNextY() {
		return nextY;
	}


	/**
	 * @param nextY the nextY to set
	 */
	public void setNextY(Step nextY) {
		this.nextY = nextY;
	}


	/**
	 * @return the nextN
	 */
	public Step getNextN() {
		return nextN;
	}


	/**
	 * @param nextN the nextN to set
	 */
	public void setNextN(Step nextN) {
		this.nextN = nextN;
	}


	/**
	 * @return the next
	 */
	public Step getNext() {
		return next;
	}


	/**
	 * @param next the next to set
	 */
	public void setNext(Step next) {
		this.next = next;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
