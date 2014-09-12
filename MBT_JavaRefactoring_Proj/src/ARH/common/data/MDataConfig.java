/*
 * FileName: MDataConfig.java
 * 
 * Description: create MDataConfig class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-23 Create
 */


package ARH.common.data;

/**
 * MDataConfig class is the basic class of data algorithm
 * @author MICKCHEN
 * @version 1.0
 */
public class MDataConfig
{    
    /**
     * define the algorithm's name
     */
    public static final String MDATA_ALGORITHM_DESCARTES_NAME = "discards";
    public static final String MDATA_ALGORITHM_ALGIN_NAME = "align";
    
    /**
     * define parameter attribute
     */
    public static final String MPARAM_NAME    = "name";
    public static final String MPARAM_VALUE    = "value";
    public static final String MPARAM_TYPE    = "type";
    
    /**
     * define data type
     */
    public static final String MDATA_TYPE_STRING    = "string";
    public static final String MDATA_TYPE_NUMBER    = "number";
    public static final String MDATA_TYPE_SCRIPT    = "script";
    
    public static final int MPARAM_MAX_NUM    = 10000;
    
}
