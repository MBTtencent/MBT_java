/*
 * FileName: MPHPUnitActionHandler.java
 * 
 * Description: create MPHPUnitActionHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */

package ARH.common.view.phpunit;

/**
 * This class is used to handle QTest action
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MPHPUnitActionHandler
{
    /**
     * Define action constants
     */
    private static final String MACTION_SET_UP       =   "setUp";
    private static final String MACTION_TEAR_DOWN     =   "tearDown";
    private static final String MACTION_TEST_FUN       =   "testFun";
    private static final String MACTION_REQUIRE       =   "require";
    private static final String MACTION_SCRIPT       =   "script";
    
    /**
     * Handler action
     * @param action action 
     * @return after handler
     */
    public String handleAction(String action)
    {
        if (action.startsWith(MACTION_SET_UP))
        {
            return handleSetUp(action);
        }
        else if (action.startsWith(MACTION_TEAR_DOWN))
        {
            return handleTearDown(action);
        }
        else if (action.startsWith(MACTION_TEST_FUN))
        {
            return handleTestFun(action);
        }
        else if (action.startsWith(MACTION_REQUIRE))
        {
            return handleRequire(action);
        }
        else if (action.startsWith(MACTION_SCRIPT))
        {
            return handleScript(action);
        }
        else
        {
            return action;
        }
    }

    /**
     * Handle setup action
     * @return setup function
     */
    protected String handleSetUp(String action)
    {
        int actionLen = action.trim().length();
        String setUpCont = action.trim().substring(7, actionLen - 3);
         
        return "function setUp()\n{\n    " + setUpCont + "\n}\n";
    }
    
    /**
     * handle tear down action
     * @param action
     * @return tear down function
     */
    protected String handleTearDown(String action)
    {
        int actionLen = action.trim().length();
        String tearDownCont = action.trim().substring(10, actionLen - 3);
        
        return "function tearDown()\n{\n    " + tearDownCont + "\n)\n";
    }
    
    /**
     * handle test function action
     * @param action
     * @return test function
     */
    protected String handleTestFun(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(9, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String funName = varlist[0];
        String funCont = varlist[1];
        
        return "function " + funName + "()\n{\n    " + funCont + "\n}\n";
    }
    
    /**
     * handle require action
     * @param action
     * @return result
     */
    protected String handleRequire(String action)
    {
        int actionLen = action.trim().length();
        String fileName = action.trim().substring(9, actionLen - 3);
        
        return "require_once(\"" + fileName + "\")\n";
    }
    
    /**
     * handle script action
     * @param action
     * @return result
     */
    protected String handleScript(String action)
    {
        int actionLen = action.trim().length();
        String script = action.trim().substring(8, actionLen - 3);
        
        return script + "\n";
    }
    
    /**
     * Set script content
     * @param content script content
     * @return result
     */
    public String setContent(String content)
    {
        return content;
    }
}
