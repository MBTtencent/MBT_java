/*
 * FileName: MGTestActionHandler.java
 * 
 * Description: create MGTestActionHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-12-09 Create file
 */


package ARH.common.view.gtest;

import java.io.*;

import org.dom4j.*;

import ARH.framework.exception.*;


/**
 * This class is used to handle QTest action
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MGTestActionHandler
{
    /**
     * Define action constants
     */
    private static final String MACTION_FUN_INFO       =   "funInfo";
    private static final String MACTION_EXCEPT_EQ     =   "exceptEq";
    private static final String MACTION_EXCEPT_GT       =   "exceptGt";
    private static final String MACTION_INCLUDE        =   "include";
    
        
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
        if (action.startsWith(MACTION_FUN_INFO))
        {
            return handleFunInfo(action);
        }
        else if (action.startsWith(MACTION_EXCEPT_EQ))
        {
            return handleExceptEq(action);
        }
        else if (action.startsWith(MACTION_EXCEPT_GT))
        {
            return handleExceptGt(action);
        }
        else if (action.startsWith(MACTION_INCLUDE))
        {
            return handleInclude(action);
        }
        else
        {
            return action;
        }
    }

    /**
     * Handle include action
     * @return test suite define
     */
    protected String handleInclude(String action)
    {
        int actionLen = action.trim().length();
        String include = action.trim().substring(9, actionLen - 3);
        
        return "#include" + include + "\n";
    }
    
    /**
     * Handle function information action
     * @return test suite define
     */
    protected String handleFunInfo(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(9, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String var1 = varlist[0];
        String var2 = varlist[1];
        
        return "TEST(" + var1 + ", " + var2 + ")\n{\n";
    }
    
    /**
     * handle except equal action
     * @param action
     * @return return function define
     */
    protected String handleExceptEq(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(9, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String expValue = varlist[0];
        String realValue = varlist[1];
        
        return "    EXPECT_EQ(" + expValue + ", " + realValue + ");\n";
    }
    
    /**
     * handle except Gt action
     * @param xmlParser
     * @param dbverifyElem
     * @return
     */
    protected String handleExceptGt(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(9, actionLen - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String expValue = varlist[0];
        String realValue = varlist[1];
        
        return "    EXPECT_GT(" + expValue + ", " + realValue + ");\n";
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
