/*
 * FileName: MPathAlgoCovRepeat.java
 * 
 * Description: create MPathAlgoCovRepeat class
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
 * that contain repeated edges
 * @author SHUYUFANG
 * @version 1.2
 */
public class MPathAlgoCovRepeat extends MPathAlgo
{
    /**
     * constructor    
     */
    public MPathAlgoCovRepeat()
    {
        super();
    }
    
    /**
     * generate all paths that contain repeated edges
     * @param graph the graph object
     * @param startStateNameList the list of start states
     * @param stopStateNameList the list of stop states
     * @param throughStateNameList the list of through states
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
     * @param curState the number of current state
     * @param curPos the position to be filled of the path
     */
    private void dfs(int curState, int curPos)
    {
        System.out.println("####" + this.statePath);
        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curState);
        ArrayList<Integer> edgenumbers = this.graph.getEdgeNumberList().get(curState);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curState);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curState);
        
        // current state is in stop state set
        // or 
        // no stop state specified and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1))
                || (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            this.addPath();
        }
        
        boolean hasOkNeighbour = false;
        
        for (int i = 0; i < neighbours.size(); ++i)
        {
            int tmpStateNumber = neighbours.get(i);
            // if current edge is used in current path
            if (-1 == tmpStateNumber)
            {
                continue;
            }
            
            hasOkNeighbour = true;
            
            // mark an used edge
            neighbours.set(i, -1);
            
            // add current edge to the paths for several times 
            // specified by repeatTime of the edge
            for (int j = 0; j < repeatTime.get(i); ++j)
            {
                this.statePath.add(curPos, tmpStateNumber);
                this.edgePath.add(curPos - 1, edgenumbers.get(i));
                this.stepPath.add(curPos - 1, stepnumbers.get(i));
                ++curPos;
            }
            
            this.dfs(tmpStateNumber, curPos);
            
            // restore paths
            for (int j = 0; j < repeatTime.get(i); ++j)
            {
                --curPos;
                this.statePath.remove(curPos);
                this.edgePath.remove(curPos - 1);
                this.stepPath.remove(curPos - 1);
            }
            
            // restore the edge to be unused
            neighbours.set(i, tmpStateNumber);
        }
        // no neighbor to be the next state
        if (!hasOkNeighbour && 0 == this.stopStateNumSet.size())
        {
            this.addPath();
        }
    }
}