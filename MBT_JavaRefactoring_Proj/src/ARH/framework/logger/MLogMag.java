/*
 * FileName: MLogMag.java
 * 
 * Description: Create MLogMag class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

import java.util.HashMap;

/**
 * This class is used to manage log objects
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogMag
{
    /**
     * Define log manager
     */
    private static MLogMag logMag = new MLogMag();
    
    /**
     * This map contain the logger objects
     */
    private static HashMap<String, MLogger> logMap = new HashMap<String, MLogger>();

    /**
     * private constructor, forbidden to use
     */
    private MLogMag()
    {
    }
    
    /**
     * get logger instance
     * @param logLevel log level
     * @return log object
     */
    public static MLogMag getInstance()
    {
        return logMag;
    }
    
    /**
     * Get logger
     * @param loglevel log level name
     * @return logger
     */
    public MLogger getLogger(String logName)
    {
        if (logMap.containsKey(logName))
        {
            return logMap.get(logName);
        }
        else
        {
            MLogger logger = new MLogger();
            logMap.put(logName, logger);
            return logger;
        }
    }
    
    /**
     * Get logger, it's name is thread id
     * @return logger
     */
    public MLogger getLogger()
    {
        return getLogger(String.valueOf(Thread.currentThread().getId()));
    }
    
    
    /**
     * remove log name
     * @param logName
     */
    public void removeLogger(String logName)
    {
        logMap.remove(logName);
    }
    
    /**
     * clear the log map
     */
    public void clear()
    {
        logMap.clear();
    }
}