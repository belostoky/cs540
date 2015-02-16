/***************************************************************************************
  PlayerImpl.java
  Implement five functions in this file.
  ---------
  Licensing Information:  You are free to use or extend these projects for
  educational purposes provided that (1) you do not distribute or publish
  solutions, (2) you retain this notice, and (3) you provide clear
  attribution to UW-Madison.
 
  Attribution Information: The Take Stone Games was developed at UW-Madison.
  The initial project was developed by Jerry(jerryzhu@cs.wisc.edu) and his TAs.
  Current version with depthLimit and SBE was developed by Fengan Li(fengan@cs.wisc.edu)
  and Chuck Dyer(dyer@cs.wisc.edu)
  
*****************************************************************************************/

import java.util.*;

public class PlayerImpl implements Player {
	// Identifies the player
	private int name = 0;
	int n = 0;
	

	// Constructor
	public PlayerImpl(int name, int n) {
		this.name = 0;
		this.n = n;
	}

	// Function to find possible successors
	@Override
	public ArrayList<Integer> generateSuccessors(int lastMove, int[] takenList) 
	{
		// TODO Add your code here
		return null;
	}

	// The max value function
	@Override
	public double max_value(GameState s, int depthLimit) 
	{
		// TODO Add your code here
		return 0;
	}

	// The min value function
	@Override
	public double min_value(GameState s, int depthLimit)
	{
		// TODO Add your code here
		return 0;
	}
	
	// Function to find the next best move
	@Override
	public int move(int lastMove, int[] takenList, int depthLimit) {
		// TODO Add your code here
		return 0;
	}
	
	// The static board evaluator function
	@Override
	public double stateEvaluator(GameState s)
	{
		// TODO Add your code here
		return 0;
	}
}
