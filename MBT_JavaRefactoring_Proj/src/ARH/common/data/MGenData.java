/*
 * FileName: MGenData.java
 * 
 * Description: create MGenData class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-25 Create
 */

package ARH.common.data;

import java.util.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.*;
import ARH.framework.logger.MLogMag;

/**
 * This class is used to generate the data
 * @author MICKCHEN
 * @version 1.0
 */
public class MGenData
{
    /**
     * generate data result by MBT XML and algorithm
     * @param mbtXml the XML file of MBT script
     * @param algo algorithm of generate data
     * @return data map
     * HashMap<number, HashMap<variable-name, variable-value>>
     */
    public static HashMap<Integer, HashMap<String, String>> genData(String mbtXml, String scenario,
            String algo, Set<String> paramSet)
    {
        if (!MDataUtils.isAlgoExisted(algo))
        {
            algo = MDataConfig.MDATA_ALGORITHM_ALGIN_NAME;
        }
        if (paramSet == null)
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
                    "The parameter set is null.");
            return null;
        }
        else if(paramSet.size() == 0)
        {
            return new HashMap<Integer, HashMap<String, String>>();
        }
        
        try
        {
            MParseParam parseParam = new MParseParam(mbtXml);
            ArrayList<HashMap<String, String>> paramList = parseParam.parseParam().get(scenario);

            return MDataAlgoMag.getInstance().getAlgorithm(algo).genData(paramList, paramSet);
        } 
        catch (MException e) 
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
                    e.getMessage());
            return null;
        }
    }
     
}