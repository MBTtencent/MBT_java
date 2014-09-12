/*
 * FileName: MPathAlgoCovAll.java
 * 
 * Description: create MPathAlgoCovAll class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-29 Create
 * 1.1 SHUYUFANG 2013-09-03 Add specified stop states and modify the algorithm
 * 1.2 SHUYUFANG 2013-09-18 Add specified through states
 */


package ARH.common.path;

import java.util.*;

import ARH.framework.graph.*;

/**
 * this class is used to generate all possible paths
 * @author SHUYUFANG
 * @version 1.2
 */
public class MPathAlgoCovAll extends MPathAlgo
{   
    /**
     * constructor
     */
    public MPathAlgoCovAll()
    {
        super();
    }
    
    /**
     * generate all possible paths
     * @param graph the graph object
     * @param startstateNameList the list of start states
     * @param stopstateNameList the list of stop states
     * @param throughstateNameList the list of through states
     * @param throughStepNameList the list of through steps
     * @param routeList the list of routes
     * @return the result map
     */
    @Override
    public HashMap<String, ArrayList<ArrayList<Integer>>> genPath(MGraph graph, 
            ArrayList<String> startStateNameList, ArrayList<String> stopStateNameList,
            ArrayList<String> throughStateNameList, ArrayList<String> throughStepNameList,
            ArrayList<HashMap<String, ArrayList<String>>> routeList)
    {
        this.init(graph, startStateNameList, stopStateNameList, throughStateNameList, 
                throughStepNameList, routeList);
                
        for (int stateNumber : this.startStateNumSet)
        {
            this.statePath.add(0, stateNumber);
            this.dfs(stateNumber, 1);
            this.statePath.remove(0);
        }

        this.delPathWithoutThroughState();
        this.delPathWithoutThroughStep();
        
        return this.pathsMap;
    }
    
    /**
     * depth first search process
     * @param curstate the number of current state
     * @param curPos the position to be filled of the path
     */
    private void dfs(int curstate, int curPos)
    {
        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curstate);
        ArrayList<Integer> edgenumbers = this.graph.getEdgeNumberList().get(curstate);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curstate);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curstate);
        
        // current state is in stop state set
        // or 
        // no stop state specified and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1)) ||
                (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            this.addPath();
            return;
        }
        
        // whether there's a state to be the next state
        boolean hasOkNeighbour = false;
        
        // the left time that the edge can be used
        int leftTime;
        for (int i = 0; i < neighbours.size(); ++i)
        {
            leftTime = repeatTime.get(i);
            if (leftTime > 0)
            {
                // minus repeat time of the edge by one
                repeatTime.set(i, leftTime - 1);
                
                // add state number, edge number, step number to the paths
                this.statePath.add(curPos, neighbours.get(i));
                this.edgePath.add(curPos - 1, edgenumbers.get(i));
                this.stepPath.add(curPos - 1, stepnumbers.get(i));
                
                this.dfs(neighbours.get(i), curPos + 1);
                
                // restore paths
                this.statePath.remove(curPos);
                this.edgePath.remove(curPos - 1);
                this.stepPath.remove(curPos - 1);
                
                // restore the repeat time of the edge
                repeatTime.set(i, leftTime);
                
                hasOkNeighbour = true;
            }     
        } 
        // no neighbor to be the next state
        if (!hasOkNeighbour && 0 == this.stopStateNumSet.size())
        {
            this.addPath();
        }
    }
}