/**
 * 
 */
package org.gui;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import org.gui.ShowGraphic.Direction;
import org.gui.ShowGraphic.Location;
import org.logical.*;
import org.swingconsole.SwingConsole;

/**
 * @author renyu
 *
 */
public class ShowGraphic extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	enum Location{NORTH,SOUTH,WEST,EAST,NORTHWEST,NORTHEAST,SOUTHWEST,SOUTHEAST};
	enum Direction{NORTH,SOUTH,WEST, EAST,NORTHWEST};
	/**
	 * 
	 */
	public ShowGraphic(String source) {
		// TODO Auto-generated constructor stub
		StringBuffer code = Lex.getCode(source);
		CodeAnalysis ca = new CodeAnalysis(code);
		Step head = ca.getHead();
		JPanel panel = new JPanel();
		panel.add(new GraphicShow(head));
		
		JScrollPane scroll = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
												ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(scroll);
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingConsole.run(new ShowGraphic("/home/renyu/workspace/bishe/c_file/add.c"), 1000, 700);
	}

}

class GraphicShow extends Canvas{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Step head;
	private int size = 30;
	Map<Step, Polygon> polygonMap = new HashMap<Step, Polygon>();
	Map<Step, Rectangle> recMap = new HashMap<Step, Rectangle>();
	public GraphicShow(Step head){
		this.setSize(1000, 1000);
		this.head = head;
	}
	
	public GraphicShow(){
		this.setSize(10000, 10000);
	}
	
	public GraphicShow(Step head, int width, int height){
		this.setSize(width, height);
		this.head = head;
	}
	
	public void paint(Graphics g){
		this.drawFlowChart(g, head, 100, 10);
	}
	
	public void print(Step head){
		Step p;
		p = head;
		for(int i = 0; i < 10; i ++){
			System.out.println(p.getExpression());
			if(p.getType() == ExpressionType.ASSIGN ){
				
				p = p.getNext();
			}else{
			if(p.getType() == ExpressionType.JUDGE){
			
				p = p.getNextY();
			}}
		}
	}
	
	public void drawFlowChart(Graphics g, Step begin, int startX, int startY){
		Step p,pre;
		ArrayList<Step> judgeList = new ArrayList<Step>();
		p = begin;
		setNode(g, startX,startY,p);
		pre = p;
		if(p.getType() == ExpressionType.JUDGE){
			judgeList.add(p);
			p = p.getNextY();	
		}
		if(p.getType() == ExpressionType.ASSIGN){
			p = p.getNext();
		}
		
		while(p != null){
			//如果菱形或矩形映射表中的健已经包含p那么该节点已经被画出，只需从pre到p画线就行了
			if(polygonMap.containsKey(p) || recMap.containsKey(p)){
				drawLineBetweenNodes(g,pre,p);
				break;
			}
			Rectangle rec;
			rec = getNodeBounds(pre);
			this.setNode(g, rec.x, rec.y + size*2, p);
			this.drawLineBetweenNodes(g, pre, p);
			pre = p;
			if(p.getType() == ExpressionType.JUDGE){
				judgeList.add(p);
				p = p.getNextY();	
			}else{
				if(p.getType() == ExpressionType.ASSIGN){
					p = p.getNext();
				}		
			}		
		}
		
		
		for(int i = 0; i < judgeList.size(); i ++){
			pre = judgeList.get(i);
			p = pre.getNextN();
			if(p != null){
				if(polygonMap.containsKey(p)){
					this.drawLineBetweenNodes(g, pre, p);
				}else{
					Rectangle rec = polygonMap.get(pre).getBounds();
					this.drawLine(g, rec, new Rectangle(rec.x + rec.width + size, rec.y,rec.width,rec.height));
					drawFlowChart(g, p, rec.x + rec.width + size, rec.y);
				}
			}
		}
		
	}
	
	public Rectangle getNodeBounds(Step p){
		Rectangle rec;
		if(p.getType() == ExpressionType.JUDGE){
			rec = polygonMap.get(p).getBounds();
		}else{
			rec = recMap.get(p).getBounds();
		}
		return rec;
	}
	
