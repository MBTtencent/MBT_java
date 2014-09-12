/*
 * FileName: MPathAlgo.java
 * 
 * Description: create MPathAlgo class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-29 Create
 * 1.1 SHUYUFANG 2013-09-03 Add specified stop nodes
 * 1.2 SHUYUFANG 2013-09-18 Add specified through nodes
 */


package ARH.common.path;

import java.util.*;

import ARH.common.config.MCommonConfig;
import ARH.framework.graph.*;

/**
 * this class is a abstract class 
 * representing a path algorithm
 * @author SHUYUFANG
 * @version 1.2
 */
public abstract class MPathAlgo
{
    /** the result map of generated paths */
    protected HashMap<String, ArrayList<ArrayList<Integer>>> pathsMap;
    
    /** the graph of the scenario */
    protected MGraph graph;
    
    /** the path of node number */
    protected ArrayList<Integer> statePath;
    
    /** the path of edge number */
    protected ArrayList<Integer> edgePath;
    
    /** the path of step number */
    protected ArrayList<Integer> stepPath;
    
    /** the specified start node set */
    protected HashSet<Integer> startStateNumSet;
    
    /** the specified stop node set */
    protected HashSet<Integer> stopStateNumSet;
    
    /** the specified through node set */
    protected HashSet<Integer> throughStateNumSet;
    
    /** the specified through edge set */
    protected HashSet<Integer> throughStepNumSet;
    
    /** the specified route list */
    protected ArrayList<HashMap<String, ArrayList<Integer>>> routeList;
    
    /**
     * constructor
     */
    public MPathAlgo()
    {
        this.statePath = new ArrayList<Integer>();
        this.edgePath = new ArrayList<Integer>();
        this.stepPath = new ArrayList<Integer>();
        
        this.startStateNumSet = new HashSet<Integer>();
        this.stopStateNumSet = new HashSet<Integer>();
        this.throughStateNumSet = new HashSet<Integer>();
        this.throughStepNumSet = new HashSet<Integer>();
        this.routeList = new ArrayList<HashMap<String, ArrayList<Integer>>>();
    }
    
    /**
     * initialize some algorithm variables
     */
    protected void init(MGraph graph, 
            ArrayList<String> startStateNameList, ArrayList<String> stopStateNameList,
            ArrayList<String> throughStateNameList, ArrayList<String> throughStepNameList,
            ArrayList<HashMap<String, ArrayList<String>>> routeList)
    {
        this.graph = graph;
        
        this.pathsMap = new HashMap<String, ArrayList<ArrayList<Integer>>>();
        this.pathsMap.put(MPathConfig.STATE_PATH, new ArrayList<ArrayList<Integer>>());
        this.pathsMap.put(MPathConfig.EDGE_PATH, new ArrayList<ArrayList<Integer>>());
        this.pathsMap.put(MPathConfig.STEP_PATH, new ArrayList<ArrayList<Integer>>());

        this.statePath.clear();
        this.edgePath.clear();
        this.stepPath.clear();
        
        this.genStartStateNumSet(startStateNameList);
        this.genStopStateNumSet(stopStateNameList);
        this.genThroughStateNumSet(throughStateNameList);
        this.genThroughStepNumSet(throughStepNameList);
        this.genRouteList(routeList);
    }
    
    /**
     * add paths to result map 
     */
    protected void addPath()
    {        
        this.pathsMap.get(MPathConfig.STATE_PATH).add(new ArrayList<Integer>(this.statePath));
        this.pathsMap.get(MPathConfig.EDGE_PATH).add(new ArrayList<Integer>(this.edgePath));
        this.pathsMap.get(MPathConfig.STEP_PATH).add(new ArrayList<Integer>(this.stepPath));
    }
    
    /**
     * whether the specified node in the stop node set
     * @param nodeNum the specified node number
     * @return node in the set or not
     */
    protected boolean isStopState(int nodeNum)
    {
        return this.stopStateNumSet.contains(nodeNum);
    }
    
