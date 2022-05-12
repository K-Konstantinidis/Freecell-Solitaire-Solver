import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

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

public class Freecell extends Define{

	public Freecell(int method, String in, String out, String stringMethod) {
		Tree frontier = new Tree(); //Create the search tree
		
		Node node = readFile(in); //Read the given file and insert the puzzle in a node
			
		System.out.println("Solving " + in + " using " + stringMethod);
		
		frontier.search(node, method, out);	//Main call
	}
	
	//This function get the input method and if it exists returns the 
	//corresponding number. Else returns -1 which causes an error.
	//Inputs:
	//		String s: The name of the search algorithm
	//Output:
	//		Node node: Either an error or a different number for each algorithm
	public static int getMethod(String s) {
		if(s.equals("breadth"))
			return breadth;
		else if(s.equals("depth"))
			return depth;
		else if(s.equals("best"))
			return best;
		else if(s.equals("astar"))
			return astar;
	
		return -1;
	}

	//This function reads a file containing a puzzle and stores the 
	//puzzle in a node
	//Inputs:
	//		String filename: The name of the file containing a puzzle.
	//Output:
	//		Node node: A node with the puzzle
	private static Node readFile(String filename){
		Node node = new Node(); //Create a node to store the given puzzle
		File file = new File(filename);
		FileReader reader = null;
		BufferedReader breader = null;
		int stack, noOfCards;

		try {
			reader = new FileReader(file.getCanonicalFile());
			breader = new BufferedReader(reader);

			String line = breader.readLine(); //Read each line separately
			stack=0; //1st stack
			noOfCards=0; //We start with 0 cards

			while(line != null) {
				String[] cards = line.split(" "); //Split the line every time you find a space

				for(String c : cards){
					noOfCards++; //Read a card so ++

					char suit = c.charAt(0); //Get the suit of the card
					int number = Integer.valueOf(c.substring(1)); //Get the number of the card which is next to the suit

					Card card = new Card(suit, number); //Create the card
					if(!is_valid(card)){
						System.out.println("There is a wrong suit or a negative number in the given file.."); //If the card is not valid
						System.exit(1);
					}

					node.getTableau().get(stack).add(card); //Add the card in the tableau					
					node.getPosition().put(card, "stack"); //Add the card (as the key) and its position (as the value) in a HashMap
				}
				stack++; //Move to the next stack
				line = breader.readLine(); //Read the next line
			}
			reader.close();
			breader.close();
			N = noOfCards/4; //All the cards '/4' as we have 4 different suits
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}

	//This function check if a card is valid
	private static boolean is_valid(Card card) {
		if(Objects.equals(card.getColor(), "empty") || card.getNumber()<0)
			return false;
	
		return true;
	}
}