	/**
	 * 在两个节点对应的矩形或菱形间划线
	 * @param g
	 * @param pre
	 * @param p
	 */
	public void drawLineBetweenNodes(Graphics g, Step pre, Step p){
		Polygon polyFrom,polyTo;
		Rectangle recFrom, recTo;
		
		if(pre.getType() == ExpressionType.JUDGE){
			polyFrom = polygonMap.get(pre);
			if(p.getType() == ExpressionType.JUDGE){
				polyTo = polygonMap.get(p);
				this.drawLine(g, polyFrom, polyTo);
			}else{
				recTo = recMap.get(p);
				this.drawLine(g, polyFrom, recTo);
			}
			
		}else{
			recFrom = recMap.get(pre);
			if(p.getType() == ExpressionType.JUDGE){
				polyTo = polygonMap.get(p);
				this.drawLine(g, recFrom, polyTo);
			}else{
				recTo = recMap.get(p);
				this.drawLine(g, recFrom, recTo);
			}
		}
	}
	
	/**
	 * 把节点p以矩形或菱形的形式画出来，外轮廓的左上顶点的坐标为startX,startY
	 * @param g 
	 * @param startX 轮廓左上角的横坐标
	 * @param startY 轮廓左上角的纵坐标
	 * @param p 要处理的节点
	 */
	public void setNode(Graphics g, int startX, int startY, Step p){
		String expression;
		Polygon poly;
		Rectangle rec;
		expression = p.getExpression();
		if(p.getType() == ExpressionType.JUDGE){
			poly = this.drawPolygon(g, expression, startX + size, startY, size);
			polygonMap.put(p, poly);
		}
		if(p.getType() == ExpressionType.ASSIGN){
			rec = this.drawRect(g, expression, startX, startY, size);
			recMap.put(p, rec);
		}
	}
	
	
	/**
	 * 画一个指定大小和位置的矩形
	 * @param g Graphics
	 * @param expression 矩形中的表达式
	 * @param x 矩形左上角的横坐标
	 * @param y 矩形左上角的纵坐标
	 * @param size 矩形大小，高size,宽为2*size
	 * @return 返回相应的矩形
	 */
	private Rectangle drawRect(Graphics g, String expression, int x, int y, int size){
		
		Rectangle rec = new Rectangle(x, y, 2*size, size);
		g.drawRect(x, y, rec.width, rec.height);
		g.drawString(expression, x, y + size/2);
		return rec;
	}
	
	/**
	 * 画菱形，高度为size,宽为2*size
	 * @param g Graphics对象
	 * @param expression 菱形中的表达式
	 * @param x 菱形上顶点的横坐标
	 * @param y 菱形上顶点的纵坐标
	 * @param size 菱形大小
	 * @return 对应的菱形
	 */
	private Polygon drawPolygon(Graphics g, String expression, int x, int y, int size){
		int[] xPoints={x,x+size,x,x-size}, yPoints={y,y+size/2,y+size,y+size/2};	
		Polygon poly = new Polygon(xPoints, yPoints, 4);
		g.drawPolygon(poly);
		g.drawString(expression, x-size + 10, y+size/2 + 5);
		return poly;
	}
	
