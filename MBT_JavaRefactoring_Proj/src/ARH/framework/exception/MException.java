/*
 * FileName: MException.java
 * 
 * Description: Create MException class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-16 Create 
 */


package ARH.framework.exception;

import java.lang.Exception;

/**
 * Self define exception
 * @author MICKCHEN
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MException extends Exception
{
    /**
     * Constructor
     */
    public MException(String ex) 
    {
        super("MException:" + ex);
    }
}
