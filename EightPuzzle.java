//Nicolas Stoian
//CS780 Artificial Intelligence
//Programming Assignment #1 - Heuristic Search

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Scanner;

/**
 * Class containing main function and algorithms
 * User input is required to choose the puzzle difficulty and algorithm to use
 * Output is sent to console and to a local file "output.txt"
 *
 *
 * @author Nicolas Stoian
 *
 */
public class EightPuzzle {
	public static void main(String args[]){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the number of the puzzle to do");
		System.out.println("1. Easy");
		System.out.println("2. Medium");
		System.out.println("3. Hard");
		System.out.println("4. Worst");
	    int puzzleChoice = in.nextInt();
	    System.out.println("Enter the number of the search algorithm to use");
		System.out.println("1. A* using tiles out of place heuristic");
		System.out.println("2. A* using manhattan heuristic");
		System.out.println("3. Iterative Deepening A* with manhattan heuristic");
		System.out.println("4. Depth-first branch and bound with manhattan heuristic");
	    int algorithmChoice = in.nextInt();
	    in.close();
	    try{
	    	PrintWriter outFile = new PrintWriter("outfile.txt");
	    	State startBoard;
	    	if (puzzleChoice == 1){
	    		startBoard = new State(1,3,4,8,6,2,7,0,5);
	    		outFile.println("Easy Puzzle");
	    	}
	    	else if (puzzleChoice == 2){
	    		startBoard = new State(2,8,1,0,4,3,7,6,5);
	    		outFile.println("Medium Puzzle");
	    	}
	    	else if (puzzleChoice == 3){
	    		startBoard = new State(2,8,1,4,6,3,0,7,5);
	    		outFile.println("Hard Puzzle");
	    	}
	    	else if (puzzleChoice == 4){
	    		startBoard = new State(5,6,7,4,0,8,3,2,1);
	    		outFile.println("Worst Puzzle");
	    	}
	    	else{
	    		System.out.println("Choice out of bounds, doing easy puzzle");
	    		startBoard = new State(1,3,4,8,6,2,7,0,5);
	    		outFile.println("Easy Puzzle");
	    	}
	    	int[] numNodes = new int[1];
	    	numNodes[0] = 0;
	    	State goalBoard = new State(1,2,3,8,0,4,7,6,5);
	    	StateNode solution = new StateNode();
	    	long startTime = System.currentTimeMillis();
	    	long startTimeNano = System.nanoTime();
	    	if (algorithmChoice == 1){
	    		outFile.println("A* using tiles out of place heuristic");
	    		solution = aStarTiles(startBoard, goalBoard, numNodes);
	    	}
	    	else if (algorithmChoice == 2){
	    		outFile.println("A* using manhattan heuristic");
	    		solution = aStarManhattan(startBoard, goalBoard, numNodes);
	    	}
	    	else if (algorithmChoice == 3){
	    		outFile.println("Iterative Deepening A* with manhattan heuristic");
	    		solution = iterativeDeepeningAStar(startBoard, goalBoard, numNodes);
	    	}
	    	else if (algorithmChoice == 4){
	    		outFile.println("Depth-first branch and bound with manhattan heuristic");
	    		solution = branchAndBoundManhattan(startBoard, goalBoard, numNodes, outFile);
	    	}
	    	else{
	    		System.out.println("Choice out of bounds, doing A* with manhattan function");
	    		outFile.println("A* using manhattan heuristic");
	    		solution = aStarManhattan(startBoard, goalBoard, numNodes);
	    	}
	    	long endTime = System.currentTimeMillis();
	    	long endTimeNano = System.nanoTime();
	    	long elapsedTime = endTime - startTime;
	    	long elapsedTimeNano = endTimeNano - startTimeNano;
	    	System.out.println();
	    	outFile.println();
	    	System.out.println("Solution path");
	    	outFile.println("Solution path");
	    	Stack<StateNode> solutionPath = new Stack<StateNode>();
	    	StateNode walker = solution;
	    	while(walker.getParent() != null){
	    		solutionPath.push(walker);
	    		walker = walker.getParent();
	    	}
	    	solutionPath.push(walker);
	    	while(!solutionPath.empty()){
	    		solutionPath.pop().printBoard(outFile);
	    		System.out.println();
	    		outFile.println();
	    	}
	    	System.out.println("Number of nodes = " + numNodes[0]);
	    	outFile.println("Number of nodes = " + numNodes[0]);
	    	System.out.println("Run time = " + elapsedTime + "ms");
	    	outFile.println("Run time = " + elapsedTime + "ms");
	    	System.out.println("Run time = " + elapsedTimeNano + "ns");
	    	outFile.println("Run time = " + elapsedTimeNano + "ns");
	    	System.out.println("Number of moves in solution = " + solution.getGval());
	    	outFile.println("Number of moves in solution = " + solution.getGval());
	    	outFile.close();
	    }
	    catch(FileNotFoundException e){
			System.err.println("File not found exception, check arguements and try again.");
            return;
		}
	}

