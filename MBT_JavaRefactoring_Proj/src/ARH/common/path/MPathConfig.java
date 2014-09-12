/*
 * FileName: MPathConfig.java
 * 
 * Description: create MPathConfig class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-28 Create
 * 1.1 SHUYUFANG 2013-09-03 Add CovShortN2N and CovLongN2N algorithms
 */


package ARH.common.path;


/**
 * this class defines some basic constant variables
 * @author SHUYUFANG
 * @version 1.1
 */
public class MPathConfig
{
    /**
     * define path algorithm type
     */
    public static final String PATH_ALL        =   "all";
    public static final String PATH_LONG       =   "long";
    public static final String PATH_SHORT      =   "short";
    public static final String PATH_REPEAT     =   "repeat";
    public static final String PATH_SHORTN2N   =   "shortn2n";
    public static final String PATH_LONGN2N    =   "longn2n";
    public static final String PATH_LEAST      =   "least";
    public static final String PATH_ROUTE      =   "route";
    
    /**
     * define maximum and minimum path distance
     */
    public static final int MAX_PATH_DIST   =   99999999;
    public static final int MIN_PATH_DIST   =   -1;
    
    /**
     * define different types of path
     */
    public static final String STATE_PATH    =   "statepath";
    public static final String EDGE_PATH    =   "edgepath";
    public static final String STEP_PATH    =   "steppath";
}