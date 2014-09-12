/*
 * FileName: MGraph.java
 * 
 * Description: create MGraph class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-20 Create
 */


package ARH.framework.graph;

import java.util.*;


/**
 * this class defines graph object
 * containing nodes, edges and some common operation
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGraph
{
    /** node map, key: nodeName, value: nodeNumber */
    private HashMap<String, Integer> nodeNumMap;
    
    /** node map, key: nodeNumber, value: nodeName */
    private HashMap<Integer, String> nodeNameMap;
    
    /** adjacency list of edge end node */
    private ArrayList<ArrayList<Integer>> edgeList;
    
    /** adjacency list of edge repeat time */
    private ArrayList<ArrayList<Integer>> edgeRepeatTimeList;
    
    /** adjacency list of edge number */
    private ArrayList<ArrayList<Integer>> edgeNumberList;
    
    /** adjacency list of edge name */
    private ArrayList<ArrayList<String>> edgeNameList;
    
    /** adjacency list of the number of step of the edge */
    private ArrayList<ArrayList<Integer>> edgeStepList;
    
    /** step map, key: stepName, value: stepNumber */
    private HashMap<String, Integer> stepNumMap;
    
    /** the number of edges */
    private int edgeCount;
    
    /** the number of nodes */
    private int nodeCount;
    
    /**
     * constructor
     */
    public MGraph()
    {
        this.nodeNumMap = new HashMap<String, Integer>();
        this.nodeNameMap = new HashMap<Integer, String>();
        this.edgeList = new ArrayList<ArrayList<Integer>>();
        this.edgeRepeatTimeList = new ArrayList<ArrayList<Integer>>();
        this.edgeNumberList = new ArrayList<ArrayList<Integer>>();
        this.edgeNameList = new ArrayList<ArrayList<String>>();
        this.edgeStepList = new ArrayList<ArrayList<Integer>>();
        this.stepNumMap = new HashMap<String, Integer>();
        this.edgeCount = 1;
        this.nodeCount = 0;
    }
    
    /**
     * add node to the graph
     * @param nodeName the name of the node
     * @return node number in the graph
     */
    public int addNode(String nodeName)
    {
        if (!this.nodeNumMap.containsKey(nodeName))
        {
            this.nodeNumMap.put(nodeName, this.nodeCount);
            this.nodeNameMap.put(this.nodeCount, nodeName);
            this.edgeList.add(new ArrayList<Integer>());
            this.edgeRepeatTimeList.add(new ArrayList<Integer>());
            this.edgeNumberList.add(new ArrayList<Integer>());
            this.edgeNameList.add(new ArrayList<String>());
            this.edgeStepList.add(new ArrayList<Integer>());
            ++this.nodeCount;
        }
        return this.nodeNumMap.get(nodeName);
    }
    
    /**
     * add edge with repeat time to the graph
     * @param begNodeName the name of the begin node(s) of the edge
     * @param endNodeName the name of the end node of the edge
     * @param stepName the name of the edge
     * @param stepNumber the number of the step
     * @param repeatTime the repeat time of the edge
     */
    public void addEdge(String begNodeName, String endNodeName, String stepName, int stepNumber, 
            int repeatTime)
    {
        begNodeName = begNodeName.trim();
        endNodeName = endNodeName.trim();
        int begNodeNum = this.addNode(begNodeName);
        int endNodeNum = this.addNode(endNodeName);
        this.edgeList.get(begNodeNum).add(endNodeNum);
        if (repeatTime < 1)
        {
            repeatTime = 1;
        }
        this.edgeRepeatTimeList.get(begNodeNum).add(repeatTime);
        this.edgeNameList.get(begNodeNum).add(stepName);
        this.edgeNumberList.get(begNodeNum).add(this.edgeCount);
        this.edgeStepList.get(begNodeNum).add(stepNumber);
        this.stepNumMap.put(stepName, stepNumber);
        ++this.edgeCount;
    }
    
    /**
     * add edge to the graph
     * @param begNodeName the begin node name(s) of the edge
     * @param endNodeName the end node name(s) of the edge
     * @param stepName the name of the edge
     * @param stepNumber the number of the step
     */
    public void addEdge(String begNodeName, String endNodeName, String stepName, int stepNumber)
    {
        this.addEdge(begNodeName, endNodeName, stepName, stepNumber, 1);
    }
    
    /**
     * get node name by node number
     * @param nodeNum the number of the node
     * @return node name
     */
    public String getNodeName(int nodeNum)
    {
        if (this.nodeNameMap.containsKey(nodeNum))
        {
            return this.nodeNameMap.get(nodeNum);
        }
        return null;
    }
    
    /**
     * get node number by node name
     * @param nodeName the name of the node
     * @return node number
     */
    public int getNodeNum(String nodeName)
    {
        if (this.nodeNumMap.containsKey(nodeName))
        {
            return this.nodeNumMap.get(nodeName);
        }
        return -1;
    }
    
    /**
     * get edge list of the graph
     * @return edge list
     */
    public ArrayList<ArrayList<Integer>> getEdgeList()
    {
        return this.edgeList;
    }
    
    /**
     * get edge repeat time list of the graph
     * @return edge repeat time list
     */
    public ArrayList<ArrayList<Integer>> getEdgeRepeatTimeList()
    {
        return this.edgeRepeatTimeList;
    }
    
    /**
     * get edge number list of the graph
     * @return edge number list
     */
    public ArrayList<ArrayList<Integer>> getEdgeNumberList()
    {
        return this.edgeNumberList;
    }
    
    /**
     * get step number list of edges of the graph
     * @return step number list
     */
    public ArrayList<ArrayList<Integer>> getEdgeStepList()
    {
        return this.edgeStepList;
    }
    
    /**
     * get edge name list of edges of the graph
     * @return edge name list
     */
    public ArrayList<ArrayList<String>> getEdgeNameList()
    {
        return this.edgeNameList;
    }
    
    /**
     * get the number of nodes of the graph
     * @return the number of nodes
     */
    public int getNodeCount()
    {
        return this.nodeCount;
    }

    /**
     * get the number of edges of the graph
     * @return the number of edges
     */
    public int getEdgeCount()
    {
        return this.edgeCount;
    }
    
    /**
     * get the map of step
     * @return the map of step
     */
    public HashMap<String, Integer> getStepNumMap()
    {
        return this.stepNumMap;
    }
}