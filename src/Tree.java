import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
	
/**
						MIT License

		Copyright (c) 2022 Konstantinidis Konstantinos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
**/

public class Tree extends Define{
	private TreeSet<Node> nodes; //A tree with all the nodes
	private ArrayList<Node> solutionNodes = new ArrayList<>(); //An array with all the nodes till the solution one
	private static ArrayList<Node> tempNodes = new ArrayList<>(); //A temporary array list with all the nodes till the solution one
	private ArrayList<Node> extract = new ArrayList<Node>(); //A list with all the extracted nodes
	private Node node; //The current node
	private int expand; //Number of nodes that have at least 1 child
	private boolean solution = false; //A flag to see if we have found a solution or not

	public Tree() {
		nodes = new TreeSet<>(); //Create the tree with the nodes
	}

	//This function will create a timer and then try to find a solution node.
	//If the solution node is found then the solution will be written
	//in a file else the program will terminate either because there 
	//is not a solution or the timer passed the timeout.
	//Inputs:
	//		The file puzzle, the method we will use and the file in which the solution will be written
	public void search(Node givenNode, int method, String out) {
		givenNode.setMethod(method); //Set the search method
		
		Instant timer = Instant.now(); //Create a timer
		Long timeElapsed = Duration.between(Instant.now(), Instant.now()).toMillis(); //Get current time
		
		expand = 0;
		nodes.add(givenNode); //Add the node in the tree

		//While the tree isn't empty and we haven't found a solution
		while(!nodes.isEmpty() && !solution) {
			if(timeElapsed < timeout) {
				System.out.println("Timeout..\n"); //If we hit the timeout mark
				System.exit(1);
			}

			node = nodes.pollFirst(); //Get the top node of the tree

			if(!extract.contains(node))
				extract.add(node); //If we haven't already checked this node
			else
				continue; //Else go to the next node

			if(node.isSolution()){
				solution = true; //If its a solution node end the search
				break;
			} 
			else{
				ArrayList<Node> children = node.findChildren(method); //Add the children of the node in a list
				for(Node n : children)
					if(!extract.contains(n)) //If we don't already have a similar node then add it in the tree
						nodes.add(n);

				expand++; //++ as we found a node with at least 1 child
			}

			if(expand % 3 == 0)
				timeElapsed = Duration.between(timer, Instant.now()).toMillis(); //Get the time every time we find 3 nodes with at least 1 child 
		}
			
		if(solution){
			solutionNodes = extractSolution(node); //If we found a solution write the solution in a file
			writeToFile(solutionNodes, out);
		}
		else{
			System.out.println("There is no solution!"); //If we didn;t find a solution exit
			System.exit(1);
		}
	}

	//This function extracts the solution
	private static ArrayList<Node> extractSolution(Node node) {
		Node parent = node; //Create a parent node

		//While there is a parent
		while(parent != null) {
			tempNodes.add(parent); //Add the parent in the temp list
			parent = parent.getParent(); //Give the parent a new value
		}

		//Reverse the solution so we have the moves in the correct order
		Collections.reverse(tempNodes);
		//Remove the top node because it is the given node
		//from the file, so it doesn't count as a new move
		tempNodes.remove(0);

		return tempNodes;
	}
	
	//This function writes the solution in a file 
	public static void writeToFile(ArrayList<Node> solution, String out) {
		File file = new File(out);
		FileWriter fwriter = null;

		try {
			fwriter = new FileWriter(file);
			fwriter.write(solution.size());
			fwriter.write(System.lineSeparator());
			for(Node node : solution) {
				fwriter.write(node.getDirection());
				fwriter.write(System.lineSeparator());
			}
			fwriter.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}