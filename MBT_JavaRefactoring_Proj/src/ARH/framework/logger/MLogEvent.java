/*
 * FileName: MLogEvent.java
 * 
 * Description: Create MLogEvent class
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

import ARH.framework.basic.MBasicApi;

/**
 * This class define the log event
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogEvent
{
    /**
     * Log event time
     */
    private String time;
    
    /**
     * log level
     */
    private int logLevel;
    
    /**
     * log message
     */
    private String logMessage;
    
    /**
     * Constructor
     * @param level
     * @param logMessage
     */
    public MLogEvent(int level, String logMessage)
    {
        this.logLevel = level;
        this.logMessage = logMessage;
        this.time = MBasicApi.getCurrentTime();
    }
    
    /**
     * get log level
     */
    public int getLogLevel()
    {
        return this.logLevel;
    }
    
    /**
     * get log message
     * @return log message
     */
    public String getLogMsg()
    {
        return this.time + MLogConfig.LF + this.logMessage + MLogConfig.LF;
    }
}