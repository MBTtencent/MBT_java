/*
 * FileName: MLogAppender.java
 * 
 * Description: Create MLogAppender class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

import ARH.framework.exception.MException;

/**
 * This class is the base class of log appenders
 * @author MICKCHEN
 * @version 1.0
 */
public abstract class MLogAppender
{
    /**
     * Define log level filter
     */
    protected int levelFilter;
    
    /**
     * Define log layout
     */
    protected MLogLayout layout;
    
    /**
     * Constructor
     */
    public MLogAppender()
    {
        this.levelFilter = MLogConfig.MLOG_LEVEL_INFO;
    }
    
    /**
     * Constructor
     * @param layout layout of the log
     */
    public MLogAppender(MLogLayout layout)
    {
        this.layout = layout;
        this.levelFilter = MLogConfig.MLOG_LEVEL_INFO;
    }
    
    /**
     * Constructor
     * @param layout layout of the log
     * @param logFilter log level filter
     */
    public MLogAppender(MLogLayout layout, int logFilter)
    {
        this.layout = layout;
        this.levelFilter = logFilter;
    }
    
    /**
     * Get level filter
     * @return level filter
     */
    public int getLevelFilter()
    {
        return this.levelFilter;
    }
    
    /**
     * Set level filter
     * @param level level filter
     */
    public void setLevelFilter(int level)
    {
        this.levelFilter = level;
    }
    
    /**
     * Write log
     * @param logEvent the log event
     */
    public void writeLog(MLogEvent logEvent)
    {
        if (logEvent == null)
        {
            return;
        }
        
        if (logEvent.getLogLevel() >= levelFilter)
        {
            write(logEvent);
        }
    }
    
    /**
     * Abstract function, implemented in the derived class
     * @param logEvent
     * @return
     * @throws MException 
     */
    abstract protected void write (MLogEvent logEvent);
}