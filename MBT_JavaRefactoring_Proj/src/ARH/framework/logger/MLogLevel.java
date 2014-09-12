/*
 * FileName: MLogLevel.java
 * 
 * Description: create MLogLevel class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-18  create by MICKCHEN
 */


package ARH.framework.logger;

/**
 * This class define functions to get/set log level
 * @author MICKCHEN
 * @version 1.0
 */
public class MLogLevel
{
    /**
     * Define log level
     */
    private int level;
    
    /**
     * Constructor
     * @param logLevel log level name
     */
    public MLogLevel(String logLevel)
    {
        setLevel(logLevel);
    }
    
    /**
     * get log level
     * @return log level
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * get level
     * @param level level name
     * @return level
     */
    public static int getLevel(String level)
    {
      if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_INFO))
      {
          return MLogConfig.MLOG_LEVEL_INFO;
      }
      else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_WARNING))
      {
          return MLogConfig.MLOG_LEVEL_WARNING;
      }
      else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_ERROR))
      {
          return MLogConfig.MLOG_LEVEL_ERROR;
      }
      else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_FATAL))
      {
          return MLogConfig.MLOG_LEVEL_FATAL;
      }
      else
      {
          return MLogConfig.MLOG_LEVEL_INFO;
      }
    }
    
    /**
     * set level
     * @param level level name
     */
    public void setLevel(String level)
    {
        if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_INFO))
        {
            setLevel(MLogConfig.MLOG_LEVEL_INFO);
        }
        else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_WARNING))
        {
            setLevel(MLogConfig.MLOG_LEVEL_WARNING);
        }
        else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_ERROR))
        {
            setLevel(MLogConfig.MLOG_LEVEL_ERROR);
        }
        else if (level.equalsIgnoreCase(MLogConfig.MLOG_LEVEL_NAME_FATAL))
        {
            setLevel(MLogConfig.MLOG_LEVEL_FATAL);
        }
        else
        {
            setLevel(MLogConfig.MLOG_LEVEL_INFO);
        }
    }
    
    /**
     * set level 
     * @param level
     */
    private void setLevel(int level)
    {
        this.level = level;
    }
}