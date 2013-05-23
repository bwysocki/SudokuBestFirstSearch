package pl.kohonen.sudoku;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;



public class BestFirstSearch {
    
    //Ordered by heuristic queue (states to visit)
    private Queue<SudokuState> open = new PriorityQueue<SudokuState>();
    
    //Set of visited states (stored are their hashcodes)
    private Set<String> closed = new HashSet<String>();
    
    //initial sudoku map
    private SudokuState initial = null;
    
    BestFirstSearch(SudokuState initial){
        this.initial = initial;
        open.add(initial);
    }
    
    public SudokuState search(){
        //do till open set will be empty
        do{
            //gets state with lowest heuristic
            SudokuState currentState = open.poll();
            
            if (isSolution(currentState)){
                return currentState;
            }else{
                List<SudokuState> children = currentState.getChildren();
                for (SudokuState state : children){
                    if (closed.contains(state.getHashCode())){
                        continue;
                    }else if (!open.contains(state)) {
                        open.add(state);
                    }
                }
            }
            
            closed.add(currentState.getHashCode());
            
        }while(!open.isEmpty());
        
        return null;
    }
    
    /*** private methods ***/
    
    private boolean isSolution(SudokuState state){
        return state.getH() < 1d;
    }
}
