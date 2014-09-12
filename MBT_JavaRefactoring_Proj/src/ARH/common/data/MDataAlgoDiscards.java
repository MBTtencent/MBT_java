/*
 * FileName: MDataAlgoDiscards.java
 * 
 * Description: create MDataAlgoDiscards class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-25 Create
 */

package ARH.common.data;

import java.util.*;
import ARH.framework.basic.*;
//import ARH.framework.logger.MLogMag;

/**
 * MDataAlgoDiscards class implement the discards algorithm
 * @author MICKCHEN
 * @version 1.0
 */
public class MDataAlgoDiscards extends MDataAlgo
{    
    @Override
    public HashMap<Integer, HashMap<String, String>> genData(ArrayList<HashMap<String, String>> paramList, 
            Set<String> paramSet)
    {
        if (paramList == null || paramList.size() == 0)
        {
//            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
//                    "The parameter list is null.");
            return null;
        }
        
        ArrayList<HashMap<String, String>> tmpParamList = new ArrayList<HashMap<String, String>>(paramList);
        
        this.datasMap.clear();
        this.paramSet.clear();
        this.paramSet = new HashSet<String>(paramSet);
        
        for (int i = 0; i < tmpParamList.size(); i++)
        {
            String name = tmpParamList.get(i).get(MDataConfig.MPARAM_NAME);
            if (!this.paramSet.contains(name))
            {
                tmpParamList.remove(i);
                i--;
            }
        }
        
        if (tmpParamList.size() > 0)
        {
            getAlgoDiscardsResult(tmpParamList, 0);
        }

        return datasMap;
    }
    
    /**
     * realize discards algorithm to get the result
     * @param dataList
     * @param num
     */
    private void getAlgoDiscardsResult(ArrayList<HashMap<String, String>> dataList, int num)
    {
        HashMap<String, String> paramMap = dataList.get(num);
        String name = paramMap.get(MDataConfig.MPARAM_NAME);
        String type = paramMap.get(MDataConfig.MPARAM_TYPE);
        String values = paramMap.get(MDataConfig.MPARAM_VALUE);
        
        String[] valueList = {values};
        if (MBasicApi.isList(values))
        {
            valueList = MBasicApi.getSubList(values);
        }
        
//        dataMap.clear();
        for (int i = 0; i < valueList.length; i++)
        {
            if ((num + 1) == dataList.size())
            {
                String value = "";
                if (MDataUtils.getValue(valueList[i].trim(), type).isEmpty())
                {
                    value = " ";
                }
                else
                {
                    value = MDataUtils.getValue(valueList[i].trim(), type);
                }
                
                dataMap.put(name, value);
                datasMap.put(datasMap.size(), new HashMap<String, String>(dataMap));
                
                continue;
            }
            
            if (dataList.size() > (num + 1))
            {
                String value = "";
                if (MDataUtils.getValue(valueList[i].trim(), type).isEmpty())
                {
                    value = " ";
                }
                else
                {
                    value = MDataUtils.getValue(valueList[i].trim(), type);
                }
                
                dataMap.put(name,value);
                getAlgoDiscardsResult(dataList, num + 1);
            }
        }
    }
}