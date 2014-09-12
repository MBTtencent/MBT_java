/*
 * FileName: MQTestActionHandler.java
 * 
 * Description: create MQTestActionHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-06 Create file
 */


package ARH.common.view.qtest;

import java.io.*;
import java.util.*;

import org.dom4j.*;

import ARH.framework.exception.*;
import ARH.framework.file.MFile;
import ARH.framework.logger.*;
import ARH.framework.xml.*;
import ARH.common.config.*;
import ARH.common.view.MQTestView;


/**
 * This class is used to handle QTest action
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MQTestActionHandler
{
    /**
     * Define action constants
     */
    private static final String MACTION_SETUP       =   "setup";
    private static final String MACTION_CLEANUP     =   "cleanup";
    private static final String MACTION_QUERY       =   "query";
    private static final String MACTION_DBVERIFY    =   "dbVerify";
    private static final String MACTION_SCRIPT      =   "script";
    private static final String MACTION_LOAD_MSG    =   "loadMsg";
    private static final String MACTION_LOAD_VAR    =   "loadVar";
    private static final String MACTION_LOAD_SCRIPT =   "loadScript";
    private static final String MACTION_LOAD_DBVERIFY   =   "loadDbVerify";
    private static final String MACTION_SET_VAR     =   "setVar";
    private static final String MACTION_REMOTE_CALL =   "remoteCall";
    private static final String MACTION_LOAD_NON_MSG =   "loadNonMsg";
    private static final String MACTION_LOAD_HTTP_MSG =   "loadHttpMsg";
    private static final String MACTION_QQ_LOGIN =   "QQLogin";
    private static final String MACTION_GET_LOGIN_KEY =   "getLoginKey";
    
    private static final String MACTION_QQ_LOGIN_IP =   "10.6.207.98";
    private static final String MACTION_QQ_LOGIN_PORT =   "80";
    
    private static final String MACTION_QQ_LOGIN_IP2 =   "10.130.130.206";
    private static final String MACTION_QQ_LOGIN_PORT2 =   "49999";
    
    private static final int MACTION_SETUP_TYPE = 1;
    private static final int MACTION_CLEANUP_TYPE = 2;
    private static final int MACTION_TEST_TYPE = 3;
    
    /**
     * Define SQL constants
     */
    private static final String MSQL_SELECT     = "select ";
    private static final String MSQL_FROM       = " from ";
//    private static final String MSQL_UPDATE     = "update ";
//    private static final String MSQL_DELETE     = "delete ";
//    private static final String MSQL_INSERT     = "insert ";
    
    private static final String MVAR_DOMAIN_GLOBAL      = "global";
//    private static final String MVAR_DOMAIN_LOCAL       = "local";
    private static final String MVAR_TYPE_STRING        = "string";
