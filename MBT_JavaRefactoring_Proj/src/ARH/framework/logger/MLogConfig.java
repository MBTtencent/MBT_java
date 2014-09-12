/*
 * FileName: MLogConfig.java
 * 
 * Description: create MLogConfig class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-16 Create
 */


package ARH.framework.logger;

import ARH.framework.basic.MBasicApi;

/**
 * MLogConfig class define some constant variables
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogConfig
{
    /**
     * Define log name, default thread id 
     */
    public final static String MLOG_NAME = MBasicApi.getProjectPath() + "/src/ARH/test/MbtLog.log";
    
    /**
     * Define log levels
     */
    public final static int MLOG_LEVEL_INFO    =   100;
    public final static int MLOG_LEVEL_WARNING  =   200;
    public final static int MLOG_LEVEL_ERROR    =   300;
    public final static int MLOG_LEVEL_FATAL    =   400;
    
    /**
     * Define log level name
     */
    public final static String MLOG_LEVEL_NAME_INFO    =   "info";
    public final static String MLOG_LEVEL_NAME_WARNING  =   "warning";
    public final static String MLOG_LEVEL_NAME_ERROR    =   "error";
    public final static String MLOG_LEVEL_NAME_FATAL    =   "fatal";
    
    /**
     * Define some constant
     */
    public final static String CR    =   "\r";
    public final static String LF    =   "\n";
    public final static String COLON    =   ":";
}