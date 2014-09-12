/*
 * FileName: MDataUtils.java
 * 
 * Description: create MDataUtils class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-23 Create
 */


package ARH.common.data;

/**
 * MDataUtils class define some basic function use in this package
 * @author MICKCHEN
 * @version 1.0
 */
public class MDataUtils
{
    /**
     * check the algorithm is defined 
     * @param algo
     * @return
     */
    public static boolean isAlgoExisted(String algo)
    {
        if (algo.equalsIgnoreCase(MDataConfig.MDATA_ALGORITHM_DESCARTES_NAME))
        {
            return true;
        }
        else if(algo.equalsIgnoreCase(MDataConfig.MDATA_ALGORITHM_ALGIN_NAME))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * get value by the s and s's type
     * @param s
     * @param type
     * @return value of s
     */
    public static String getValue(String s, String type)
    {
        return s;
//        type = type.trim().toLowerCase();
//        if (type.endsWith(MDataConfig.MDATA_TYPE_STRING))
//        {
//            return s;
//        }
//        else if (type.endsWith(MDataConfig.MDATA_TYPE_NUMBER))
//        {
//            return s;
//        }
//        else if (type.endsWith(MDataConfig.MDATA_TYPE_SCRIPT))
//        {
//            return s;
//        }
//        else
//        {
//            return s;
//        }
    }
}