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
    

    public static ArrayList<Integer> ints_to_ArrayList(int[] a) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
        }
        return list;
    }

    public static int[] ArrayList_to_ints(ArrayList<Integer> list) {
        int[] a = new int[list.size()];
        //this should be modified to use iterator and List instead of ArrayList, but that's too much work for now.
        for (int i = 0; i < list.size(); i++) {
            a[i] = list.get(i);
        }
        return a;
    }


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
        ArrayList<Integer> successors = new ArrayList<Integer>();
        if (-1 == lastMove) {
            //It's the first move, so it must be an odd number < n/2
            //We also know that takenList is empty, so we don't have to deal with it.
            int a = 1;
            while (a < n/2) {
                // so a % 2 == 1
                successors.add(a);
                a += 2;
            }
        } else {
            // Number must be <= n, factor or multiple of LastMove, and !in takenList
            ArrayList<Integer> factors = new ArrayList<Integer>();
            ArrayList<Integer> multiples = new ArrayList<Integer>();

            factors = Math.get_factors(lastMove);
            multiples = Math.get_multiples(lastMove, n);

            for (int i = 0; i < factors.size(); i++) {
                if (takenList[factors.get(i)] == 0) {
                    successors.add(factors.get(i));
                }
            }

            for (int i = 0; i < multiples.size(); i++) {
                if (takenList[multiples.get(i)] == 0) {
                    successors.add(multiples.get(i));
                }
            }
        }
        return successors;
    }



    public double minimax_value(GameState s, int depthLimit, boolean max_player) {
        // use one function to eliminate some redundancy.
        // Well, I thought this would eliminate more redundancy than it
        // actually did.  Still ought to work though.
        ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.takenList);
        if (successors.isEmpty()) {
            s.leaf = true;
        }
        
        if (s.leaf) {
            if (max_player) {
                return -1.0;
            } else {
                return 1.0;
            }
        }
        if (0 == depthLimit) {
            if (max_player) {
                return stateEvaluator(s);
            } else {
                return -stateEvaluator(s);
            }
        }

        int new_depthLimit;
        if (-1 == depthLimit) {
            new_depthLimit = -1;
        } else {
            new_depthLimit = depthLimit-1;
        }

        double minimax;
        if (max_player) {
            minimax = -1.0;
        } else {
            minimax = 1.0;
        }

        for (int i = 0; i < successors.size(); i++) {
            //With all the array arithmetic going on here,
            //this will probably run in something like O(n^4).
            s.takenList[successors.get(i)] = 1;
            GameState next_GameState = new GameState(
                    s.takenList,
                    successors.get(i)
            );
            if (max_player) {
                //using the separate min_value and max_value functions that now
                //do no more than call this will make for an interesting-
                //looking call stack.
                double provisional_minimax = min_value(
                        next_GameState,
                        new_depthLimit
                );
                if (provisional_minimax >= minimax) {
                    minimax = provisional_minimax;
                    s.bestMove = successors.get(i);
                }
            } else {
                double provisional_minimax = max_value(
                        next_GameState,
                        new_depthLimit
                );
                if (provisional_minimax <= minimax) {
                    minimax = provisional_minimax;
                    s.bestMove = successors.get(i);
                }
            }

        s.takenList[successors.get(i)] = 0;
        }
        return minimax;
    }

    // The max value function
    @Override
    public double max_value(GameState s, int depthLimit) 
    {
        // combine this with min_value to minimize redundancy
        return minimax_value(s, depthLimit, true);
    }

    // The min value function
    @Override
    public double min_value(GameState s, int depthLimit)
    {
        // TODO Add your code here
        // combine this with min_value to minimize redundancy
        return minimax_value(s, depthLimit, false);
    }
    
    // Function to find the next best move
    @Override
    public int move(int lastMove, int[] takenList, int depthLimit) {
        // TODO Add your code here
        GameState gamestate = new GameState(takenList, lastMove);
        max_value(gamestate, depthLimit);
        if (gamestate.leaf) return -1;
        return gamestate.bestMove;
    }
    
    // The static board evaluator function
    @Override
    public double stateEvaluator(GameState s)
    {
        // TODO Add your code here
        ArrayList<Integer> taken_list = ints_to_ArrayList(s.takenList);
        if (s.takenList[1] == 0) {
            return 0.0;
        }
        ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.takenList);
        if (1 == s.lastMove) {
            if (successors.size() % 2 == 0) {
                return -0.5;
            } else {
                return 0.5;
            }
        }
        if (Math.is_prime(s.lastMove)) {
            int num_multiples = 0;
            for (int i = 0; i < successors.size(); i++) {
                if (successors.get(i) % s.lastMove == 0) {
                    num_multiples++;
                }
            }
            if (num_multiples % 2 == 0) {
                return -0.7;
            } else {
                return 0.7;
            }
        }
        int largest_prime_divisor = s.lastMove/2;
        do {
            largest_prime_divisor--;
        } while (s.lastMove % largest_prime_divisor != 0);
        int num_multiples = 0;
        for (int i = 0; i < successors.size(); i++) {
            if (successors.get(i) % largest_prime_divisor == 0) {
                num_multiples++;
            }
        }
        if (num_multiples % 2 == 0) {
            return -0.6;
        }
        return 0.6;

    }
}
