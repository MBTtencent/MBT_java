/*
 * FileName: MLogUtils.java
 * 
 * Description: Create MLogUtils class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

/**
 * This class implemented some common API in this package
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogUtils
{
    /**
     * Check the level is existed
     * @param logLevel log level name
     * @return true if level exist,otherwise false
     */
    public static boolean isLevelExist(String logLevel)
    {
        int level = MLogLevel.getLevel(logLevel);
        switch (level)
        {
        case MLogConfig.MLOG_LEVEL_INFO:
        case MLogConfig.MLOG_LEVEL_WARNING:
        case MLogConfig.MLOG_LEVEL_ERROR:
        case MLogConfig.MLOG_LEVEL_FATAL:
            return true;
            
        default:
            return false;
        }
       
    }
}
