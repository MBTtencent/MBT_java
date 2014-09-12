/*
 * FileName: MFileAppender.java
 * 
 * Description: Create MFileAppender class
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

import ARH.framework.exception.MException;
import ARH.framework.file.MFile;

/**
 * This class is used to output log in file 
 * @author MICKCHEN
 * @version 1.0
 */
public class MFileAppender extends MLogAppender
{
    /**
     * Define file path
     */
    private String file; 
    
    public MFileAppender()
    {
        this.file = MLogConfig.MLOG_NAME;
        this.levelFilter = MLogConfig.MLOG_LEVEL_INFO;
    }
    
    /**
     * Constructor
     * @param file log file path
     */
    public MFileAppender(String file)
    {
        this.file = file;
        this.levelFilter = MLogConfig.MLOG_LEVEL_INFO;
    }
    
    /**
     * Constructor
     * @param file log file path
     * @param logLayout layout of the log
     */
    public MFileAppender(String file, MLogLayout layout)
    {
        this.file = file;
        this.layout = layout;
        this.levelFilter = MLogConfig.MLOG_LEVEL_INFO;
    }
    
    /**
     * Constructor
     * @param file log file path
     * @param logLayout layout of the log
     * @param levelFilter level filter
     */
    public MFileAppender(String file, MLogLayout logLayout, int levelFilter)
    {
        this.file = file;
        this.layout = logLayout;
        this.levelFilter = levelFilter;
    }
    
    @Override
    protected void write(MLogEvent logEvent)
    {
        try
        {
            if (layout == null)
            {
                MFile.appendFileLn(file, logEvent.getLogMsg());
            }
            else
            {
                MFile.appendFileLn(file, layout.format(logEvent));
            }
        }
        catch (MException e)
        {
            
        }
    }
}