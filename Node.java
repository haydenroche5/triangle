/**
 * This class is used to represent the numbers in the triangle as nodes.
 * A node has a value (the number), a level in the triangle, an index
 * (the top of the triangle is at index 0 and indices increase going down
 * and left to right), potentially two children, potentially a parent, and
 * a greatest sum from the top of the triangle. The first methods of the class
 * are simple accessors and mutators. The interesting part of this class comes
 * with the compareTo method, which is documented further below.
 */

public class Node implements Comparable<Node> {

	private int value;
	private int level;
	private int index;
	private Node[] children = new Node[2];

	private int sumFromTop = Integer.MIN_VALUE;
	private Node pathParent = null;

	public Node(int value, int level) {
		this.value= value;
		this.level = level;
	}

	public int getValue() {
		return this.value;
	}

	public int getLevel() {
		return this.level;
	}

	public Node[] getChildren() {
		return this.children;
	}

	public int getSumFromTop() {
		return this.sumFromTop;
	}

	public Node getPathParent() {
		return this.pathParent;
	}
	
	public int getIndex() {
		return this.index;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public void setChildTwo(Node childTwo) {
		this.children[1] = childTwo;
	}

	public void setSumFromTop(int sum) {
		this.sumFromTop = sum;
	}

	public void setPathParent(Node pathParent) {
		this.pathParent = pathParent;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		if(this.children[0] == null) {
			return "Value: " + this.value + " Level: " + this.level + " No children"+ " Sum from top: " + this.sumFromTop;   
		}
		else {
			return "Value: " + this.value + " Level: " + this.level + " First child: " + this.children[0].getValue() + " Second child: " + this.children[1].getValue() + " Sum from top: " + this.sumFromTop; 
		}
	}

	
	/** 
	 * It is extremely important to note that both the return methods below will be inverted
	 * in the actual implementation in order to implement an effective max priority queue.
	 * By passing Collections.reverseOrder() to the priority queue, we will flip these return
	 * statements. (e.g. if this has a greater sum than otherNode, this comes before otherNode)
	 * I do this because PriorityQueue is a min priority queue by default.
	 **/
	
	@Override
	// Order the Nodes by increasing sumFromTop and if sumFromTop is equal for two nodes,
	// order by decreasing index.
	public int compareTo(Node otherNode) {
		if(this.sumFromTop > otherNode.getSumFromTop()) {
			return 1;
		}
		else if (this.sumFromTop < otherNode.getSumFromTop()) {
			return -1;
		}
		else {
			if(this.index < otherNode.getIndex()) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}

}