	/**
	 * Function to perform A* search using the tiles out of place heuristic function
	 *
	 * @param startBoard
	 * @param goalBoard
	 * @param numNodes
	 * @return The solution node
	 */
	public static StateNode aStarTiles(State startBoard, State goalBoard, int[] numNodes){
		boolean goalReached = false;
		StateNode start = new StateNode(startBoard, 0, null);
		numNodes[0]++;
		start.computeValsAStarTiles(goalBoard);
		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>();
		HashMap<State, StateNode> closed = new HashMap<State, StateNode>();
		open.add(start);
		StateNode current = new StateNode();
		while(!goalReached){
			current = open.poll();
			if (current.state.equals(goalBoard)){
				goalReached = true;
			}
			else{
				closed.put(current.state, current);
				Vector<StateNode> children = current.generateSuccessors();
				for(int i = 0; i < children.size(); i++){
					numNodes[0]++;
					children.elementAt(i).computeValsAStarTiles(goalBoard);
					boolean removed = false;
					boolean inOpen = false;
					Iterator<StateNode> itr = open.iterator();
					while(itr.hasNext()){
						StateNode element = (StateNode)itr.next();
						if(element.state.equals(children.elementAt(i).state)){
							inOpen = true;
							if(element.getGval() > children.elementAt(i).getGval()){
								itr.remove();
								removed = true;
							}
						}
					}
					if(closed.containsKey(children.elementAt(i).state)){
						if(children.elementAt(i).getGval() < closed.get(children.elementAt(i).state).getGval()){
							closed.remove(children.elementAt(i).state);
							open.add(children.elementAt(i));
						}
					}
					else if(inOpen){
						if(removed){
							open.add(children.elementAt(i));
						}
					}
					else{
						open.add(children.elementAt(i));
					}
				}
			}
		}
		return current;
	}

	/**
	 * Function to perform A* search using the Manhattan heuristic function
	 *
	 * @param startBoard
	 * @param goalBoard
	 * @param numNodes
	 * @return The solution node
	 */
	public static StateNode aStarManhattan(State startBoard, State goalBoard, int[] numNodes){
		boolean goalReached = false;
		StateNode start = new StateNode(startBoard, 0, null);
		numNodes[0]++;
		start.computeValsAStarManhattan(goalBoard);
		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>();
		HashMap<State, StateNode> closed = new HashMap<State, StateNode>();
		open.add(start);
		StateNode current = new StateNode();
		while(!goalReached){
			current = open.poll();
			if (current.state.equals(goalBoard)){
				goalReached = true;
			}
			else{
				closed.put(current.state, current);
				Vector<StateNode> children = current.generateSuccessors();
				for(int i = 0; i < children.size(); i++){
					numNodes[0]++;
					children.elementAt(i).computeValsAStarManhattan(goalBoard);
					boolean removed = false;
					boolean inOpen = false;
					Iterator<StateNode> itr = open.iterator();
					while(itr.hasNext()){
						StateNode element = (StateNode)itr.next();
						if(element.state.equals(children.elementAt(i).state)){
							inOpen = true;
							if(element.getGval() > children.elementAt(i).getGval()){
								itr.remove();
								removed = true;
							}
						}
					}
					if(closed.containsKey(children.elementAt(i).state)){
						if(children.elementAt(i).getGval() < closed.get(children.elementAt(i).state).getGval()){
							closed.remove(children.elementAt(i).state);
							open.add(children.elementAt(i));
						}
					}
					else if(inOpen){
						if(removed){
							open.add(children.elementAt(i));
						}
					}
					else{
						open.add(children.elementAt(i));
					}
				}
			}
		}
		return current;
	}

