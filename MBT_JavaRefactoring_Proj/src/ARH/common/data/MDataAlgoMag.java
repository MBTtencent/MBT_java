/*
 * FileName: MDataAlgoMag.java
 * 
 * Description: Create MDataAlgoMag class
 * 
 * Version: 1.0
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-21 Create
 */

package ARH.common.data;

import java.util.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.logger.MLogMag;

/**
 * MDataAlgoMag class manage the Algorithm
 * @author MICKCHEN
 * @version 1.0
 */
public class MDataAlgoMag
{
    /**
     * hash algoMap contain the algorithms
     */
    private HashMap<String, MDataAlgo> algoMap = new HashMap<String, MDataAlgo>();
    
    /**
     * MDataAlgoMag object
     */
    private static MDataAlgoMag manage = new MDataAlgoMag();
    
    /**
     * private MDataAlgoMag
     * forbidden to use
     */
    private MDataAlgoMag()
    {
        MDataAlgoDiscards discards = new MDataAlgoDiscards();
        this.algoMap.put(MDataConfig.MDATA_ALGORITHM_DESCARTES_NAME, discards);
        
        MDataAlgoAlign algin = new MDataAlgoAlign();
        this.algoMap.put(MDataConfig.MDATA_ALGORITHM_ALGIN_NAME, algin);
        // TODO add algorithm here
    }
    
    /**
     * get instance of this class
     * 
     * @return MDataAlgoMag object
     */
    public static MDataAlgoMag getInstance()
    {
        return manage;
    }
    
    /**
     * register the algorithms
     * @param name
     * @param algotithm
     */
    public void registerAlgorithm(String name, MDataAlgo algotithm)
    {
        algoMap.put(name, algotithm);
    }
    
    /**
     * get algorithm by name
     * @param name
     * @return algorithm
     */
    public MDataAlgo getAlgorithm(String name)
    {
        name = name.toLowerCase();
        if (algoMap.containsKey(name))
        {
            return algoMap.get(name);
        }
        if (!name.trim().equals(""))
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "data algorithm \"" + name + "\" doesn't exist in MDataAlgoMag");
        }
        return algoMap.get(MDataConfig.MDATA_ALGORITHM_ALGIN_NAME);
    }
}