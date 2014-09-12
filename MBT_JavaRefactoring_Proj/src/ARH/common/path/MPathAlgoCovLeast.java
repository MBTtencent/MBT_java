/*
 * FileName: MPathAlgoCovLeast.java
 * 
 * Description: create MPathAlgoCovLeast class
 * 
 * History:
 * 1.2 SHUYUFANG 2013-09-18 Create
 */


package ARH.common.path;

import java.util.*;

import ARH.framework.graph.*;

/**
 * this class is used to generate least paths which covers all Steps
 * @author SHUYUFANG
 * @version 1.2
 */
public class MPathAlgoCovLeast extends MPathAlgo
{
    /**
     * constructor
     */
    public MPathAlgoCovLeast()
    {
        super();
    }
    
    /**
     * generate all possible paths
     * @param graph the graph object
     * @param startStateNameList the list of start States
     * @param stopStateNameList the list of stop States
     * @param throughStateNameList the list of through States
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
                
        for (int StateNumber : this.startStateNumSet)
        {
            this.statePath.add(0, StateNumber);
            this.dfs(StateNumber, 1);
            this.statePath.remove(0);
        }
        
        this.delPathWithoutThroughState();
        this.delPathWithoutThroughStep();
        
        HashSet<Integer> leastPathNumSet = this.getCoverSet(this.pathsMap.get(MPathConfig.STEP_PATH));
        
        for (int i = this.pathsMap.get(MPathConfig.STATE_PATH).size() - 1; i >= 0; --i)
        {
            if (!leastPathNumSet.contains(i))
            {
                this.pathsMap.get(MPathConfig.STATE_PATH).remove(i);
                this.pathsMap.get(MPathConfig.EDGE_PATH).remove(i);
                this.pathsMap.get(MPathConfig.STEP_PATH).remove(i);
            }
        }

        return this.pathsMap;
    }
    
    /**
     * depth first search process
     * @param curState the number of current State
     * @param curPos the position to be filled of the path
     */
    private void dfs(int curState, int curPos)
    {        
        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curState);
        ArrayList<Integer> Stepnumbers = this.graph.getEdgeNumberList().get(curState);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curState);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curState);
        
        // current State is in stop State set
        // or 
        // no stop State specified and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1)) ||
                (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            this.addPath();
            return;
        }
        
        // whether there's a State to be the next State
        boolean hasOkNeighbour = false;
        
        // the left time that the Step can be used
        int leftTime;
        for (int i = 0; i < neighbours.size(); ++i)
        {
            leftTime = repeatTime.get(i);
            if (leftTime > 0)
            {
                // minus repeat time of the Step by one
                repeatTime.set(i, leftTime - 1);
                
                // add State number, Step number, step number to the paths
                this.statePath.add(curPos, neighbours.get(i));
                this.edgePath.add(curPos - 1, Stepnumbers.get(i));
                this.stepPath.add(curPos - 1, stepnumbers.get(i));
                
                this.dfs(neighbours.get(i), curPos + 1);
                
                // restore paths
                this.statePath.remove(curPos);
                this.edgePath.remove(curPos - 1);
                this.stepPath.remove(curPos - 1);
                
                // restore the repeat time of the Step
                repeatTime.set(i, leftTime);
                
                hasOkNeighbour = true;
            }     
        } 
        // no neighbor to be the next State
        if (!hasOkNeighbour && 0 == this.stopStateNumSet.size())
        {
            this.addPath();
        }
    }
    
    /**
     * set cover algorithm used to find least paths which cover all Steps
     * @param pathLists all the Step paths generated
     * @return the set of numbers of chosen paths
     */
    private HashSet<Integer> getCoverSet(ArrayList<ArrayList<Integer>> pathLists)
    {
        // the set of numbers of chosen paths
        HashSet<Integer> setNumberList = new HashSet<Integer>();
        
        // whether the path is already chosen
        ArrayList<Boolean> isSetUsedList = new ArrayList<Boolean>();
        
        // the set which includes all Step numbers
        HashSet<Integer> allSet = new HashSet<Integer>();
        
        // the set which includes Step numbers of the chosen paths
        HashSet<Integer> curSet = new HashSet<Integer>();
        
        for (int i = 0; i < pathLists.size(); ++i)
        {
            isSetUsedList.add(false);
            allSet.addAll(pathLists.get(i));
        }
        
        // greedy algorithm
        while (curSet.size() < allSet.size())
        {
            // find an unused set to maximize current set
            
            int setIdx = -1;
            int maxUnionSize = -1;
            for (int i = 0; i < pathLists.size(); ++i)
            {
                if (isSetUsedList.get(i)) continue;
                HashSet<Integer> tmpSet = new HashSet<Integer>(curSet);
                tmpSet.addAll(pathLists.get(i));
                if (tmpSet.size() > maxUnionSize)
                {
                    maxUnionSize = tmpSet.size();
                    setIdx = i;
                }
            }
            
            isSetUsedList.set(setIdx, true);
            setNumberList.add(setIdx);
            curSet.addAll(pathLists.get(setIdx));
        }
        
        return setNumberList;
    }
}