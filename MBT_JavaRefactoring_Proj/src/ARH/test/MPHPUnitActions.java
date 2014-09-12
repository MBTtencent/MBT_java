/*
 * FileName: MPHPUnitActions.java
 * 
 * Description: create MPHPUnitActions class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */

package ARH.test;

import ARH.framework.annotation.*;

public class MPHPUnitActions
{    
    /**
     * Function information action.
     * @param content script content
     */
    @MAction
    public static void setUp(String content)
    {
    }
    
    /**
     * Tear down action.
     * @param content script content
     */
    @MAction
    public static void tearDown(String content)
    {
    }
    
    /**
     * Test function action.
     * @param funName function name
     * @param funCont function content
     */
    @MAction
    public static void testFun(String funName, String funCont)
    {
    }
    
    /**
     * Script action.
     * @param content script content
     */
    @MAction
    public static void script(String content)
    {
    }
    
    /**
     * Require action
     * @param content fileName
     */
    @MAction
    public static void require(String fileName)
    {
    }
    
}