    /**
     * generate stop state number set according to name list
     * @param stopNodeNameList the list of node name
     */
    protected void genStopStateNumSet(ArrayList<String> stopStateNameList)
    {        
        this.stopStateNumSet.clear();
        
        if (null == stopStateNameList || 0 == stopStateNameList.size()) 
        {
            return;
        }

        for (String stopStateName : stopStateNameList)
        {
            int stopStateNum = this.graph.getNodeNum(stopStateName);
            if (-1 != stopStateNum)
            {
                this.stopStateNumSet.add(stopStateNum);
            }
        }
    }
    
    /**
     * generate start state number set according to name list
     * @param startNodeNameList the list of node name
     */
    protected void genStartStateNumSet(ArrayList<String> startStateNameList)
    {
        this.startStateNumSet.clear();
        
        if (null != startStateNameList && 0 < startStateNameList.size())
        {
            for (String startStateName : startStateNameList)
            {
                int startStateNum = this.graph.getNodeNum(startStateName);
                if (-1 != startStateNum)
                {
                    this.startStateNumSet.add(startStateNum);
                }
            }
        }
        else
        {
            int StateCount = this.graph.getNodeCount();
            boolean[] isStartStateArray = new boolean[StateCount];
            for (int i = 0; i < StateCount; ++i)
            {
                isStartStateArray[i] = true;
            }
            
            ArrayList<ArrayList<Integer>> edgeList = this.graph.getEdgeList();
            for (int i = 0; i < edgeList.size(); ++i)
            {
                for (int j = 0; j < edgeList.get(i).size(); ++j)
                {
                    int idx = edgeList.get(i).get(j);
                    isStartStateArray[idx] = false;
                }
            }
            
            for (int i = 0; i < StateCount; ++i)
            {
                if (isStartStateArray[i])
                {
                    this.startStateNumSet.add(i);
                }
            }
        }
    }
    
    /**
     * generate through state number set according to name list
     * @param throughNodeNameList the list of node name
     */
    protected void genThroughStateNumSet(ArrayList<String> throughStateNameList)
    {
        this.throughStateNumSet.clear();
        
        if (null == throughStateNameList || 0 == throughStateNameList.size()) 
        {
            return;
        }

        for (String throughStateName : throughStateNameList)
        {
            int throughStateNum = this.graph.getNodeNum(throughStateName);
            if (-1 != throughStateNum)
            {
                this.throughStateNumSet.add(throughStateNum);
            }
        }
    }
    
    /**
     * generate through step number set according to name list
     * @param throughEdgeNameList the list of edge name
     */
    protected void genThroughStepNumSet(ArrayList<String> throughStepNameList)
    {
        this.throughStepNumSet.clear();
        
        if (null == throughStepNameList || 0 == throughStepNameList.size())
        {
            return;
        }
        
        HashMap<String, Integer> stepNumMap = this.graph.getStepNumMap();
        
        for (String throughStepName : throughStepNameList)
        {
            if (stepNumMap.containsKey(throughStepName))
            {
                this.throughStepNumSet.add(stepNumMap.get(throughStepName));
            }
        }
    }
    
