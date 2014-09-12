/*
 * FileName: MConsoleAppender.java
 * 
 * Description: Create MConsoleAppender class
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

import ARH.framework.basic.MBasicApi;

/**
 * This class is used to print log on console
 * @author MICKCHEN
 * @version 1.0
 */
public class MConsoleAppender extends MLogAppender
{
    /**
     * Constructor
     */
    public MConsoleAppender()
    {
    }
    
    /**
     * Constructor
     * @param LogLayout layout of the log
     */
    public MConsoleAppender(MLogLayout logLayout)
    {
        this.layout = logLayout;
    }
    
    /**
     * Constructor
     * @param logLayout layout of the log
     * @param logLevel log level
     */
    public MConsoleAppender(MLogLayout logLayout, int levelFilter)
    {
        this.layout = logLayout;
        this.levelFilter = levelFilter;
    }
    
    @Override
    protected void write(MLogEvent logEvent)
    {
        if (layout == null)
        {
            MBasicApi.println(logEvent.getLogMsg());
        }
        else
        {
            MBasicApi.println(layout.format(logEvent));
        }
    }
}
