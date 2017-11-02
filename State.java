//Nicolas Stoian
//CS780 Artificial Intelligence
//Programming Assignment #1 - Heuristic Search

import java.util.Arrays;

/**
 * Class to represent board states
 * 0 represents the empty tile
 *
 * @author Nicolas Stoian
 *
 */
public class State {
	protected int[][] board;

	/**
	 * Default constructor
	 */
	public State(){
		board = new int[3][3];
		int num = 0;
		for(int row = 0; row < 3; row++){
	        for(int col = 0; col < 3; col++){
	        	board[row][col] = num++;
	        }
	    }
	}

	/**
	 * Constructor for a given board configuration in a 3x3 array
	 *
	 * @param board
	 */
	public State(int[][] board){
		this.board = new int[3][3];
		for(int row = 0; row < 3; row++){
	        for(int col = 0; col < 3; col++){
	        	this.board[row][col] = board[row][col];
	        }
	    }
	}

	/**
	 * Constructor for an individually specified board configuration
	 *
	 * @param topLeft
	 * @param topCenter
	 * @param topRight
	 * @param middleLeft
	 * @param middleCenter
	 * @param middleRight
	 * @param bottomLeft
	 * @param bottomCenter
	 * @param bottomRight
	 */
	public State(int topLeft, int topCenter, int topRight, int middleLeft, int middleCenter, int middleRight, int bottomLeft, int bottomCenter, int bottomRight){
		board = new int[3][3];
		board[0][0] = topLeft;
		board[0][1] = topCenter;
		board[0][2] = topRight;
		board[1][0] = middleLeft;
		board[1][1] = middleCenter;
		board[1][2] = middleRight;
		board[2][0] = bottomLeft;
		board[2][1] = bottomCenter;
		board[2][2] = bottomRight;
	}

	/**
	 * Overridden hashCode function so that this class can be compared as a HashMap key
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
		return result;
	}

	/**
	 * Overridden equals function so that this class can be compared as a HashMap key
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
		return true;
	}
}