//    private static final String MVAR_TYPE_NUMBER        = "number";
    
    
    private HashSet<String> globalVar = new HashSet<String>();
    
    /**
     * Handler action
     * @param action action 
     * @return after handler
     * @throws IOException 
     * @throws DocumentException 
     * @throws MException 
     */
    public String handleAction(String action) throws MException, DocumentException, IOException
    {
        if (action.startsWith(MACTION_QUERY))
        {
            return handleQuery(action);
        }
        else if (action.startsWith(MACTION_LOAD_DBVERIFY))
        {
            return handleLoadDbVerify(action);
        }
        else if (action.startsWith(MACTION_DBVERIFY))
        {
            return handleDbVerify(action);
        }
        else if (action.startsWith(MACTION_LOAD_SCRIPT))
        {
            return handleLoadScript(action);
        }
        else if (action.startsWith(MACTION_SCRIPT))
        {
            return handleScript(action);
        }
        else if (action.startsWith(MACTION_LOAD_MSG))
        {
            return handleLoadMsg(action);
        }
        else if (action.startsWith(MACTION_LOAD_VAR))
        {
            return handleLoadVar(action);
        }
        else if (action.startsWith(MACTION_SET_VAR))
        {
            return handleSetVar(action);
        }
        else if (action.startsWith(MACTION_REMOTE_CALL))
        {
            return handleRemoteCall(action);
        }
        else if (action.startsWith(MACTION_LOAD_NON_MSG))
        {
            int len = action.trim().length();
            String vars = action.trim().substring(12, len - 3);
            
            String[] varList = vars.split("\", \"");
            if (varList.length == 1)
            {
                varList = vars.split("\",\"");
            }
            
            int type = Integer.parseInt(varList[0]);
            
            switch (type)
            {   
            case MQTestView.MLOAD_NON_MSG_SCRIPT:
                return handleScript(action);
                
            case MQTestView.MLOAD_NON_MSG_LOAD_VAR1:
            case MQTestView.MLOAD_NON_MSG_LOAD_VAR2:
                return handleLoadVar(action);
                
            case MQTestView.MLOAD_NON_MSG_LOAD_SCRIPT1:
            case MQTestView.MLOAD_NON_MSG_LOAD_SCRIPT2:
                return handleLoadScript(action);
                
            case MQTestView.MLOAD_NON_MSG_LOAD_DB_VERIFY1:
            case MQTestView.MLOAD_NON_MSG_LOAD_DB_VERIFY2:
                return handleLoadDbVerify(action);
                
            case MQTestView.MLOAD_NON_MSG_QUERY:
                return handleQuery(action);
                
            case MQTestView.MLOAD_NON_MSG_REMOTE_CALL:
                return handleRemoteCall(action);
                
            case MQTestView.MLOAD_NON_MSG_DB_VERIFY:
                return handleDbVerify(action);
                
            case MQTestView.MLOAD_NON_MSG_SET_VAR:
                return handleSetVar(action);
            default:
                return action;
            }
        }
        else if (action.startsWith(MACTION_LOAD_HTTP_MSG))
        {
            return handleLoadHttpMsg(action);
        }
        else if (action.startsWith(MACTION_QQ_LOGIN))
        {
            return handleQQLogin(action);
        }
        else if (action.startsWith(MACTION_GET_LOGIN_KEY))
        {
            return handleGetLoginKey(action);
        }
        else
        {
            return action;
        }
    }
    
    /**
     * Get global variable
     * @return global variable
     */
    public HashSet<String> getGlobalVar()
    {
        return globalVar;
    }
    
    /**
     * Handle query action
     * @return result
     */
    protected String handleQuery(String action)
    {
        int actionLen = action.trim().length();
        String vars = action.trim().substring(7, actionLen - 3);
        
        String[] varList = vars.split("\", \"");
        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String sql = varList[0];
        String entity = varList[1];
        
        String entityStr = handleDBEntity(entity);
        
        String sqlVars = getSqlVars(sql);
        String sqlNew = handleSql(sql);

        if (!sqlVars.isEmpty())
        {
            sqlVars += " = ";
        }
        
        return entityStr + sqlVars + "varDbHandler." + 
                MACTION_QUERY + "(\"" + sqlNew + "\")\n";
    }
    
    /**
     * handle load DbVerify action
     * @param action
     * @return
     * @throws MException
     * @throws DocumentException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected String handleLoadDbVerify(String action)
            throws MException, DocumentException, IOException
    {
        action = action.trim();
        int firstIdx = action.indexOf("\"");
        int secondIdx = action.indexOf("\"", firstIdx + 1);
        int thirdIdx = action.indexOf("\"", secondIdx + 1);
        int fourthIdx = action.indexOf("\"", thirdIdx + 1);
        int fifthIdx = action.indexOf("\"", fourthIdx + 1);
        int sixthIdx = action.indexOf("\"", fifthIdx + 1);
        
        String filePath = action.substring(firstIdx + 1, secondIdx).trim();
        if (!MFile.isExisted(filePath))
        {
            MLogMag.getInstance().getLogger().warning("File \"" + filePath + "\" not existed!");
            return null;
        }
        
        String type = "";
        String filter = "";
        if (-1 != thirdIdx && -1 != fourthIdx)
        {
            type = action.substring(thirdIdx + 1, fourthIdx).trim();
            if (!type.equals("0") && !type.equals("1"))
            {
                type = "";
            }
        }
        
        String dbVerifyScripts = "";
        MXmlParse xmlParser = new MXmlParse(filePath);
        Element dbverifiesElem = xmlParser.getRoot();
        List<Element> dbverifyElemList = xmlParser.getSubElement(dbverifiesElem);
        
        // select elements by tag
        if (type.equals("0"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
            String[] tags = filter.split(",");
            HashSet<String> tagSet = new HashSet<String>();
            for (String tag : tags)
            {
                tagSet.add(tag.trim());
            }
            
            for (Element dbverifyElem : dbverifyElemList)
            {
                String tagAttr = xmlParser.getAttrValue(dbverifyElem, MCommonConfig.MBT_XML_TAG);
                if (tagSet.contains(tagAttr))
                {
                    dbVerifyScripts += handleDbVerify(getDbVerifyAction(xmlParser, dbverifyElem)) + "\n";
                }
            }
        }
        // select elements by id
        else if (type.equals("1"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
//            HashSet<String> idSet = new HashSet<String>();
            ArrayList<String> idList = new ArrayList<String>();
            int tmpIdx = filter.indexOf("-");
            if (-1 != tmpIdx)
            {
                int begPos = Integer.valueOf(filter.substring(0, tmpIdx));
                int endPos = Integer.valueOf(filter.substring(tmpIdx + 1));
                for (int i = begPos; i <= endPos; ++i)
                {
                    idList.add(String.valueOf(i));
//                    idSet.add(String.valueOf(i));
                }
            }
            else
            {
                String[] ids = filter.split(",");
                for (String id : ids)
                {
                    idList.add(id);
//                    idSet.add(id.trim());
                }
            }
            
//            for (Element dbverifyElem : dbverifyElemList)
//            {
//                String idAttr = xmlParser.getAttrValue(dbverifyElem, MCommonConfig.MBT_XML_ID);
//                if (idSet.contains(idAttr))
//                {
//                    dbVerifyScripts += handleDbVerify(getDbVerifyAction(xmlParser, dbverifyElem)) + "\n";
//                }
//            }
            
            for (String id : idList)
            {
                for (Element dbverifyElem : dbverifyElemList)
                {
                    String idAttr = xmlParser.getAttrValue(dbverifyElem, MCommonConfig.MBT_XML_ID);
                    if (id.trim().compareTo(idAttr.trim()) == 0)
                    {
                        dbVerifyScripts += handleDbVerify(getDbVerifyAction(xmlParser, dbverifyElem)) + "\n";
                    }
                }
            }
        }
        // no type or unrecognized type
        else
        {
            for (Element dbverifyElem : dbverifyElemList)
            {
                dbVerifyScripts += handleDbVerify(getDbVerifyAction(xmlParser, dbverifyElem)) + "\n";
            }
        }
        
        return dbVerifyScripts.trim() + "\n";
    }
    
    /**
     * get DbVerify action from DBVERIFY element
     * @param xmlParser
     * @param dbverifyElem
     * @return
     */
    private String getDbVerifyAction(MXmlParse xmlParser, Element dbverifyElem)
    {
        String sql = "";
        Element sqlElem = xmlParser.getSubFirstElement(dbverifyElem, MCommonConfig.MBT_XML_SQL);
        
        Element selectElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_SELECT);
        String selectData = xmlParser.getNodeData(selectElem).trim();
        sql += "select " + selectData;
        
        Element fromElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_FROM);
        String fromData = xmlParser.getNodeData(fromElem).trim();
        sql += " from " + fromData;
        
        Element whereElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_WHERE);
        String whereData = xmlParser.getNodeData(whereElem).trim();
        if (!whereData.equals(""))
        {
            sql += " where " + whereData;
        }
        
        Element groupbyElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_GROUPBY);
        String groupbyData = xmlParser.getNodeData(groupbyElem).trim();
        if (!groupbyData.equals(""))
        {
            sql += " group by " + groupbyData;
        }
        
        Element havingElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_HAVING);
        String havingData = xmlParser.getNodeData(havingElem).trim();
        if (!havingData.equals(""))
        {
            sql += " having " + havingData;
        }
            
        Element orderbyElem = xmlParser.getSubFirstElement(sqlElem, MCommonConfig.MBT_XML_ORDERBY);
        String orderbyData = xmlParser.getNodeData(orderbyElem).trim();
        if (!orderbyData.equals(""))
        {
            sql += " order by " + orderbyData;
        }
            
        Element valueElem = xmlParser.getSubFirstElement(dbverifyElem, MCommonConfig.MBT_XML_VALUE);
        String expectValue = xmlParser.getNodeData(valueElem);
        
        Element entityElem = xmlParser.getSubFirstElement(dbverifyElem, MCommonConfig.MBT_XML_ENTITY);
        String entity = xmlParser.getNodeData(entityElem);
        
        String dbVerifyAction = "dbVerify(\"" + sql + "\", \"" + expectValue + "\", \"" + entity + "\");";
        return dbVerifyAction;
    }
    
    /**
     * Handle DbVerify action
     * @param action
     * @return result
     */
    protected String handleDbVerify(String action)
    {
        String dbVerify = "";
        int len = action.trim().length();
        String vars = action.trim().substring(10, len - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String sql = varlist[0];
        String values = varlist[1];
        String entity = varlist[2];
        String entityStr = handleDBEntity(entity);
        
        String[] valueList = values.split(",");
        
        
        String sqlVars = getSqlVars(sql);

        String sqlNew = handleSql(sql);
        
        dbVerify += sqlVars + " = varDbHandler.query(\"" + sqlNew + "\")\n";
        
        int sqlVarsLen = sqlVars.trim().length();
        String[] varList = sqlVars.substring(1, sqlVarsLen - 1).split(",");
        
        for (int i = 0; i < valueList.length; i++)
        {
            dbVerify += "assertEquals(" + varList[i].trim() + ", " + valueList[i] + ", \""
                    + varList[i].trim() + "\", \"Database verify error\")\n";
        }
        
        return entityStr + dbVerify;
    }
    
    /**
     * handle load script action
     * @param action
     * @return
     * @throws MException
     * @throws DocumentException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected String handleLoadScript(String action) 
            throws MException, DocumentException, IOException
    {
        action = action.trim();
        int firstIdx = action.indexOf("\"");
        int secondIdx = action.indexOf("\"", firstIdx + 1);
        int thirdIdx = action.indexOf("\"", secondIdx + 1);
        int fourthIdx = action.indexOf("\"", thirdIdx + 1);
        int fifthIdx = action.indexOf("\"", fourthIdx + 1);
        int sixthIdx = action.indexOf("\"", fifthIdx + 1);
        
        String filePath = action.substring(firstIdx + 1, secondIdx).trim();
        if (!MFile.isExisted(filePath))
        {
            MLogMag.getInstance().getLogger().warning("File \"" + filePath + "\" not existed!");
            return null;
        }
        
        String type = "";
        String filter = "";
        if (-1 != thirdIdx && -1 != fourthIdx)
        {
            type = action.substring(thirdIdx + 1, fourthIdx).trim();
            if (!type.equals("0") && !type.equals("1"))
            {
                type = "";
            }
        }
        
        String scripts = "";
        MXmlParse xmlParser = new MXmlParse(filePath);
        Element scriptsElem = xmlParser.getRoot();
        List<Element> scriptElemList = xmlParser.getSubElement(scriptsElem);
        
        // select elements by tag
        if (type.equals("0"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
            String[] tags = filter.split(",");
            HashSet<String> tagSet = new HashSet<String>();
            for (String tag : tags)
            {
                tagSet.add(tag.trim());
            }
            
            for (Element scriptElem : scriptElemList)
            {
                String tagAttr = xmlParser.getAttrValue(scriptElem, MCommonConfig.MBT_XML_TAG);
                if (tagSet.contains(tagAttr))
                {
                    scripts += handleScript(getScriptAction(xmlParser, scriptElem)) + "\n";
                }
            }
        }
        // select elements by id
        else if (type.equals("1"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
//            HashSet<String> idSet = new HashSet<String>();
            ArrayList<String> idList = new ArrayList<String>();
            int tmpIdx = filter.indexOf("-");
            if (-1 != tmpIdx)
            {
                int begPos = Integer.valueOf(filter.substring(0, tmpIdx));
                int endPos = Integer.valueOf(filter.substring(tmpIdx + 1));
                for (int i = begPos; i <= endPos; ++i)
                {
                    idList.add(String.valueOf(i));
//                    idSet.add(String.valueOf(i));
                }
            }
            else
            {
                String[] ids = filter.split(",");
                for (String id : ids)
                {
                    idList.add(id);
//                    idSet.add(id.trim());
                }
            }
            
//            for (Element scriptElem : scriptElemList)
//            {
//                String idAttr = xmlParser.getAttrValue(scriptElem, MCommonConfig.MBT_XML_ID);
//                if (idSet.contains(idAttr))
//                {
//                    scripts += handleScript(getScriptAction(xmlParser, scriptElem)) + "\n";
//                }
//            }
            
            for (String id : idList)
            {
                for (Element scriptElem : scriptElemList)
                {
                    String idAttr = xmlParser.getAttrValue(scriptElem, MCommonConfig.MBT_XML_ID);
                    if (id.trim().compareTo(idAttr.trim()) == 0)
                    {
                        scripts += handleScript(getScriptAction(xmlParser, scriptElem)) + "\n";
                        break;
                    }
                }
            }
        }
        // no type or unrecognized type
        else
        {
            for (Element scriptElem : scriptElemList)
            {
                scripts += handleScript(getScriptAction(xmlParser, scriptElem)) + "\n";
            }
        }
        
        return scripts.trim() + "\n";
    }
    
    /**
     * get script action from SCRIPT element
     * @param xmlParser
     * @param scriptElem
     * @return
     */
    private String getScriptAction(MXmlParse xmlParser, Element scriptElem)
    {
        String script = xmlParser.getNodeData(scriptElem);
        String scriptAction = "script(\"" + script + "\");";
        return scriptAction;
    }
    
    /**
     * Handle script action
     * @param action
     * @return result
     */
    protected String handleScript(String action)
    {
        int scriptLen = action.trim().length();
        return action.trim().substring(8, scriptLen - 3);
    }
    
    /**
     * Handle loadMsg action
     * @param action
     * @return result
     * @throws IOException 
     * @throws DocumentException 
     * @throws MException 
     */
    protected String handleLoadMsg(String action) throws MException, DocumentException, IOException
    {
        int len = action.trim().length();
        String vars = action.trim().substring(9, len - 3);
        
        String[] varList = vars.split("\", \"");
        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String wizard = varList[0];
        String data = varList[1];
        String entity = varList[2];
        ArrayList<String> dataFiles = new ArrayList<String>();

        if (!MFile.isExisted(wizard))
        {
            MLogMag.getInstance().getLogger().warning("File \"" + wizard + "\" not existed!");
            return null;
        }
        
        MGenQTestMsg genQTestMsg = new MGenQTestMsg();
        return genQTestMsg.genMsg(wizard, data, entity, dataFiles);
    }
    
    /**
     * handle load variables action
     * @param action
     * @return
     * @throws MException
     * @throws DocumentException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected String handleLoadVar(String action) throws MException, DocumentException, IOException
    {
        action = action.trim();
        int firstIdx = action.indexOf("\"");
        int secondIdx = action.indexOf("\"", firstIdx + 1);
        int thirdIdx = action.indexOf("\"", secondIdx + 1);
        int fourthIdx = action.indexOf("\"", thirdIdx + 1);
        int fifthIdx = action.indexOf("\"", fourthIdx + 1);
        int sixthIdx = action.indexOf("\"", fifthIdx + 1);
        
        String filePath = action.substring(firstIdx + 1, secondIdx).trim();
        if (!MFile.isExisted(filePath))
        {
            MLogMag.getInstance().getLogger().warning("File \"" + filePath + "\" not existed!");
            return null;
        }
        
        String type = "";
        String filter = "";
        if (-1 != thirdIdx && -1 != fourthIdx)
        {
            type = action.substring(thirdIdx + 1, fourthIdx).trim();
            if (!type.equals("0") && !type.equals("1"))
            {
                type = "";
            }
        }
        
        String varScript = "";
        MXmlParse xmlParser = new MXmlParse(filePath);
        Element varsElem = xmlParser.getRoot();
        List<Element> varElemList = xmlParser.getSubElement(varsElem);
        
        // select elements by tag
        if (type.equals("0"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
            String[] tags = filter.split(",");
            HashSet<String> tagSet = new HashSet<String>();
            for (String tag : tags)
            {
                tagSet.add(tag.trim());
            }
            
            for (Element varElem : varElemList)
            {
                String tagAttr = xmlParser.getAttrValue(varElem, MCommonConfig.MBT_XML_TAG);
                if (tagSet.contains(tagAttr))
                {
                    varScript += handleSetVar(getSetVarAction(xmlParser, varElem)) + "\n";
                }
            }
        }
        // select elements by id
        else if (type.equals("1"))
        {
            filter = action.substring(fifthIdx + 1, sixthIdx).trim();
//            HashSet<String> idSet = new HashSet<String>();
            ArrayList<String> idList = new ArrayList<String>();
            int tmpIdx = filter.indexOf("-");
            if (-1 != tmpIdx)
            {
                int begPos = Integer.valueOf(filter.substring(0, tmpIdx));
                int endPos = Integer.valueOf(filter.substring(tmpIdx + 1));
                for (int i = begPos; i <= endPos; ++i)
                {
//                    idSet.add(String.valueOf(i));
                    idList.add(String.valueOf(i));
                }
            }
            else
            {
                String[] ids = filter.split(",");
                for (String id : ids)
                {
//                    idSet.add(id.trim());
                    idList.add(id);
                }
            }
            
//            for (Element varElem : varElemList)
//            {
//                String idAttr = xmlParser.getAttrValue(varElem, MCommonConfig.MBT_XML_ID);
//                if (idSet.contains(idAttr))
//                {
//                    varScript += handleSetVar(getSetVarAction(xmlParser, varElem)) + "\n";
//                }
//            }
            
            for (String id : idList)
            {
                for (Element varElem : varElemList)
                {
                    String idAttr = xmlParser.getAttrValue(varElem, MCommonConfig.MBT_XML_ID);
                    if (id.trim().compareTo(idAttr.trim()) == 0)
                    {
                        varScript += handleSetVar(getSetVarAction(xmlParser, varElem)) + "\n";
                    }
                }
            }
        }
        // no type or unrecognized type
        else
        {
            for (Element varElem : varElemList)
            {
                varScript += handleSetVar(getSetVarAction(xmlParser, varElem)) + "\n";
            }
        }

        return varScript.trim();
    }
    
    /**
     * get set variables action from VAR element
     * @param xmlParser
     * @param varElem
     * @return
     * @throws MException
     * @throws DocumentException
     * @throws IOException
     */
    private String getSetVarAction(MXmlParse xmlParser, Element varElem) 
            throws MException, DocumentException, IOException
    {
        Element nameElem = xmlParser.getSubFirstElement(varElem, MCommonConfig.MBT_XML_NAME);
        Element valueElem = xmlParser.getSubFirstElement(varElem, MCommonConfig.MBT_XML_VALUE);
        Element typeElem = xmlParser.getSubFirstElement(varElem, MCommonConfig.MBT_XML_TYPE);
        Element areaElem = xmlParser.getSubFirstElement(varElem, MCommonConfig.MBT_XML_AREA);
        
        String nameData = xmlParser.getNodeData(nameElem);
        String valueData = xmlParser.getNodeData(valueElem);
        String typeData = xmlParser.getNodeData(typeElem);
        String areaData = xmlParser.getNodeData(areaElem);
        
        String setVarAction = "setVar(\"" + nameData + "\", \"" + valueData + "\", \"" + 
                typeData + "\", \"" + areaData + "\");";
        
        return setVarAction;
    }
    
    /**
     * Handle setVar action
     * @param action
     * @return result
     */
    protected String handleSetVar(String action)
    {
        String varScript = "";
        
        int len = action.trim().length();
        
        String vars = action.trim().substring(8, len - 3);
        
        String[] varlist = vars.split("\", \"");
        if (varlist.length == 1)
        {
            varlist = vars.split("\",\"");
        }
        
        String name = varlist[0].trim();
        String value = varlist[1].trim();
        String type = varlist[2].trim();
        String domain = varlist[3].trim();
        
        if (domain.equalsIgnoreCase(MVAR_DOMAIN_GLOBAL))
        {
            globalVar.add(name);
            varScript += MVAR_DOMAIN_GLOBAL + " " + name + "\n";
            if (type.equalsIgnoreCase(MVAR_TYPE_STRING))
            {
                varScript += name + " = '" + value + "'\n";
            }
            else
            {
                varScript += name + " = " + value + "\n";
            }
        }
        else
        {
            if (type.equalsIgnoreCase(MVAR_TYPE_STRING))
            {
                varScript += name + " = '" + value + "'\n";
            }
            else
            {
                varScript += name + " = " + value + "\n";
            }
        }
        
        return varScript;
    }
    
    /**
     * Handle remote call
     * @param action
     * @return result
     */
    protected String handleRemoteCall(String action)
    {
        int len = action.trim().length();
        String vars = action.trim().substring(12, len - 3);
        
        String[] varList = vars.split("\", \"");
        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String script = varList[0];
        String expValue = varList[1];
        String entity = varList[2];
        
        String remoteEntity = handleRemoteEntity(entity);
        String remoteCall = handleRemoteCallScript(script, expValue);
        
        return remoteEntity + remoteCall;
    }
    
    /**
     * Set script content
     * @param content script content
     * @return result
     */
    public String setContent(String content)
    {
        String contentNew = "";
        
        String lastLine = "";
        String line = "";
        String step = "";
        String commonPart = "";
        String setupPart = "def Setup():\n";
        String cleanupPart = "\ndef Cleanup():\n";
        String testPart = "\ndef Test():\n";
        boolean inStep = false;
        int stepType = 0;
        String[] lineList = content.split("\n");
        for (int i = 0; i < lineList.length; i++)
        {
            line = lineList[i] + "\n";
            if (line.startsWith("stepStart"))
            {
                lastLine = lineList[i - 1] + "\n";
                step += "    " + lastLine + "    " + line;
                stepType = MACTION_TEST_TYPE;
                inStep = true;
            }
            else if (line.startsWith("stepEnd"))
            {
                step += "    " + line + "\n";
                inStep = false;
                if (stepType == MACTION_SETUP_TYPE)
                {
                    setupPart += step;
                }
                else if (stepType == MACTION_CLEANUP_TYPE)
                {
                    cleanupPart += step;
                }
                else if (stepType == MACTION_TEST_TYPE)
                {
                    testPart += step;
                }
                
                stepType = 0;
                step = "";
            }
            else if (inStep == true)
            {
                if (line.startsWith(MACTION_SETUP))
                {
                    stepType = MACTION_SETUP_TYPE;
                }
                else if (line.startsWith(MACTION_CLEANUP))
                {
                    stepType = MACTION_CLEANUP_TYPE;
                }
                else
                {
                    step += "    " + line;
                }
            }
            else if (!line.startsWith("# "))
            {
                commonPart += line;
            }
        }
        
        if (setupPart.equals("def Setup():\n"))
        {
            setupPart += "    pass\n";
        }
        if (cleanupPart.equals("\ndef Cleanup():\n"))
        {
            cleanupPart += "    pass\n";
        }
        if (testPart.equals("\ndef Test():\n"))
        {
            testPart += "    pass\n";
        }
        
        contentNew = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        contentNew += "<script-case class-id=\"{MTC1078D2ED2DED-20130828-151750016-00000001}\">\n";
        contentNew += "<manual-script><![CDATA[";
        contentNew += commonPart.replaceAll("\n\n", "\n") + setupPart + testPart + cleanupPart;
        contentNew += "]]></manual-script></script-case>";
        
        return contentNew;
    }
    
    
    /**
     * Get SQL variables
     * @param sql SQL
     * @return variables
     */
    private String getSqlVars(String sql)
    {
        String varStr = "";
        
        if (sql.startsWith(MSQL_SELECT))
        {
            sql = sql.trim();
            int fromIdx = sql.indexOf(MSQL_FROM);
            String[] varList = sql.trim().substring(6, fromIdx).split(",");
            
            for (int i = 0; i < varList.length; i++)
            {
                if (i == 0 && varList.length > 1)
                {
                    varStr = "[var" + varList[i].trim() + ", ";
                }
                else if (i == 0 && varList.length == 1)
                {
                    varStr = "[var" + varList[i].trim() + "]";
                }
                else if (i == varList.length - 1)
                {
                    varStr += "var" + varList[i].trim() + "]";
                }
                else
                {
                    varStr += "var" + varList[i].trim() + ", ";
                }
            }
        }
        
        return varStr;
    }
    
    /**
     * Handle SQL
     * @param sql
     * @return result 
     */
    private String handleSql(String sql)
    {
        if (!sql.endsWith(";"))
        {
            sql += ";";
        }

        String sql1 = sql.replaceAll("\\$\\{", "\" + ");
        String sql2 = sql1.replaceAll("\\}", " + \"");
        
        return sql2;
    }
    
    /**
     * Handle database entity
     * @param entity database entity
     * @return result
     */
    private String handleDBEntity(String entity)
    {
        return "varDbHandler = ResObjFactory.getObject('" + entity + "')\n";
    }
    
    /**
     * Handle entity
     * @param entity
     * @return result
     */
    private String handleRemoteEntity(String entity)
    {   
        return "varRemote = ResObjFactory.getObject('" + entity + "')\n";
    }
    
    /**
     * Handle shell script
     * @param script shell script
     * @param expValue the except value
     * @return result
     */
    private String handleRemoteCallScript(String script, String expValue)
    {
        String remote = "varRemote.setTimeout(1000 * 30)\n";
        remote += "varRemoteResult = varRemote.remoteCall('" + script + "', \"" + expValue + "\");\n";
        
        return remote;
    }
    
    /**
     * Handle HTTP message
     * @param action
     * @return result
     * @throws DocumentException 
     * @throws MException 
     */
    private String handleLoadHttpMsg(String action) throws MException, DocumentException
    {
        int len = action.trim().length();
        String vars = action.trim().substring(13, len - 3);
        
        String[] varList = vars.split("\", \"");

        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String configPath = varList[0];
        String ip = varList[1];
        String port = varList[2];
        
        String content = "";
        
        MGenQTestHttpMsg genQTestHttpMsg = new MGenQTestHttpMsg(configPath); 
        content = "oHttpEnt = HttpEntity('" + ip + "', " + port + ");\n";
        
        content += genQTestHttpMsg.genMsg();
        
        return content;
    }
    
    /**
     * Handle QQ Login
     * @param action
     * @return result
     */
    private String handleQQLogin(String action)
    {
        String content = "";
        int len = action.trim().length();
        String vars = action.trim().substring(9, len - 3);
        
        String[] varList = vars.split("\", \"");

        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String uin = varList[0];
        String password = varList[1];
        String skey = varList[2];
        String key = varList[3];
        
        content += "mVarEntity = AttLoginEntity('" + MACTION_QQ_LOGIN_IP + "', " 
                   + MACTION_QQ_LOGIN_PORT + ");\n";
        content += "mVarQQLoginMsg = QQLoginMsg();\n";
        content += "mVarQQLoginMsg.getUin().setValue('" + uin + "');\n";
        content += "mVarQQLoginMsg.getPsw().setValue('" + password + "');\n\n";
        content += "mVarEntity.sendMsg(mVarQQLoginMsg);\n";
        content += "mVarQQLoginMsg = QQLoginMsg();\n";
        content += "mVarEntity.setTimeout(1000 * 30);\n\n";
        content += "mVarRecvQQLoginMsg = mVarEntity.receiveMsg(mVarQQLoginMsg);\n";
        content += skey + " = mVarRecvQQLoginMsg.getSkey().getValue();\n";
        content += key + " = mVarRecvQQLoginMsg.getKey().getValue();\n";
        
        return content;
    }
    
    /**
     * Handle QQ Login
     * @param action
     * @return result
     */
    private String handleGetLoginKey(String action)
    {
        String content = "";
        int len = action.trim().length();
        String vars = action.trim().substring(13, len - 3);
        
        String[] varList = vars.split("\", \"");

        if (varList.length == 1)
        {
            varList = vars.split("\",\"");
        }
        
        String uin = varList[0];
        String password = varList[1];
        String appId = varList[2]; 
        String skey = varList[3];
        String key = varList[4];
        String psKey = varList[5];
        
        content += "mVarEntity = GetAttQQEntity('" + MACTION_QQ_LOGIN_IP2 + "', " 
            + MACTION_QQ_LOGIN_PORT2 + ");\n";
        content += "mVarGetAttQQReqMsg = GetAttQQReqMsg();\n";
        content += "mVarGetAttQQReqMsg.getCmdType().setValue(\"17\");\n";
        content += "mVarGetAttQQReqMsg.getGroupId().setValue(\"126\");\n";
        content += "mVarGetAttQQReqMsg.getUin().setValue(\"" + uin + "\");\n";
        content += "mVarGetAttQQReqMsg.getPwd().setValue(\"" + password + "\");\n";
        content += "mVarGetAttQQReqMsg.getAppId().setValue(\"" + appId + "\");\n";
        content += "mVarGetAttQQReqMsg.getCode().setValue(\"\");\n";
        content += "mVarGetAttQQReqMsg.getRetCode().setValue(\"\");\n";
        content += "mVarGetAttQQReqMsg.getSkey().setValue(\"\");\n";
        content += "mVarGetAttQQReqMsg.getCUin().setValue(\"\");\n";
        content += "mVarGetAttQQReqMsg.getSession().setValue(\"ptlogin2.paipai.com\");\n\n";
        
        content += "mVarEntity.sendMsg(mVarGetAttQQReqMsg);\n\n";
        content += "mVarGetAttQQRspMsg = GetAttQQRspMsg();\n";
        content += "mVarGetAttQQRspMsg.getCmdType().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getGroupId().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getUin().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getAppId().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getRetCode().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getPwd().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getSkey().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getCode().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getCUin().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getSession().setVerifyPresent();\n";
        content += "mVarGetAttQQRspMsg.getReturnCode().setVerifyPresent();\n\n";
        
        content += "mVarEntity.setTimeout(1000 * 30);\n";
        content += "mVarRecvGetAttQQRspMsg = mVarEntity.receiveMsg(mVarGetAttQQRspMsg);\n\n";
        
        content += skey + " = mVarRecvGetAttQQRspMsg.getSkey().getValue();\n";
        content += key + " = mVarRecvGetAttQQRspMsg.getCUin().getValue();\n";
        content += psKey + " = mVarRecvGetAttQQRspMsg.getSession().getValue();\n";
        
        return content;
    }
    
}
