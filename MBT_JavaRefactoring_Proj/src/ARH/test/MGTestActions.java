/*
 * FileName: MGTestActions.java
 * 
 * Description: create MGTestActions class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */

package ARH.test;

import ARH.framework.annotation.*;

public class MGTestActions
{
    /**
     * Include action
     * @param include head file  
     */
    @MAction
    public static void include(String include)
    {
    }
    
    /**
     * Function information action.
     * @param suiteName suite name
     * @param funName function name
     */
    @MAction
    public static void funInfo(String suiteName, String funName)
    {
    }
    
    /**
     * Except equal action.
     * @param expValue except value
     * @param realValue real value
     */
    @MAction
    public static void exceptEq(String expValue, String realValue)
    {
    }
    
    /**
     * Except gt action.
     * @param expValue except value
     * @param realValue real value
     */
    @MAction
    public static void exceptGt(String expValue, String realValue)
    {
    }
    
}