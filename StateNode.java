//Nicolas Stoian
//CS780 Artificial Intelligence
//Programming Assignment #1 - Heuristic Search

import java.io.PrintWriter;
import java.util.Vector;

/**
 * Class to represent nodes of state configurations
 *
 * @author Nicolas Stoian
 *
 */
public class StateNode implements Comparable<StateNode>{

	protected State state;
	private int gVal;
	private int hVal;
	private int fVal;
	private StateNode parent;

	/**
	 * Default constructor
	 */
	public StateNode(){
		state = new State();
		gVal = 0;
	}

	/**
	 * 3 argument constructor used to initialize new StateNodes using given parameters
	 *
	 * @param board
	 * @param gVal
	 * @param parent
	 */
	public StateNode(State board, int gVal, StateNode parent){
		this.state = board;
		this.gVal = gVal;
		this.parent = parent;
	}

	/**
	 * Getter function for gVal
	 *
	 * @return gVal
	 */
	public int getGval(){
		return gVal;
	}

	/**
	 * Getter function for hVal
	 *
	 * @return hVal
	 */
	public int getHval(){
		return hVal;
	}

	/**
	 * Getter function for fVal
	 *
	 * @return fVal
	 */
	public int getFval(){
		return fVal;
	}

	/**
	 * Getter function for parent
	 *
	 * @return parent StateNode
	 */
	public StateNode getParent(){
		return parent;
	}

	/**
	 * Function to compute hVal using the tiles out of place heuristic
	 * sets hVal and fVal
	 *
	 * @param goalBoard
	 */
	public void computeValsAStarTiles(State goalBoard){
		int numOutOfPlace = 0;
		for(int row = 0; row < 3; row++){
	        for(int col = 0; col < 3; col++){
	        	if(state.board[row][col] != 0 && state.board[row][col] != goalBoard.board[row][col]){
	        		numOutOfPlace++;
	        	}
	        }
	    }
		hVal = numOutOfPlace;
		fVal = gVal + hVal;
	}

	/**
	 * Function to compute hVal using the Manhattan heuristic
	 * sets hVal and fVal
	 *
	 * @param goalBoard
	 */
	public void computeValsAStarManhattan(State goalBoard){
		int manhattanDistance = 0;
		for(int i = 1; i < 9; i++){
			int x0 = 0;
			int x1 = 0;
			int y0 = 0;
			int y1 = 0;
			for(int row = 0; row < 3; row++){
		        for(int col = 0; col < 3; col++){

		        	if(state.board[row][col] == i){
		        		x0 = row;
		        		y0 = col;
		        	}
		        	if(goalBoard.board[row][col] == i){
		        		x1 = row;
		        		y1 = col;
		        	}
		        }
		    }
			int distance = Math.abs(x1-x0) + Math.abs(y1-y0);
			manhattanDistance += distance;
		}
		hVal = manhattanDistance;
		fVal = gVal + hVal;
	}

	/**
	 * Function to generate all possible successors of this StateNode
	 *
	 * @return Vector containing all possible successors
	 */
	public Vector<StateNode> generateSuccessors(){
		int emptySpaceRow = 0;
		int emptySpaceCol = 0;
		for(int row = 0; row < 3; row++){
	        for(int col = 0; col < 3; col++){
	        	if(state.board[row][col] == 0){
	        		emptySpaceRow = row;
	        		emptySpaceCol = col;
	        	}
	        }
	    }
		Vector<StateNode> toReturn = new Vector<StateNode>();
		if(emptySpaceCol != 2){
			State rightMove = new State(state.board);
			rightMove.board[emptySpaceRow][emptySpaceCol] = rightMove.board[emptySpaceRow][emptySpaceCol + 1];
			rightMove.board[emptySpaceRow][emptySpaceCol + 1] = 0;
			toReturn.add(new StateNode(rightMove, gVal+1, this));
		}
		if(emptySpaceRow != 2){
			State downMove = new State(state.board);
			downMove.board[emptySpaceRow][emptySpaceCol] = downMove.board[emptySpaceRow + 1][emptySpaceCol];
			downMove.board[emptySpaceRow + 1][emptySpaceCol] = 0;
			toReturn.add(new StateNode(downMove, gVal+1, this));
		}
		if(emptySpaceCol != 0){
			State leftMove = new State(state.board);
			leftMove.board[emptySpaceRow][emptySpaceCol] = leftMove.board[emptySpaceRow][emptySpaceCol - 1];
			leftMove.board[emptySpaceRow][emptySpaceCol - 1] = 0;
			toReturn.add(new StateNode(leftMove, gVal+1, this));
		}
		if(emptySpaceRow != 0){
			State upMove = new State(state.board);
			upMove.board[emptySpaceRow][emptySpaceCol] = upMove.board[emptySpaceRow - 1][emptySpaceCol];
			upMove.board[emptySpaceRow - 1][emptySpaceCol] = 0;
			toReturn.add(new StateNode(upMove, gVal+1, this));
		}
		return toReturn;
	}


	/**
	 * Comparison function for StateNodes
	 * Used to order PriorityQueue of StateNodes
	 */
	public int compareTo(StateNode other){
		if(this.getFval() == other.getFval()){
			return 0;
		}
		else if(this.getFval() < other.getFval()){
			return -1;
		}
		else{
			return 1;
		}
	}

	/**
	 * Function to print the board state
	 */
	public void printBoard(PrintWriter outFile){
		for(int row = 0; row < 3; row++){
	        for(int col = 0; col < 3; col++){
	        	System.out.print(state.board[row][col] + " ");
	        	outFile.print(state.board[row][col] + " ");
	        }
	        System.out.println();
	        outFile.println();
		}
	}
}
