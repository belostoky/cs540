import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the DFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

    State successor(int x, int y) {
        return new State (
            new Square(x, y),
            this,
            gValue + 1,
            depth + 1
        );
    }

    // this assumes x and y are within valid bounds!!!
    boolean is_valid(int x, int y, boolean[][]closed, Maze maze) {
        //return (!closed[x][y] && maze.getSquareValue(x, y) == ' ');
        if (!closed[x][y] && maze.getSquareValue(x, y) != '%') {
            return true;
        } else {
            return false;
        }
    }

	/**
	 * @param visited
	 *            closed[i][j] is true if (i,j) is already expanded
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] closed, Maze maze) {
		// FILL THIS METHOD
        ArrayList<State> successors = new ArrayList<State>(4);
        int x, y;

        // sadly, I can't think of an easy way to make a loop of these
        // And I can't just use one is_valid function because of boundary checks
        x = square.X;
        y = square.Y - 1;
        if (y >= 0 && is_valid(x, y, closed, maze)) {
            successors.add(successor(x, y));
        }
        x = square.X + 1;
        y = square.Y;
        if (x < maze.getNoOfRows() && is_valid(x, y, closed, maze)) {
            successors.add(successor(x, y));
        }
        x = square.X;
        y = square.Y + 1;
        if (y < maze.getNoOfCols() && is_valid(x, y, closed, maze)) {
            successors.add(successor(x, y));
        }
        x = square.X - 1;
        y = square.Y;
        if (x >= 0 && is_valid(x, y, closed, maze)) {
            successors.add(successor(x, y));
        }

		// TODO check all four neighbors (up, right, down, left)
		// TODO return all unvisited neighbors
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		return successors;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the DFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
}
