/*
 * FileName: MQTest4JSActionHandler.java
 * 
 * Description: create MQTest4JSActionHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */


package ARH.common.view.qtest4js;

import java.io.*;
import java.util.*;

import org.dom4j.*;

import ARH.framework.exception.*;
import ARH.framework.file.MFile;
import ARH.framework.logger.*;
import ARH.framework.xml.*;
import ARH.common.config.*;


/**
 * This class is used to handle QTest action
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MQTest4JSActionHandler
{
    /**
     * Define action constants
     */
    private static final String MACTION_TEST_SUITE       =   "testSuite";
    private static final String MACTION_FUN_NAME     =   "funName";
    private static final String MACTION_SET_VAR       =   "setVar";
    private static final String MACTION_ASSERT_SAME    =   "assertSame";
    private static final String MACTION_ASSERT_TRUE      =   "assertTrue";
    private static final String MACTION_LOG      =   "log";
        
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
        if (action.startsWith(MACTION_TEST_SUITE))
        {
            return handleTestSuite(action);
        }
        else if (action.startsWith(MACTION_FUN_NAME))
        {
            return handleFunName(action);
        }
        else if (action.startsWith(MACTION_SET_VAR))
        {
            return handleSetVar(action);
        }
        else if (action.startsWith(MACTION_ASSERT_SAME))
        {
            return handleAssertSame(action);
        }
        else if (action.startsWith(MACTION_ASSERT_TRUE))
        {
            return handleAssertTrue(action);
        }
        else if (action.startsWith(MACTION_LOG))
        {
            return handleLog(action);
        }
        else
        {
            return action;
        }
    }

    /**
     * Handle test suite action
     * @return test suite define
     */
    protected String handleTestSuite(String action)
    {
        int actionLen = action.trim().length();
        String suiteName = action.trim().substring(11, actionLen - 3);
        
        return suiteName + " = TestCase(\"" + suiteName + "\");\n";
    }
    
    /**
     * handle function name action
     * @param action
     * @return return function define
     */
    protected String handleFunName(String action)
    {
        int actionLen = action.trim().length();
        String funName = action.trim().substring(9, actionLen - 3);
        
        return funName + " = function()\n{\n";
    }
    
    /**
     * get DbVerify action from DBVERIFY element
     * @param xmlParser
     * @param dbverifyElem
     * @return
     */
    protected String handleSetVar(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(8, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String varName = varlist[0];
        String value = varlist[1];
        
        return "    var " + varName + " = " + value + ";\n";
    }
    
    /**
     * Handle DbVerify action
     * @param action
     * @return result
     */
    protected String handleAssertSame(String action)
    {
        int len = action.trim().length();
        String vars = action.trim().substring(12, len - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String realValue = varlist[0];
        String expValue = varlist[1];
        
        return "    assertSame(\"assert same failed!\", " + realValue + ", " + expValue + ");\n";
    }
    
    /**
     * handle load script action
     * @param action
     * @return result
     */
    protected String handleAssertTrue(String action)
    {
        int len = action.trim().length();
        String realValue = action.trim().substring(12, len - 3);
        
        return "    assertTrue(\"assert true failed!\", " + realValue + ");\n";
    }
    
    /**
     * Handle log action
     * @param action
     * @return result
     */
    protected String handleLog(String action)
    {
        int len = action.trim().length();
        String log = action.trim().substring(5, len - 3);
        
        return "    jstestdriver.console.log(\"" + log + "\");\n";
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
