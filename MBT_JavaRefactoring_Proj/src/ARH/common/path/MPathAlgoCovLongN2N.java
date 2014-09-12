/*
 * FileName: MPathAlgoCovLongN2N.java
 * 
 * Description: create MPathAlgoCovLongN2N class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-03 Create
 * 1.2 SHUYUFANG 2013-09-18 Add specified through states
 */


package ARH.common.path;

import java.util.*;

import ARH.framework.graph.*;

/**
 * this class is used to generate longest paths from each start
 * state to each stop state
 * @author SHUYUFANG
 * @version 1.2
 */
public class MPathAlgoCovLongN2N extends MPathAlgo
{
    /** shortest distance list to get to states */
    private ArrayList<Integer> stateDistList;
    
    /** maximum distance from start state to other states in one round */
    private ArrayList<Integer> maxDistList;
    
    /** temporary state path lists */
    private ArrayList<ArrayList<Integer>> tmpStatePathLists;
    
    /** temporary edge path lists */
    private ArrayList<ArrayList<Integer>> tmpEdgePathLists;
    
    /** temporary step path lists */
    private ArrayList<ArrayList<Integer>> tmpStepPathLists;
    
    /**
     * constructor
     */
    public MPathAlgoCovLongN2N()
    {
        super();
        this.stateDistList = new ArrayList<Integer>();
        this.maxDistList = new ArrayList<Integer>();
        this.tmpStatePathLists = new ArrayList<ArrayList<Integer>>();
        this.tmpEdgePathLists = new ArrayList<ArrayList<Integer>>();
        this.tmpStepPathLists = new ArrayList<ArrayList<Integer>>();
    }

    /**
     * reset maximum distance to be minimum value
     */
    private void resetMaxDistList()
    {
        for (int i = 0; i < this.maxDistList.size(); ++i)
        {
            this.maxDistList.set(i, MPathConfig.MIN_PATH_DIST);
        }
    }
    
    /**
     * delete paths that have same start and stop state
     * with a longer path
     */
    private void deleteShortPath()
    {
        for (int i = this.tmpStatePathLists.size() - 1; i >= 0; --i)
        {
            int pathLength = this.tmpStatePathLists.get(i).size();
            int endStateNum = this.tmpStatePathLists.get(i).get(pathLength - 1);
            // the path is not one of the longest
            if (this.maxDistList.get(endStateNum) > pathLength)
            {
                this.tmpStatePathLists.remove(i);
                this.tmpEdgePathLists.remove(i);
                this.tmpStepPathLists.remove(i);
            }
        }
    }
    
    /**
     * add paths of current start state to the path map
     */
    private void addPathToMap()
    {
        for (int i = 0; i < this.tmpStatePathLists.size(); ++i)
        {
            this.statePath = this.tmpStatePathLists.get(i);
            this.edgePath = this.tmpEdgePathLists.get(i);
            this.stepPath = this.tmpStepPathLists.get(i);
            this.addPath();
        }
        this.statePath = new ArrayList<Integer>();
        this.edgePath = new ArrayList<Integer>();
        this.stepPath = new ArrayList<Integer>();
    }
    
    /**
     * generate longest STATE TO STATE paths
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

        for (int i = 0; i < this.graph.getNodeCount(); ++i)
        {
            this.maxDistList.add(MPathConfig.MIN_PATH_DIST);
            this.stateDistList.add(MPathConfig.MIN_PATH_DIST);
        }
        
        for (int stateNumber : this.startStateNumSet)
        {
            this.resetMaxDistList();
            
            this.statePath.add(0, stateNumber);
            dfs(stateNumber, 1);
            this.statePath.remove(0);

            this.deleteShortPath();
            this.addPathToMap();
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
        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curState);
        ArrayList<Integer> edgenumbers = this.graph.getEdgeNumberList().get(curState);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curState);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curState);
        
        // current state is in the stop state set
        // or no specified stop state and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1))
                || (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            if (this.maxDistList.get(this.statePath.get(curPos - 1)) 
                    <= this.statePath.size())
            {
                this.maxDistList.set(this.statePath.get(curPos - 1), 
                        this.statePath.size());
                
                this.tmpStatePathLists.add(new ArrayList<Integer>(this.statePath));
                this.tmpEdgePathLists.add(new ArrayList<Integer>(this.edgePath));
                this.tmpStepPathLists.add(new ArrayList<Integer>(this.stepPath));
            }
            return;
        }
        
        // current state has no neighbor
        if (0 == neighbours.size())
        {
            return;
        }
        
        // whether there's a state to be the next state
        boolean hasOkNeighbour = false;
        
        // the left time that the edge can be used
        int leftTime;
        
        // current neighbor state's longest distance
        int curDistance;
        
        for (int i = 0; i < neighbours.size(); ++i)
        {
            leftTime = repeatTime.get(i);
            curDistance = this.stateDistList.get(neighbours.get(i));
            
            // the edge can be used and current distance is long enough
            if (leftTime > 0 && curDistance <= curPos)
            {
                // minus repeat time of the edge by one
                repeatTime.set(i, leftTime - 1);
                
                // add state number, edge number, step number to the paths
                this.statePath.add(curPos, neighbours.get(i));
                this.edgePath.add(curPos - 1, edgenumbers.get(i));
                this.stepPath.add(curPos - 1, stepnumbers.get(i));
                this.stateDistList.set(neighbours.get(i), curPos);
                
                this.dfs(neighbours.get(i), curPos + 1);
                
                // restore paths
                this.statePath.remove(curPos);
                this.edgePath.remove(curPos - 1);
                this.stepPath.remove(curPos - 1);
                this.stateDistList.set(neighbours.get(i), curDistance);

                // restore the repeat time of the edge
                repeatTime.set(i, leftTime);
                
                hasOkNeighbour = true;
            }
            // the edge can be used but current distance is not long enough
            else if (leftTime > 0 && curDistance > curPos)
            {
                hasOkNeighbour = true;
            }
        } 
        // no neighbor to be the next state and no specified stop state
        if (!hasOkNeighbour && 0 == this.stopStateNumSet.size())
        {
            if (this.maxDistList.get(this.statePath.get(curPos - 1)) 
                    <= this.statePath.size())
            {
                this.maxDistList.set(this.statePath.get(curPos - 1), 
                        this.statePath.size());
                
                this.tmpStatePathLists.add(new ArrayList<Integer>(this.statePath));
                this.tmpEdgePathLists.add(new ArrayList<Integer>(this.edgePath));
                this.tmpStepPathLists.add(new ArrayList<Integer>(this.stepPath));
            }
        }
    }
}