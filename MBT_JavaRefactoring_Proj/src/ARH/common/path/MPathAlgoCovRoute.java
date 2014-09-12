/*
 * FileName: MPathAlgoCovRoute.java
 * 
 * Description: create MPathAlgoCovRoute class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-10-25 Create
 */


package ARH.common.path;

import java.util.*;

import ARH.common.config.MCommonConfig;
import ARH.framework.graph.MGraph;

/**
 * this class is used to generate paths of specified routes
 * @author SHUYUFANG
 * @version 1.0
 */
public class MPathAlgoCovRoute extends MPathAlgo
{
    /** set of states */
    private HashSet<Integer> stateSet;
    
    /** set of steps */
    private HashSet<Integer> stepSet;
    
    /** list of steps */
    @SuppressWarnings("unused")
    private ArrayList<Integer> stepList;
    
    /** start state number of overall route */
    private int startStateNum;
    
    /** stop state number of overall route */
    private int stopStateNum;
    
    /** type of routes */
    private int cov;
    
    /**
     * Constructor
     */
    public MPathAlgoCovRoute()
    {
        super();
    }

    /**
     * generate specified route paths
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
        
        for (int i = 0; i < this.routeList.size(); ++i)
        {
            this.cov = this.routeList.get(i).get(MCommonConfig.MBT_XML_COV).get(0);
            this.stateSet = new HashSet<Integer>(this.routeList.get(i).get(MCommonConfig.MBT_XML_STATE));
            this.stepSet = new HashSet<Integer>(this.routeList.get(i).get(MCommonConfig.MBT_XML_STEP));
            this.stepList = this.routeList.get(i).get(MCommonConfig.MBT_XML_STEP);
            
            if (this.cov == 1) // overall
            {
                int statesSize = this.routeList.get(i).get(MCommonConfig.MBT_XML_STATE).size();
                this.startStateNum = this.routeList.get(i).get(MCommonConfig.MBT_XML_STATE).get(0);
                this.stopStateNum = this.routeList.get(i).get(MCommonConfig.MBT_XML_STATE).get(statesSize - 1);
                
                this.statePath.add(0, this.startStateNum);
                this.dfsOverall(this.startStateNum, 1);
                this.statePath.remove(0);
            }
            else // partial
            {
                for (int stateNumber : this.startStateNumSet)
                {
                    this.statePath.add(0, stateNumber);
                    this.dfsPartial(stateNumber, 1);
                    this.statePath.remove(0);
                }
            }
        }
        
        return this.pathsMap;
    }
    
    /**
     * Deep search first of overall routes
     * @param curState current state
     * @param curPos current array position
     */
    private void dfsOverall(int curState, int curPos)
    {
        if (this.stopStateNum == curState)
        {
            if (this.stepPath.size() == this.stepSet.size())
            {
                this.addPath();
            }
        }

        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curState);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curState);
        ArrayList<Integer> edgenumbers = this.graph.getEdgeNumberList().get(curState);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curState);
        int leftTime;
        
        for (int i = 0; i < neighbours.size(); ++i)
        {
            leftTime = repeatTime.get(i);
            if (0 == leftTime)
            {
                continue;
            }
            
            boolean hasOneEdge = neighbours.indexOf(neighbours.get(i)) == 
                    neighbours.lastIndexOf(neighbours.get(i));
            
            if ((this.stateSet.contains(neighbours.get(i)) && !hasOneEdge && 
                    this.stepSet.contains(stepnumbers.get(i)))
                    ||
                    (this.stateSet.contains(neighbours.get(i)) && hasOneEdge))
            {
                repeatTime.set(i, leftTime - 1);

                this.statePath.add(curPos, neighbours.get(i));
                this.edgePath.add(curPos - 1, edgenumbers.get(i));
                this.stepPath.add(curPos - 1, stepnumbers.get(i));
                
                this.dfsOverall(neighbours.get(i), curPos + 1);

                this.statePath.remove(curPos);
                this.edgePath.remove(curPos - 1);
                this.stepPath.remove(curPos - 1);
                
                repeatTime.set(i, leftTime);
            }
        }
    }
    
    /**
     * Deep search first of partial routes
     * @param curState current state
     * @param curPos current array position
     */
    private void dfsPartial(int curState, int curPos)
    {
        ArrayList<Integer> neighbours = this.graph.getEdgeList().get(curState);
        ArrayList<Integer> edgenumbers = this.graph.getEdgeNumberList().get(curState);
        ArrayList<Integer> stepnumbers = this.graph.getEdgeStepList().get(curState);
        ArrayList<Integer> repeatTime = this.graph.getEdgeRepeatTimeList().get(curState);
        
        // current state is in stop state set
        // or 
        // no stop state specified and no neighbor
        if (this.isStopState(this.statePath.get(curPos - 1)) ||
                (0 == neighbours.size() && 0 == this.stopStateNumSet.size()))
        {
            if (this.partialFilter())
            {
                this.addPath();
            }
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
                
                this.dfsPartial(neighbours.get(i), curPos + 1);
                
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
            if (this.partialFilter())
            {
                this.addPath();
            }
        }
    }
    
    /**
     * filter partial routes
     * @return current path is a partial path or not
     */
    private boolean partialFilter()
    {        
        boolean containState = true;
        HashSet<Integer> pathStateSet = new HashSet<Integer>(this.statePath);
        Iterator<Integer> stateIter = this.stateSet.iterator();
        
        while (stateIter.hasNext())
        {
            if (!pathStateSet.contains(stateIter.next()))
            {
                containState = false;
                break;
            }
        }
        
        if (!containState)
        {
            return false;
        }
        
//        int startStepPos = this.stepPath.indexOf(this.stepList.get(0));
//        while (-1 != startStepPos)
//        {
//            int i = 0;
//            for (; i < this.stepList.size(); ++i)
//            {
//                if (this.stepPath.get(startStepPos + i) != this.stepList.get(i))
//                {
//                    break;
//                }
//            }
//            if (this.stepList.size() == i)
//            {
//                containStep = true;
//                break;
//            }
//            
//            startStepPos = this.stepPath.subList(startStepPos + 1, this.stepPath.size())
//                    .indexOf(this.startStateNum);
//        }
        boolean containStep = true;
        HashSet<Integer> pathStepSet = new HashSet<Integer>(this.stepPath);
        Iterator<Integer> stepIter = this.stepSet.iterator();
        
        while (stepIter.hasNext())
        {
            if (!pathStepSet.contains(stepIter.next()))
            {
                containStep = false;
                break;
            }
        }
        
        if (!containStep)
        {
            return false;
        }
        
        return true;
    }
}