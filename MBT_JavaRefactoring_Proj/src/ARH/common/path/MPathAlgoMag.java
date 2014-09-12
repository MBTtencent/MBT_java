/*
 * FileName: MPathAlgoMag.java
 * 
 * Description: create MPathAlgoMag class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-29 Create
 * 1.1 SHUYUFANG 2013-09-03 Add CovShortN2N and CovLongN2N algorithms
 * 1.2 SHUYUFANG 2013-09-18 Add specified through nodes
 */


package ARH.common.path;

import java.util.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.logger.*;

/**
 * this class provides the manager of all path algorithms
 * @author SHUYUFANG
 * @version 1.1
 */
public class MPathAlgoMag
{
    /** the map of path algorithms */
    private HashMap<String, MPathAlgo> algoMap = new HashMap<String, MPathAlgo>();
    
    /** the manager object */
    private static MPathAlgoMag manager = new MPathAlgoMag();
    
    /**
     * constructor
     */
    private MPathAlgoMag()
    {
        // algorithm of covering all paths
        MPathAlgoCovAll covAll = new MPathAlgoCovAll();
        this.algoMap.put(MPathConfig.PATH_ALL, covAll);
        
        // algorithm of covering longest paths
        MPathAlgoCovLong covLong = new MPathAlgoCovLong();
        this.algoMap.put(MPathConfig.PATH_LONG, covLong);
        
        // algorithms of covering shortest paths
        MPathAlgoCovShort covShort = new MPathAlgoCovShort();
        this.algoMap.put(MPathConfig.PATH_SHORT, covShort);
        
        // algorithms of covering all paths, but the repeat time
        // of edges will be repeated at one time
        MPathAlgoCovRepeat covRepeat = new MPathAlgoCovRepeat();
        this.algoMap.put(MPathConfig.PATH_REPEAT, covRepeat);
        
        // algorithms of covering all shortest paths between each
        // pair of start node and stop node
        MPathAlgoCovShortN2N covShortN2N = new MPathAlgoCovShortN2N();
        this.algoMap.put(MPathConfig.PATH_SHORTN2N, covShortN2N);

        // algorithms of covering all longest paths between each
        // pair of start node and stop node
        MPathAlgoCovLongN2N covLongN2N = new MPathAlgoCovLongN2N();
        this.algoMap.put(MPathConfig.PATH_LONGN2N, covLongN2N);
        
        // algorithms of covering all edges with least number of paths
        MPathAlgoCovLeast covLeast = new MPathAlgoCovLeast();
        this.algoMap.put(MPathConfig.PATH_LEAST, covLeast);
        
        // algorithms of covering specified paths
        MPathAlgoCovRoute covRoute = new MPathAlgoCovRoute();
        this.algoMap.put(MPathConfig.PATH_ROUTE, covRoute);
        
        // TODO add algorithm here
    }
    
    // get the manager object
    public static MPathAlgoMag getInstance()
    {
        return manager;
    }
    
    // register new algorithm to the manager
    public void registerAlgorithm(String name, MPathAlgo algorithm)
    {
        if (null != algorithm && !algoMap.containsKey(name))
        {
            algoMap.put(name, algorithm);
        }
    }
    
    // get the specified path algorithm
    public MPathAlgo getAlgorithm(String name)
    {
        name = name.toLowerCase();
        if (algoMap.containsKey(name))
        {
            return algoMap.get(name);
        }
        if (!name.trim().equals(""))
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "path algorithm \"" + name + "\" doesn't exist in MPathAlgoMag");
        }
        return algoMap.get(MPathConfig.PATH_ALL);
    }
}