/*
 * FileName: MJUnitActions.java
 * 
 * Description: create MJUnitActions class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */

package ARH.test;

import ARH.framework.annotation.*;

public class MJUnitActions
{    
    /**
     * Set up action.
     * @param content set up content
     */
    @MAction
    public static void setUp(String content)
    {
    }
    
    /**
     * Tear down action.
     * @param content tear down content
     */
    @MAction
    public static void tearDown(String content)
    {
    }
    
    /**
     * Test function action.
     * @param funName function name
     * @param realValue function content
     */
    @MAction
    public static void testFun(String funName, String funCont)
    {
    }
    
    /**
     * Before action.
     */
    @MAction
    public static void before()
    {
    }
    
    /**
     * Test action.
     * @param expression
     */
    @MAction
    public static void test(String expression)
    {
    }
    
    /**
     * After action.
     */
    @MAction
    public static void after()
    {
    }
    
    /**
     * Ignore action.
     * @param comment 
     */
    @MAction
    public static void ignore(String comment)
    {
    }
    
}