	/**
	 * Function to perform iterative deepening A* search using the Manhattan heuristic function
	 *
	 * @param startBoard
	 * @param goalBoard
	 * @param numNodes
	 * @return The solution node
	 */
	public static StateNode iterativeDeepeningAStar(State startBoard, State goalBoard, int[] numNodes){
		StateNode start = new StateNode(startBoard, 0, null);
		numNodes[0]++;
		start.computeValsAStarManhattan(goalBoard);
		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>();
		HashMap<State, StateNode> closed = new HashMap<State, StateNode>();
		int fValue = start.getFval();
		boolean goalReached = false;
		StateNode bestNode = new StateNode();
		while(!goalReached){
			open.add(start);
			closed.clear();
			StateNode current = new StateNode();
			bestNode = new StateNode(goalBoard, fValue++, null);
			bestNode.computeValsAStarManhattan(goalBoard);
			while(!open.isEmpty()){
				current = open.poll();
				if (current.state.equals(goalBoard)){
					if(current.getGval() < bestNode.getGval()){
						bestNode = current;
						goalReached = true;
					}
				}
				else{
					closed.put(current.state, current);
					Vector<StateNode> children = current.generateSuccessors();
					for(int i = 0; i < children.size(); i++){
						numNodes[0]++;

						if(closed.containsKey(children.elementAt(i).state)){
						}
						else{
							children.elementAt(i).computeValsAStarManhattan(goalBoard);
							if(children.elementAt(i).getFval() < bestNode.getFval()){
								open.add(children.elementAt(i));
							}
						}
					}
				}
			}

		}
		return bestNode;
	}


	/**
	 * Function to perform depth first branch and bound search using the Manhattan heuristic function
	 *
	 * @param startBoard
	 * @param goalBoard
	 * @param numNodes
	 * @return The solution node
	 */
	public static StateNode branchAndBoundManhattan(State startBoard, State goalBoard, int[] numNodes, PrintWriter outFile){
		long startTimeNano = System.nanoTime();
		StateNode start = new StateNode(startBoard, 0, null);
		numNodes[0]++;
		start.computeValsAStarManhattan(goalBoard);
		PriorityQueue<StateNode> open = new PriorityQueue<StateNode>();
		HashMap<State, StateNode> closed = new HashMap<State, StateNode>();
		open.add(start);
		StateNode current = new StateNode();
		StateNode bestNode = new StateNode(goalBoard, 10000000, null);
		bestNode.computeValsAStarManhattan(goalBoard);
		while(!open.isEmpty()){
			current = open.poll();
			if (current.state.equals(goalBoard)){
				if(current.getGval() < bestNode.getGval()){
					bestNode = current;
					long endTimeNano = System.nanoTime();
			    	long elapsedTimeNano = endTimeNano - startTimeNano;
			    	System.out.println("Best solution time = " + elapsedTimeNano + "ns");
			    	outFile.println("Best solution time = " + elapsedTimeNano + "ns");
				}
			}
			else{
				closed.put(current.state, current);
				Vector<StateNode> children = current.generateSuccessors();
				for(int i = 0; i < children.size(); i++){
					numNodes[0]++;

					if(closed.containsKey(children.elementAt(i).state)){
					}
					else{
						children.elementAt(i).computeValsAStarManhattan(goalBoard);
						if(children.elementAt(i).getFval() < bestNode.getFval()){
							open.add(children.elementAt(i));
						}
					}
				}
			}
		}
		return bestNode;
	}
}
