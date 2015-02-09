import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// CLOSED list is a Boolean array that indicates if a state associated with a given position in the maze has already been expanded. 
		boolean[][] closed = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		// OPEN list (aka Frontier list)
		PriorityQueue<StateFValuePair> open = new PriorityQueue<StateFValuePair>();
        // The h() value need not be tracked, as it can be computed
        ArrayList<State> expanded = new ArrayList<State>();

		// TODO initialize the root state and add
		// to OPEN list
		// ...
        open.add(new StateFValuePair(
                new State(maze.getPlayerSquare(), null, 0, 0),
                Math.abs(maze.getPlayerSquare().X - maze.getGoalSquare().X)
                    + Math.abs(maze.getPlayerSquare().Y - maze.getGoalSquare().y)
        ));

		while (!open.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded,
			// TODO update the maze if a solution found

			// use open.poll() to extract the minimum stateFValuePair.
			// use open.add(...) to add stateFValue pairs
            State state = open.poll().getState();
            if (state.isGoal(maze)) {
                cost = state.getGValue();
                state = state.getParent();
                while (state.getParent() != null) {
                    maze.setOneSquare(state.getSquare(), '.');
                    state = state.getParent();
                }
                return true;
            }

            for (State successor : state.getSuccessors(closed, maze)) {
                //shit
                // I'm not keeping track of closed as a boolean here,
                // because under some circumstances, we'll want to re-add
                // nodes to the graph.
                for (State prior : expanded) {
                    // I could use more conditions here, but it's easer to
                    // just layer the ifs
                    if (prior.getX() == successor.getX() && prior.getY() == successor.getY()) {
                        // we don't need to compute f(), as h() is fixed
                        // for the x and y coords, which are equal
                        if (successor.getGValue() < prior.getGValue()) {
                            // we want to add successor, even though we've
                            // already visited a node at the same place
                            open.add(new StateFValuePair(successor,
                                    successor.getGValue()
                                    + Math.abs(successor.getX() - maze.getGoalSquare().X)
                                    + Math.abs(successor.getY() - maze.getGoalSquare().Y)
                            ));
                        }
                    }
                }
            }
		}

		// TODO return false if no solution
		return false;
	}

}
