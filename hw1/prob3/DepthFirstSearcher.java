import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Depth-First Search (DFS)
 * 
 * You should fill the search() method of this class.
 */
public class DepthFirstSearcher extends Searcher {


	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public DepthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main depth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD
        noOfNodesExpanded = 0;

		// CLOSED list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been expanded.
		boolean[][] closed = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		// Stack implementing the Frontier list
		LinkedList<State> stack = new LinkedList<State>();

        stack.push(new State(maze.getPlayerSquare(), null, 0, 0));

		while (!stack.isEmpty()) {
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded
			// TODO update the maze if a solution found

            State state = stack.pop();
            ++noOfNodesExpanded;
            Square square = state.getSquare();

            if (state.isGoal(maze)) {
                cost = state.getGValue();
                //Don't mark the goal square
                state = state.getParent();
                //traverse state's ancestors, adding a . to the MazeMatrix at every point
                while (state.getParent() != null) {
                    maze.setOneSquare(state.getSquare(), '.');
                    state = state.getParent();
                }

                return true;
            }

            closed[square.X][square.Y] = true;

            ArrayList<State> successors = state.getSuccessors(closed, maze);

//          for (State successor : successors) {
            for (int i = 0; i < successors.size(); i++) {
                State successor = successors.get(i);
                stack.push(successor);
            }

			// use stack.pop() to pop the stack.
			// use stack.push(...) to elements to stack
		}

		// TODO return false if no solution
		return false;
	}
}
