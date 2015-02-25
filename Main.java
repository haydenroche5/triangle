import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Main {
	
	/**
	 * This method creates an ArrayList of Node objects representing a triangle
	 * from a specified file path. In our case, this is the triangle.txt file.
	 */
	public static ArrayList<Node> createTriangle(String pathToFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(pathToFile));
		String line;
		ArrayList<Node> triangle = new ArrayList<Node>();
		int lineNumber = 0;
		
		// While there are still lines to read from the file
		while ((line = br.readLine()) != null) {
			// j is used to index the position we're at in an individual line of the file
			int j = 0;
			// While j is not past the end of the line
			while (j < line.length()) {
				String stringFromLine = "";
				int intFromLine;
				// Create a string using the characters in the line until we hit a space
				while(j < line.length() && line.charAt(j) != ' ') {
					stringFromLine += line.charAt(j);
					j++;
				}
				// Increment j here to skip past the space we just found
				j++;
				// Parse the string as an integer and create a new Node object for it,
				// specifying its level and value in the triangle
				intFromLine = Integer.parseInt(stringFromLine);
				Node nodeToAdd = new Node(intFromLine, lineNumber);
				nodeToAdd.setIndex(triangle.size());
				triangle.add(nodeToAdd);
			}
			lineNumber++;
		}
		
		// Set the children of each node.
		for(int i = 0; i < triangle.size(); i++) {
			Node parent = triangle.get(i);
			// These are generalized indices for children.
			int childOneIndex = i + parent.getLevel() + 1;
			int childTwoIndex = i + parent.getLevel() + 2;
			// We're done as soon as we hit the bottom of the triangle (no children,
			// so any index computed will be out of bounds).
			if(childOneIndex >= triangle.size()) {
				break;
			}
			Node[] children = {triangle.get(childOneIndex), triangle.get(childTwoIndex)};
			parent.setChildren(children);
		}

		br.close();
		// The top of the triangle always has a sum equal to its value.
		triangle.get(0).setSumFromTop(triangle.get(0).getValue());
		return triangle;
	}
	
	/**
	 * This method relaxes edges in the same way that Dijkstra's algorithm does, but
	 * with the goal of finding greater length paths instead of shorter length ones.
	 * This difference, in code, is reflected in the < sign instead of a > sign in the if
	 * statement.
	 */
	public static Node relax(Node fromNode, Node toNode, PriorityQueue<Node> queue) {
		// If we find a path to a node that results in a great sumFromTop for that node,
		// the sum of this path should be the new sumFromTop for the node.
		if(toNode.getSumFromTop() < fromNode.getSumFromTop() + toNode.getValue()) {
			queue.remove(toNode);
			toNode.setSumFromTop(fromNode.getSumFromTop() + toNode.getValue());
			toNode.setPathParent(fromNode);
			queue.add(toNode);
		}
		
		return toNode;
	}

	public static void printGreatestSumAndPath(ArrayList<Node> triangle) {
		// Finds the maximum sum in the triangle.
		int maxSum = Integer.MIN_VALUE;
		Node maxSumNode = null;
		int bottomLevel = triangle.get(triangle.size() - 1).getLevel();
		for(int i = 0; i < triangle.size(); i++) {
			if(triangle.get(i).getSumFromTop() > maxSum) {
				
				maxSum = triangle.get(i).getSumFromTop();
				
				// We save the node itself so that we can print out the path to that
				// node later.
				maxSumNode = triangle.get(i);
			}
		}	
		// Print the maximum sum found and then the path to get to that node (bottom to top)
		System.out.println("max sum = " + maxSum);
		Node pathParent = maxSumNode.getPathParent();
		System.out.println("---Path (bottom to top)---");
		System.out.println(maxSumNode);
		while(pathParent != null) {
			System.out.println(pathParent);
			pathParent = pathParent.getPathParent();
		}
	}
	
	/**
	 * The main method uses the methods defined in this class and in Node.java
	 * to run a modified version of Dijkstra's shortest paths algorithm that instead
	 * finds longest paths. I treat the triangle as a directed acyclic graph with
	 * edge weights defined by the value at the terminal node of the edge. I make some 
	 * simple modifications to Dijkstra's algorithm (e.g. using a max priority queue 
	 * instead of a min priority queue) to instead allow me to access the greatest
	 * "length" (i.e. sum) path from the source, which is the top of the triangle.
	 */
	
	public static void main(String[] args) throws IOException {
		ArrayList<Node> triangle = createTriangle("/Users/haydenroche/Downloads/triangle.txt");
		/** 
		 * The standard Java priority queue is a min queue, but supplying the 
		 * Collections.reverseOrder() comparator will reverse this, sorting
		 * elements by the reverse of their natural ordering. The natural
		 * ordering for Nodes is by increasing distanceFromTop, as specified
		 * in Node.java's compareTo method. (Node implements the Comparable
		 * interface) In the end, we get a max priority queue, which we will
		 * use to find the longest path using a reversal of Dijkstra's algorithm.
		 **/
		
		PriorityQueue<Node> maxPQ = new PriorityQueue<Node>(triangle.size(), Collections.reverseOrder());
		
		for(int i = 0; i < triangle.size(); i++) {
			maxPQ.add(triangle.get(i));
		}
		
		ArrayList<Node> triangleWithUpdatedSums = new ArrayList<Node>();
		
		while(!maxPQ.isEmpty()) {
			Node maxNode = maxPQ.poll();
			for(int i = 0; i < maxNode.getChildren().length; i++) {
				if(maxNode.getChildren()[i] != null) {
					Node checkedNode = relax(maxNode, maxNode.getChildren()[i], maxPQ);
					triangleWithUpdatedSums.add(checkedNode);
				}
			}
		}
		printGreatestSumAndPath(triangleWithUpdatedSums);
	}
	
}