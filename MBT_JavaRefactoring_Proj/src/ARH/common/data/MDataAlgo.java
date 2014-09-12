/*
 * FileName: MDataAlgo.java
 * 
 * Description: create MDataAlgo class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-23 Create
 */


package ARH.common.data;

import java.util.*;

/**
 * This class is the basic class of data algorithm
 * @author MICKCHEN
 * @version 1.0
 */
public abstract class MDataAlgo
{
    /**
     * Define map to contain result
     * HashMap<scenario, HashMap<variable, variable-value>>
     */
    public HashMap<Integer, HashMap<String, String>> datasMap = new 
    HashMap<Integer, HashMap<String, String>>();
    
    /**
     * Define data map to contain one data
     * HashMap<variable, variable-value>
     */
    public HashMap<String, String> dataMap = new HashMap<String, String>();
    
    /**
     * Parameter name set
     */
    public Set<String> paramSet = new HashSet<String>(); 
    
    /**
     * generate the data by the parameter and type
     */
    abstract public HashMap<Integer, HashMap<String, String>> genData(ArrayList<HashMap<String, String>> argMap, 
            Set<String> paramSet);
}
