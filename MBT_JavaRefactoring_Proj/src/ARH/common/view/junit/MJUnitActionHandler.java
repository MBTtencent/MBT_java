/*
 * FileName: MJUnitActionHandler.java
 * 
 * Description: create MJUnitActionHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */


package ARH.common.view.junit;

import java.io.*;

import org.dom4j.*;

import ARH.framework.exception.*;


/**
 * This class is used to handle QTest action
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MJUnitActionHandler
{
    /**
     * Define action constants
     */
    private static final String MACTION_SET_UP       =   "setUp";
    private static final String MACTION_TEAR_DOWN     =   "tearDown";
    private static final String MACTION_TEST       =   "test";
    private static final String MACTION_before       =   "before";
    private static final String MACTION_AFTER       =   "after";
    private static final String MACTION_IGNORE       =   "ignore";
    private static final String MACTION_TEST_FUN       =   "testFun";
        
    /**
     * Handler action
     * @param action action 
     * @return after handler
     * @throws IOException 
     * @throws DocumentException 
     * @throws MException 
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
        else if (action.startsWith(MACTION_TEST))
        {
            return handleTest(action);
        }
        else if (action.startsWith(MACTION_before))
        {
            return handleBefore(action);
        }
        else if (action.startsWith(MACTION_AFTER))
        {
            return handleAfter(action);
        }
        else if (action.startsWith(MACTION_IGNORE))
        {
            return handleIgnore(action);
        }
        else
        {
            return action;
        }
    }

    /**
     * Handle set up action
     * @return set up function
     */
    protected String handleSetUp(String action)
    {
        int actionLen = action.trim().length();
        String setUpCont = action.trim().substring(7, actionLen - 3);
        
        return "public void setUp()\n{\n    " + setUpCont + "\n}\n";
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
        
        return "public void tearDown()\n{\n    " + tearDownCont + "\n}\n";
    }
    
    /**
     * handle test function action
     * @param action
     * @return 
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
        
        return "public void " + funName + "()\n{\n    " + funCont + "\n}\n";
    }
    
    /**
     * handle test function action
     * @return
     */
    protected String handleTest(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(6, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String expression = varlist[0];
        if (expression.trim().isEmpty())
        {
            return "@Test\n"; 
        }
        
        return "\n@Test(" + expression + ")\n";
    }
    
    /**
     * handle Before action
     * @return
     */
    protected String handleBefore(String action)
    {
        return "\n@Before\n";
    }
    
    /**
     * handle after action
     * @return
     */
    protected String handleAfter(String action)
    {
        return "\n@After\n";
    }
    
    /**
     * handle ignore action
     * @return
     */
    protected String handleIgnore(String action)
    {
        int actionLen = action.trim().length();
        String ignoreComment = action.trim().substring(8, actionLen - 3);
        
        return "\n@Ignore(\"" + ignoreComment + "\")\n";
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