	/**
	 * 根据源图形到目的图形的相对位置连线，源图形目的图形为矩形或菱形
	 * @param g
	 * @param shape1 源图形
	 * @param shape2 目的图形
	 */
	private void drawLine(Graphics g, Shape from, Shape to){
		Rectangle recFrom,recTo;	
		recFrom = from.getBounds();
		recTo = to.getBounds();
		Location loc = getLocation(recFrom, recTo);
		//如果to在from的下方
		if(loc == Location.SOUTH){
			//如果不相邻
			if(recTo.y - recFrom.y > recFrom.height*2){
				g.drawLine(recFrom.x + recFrom.width, recFrom.y + recFrom.height/2, 
						recFrom.x + recFrom.width + recFrom.height, recFrom.y + recFrom.height/2);
				g.drawLine(recFrom.x + recFrom.width + recFrom.height, recFrom.y + recFrom.height/2,
						recTo.x + recTo.width + recTo.height, recTo.y + recTo.height/2);
				g.drawLine(recTo.x + recTo.width + recTo.height, recTo.y + recTo.height/2,
						recTo.x + recTo.width, recTo.y + recTo.height/2);
				drawSword(g,recTo.x + recTo.width,recTo.y + recTo.height/2,Direction.WEST);
				
			}else{//如果相邻
				g.drawLine(recFrom.x + recFrom.width/2,  recFrom.y + recFrom.height,  
						recTo.x + recTo.width/2, recTo.y);
				this.drawSword(g, recTo.x + recTo.width/2, recTo.y, Direction.SOUTH);
		
			}
		}
		//如果to在from的正上方
		if(loc == Location.NORTH){
				g.drawLine(recFrom.x , recFrom.y + recFrom.height/2, 
						recFrom.x - recFrom.height, recFrom.y + recFrom.height/2);
				g.drawLine(recFrom.x - recFrom.height, recFrom.y + recFrom.height/2, 
						recTo.x - recTo.height, recTo.y + recTo.height/2);
				g.drawLine(recTo.x - recTo.height, recTo.y + recTo.height/2, 
						recTo.x, recTo.y + recTo.height/2);
				this.drawSword(g, recTo.x, recTo.y + recTo.height/2, Direction.EAST);
				
		}
		//如果to在from的右方
		if(loc == Location.EAST){
			g.drawLine(recFrom.x + recFrom.width, recFrom.y + recFrom.height/2, 
					recTo.x, recTo.y + recTo.height/2);
			this.drawSword(g, recTo.x, recTo.y + recTo.height/2, Direction.EAST);
		}
		//如果to在from的左上方
		if(loc == Location.NORTHWEST){
			g.drawLine(recFrom.x + recFrom.width, recFrom.y + recFrom.height/2, 
					recFrom.x + recFrom.width + recFrom.height, recFrom.y + recFrom.height/2);
			g.drawLine(recFrom.x + recFrom.width + recFrom.height, recFrom.y + recFrom.height/2,
					recFrom.x + recFrom.width + recFrom.height, recTo.y + recTo.height*2 - recTo.height/2);
			g.drawLine(recFrom.x + recFrom.width + recFrom.height, recTo.y + recTo.height*2 - recTo.height/2, 
					recFrom.x, recTo.y + recTo.height*2 - recTo.height/2);
			g.drawLine(recFrom.x, recTo.y + recTo.height*2 - recTo.height/2, 
					recTo.x + recTo.width, recTo.y + recTo.height/2);
			this.drawSword(g, recTo.x + recTo.width, recTo.y + recTo.height/2, Direction.NORTHWEST);
			
		}
		//如果to在from的左下方
		if(loc == Location.SOUTHWEST){
			g.drawLine(recFrom.x + recFrom.width/2, recFrom.y + recFrom.height,
					recFrom.x + recFrom.width/2, recTo.y + recTo.height/2);
			g.drawLine(recFrom.x + recFrom.width/2, recTo.y + recTo.height/2, 
						recTo.x + recTo.width, recTo.y + recTo.height/2);
			drawSword(g,recTo.x + recTo.width, recTo.y + recTo.height/2,Direction.WEST);
		}
		
	}
	
	public void drawSword(Graphics g, int x, int y, Direction direction){
		if(direction == Direction.EAST){
			g.drawLine(x, y, x - 5, y - 5);
			g.drawLine(x, y, x - 5, y + 5);
		}
		if(direction == Direction.WEST){
			g.drawLine(x, y, x + 5, y - 5);
			g.drawLine(x, y, x + 5, y + 5);
		}
		if(direction == Direction.NORTH){
			g.drawLine(x,  y,  x - 5, y + 5);
			g.drawLine(x, y, x + 5, y + 5);
		}
		if(direction == Direction.SOUTH){
			g.drawLine(x, y, x - 5, y - 5);
			g.drawLine(x,  y, x + 5, y -5);
		}
		if(direction == Direction.NORTHWEST){
			g.drawLine(x,  y, x + 1,  y + 10);
			g.drawLine(x,  y, x + 10, y + 4);
		}
	}
	
	
	public Location getLocation(Shape from, Shape to){
		Location type = null;
		Rectangle recFrom,recTo;
		recFrom = from.getBounds();
		recTo = to.getBounds();
		if(recFrom.x == recTo.x){
			if(recFrom.y < recTo.y){
				type = Location.SOUTH;
			}else{
				type = Location.NORTH;
			}
		}
		if(recFrom.y == recTo.y){
			if(recFrom.x < recTo.x){
				type = Location.EAST;
			}else{
				type= Location.WEST;
			}
		}else{
			if(recTo.x < recFrom.x){
				if(recTo.y < recFrom.y){
					type = Location.NORTHWEST;
				}else{
					type = Location.SOUTHWEST;
				}
			}
			if(recTo.x > recFrom.x){
				if(recTo.y < recFrom.y){
					type = Location.NORTHEAST;
				}else{
					type = Location.SOUTHEAST;
				}
			}	
		}	
		return type;
	}
	
	
	
}