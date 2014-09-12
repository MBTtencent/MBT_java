/*
 * FileName: MPathAlgoCovShort.java
 * 
 * Description: create MPathAlgoCovShort class
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
 * this class is used to generate shortest paths
 * @author SHUYUFANG
 * @version 1.2
 */
public class MPathAlgoCovShort extends MPathAlgo
{
    /** shortest distance list to get to states */
    private ArrayList<Integer> stateDistList;

    /** shortest path distance ever met */
//    private int shortestPathDist;
    
    /**
     * constructor
     */
    public MPathAlgoCovShort()
    {
        super();
        this.stateDistList = new ArrayList<Integer>();
//        this.shortestPathDist = MPathConfig.MAX_PATH_DIST;
    }
    
    /**
     * delete paths whose length is longer than that of shortest paths
     */
    private void deleteLongPath()
    {
        ArrayList<ArrayList<Integer>> statePathList = this.pathsMap.get(MPathConfig.STATE_PATH);
        ArrayList<ArrayList<Integer>> edgePathList = this.pathsMap.get(MPathConfig.EDGE_PATH);
        ArrayList<ArrayList<Integer>> stepPathList = this.pathsMap.get(MPathConfig.STEP_PATH);
        
        HashSet<Integer> resultSet = new HashSet<Integer>();

        Iterator<Integer> iter = this.throughStateNumSet.iterator();
        while (iter.hasNext())
        {
            int throughStateNum = iter.next();
            int shortestDist = MPathConfig.MAX_PATH_DIST;
            for (int i = 0; i < statePathList.size(); ++i)
            {
                if (statePathList.get(i).contains(throughStateNum) && 
                        shortestDist > statePathList.get(i).size())
                {
                    shortestDist = statePathList.get(i).size();
                }
            }
            for (int i = 0; i < statePathList.size(); ++i)
            {
                if (statePathList.get(i).contains(throughStateNum) &&
                        shortestDist == statePathList.get(i).size())
                {
                    resultSet.add(i);
                }
            }
        }
        
        iter = this.throughStepNumSet.iterator();
        while (iter.hasNext())
        {
            int throughStepNum = iter.next();
            int shortestDist = MPathConfig.MAX_PATH_DIST;
            for (int i = 0; i < stepPathList.size(); ++i)
            {
                if (stepPathList.get(i).contains(throughStepNum) &&
                        shortestDist > stepPathList.get(i).size())
                {
                    shortestDist = stepPathList.get(i).size();
                }
            }
            for (int i = 0; i < stepPathList.size(); ++i)
            {
                if (stepPathList.get(i).contains(throughStepNum) &&
                        shortestDist == stepPathList.get(i).size())
                {
                    resultSet.add(i);
                }
            }
        }
        
        for (int i = statePathList.size() - 1; i >= 0; --i)
        {
            if (!resultSet.contains(i))
            {
                statePathList.remove(i);
                edgePathList.remove(i);
                stepPathList.remove(i);
            }
        }
    }
    
    /**
     * generate shortest paths
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
            this.stateDistList.add(MPathConfig.MAX_PATH_DIST);
        }
            
        for (int stateNumber : this.startStateNumSet)
        {
            this.statePath.add(0, stateNumber);
            dfs(stateNumber, 1);
            this.statePath.remove(0);
        }
        
        this.delPathWithoutThroughState();
        this.delPathWithoutThroughStep();

        this.deleteLongPath();  
        
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
        
        // current state is in stop state set
        // or 
        // no stop state specified and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1))
                || (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            // short enough
//            if (this.statePath.size() <= this.shortestPathDist)
//            {
//                // update shortest distance of paths
//                this.shortestPathDist = this.statePath.size();
//                this.addPath();
//            }
            this.addPath();
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
        
        // current neighbor state's shortest distance
        int curDistance;
        
        for (int i = 0; i < neighbours.size(); ++i)
        {
            leftTime = repeatTime.get(i);
            curDistance = this.stateDistList.get(neighbours.get(i));
            
            // the edge can be used and current distance is short enough
            if (leftTime > 0 && curDistance >= curPos)
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
            // the edge can be used but current distance is not short enough
            else if (leftTime > 0 && curDistance < curPos)
            {
                hasOkNeighbour = true;
            }
        } 
        // no neighbor to be the next state and current path is short enough
        if (!hasOkNeighbour && 0 == this.stopStateNumSet.size())
//                && this.statePath.size() <= this.shortestPathDist)
        {
            // update shortest distance of paths
//            this.shortestPathDist = this.statePath.size();
            this.addPath();
        }
    }
}