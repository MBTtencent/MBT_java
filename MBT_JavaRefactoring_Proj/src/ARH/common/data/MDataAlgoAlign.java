/*
 * FileName: MDataAlgoAlgin.java
 * 
 * Description: create MDataAlgoAlgin class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-25 Create
 */


package ARH.common.data;

import java.util.*;
import ARH.framework.basic.*;
//import ARH.framework.logger.MLogMag;

/**
 * This class is used to implement the algorithm,
 * that all the data align 
 * @author MICKCHEN
 * @version 1.0
 */
public class  MDataAlgoAlign extends MDataAlgo
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

        datasMap.clear();
        this.paramSet.clear();
        this.dataMap.clear();
        this.paramSet = paramSet;
        Integer dataNum = MDataConfig.MPARAM_MAX_NUM;

        for (int i = 0; i < paramList.size(); i++)
        {
            HashMap<String, String> paramMap = paramList.get(i);
            
            String value = paramMap.get(MDataConfig.MPARAM_VALUE);
            String name = paramMap.get(MDataConfig.MPARAM_NAME);
            
            if (!this.paramSet.contains(name))
            {
                continue;
            }
            
            int valueNum = 1;
            if (MBasicApi.isList(value))
            {
                String[] valueList = MBasicApi.getSubList(value);
                valueNum = valueList.length;
            }
            
            if (valueNum < dataNum)
            {
                dataNum = valueNum;
            }
        }

        for (int j = 0; j < dataNum; j++)
        {            
            for (int i = 0; i < paramList.size(); i++)
            {
                HashMap<String, String> paramMap = paramList.get(i);
                
                String name = paramMap.get(MDataConfig.MPARAM_NAME);
                String type = paramMap.get(MDataConfig.MPARAM_TYPE);
                String values = paramMap.get(MDataConfig.MPARAM_VALUE);
                
                if (!this.paramSet.contains(name))
                {
                    continue;
                }
                
                String[] valueList = {values};
                if (MBasicApi.isList(values))
                {
                    valueList = MBasicApi.getSubList(values);
                }
                
                String value = "";
                if (MDataUtils.getValue(valueList[j].trim(), type).isEmpty())
                {
                    value = " ";
                }
                else
                {
                    value = MDataUtils.getValue(valueList[j].trim(), type);
                }
                dataMap.put(name, value);
                datasMap.put(j, new HashMap<String, String>(dataMap));
            }
        }
        
        return datasMap;
    }
}
