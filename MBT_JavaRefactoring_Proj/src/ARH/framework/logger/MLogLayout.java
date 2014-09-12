/*
 * FileName: MLogLayout.java
 * 
 * Description: Create MLogLayout class
 * 
 * Version: 1.0
 * 
 * History: 
 * 1.0 2013-08-16  create by MICKCHEN
 */


package ARH.framework.logger;

/**
 * This is the base class of the log layout
 * @author MICKCHEN
 * @version 1.0
 */
public abstract class MLogLayout
{
    public abstract String format(MLogEvent logEvent);
}
