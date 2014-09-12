/*
 * FileName: MQTest4JSActions.java
 * 
 * Description: create MQTest4JSActions class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */

package ARH.test;

import ARH.framework.annotation.*;

public class MQTest4JSActions
{    
    /**
     * Test case action. Set test case
     * @param testSuiteName suite name
     */
    @MAction
    public static void testSuite(String testSuiteName)
    {
    }
    
    /**
     * Function name action. Set function name
     * @param funName function name
     */
    @MAction
    public static void funName(String funName)
    {
    }
    
    /**
     * Set variable action. Set variable
     * @param var variable
     * @param value value of variable
     */
    @MAction
    public static void setVar(String var, String value)
    {   
    }
    
    /**
     * Assert same action. Set assert same
     * @param realValue real value
     * @param expValue except value
     */
    @MAction
    public static void assertSame(String realValue, String expValue)
    {
    }
    
    /**
     * Assert true action. Set assert true
     * @param value
     */
    @MAction
    public static void assertTrue(String value)
    {
    }
    
    /**
     * Log action.
     * @param logInfo log information
     */
    @MAction
    public static void log(String logInfo)
    {
    }
}