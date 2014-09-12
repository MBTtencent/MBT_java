/*
 * FileName: MLogger.java
 * 
 * Description: Create MLogger class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */

package ARH.framework.logger;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ARH.framework.logger.MLogConfig;

/**
 * This class is used to logger
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogger
{
    /**
     * Define lowest level
     */
    private int lowestLevel;

    /**
     * List of log appender
     */
    private ArrayList<MLogAppender> logList;
    
    /**
     * The read/write lock
     */
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Define write lock
     */
    private Lock wLock = lock.writeLock();
    
    /**
     * Constructor
     * @param logLevel
     */
    public MLogger()
    {
        this.lowestLevel = MLogConfig.MLOG_LEVEL_FATAL;
        this.logList = new ArrayList<MLogAppender>();
    }
    
    /**
     * Add appender into log list
     * @param appender log appender
     * @return true if add success,otherwise false
     */
    public boolean addAppender(MLogAppender appender)
    {
        if (appender == null)
        {
            return false;
        }
        
        wLock.lock();
        for (int i = 0; i < logList.size(); i++)
        {
            if (logList.get(i) == appender)
            {
                return false;
            }
        }
        
        logList.add(appender);
        if (lowestLevel > appender.getLevelFilter())
        {
            lowestLevel = appender.getLevelFilter();
        }
        
        wLock.lock();
        return true;
    }
    
    /**
     * remove log appender
     * @param appender log appender
     */
    public void removeAppender(MLogAppender appender)
    {
        wLock.lock();
        logList.remove(appender);
        
        lowestLevel = MLogConfig.MLOG_LEVEL_FATAL;
        for (int i = 0; i < logList.size(); i++)
        {
            if (lowestLevel > logList.get(i).getLevelFilter())
            {
                lowestLevel = logList.get(i).getLevelFilter();
            }
        }
        
        wLock.unlock();
    }
    
    /**
     * set log level
     * @param level log level
     */
    public void setLogLevel(int level)
    {
        wLock.lock();
        
        for (int i = 0; i < logList.size(); i++)
        {
            logList.get(i).setLevelFilter(level);
        }
        
        wLock.unlock();
        
        if (lowestLevel > level)
        {
            lowestLevel = level;
        }
    }
    
    /**
     * Output information
     * @param msg message content
     */
    public void info(String msg)
    {
        writeLog(MLogConfig.MLOG_LEVEL_INFO, 
                MLogConfig.MLOG_LEVEL_NAME_INFO + MLogConfig.COLON + msg);
    }
    
    /**
     * Output warning
     * @param msg message content
     */
    public void warning(String msg)
    {
        writeLog(MLogConfig.MLOG_LEVEL_WARNING, 
                MLogConfig.MLOG_LEVEL_NAME_WARNING + MLogConfig.COLON + msg);
    }
    
    /**
     * Output error
     * @param msg message content
     */
    public void error(String msg)
    {
        writeLog(MLogConfig.MLOG_LEVEL_ERROR, 
                MLogConfig.MLOG_LEVEL_NAME_ERROR + MLogConfig.COLON + msg);
    }
    
    /**
     * Output fatal
     * @param msg message content
     */
    public void fatal(String msg)
    {
        writeLog(MLogConfig.MLOG_LEVEL_FATAL, 
                MLogConfig.MLOG_LEVEL_NAME_FATAL + MLogConfig.COLON + msg);
    }
    
    /**
     * Write log
     * @param info the content will write into the log
     * @return true if write success,otherwise false
     */
    private void writeLog(int level, String msg)
    {
        MLogEvent logEvent = new MLogEvent(level, msg);
        
        wLock.lock();
        for (int i = 0; i < logList.size(); i++)
        {
            logList.get(i).writeLog(logEvent);
        }
        wLock.unlock();
    }
}