    protected void genRouteList(ArrayList<HashMap<String, ArrayList<String>>> routeList)
    {
        this.routeList.clear();
        
        if (null == routeList || 0 == routeList.size())
        {
            return;
        }

        HashMap<String, Integer> stepNumMap = this.graph.getStepNumMap();
        
        for (int i = 0; i < routeList.size(); ++i)
        {
            this.routeList.add(new HashMap<String, ArrayList<Integer>>());
            this.routeList.get(i).put(MCommonConfig.MBT_XML_STATE, new ArrayList<Integer>());
            this.routeList.get(i).put(MCommonConfig.MBT_XML_STEP, new ArrayList<Integer>());
            this.routeList.get(i).put(MCommonConfig.MBT_XML_COV, new ArrayList<Integer>());
            
            ArrayList<String> tmpStateList = routeList.get(i).get(MCommonConfig.MBT_XML_STATE);
            for (String stateStr : tmpStateList)
            {
                int stateNum = this.graph.getNodeNum(stateStr);
                if (-1 != stateNum)
                {
                    this.routeList.get(i).get(MCommonConfig.MBT_XML_STATE).add(stateNum);
                }
            }
            
            ArrayList<String> tmpStepList = routeList.get(i).get(MCommonConfig.MBT_XML_STEP);
            for (String stepStr : tmpStepList)
            {
                if (stepNumMap.containsKey(stepStr))
                {
                    this.routeList.get(i).get(MCommonConfig.MBT_XML_STEP).add(stepNumMap.get(stepStr));
                }
            }
            
            String tmpCovStr = routeList.get(i).get(MCommonConfig.MBT_XML_COV).get(0);
            if (0 == tmpCovStr.trim().compareTo("partial"))
            {
                this.routeList.get(i).get(MCommonConfig.MBT_XML_COV).add(0);
            }
            else
            {
                this.routeList.get(i).get(MCommonConfig.MBT_XML_COV).add(1);
            }
        }
    }
    
    /**
     * delete paths which don't contain any one of the through states
     */
    protected void delPathWithoutThroughState()
    {
        if (0 == this.throughStateNumSet.size())
        {
            return;
        }
        
        ArrayList<ArrayList<Integer>> nodePathLists = this.pathsMap.get(MPathConfig.STATE_PATH);
        ArrayList<ArrayList<Integer>> edgePathLists = this.pathsMap.get(MPathConfig.EDGE_PATH);
        ArrayList<ArrayList<Integer>> stepPathLists = this.pathsMap.get(MPathConfig.STEP_PATH);
        
        for (int i = nodePathLists.size() - 1; i >= 0; --i)
        {
            boolean contain = false;
            for (int j = 0; j < nodePathLists.get(i).size(); ++j)
            {
                if (this.throughStateNumSet.contains(nodePathLists.get(i).get(j)))
                {
                    contain = true;
                    break;
                }
            }
            if (!contain)
            {
                nodePathLists.remove(i);
                edgePathLists.remove(i);
                stepPathLists.remove(i);
            }
        }
    }

    /**
     * delete paths which don't contain any one of the through steps
     */
    protected void delPathWithoutThroughStep()
    {
        if (0 == this.throughStepNumSet.size())
        {
            return;
        }
        
        ArrayList<ArrayList<Integer>> nodePathLists = this.pathsMap.get(MPathConfig.STATE_PATH);
        ArrayList<ArrayList<Integer>> edgePathLists = this.pathsMap.get(MPathConfig.EDGE_PATH);
        ArrayList<ArrayList<Integer>> stepPathLists = this.pathsMap.get(MPathConfig.STEP_PATH);
        
        for (int i = stepPathLists.size() - 1; i >= 0; --i)
        {
            boolean contain = false;
            for (int j = 0; j < stepPathLists.get(i).size(); ++j)
            {
                if (this.throughStepNumSet.contains(stepPathLists.get(i).get(j)))
                {
                    contain = true;
                    break;
                }
            }
            if (!contain)
            {
                nodePathLists.remove(i);
                edgePathLists.remove(i);
                stepPathLists.remove(i);
            }
        }
    }
    
    /**
     * specific method to generate the path
     * @param graph the graph object
     * @param startNodeNameList the list of start nodes
     * @param stopNodeNameList the list of stop nodes
     * @param throughNodeNameList the list of through nodes
     * @param throughStepNameList the list of through steps
     * @return the result map
     */
    abstract public HashMap<String, ArrayList<ArrayList<Integer>>> genPath(MGraph graph, 
            ArrayList<String> startStateNameList, ArrayList<String> stopStateNameList,
            ArrayList<String> throughStateNameList, ArrayList<String> throughStepNameList,
            ArrayList<HashMap<String, ArrayList<String>>> routeList);
}