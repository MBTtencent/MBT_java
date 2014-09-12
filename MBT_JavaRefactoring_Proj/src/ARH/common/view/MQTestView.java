/*
 * FileName: MQTestViewBase.java
 * 
 * Description: create MQTestViewBase class
 * 
 * Version: 1.0
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-06 Create
 */


package ARH.common.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import ARH.common.data.MDataAlgoMag;
import ARH.common.data.MDataConfig;
import ARH.common.view.qtest.MQTestActionHandler;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.json.MJsonHandler;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.MXmlParse;

/**
 * This class is used to set QTest case format, 
 * @author MICKCHEN
 * @version 1.0
 */
public class MQTestView extends MViewBase
{
    /**
     * Define action type
     */
    public static final int MLOAD_NON_MSG_SCRIPT = 1;
    public static final int MLOAD_NON_MSG_LOAD_VAR1 = 2;
    public static final int MLOAD_NON_MSG_LOAD_SCRIPT1 = 3;
    public static final int MLOAD_NON_MSG_LOAD_DB_VERIFY1 = 4;
    public static final int MLOAD_NON_MSG_QUERY = 5;
    public static final int MLOAD_NON_MSG_REMOTE_CALL = 6;
    public static final int MLOAD_NON_MSG_DB_VERIFY = 7;
    public static final int MLOAD_NON_MSG_LOAD_VAR2 = 8;
    public static final int MLOAD_NON_MSG_LOAD_SCRIPT2 = 9;
    public static final int MLOAD_NON_MSG_LOAD_DB_VERIFY2 = 10;
    public static final int MLOAD_NON_MSG_SET_VAR = 11;
    
    private MQTestActionHandler qTestActionHandler = new MQTestActionHandler();
    
    public static ArrayList<String> dataFileList = new ArrayList<String>();
    
    /**
     * Set case name
     * @param caseName original case name
     * @return case name after updated
     */
    public String setCaseName(String caseName)
    {
//        return caseName + ".py";
        return caseName + ".mtc";
    }
    
    /**
     * Set case comment
     * @param caseName original case name
     * @return case comment
     */
    public String setCaseComment(String caseName)
    {
        return "";
    }
    
    /**
     * Set case start log
     * @param caseName case name
     * @return case start log
     */
    public String setCaseStartLog(String caseName)
    {
        return "";
    }
    
    /**
     * Set case end log
     * @param caseName case name
     * @return case end log
     */
    public String setCaseEndLog(String caseName)
    {
        return "";
    }
    
    /**
     * Set step comment
     * @param stepNum step number
     * @param stepName step name
     * @return step comment
     */
    public String setStepComment(int stepNum, String stepName)
    {
        return "# " + String.valueOf(stepNum) + " " + stepName + "\n";
    }
    
    /**
     * Set step log
     * @param stepNum step number
     * @param stepName step name
     * @return log content
     */
    public String setStepLog(int stepNum, String stepName)
    {
        return "";
    }
    
    /**
     * Set step start log
     * @param stepNum step number 
     * @param stepName step name
     * @return step start log
     */
    public String setStepStartLog(int stepNum, String stepName)
    {
        return "stepStart(\"" + String.valueOf(stepNum) +
                ": " + stepName + "\")\n\n";
    }
    
    /**
     * Set step end log
     * @param stepNum step number 
     * @param stepName step name
     * @return step end log
     */
    public String setStepEndLog(int stepNum, String stepName)
    {
        return "\nstepEnd(\"" + String.valueOf(stepNum) + 
                ": " + stepName + "\")\n\n";
    }
    
    /**
     * Set action
     * @param actionName action name
     * @return action content
     */
    public String setAction(String actionStr)
    {
        try 
        {
            return qTestActionHandler.handleAction(actionStr) + "\n";
        } 
        catch (Exception e) {
            e.printStackTrace();
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + 
                    "Set action error...");
            
            return "";
        }
    }
    
    /**
     * Set previous data
     * @return result
     */
    public String setPreData()
    {
        String globalVars = "";
        HashSet<String> globalVar = qTestActionHandler.getGlobalVar();
        Iterator<String> it = globalVar.iterator();

        while (it.hasNext())
        {
            String var = it.next();
            globalVars += var + " = ''\n";
        }
        
        return globalVars + "\n";
    }
    
    /**
     * Set content
     * @param content script content
     * @return result 
     */
    public String setContent(String content)
    {
        return qTestActionHandler.setContent(content);
    }
    
    /**
     * Add view
     */
    public void addView()
    {
        MViewMag.getInsctance().addView("MQTestView");
    }
    
    /**
     * Handle the variables in the data file
     * @param content
     * @param sVarPath
     * @param caseName
     * @param dir
     * @param MBT XML path
     * @return true if success, otherwise false
     * @throws DocumentException 
     * @throws MException 
     */
    @Override
    public boolean handleVars(String content, String varPath, String caseName, 
            HashMap<Integer,String> nameMap, String dir, String mbtXmlPath)
            throws MException, DocumentException
    {
        return handleVarsAlign(content, varPath, caseName, nameMap, dir, mbtXmlPath);
        //return handleVarsDiscards(content, varPath, caseName, nameMap, dir, mbtXmlPath);
    }
    
    /**
     * Handle the variables in the data file
     * @param content
     * @param sVarPath
     * @param caseName
     * @param dir
     * @param MBT XML path
     * @return true if success, otherwise false
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean handleVarsAlign(String content, String varPath, String caseName, 
            HashMap<Integer,String> nameMap, String dir, String mbtXmlPath) throws MException, DocumentException
    {
        HashMap<String, Set<String>> stepVarMap = GetParamInStep(content);
        Set<String> keySet = stepVarMap.keySet();
        Iterator iter = keySet.iterator();
        
        HashMap<String, HashMap<Integer, HashMap<String, String>>> stepsVarMap =
            new HashMap<String, HashMap<Integer, HashMap<String, String>>>();
        
        while (iter.hasNext())
        {
            String stepName = (String) iter.next();
            Set<String> vars = stepVarMap.get(stepName);
            MXmlParse xmlParse = new MXmlParse(varPath);
            Element root = xmlParse.getRoot();
            List stepList = xmlParse.getSubElementByKey(root, "name", stepName);

            Element step = (Element) stepList.get(0);
            String algo = xmlParse.getAttrValue(step, "data");

            List stepVars = xmlParse.getSubElement(step, "var");
            ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();

            int stepVarSize = stepVars.size();
            for (int i = 0; i < stepVarSize; i++)
            {
                HashMap<String, String> stepVarsMap = new HashMap<String, String>();
                Element var = (Element) stepVars.get(i);
                String varName = xmlParse.getAttrValue(var, "name");
                stepVarsMap.put("name", varName);
                String varValue = xmlParse.getAttrValue(var, "value");
                stepVarsMap.put("value", varValue);
                String varType = xmlParse.getAttrValue(var, "type");
                stepVarsMap.put("type", varType);
                paramList.add(stepVarsMap);
            }

            HashMap<Integer, HashMap<String, String>> stepVarsMap;
            
            stepVarsMap = MDataAlgoMag.getInstance().getAlgorithm(algo).genData(paramList, vars);
            
            if (stepVarsMap != null)
            {
                stepsVarMap.put(stepName, (HashMap<Integer, HashMap<String, String>>)stepVarsMap.clone());
            }
            else
            {
                stepsVarMap.put(stepName, null);
            }
        }

        int size = 0; 
        Set<String> stepNameSet = stepsVarMap.keySet();
        Iterator stepNameIter = stepNameSet.iterator();
        while (stepNameIter.hasNext())
        {
            String stepName = (String) stepNameIter.next();
            if (stepsVarMap.get(stepName) != null)
            {
                if (size == 0)
                {
                    size = stepsVarMap.get(stepName).size();
                }
                else
                {
                    if (size > stepsVarMap.get(stepName).size())
                    {
                        size = stepsVarMap.get(stepName).size();
                    }
                }
            }
        }

        ArrayList<String> dataPathList = new ArrayList();
        ArrayList<String> contentList = new ArrayList();
        for (int i = 0; i < size; i++)
        {
            HashMap<String, String> stepVar = new HashMap<String, String>();
            String[] lineArr = getLines(content);
            int lineArrSize = lineArr.length;
            String dataPath = "";
            String dataPathTmp = "";
            String newContent = "";
            boolean flag = false;
            for (int j = 0; j < lineArrSize; j++)
            {
                String line = lineArr[j];
                if (line.trim().startsWith("stepStart"))
                {
                    int begin = line.indexOf(": ") + 2;
                    int end = line.indexOf("\")");
                    String stepNameTmp = line.substring(begin, end).trim();

                    if (stepsVarMap.get(stepNameTmp) != null)
                    {
                        stepVar = (HashMap<String, String>) stepsVarMap.get(stepNameTmp).get(i).clone();
                        flag = true;
                        dataPathTmp = "[(" + stepNameTmp + ")";
                    }
                    newContent += line + "\n";
                    continue;
                }
                else if (line.trim().startsWith("stepEnd"))
                {
                    stepVar.clear();
                    if (flag == true)
                    {
                        dataPathTmp += "]";
                        dataPath += dataPathTmp;
                    }
                    flag = false;
                    newContent += line + "\n";
                    continue;
                }
                else if ((line.trim().contains("setValue(getParams(") ||
                        line.trim().contains("setValue(\"getParams(")) &&
                        flag == true)
                {
                    int begin = 0;
                    if (-1 == line.trim().indexOf("setValue(getParams("))
                    {
                        begin = line.trim().indexOf("setValue(\"getParams(") + 20;
                    }
                    else
                    {
                        begin = line.trim().indexOf("setValue(getParams(") + 19;
                    }
                    int end = line.trim().indexOf("))");
                    if (end == -1)
                    {
                        end = line.trim().indexOf(")\")");
                    }

                    String var = line.trim().substring(begin, end);

                    if (stepVar.get(var) != null)
                    {
                        String varValue = var + "=" + stepVar.get(var);
                        if (dataPathTmp.endsWith(")"))
                        {
                            dataPathTmp += varValue;
                        }
                        else
                        {
                            if (!dataPathTmp.contains(varValue + ",") &&
                                    !dataPathTmp.endsWith(varValue))
                            {
                                dataPathTmp += ", " + var + "=" + stepVar.get(var);
                            }
                        }
                        
                        line = line.replace("getParams(" + var + ")", stepVar.get(var));
                    }
                }
                else if (line.trim().startsWith("assertEquals") && 
                        line.contains("getParams(") && flag == true)
                {
                    if (line.trim().contains("getParams("))
                    {
                        int begin = 0;
                        if (-1 == line.trim().indexOf("getParams("))
                        {
                            begin = line.trim().indexOf("\"getParams(") + 11;
                        }
                        else
                        {
                            begin = line.trim().indexOf("getParams(") + 10;
                        }
                        
                        int end = 0;
                        if (-1 == line.trim().indexOf(")\","))
                        {
                            end = line.trim().indexOf("),");
                        }
                        else
                        {
                            end = line.trim().indexOf(")\",");
                        }
                        
                        String var = line.trim().substring(begin, end);

                        if (stepVar.get(var) != null)
                        {
                            String varValue = var + "=" + stepVar.get(var);
                            if (dataPathTmp.endsWith(")"))
                            {
                                dataPathTmp += varValue;
                            }
                            else
                            {
                                if (!dataPathTmp.contains(varValue + ",") &&
                                        !dataPathTmp.endsWith(varValue))
                                {
                                    dataPathTmp += ", " + var + "=" + stepVar.get(var);
                                }
                            }
                            
                            line = line.replace("getParams(" + var + ")", stepVar.get(var));
                        }
                    }
                }
                
                newContent += line + "\n";
            }
            dataPathList.add(dataPath);
            contentList.add(newContent);
        }

        int contentListSize = contentList.size();
        for (int k = 0; k < contentListSize; k++)
        {
            ArrayList<String> scriptList = new ArrayList<String>();
            ArrayList<String> mbtDataPathList = new ArrayList<String>();
            HashMap<String, ArrayList<String>> dataCaseMap = ConvertParam(contentList.get(k), mbtXmlPath, caseName);
            
            if (dataCaseMap.get("case") != null)
            {
                scriptList = dataCaseMap.get("case");
            }
            if (dataCaseMap.get("data") != null)
            {
                mbtDataPathList = dataCaseMap.get("data");
            }
            
            int scriptListSize = scriptList.size();
            for (int i = 0; i < scriptListSize; i++)
            {
                this.genCase(scriptList.get(i), dir + "/" + 
                    this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + mbtDataPathList.get(i) + dataPathList.get(k)));/*String.valueOf(++scriptCount))*/ 
            }   
            
            if (scriptList.size() == 0)
            {
                this.genCase(contentList.get(k), dir + "/" + 
                        this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + dataPathList.get(k)));/*String.valueOf(k + 1))*/ 
            }
        }
        
        if (contentList.size() == 0)
        {
            ArrayList<String> scriptList = new ArrayList<String>();
            ArrayList<String> mbtDataPathList = new ArrayList<String>();
            HashMap<String, ArrayList<String>> dataCaseMap = ConvertParam(content, mbtXmlPath, caseName);
            
            if (dataCaseMap.get("case") != null)
            {
                scriptList = dataCaseMap.get("case");
            }
            if (dataCaseMap.get("data") != null)
            {
                mbtDataPathList = dataCaseMap.get("data");
            }
            
            int scriptListSize = scriptList.size();
            for (int i = 0; i < scriptListSize; i++)
            {
                this.genCase(scriptList.get(i), dir + "/" + 
                    this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + mbtDataPathList.get(i)));  /*String.valueOf(++scriptCount)*/
            }
            
            if (scriptListSize == 0)
            {
                this.genCase(content, dir + "/" + 
                        this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM)));  
            }
        }
        
        return true;
    }
    
    /**
     * Handle the variables in the data file
     * @param content
     * @param sVarPath
     * @param caseName
     * @param dir
     * @param MBT XML path
     * @return true if success, otherwise false
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    private boolean handleVarsDiscards(String content, String varPath, String caseName, 
            HashMap<Integer,String> nameMap, String dir, String mbtXmlPath) throws MException, DocumentException       
            
    {
        HashMap<String, Set<String>> stepVarMap = GetParamInStep(content);
        
        // combine the variables
        Set<String> keySet = stepVarMap.keySet();
        Iterator iter = keySet.iterator();

        // script list
        ArrayList<String> contentList = new ArrayList<String>();
        ArrayList<String> dataPathList = new ArrayList<String>();
        contentList.add("");
        dataPathList.add("");
        int unused = 1;
        int count = 0;
        while (iter.hasNext())
        {
            count++;
            
            String stepNameTmp = (String) iter.next();
            Set<String> vars = stepVarMap.get(stepNameTmp);
            
            if (vars.size() == 0)
            {
                count--;
                continue;
            }

            // get algorithm by step name
            MXmlParse xmlParse = new MXmlParse(varPath);
            Element root = xmlParse.getRoot();
            List steps = xmlParse.getSubElementByKey(root, "name", stepNameTmp);
            if (steps.size() == 0)
            {
                count--;
                continue;
            }
            else
            {
                Element step = (Element) steps.get(0);
                String algo = xmlParse.getAttrValue(step, "data");
                
                if (algo.isEmpty())
                {
                    algo = MDataConfig.MDATA_ALGORITHM_ALGIN_NAME;
                }

                // get variable
                List stepVars = xmlParse.getSubElement(step, "var");
                ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();
                
                int stepVarSize = stepVars.size();
                for (int i = 0; i < stepVarSize; i++)
                {
                    HashMap<String, String> stepVarsMap = new HashMap<String, String>();
                    Element var = (Element) stepVars.get(i);
                    String varName = xmlParse.getAttrValue(var, "name");
                    stepVarsMap.put("name", varName);
                    String varValue = xmlParse.getAttrValue(var, "value");
                    stepVarsMap.put("value", varValue);
                    String varType = xmlParse.getAttrValue(var, "type");
                    stepVarsMap.put("type", varType);
                    paramList.add(stepVarsMap);
                }
                
                HashMap<Integer, HashMap<String, String>> stepVarsMap = 
                    MDataAlgoMag.getInstance().getAlgorithm(algo).genData(paramList, 
                            stepVarMap.get(stepNameTmp));
                
                unused = unused * contentList.size();

                ArrayList<String> newContentList = (ArrayList<String>) contentList.clone();
                ArrayList<String> newDataPathList = (ArrayList<String>) dataPathList.clone();
                int newContentListSize = newContentList.size();
                for (int k = 0; k < newContentListSize; k++)
                {
                    String newContent = newContentList.get(k);
                    String newDataPath = newDataPathList.get(k);
                    String tmpDataPath = "";
                    String tmpContent = "";
                    if (newContent.isEmpty())
                    {
                        if (count == 1)
                        {
                            tmpContent = content; 
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else
                    {
                        tmpContent = newContent;
                        tmpDataPath = newDataPath;
                    }

                    if (stepVarsMap == null)
                    {
                        continue;
                    }
                    
                    int stepVarsMapSize = stepVarsMap.size();
                    for (int i = 0 ; i < stepVarsMapSize; i++)
                    {
                        String dataPath = "";
                        newContent = "";
                        HashMap<String, String> varMap = stepVarsMap.get(i);

                        if (!isParamSet(tmpContent))
                        {
                            newContent = tmpContent;
                        }
                        else
                        {
                            String[] lineArr = getLines(tmpContent);
                            int lineArrSize = lineArr.length;
                            boolean flag = false;
                            for (int j = 0; j < lineArrSize; j++)
                            {
                                String line = lineArr[j];
                                if (line.trim().startsWith("stepStart") &&
                                        line.contains(stepNameTmp))
                                {

                                    flag = true;
                                }
                                else if (line.trim().startsWith("stepEnd") &&
                                        line.trim().contains(stepNameTmp))
                                {
                                    flag = false;
                                }
                                else if ((line.trim().contains("setValue(getParams(") ||
                                        line.trim().contains("setValue(\"getParams(")) &&
                                        flag == true)
                                {
                                    int begin = 0;
                                    if (-1 == line.trim().indexOf("setValue(getParams("))
                                    {
                                        begin = line.trim().indexOf("setValue(\"getParams(") + 20;
                                    }
                                    else
                                    {
                                        begin = line.trim().indexOf("setValue(getParams(") + 19;
                                    }
                                    int end = line.trim().indexOf("))");
                                    if (end == -1)
                                    {
                                        end = line.trim().indexOf(")\")");
                                    }
    
                                    String var = line.trim().substring(begin, end);
    
                                    if (varMap.get(var) != null)
                                    {   
                                        if (dataPath.isEmpty())
                                        {
                                            dataPath = var + "=" + varMap.get(var);
                                        }
                                        else
                                        {
                                            String varValue = var + "=" + varMap.get(var);
                                            if (!dataPath.contains(varValue + ",") &&
                                                    !dataPath.endsWith(varValue))
                                            {
                                                dataPath += ", " + varValue;
                                            }
                                        }
                                        
                                        line = line.replace("getParams(" + var + ")", varMap.get(var));
                                    }
                                }
                                else if (line.trim().startsWith("assertEquals") && 
                                        line.contains("getParams(") && flag == true)
                                {
                                    if (line.trim().contains("getParams("))
                                    {
                                        int begin = 0;
                                        if (-1 == line.trim().indexOf("getParams("))
                                        {
                                            begin = line.trim().indexOf("\"getParams(") + 11;
                                        }
                                        else
                                        {
                                            begin = line.trim().indexOf("getParams(") + 10;
                                        }
                                        
                                        int end = 0;
                                        if (-1 == line.trim().indexOf(")\","))
                                        {
                                            end = line.trim().indexOf("),");
                                        }
                                        else
                                        {
                                            end = line.trim().indexOf(")\",");
                                        }
                                        
                                        String var = line.trim().substring(begin, end);
                                        if (varMap.get(var) != null)
                                        {
                                            if (dataPath.isEmpty())
                                            {
                                                dataPath = var + "=" + varMap.get(var);
//                                                
                                            }
                                            else
                                            {
                                                String varValue = var + "=" + varMap.get(var);
                                                if (!dataPath.contains(varValue + ",") &&
                                                        !dataPath.endsWith(varValue))
                                                {
                                                    dataPath += ", " + varValue;
                                                }
                                            }
                                            
                                            line = line.replace("getParams(" + var + ")", varMap.get(var));
                                        }
                                    }
                                }
                                
                                newContent += line + "\n";
                            }
                        }
                        
                        String stepDataPath = tmpDataPath;
                        if (!dataPath.isEmpty())
                        {
                            stepDataPath += "[(" + stepNameTmp + ")" + dataPath + "]";
                        }
                        
                        dataPathList.add(stepDataPath);
                        contentList.add(newContent);
                    }
                }
            }
        }
        
        for (int i = 0; i < unused; i++)
        {
            contentList.remove(0);
            dataPathList.remove(0);
        }
        
//        int scriptCount = 0;
        int contentListSize = contentList.size();
        for (int k = 0; k < contentListSize; k++)
        {
            ArrayList<String> scriptList = new ArrayList<String>();
            ArrayList<String> mbtDataPathList = new ArrayList<String>();
            HashMap<String, ArrayList<String>> dataCaseMap = ConvertParam(contentList.get(k), mbtXmlPath, caseName);
            
            if (dataCaseMap.get("case") != null)
            {
                scriptList = dataCaseMap.get("case");
            }
            if (dataCaseMap.get("data") != null)
            {
                mbtDataPathList = dataCaseMap.get("data");
            }
            
            int scriptListSize = scriptList.size();
            for (int i = 0; i < scriptListSize; i++)
            {
                this.genCase(scriptList.get(i), dir + "/" + 
                    this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + mbtDataPathList.get(i) + dataPathList.get(k)));/*String.valueOf(++scriptCount))*/ 
            }   
            
            if (scriptList.size() == 0)
            {
                this.genCase(contentList.get(k), dir + "/" + 
                        this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + dataPathList.get(k)));/*String.valueOf(k + 1))*/ 
            }
        }
        
        if (contentList.size() == 0)
        {
            ArrayList<String> scriptList = new ArrayList<String>();
            ArrayList<String> mbtDataPathList = new ArrayList<String>();
            HashMap<String, ArrayList<String>> dataCaseMap = ConvertParam(content, mbtXmlPath, caseName);
            
            if (dataCaseMap.get("case") != null)
            {
                scriptList = dataCaseMap.get("case");
            }
            if (dataCaseMap.get("data") != null)
            {
                mbtDataPathList = dataCaseMap.get("data");
            }
            
            int scriptListSize = scriptList.size();
            for (int i = 0; i < scriptListSize; i++)
            {
                this.genCase(scriptList.get(i), dir + "/" + 
                    this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM) + mbtDataPathList.get(i)));  /*String.valueOf(++scriptCount)*/
            }
            
            if (scriptListSize == 0)
            {
                this.genCase(content, dir + "/" + 
                        this.setCaseName(caseName + nameMap.get(MCASE_NAME_TYPE_NUM)));  
            }
        }
        
        return true;
    }
    
    /**
     * Convert the scenario parameter to value
     * @param script content
     * @param MBT XML path
     * @throws DocumentException 
     * @throws MException 
     */
    protected HashMap<String, ArrayList<String>> ConvertParam(String content, String mbtXmlPath, String caseName) 
            throws MException, DocumentException
    {
        HashMap<String, ArrayList<String>> retMap = new HashMap<String, ArrayList<String>>();
        
        ArrayList<String> scriptList = new ArrayList<String>();
        ArrayList<String> dataPathList = new ArrayList<String>();
        
        // Get scenario parameter
        String scenarioName = caseName.substring(0, caseName.indexOf('_'));
        HashMap<Integer, HashMap<String, String>> paramMap = 
                    GetScenarioParam(content, mbtXmlPath, scenarioName);

        if (paramMap == null)
        {
            return retMap;
        }
        for (int i = 0; i < paramMap.size(); i++)
        {
            String newContent = "";
            String dataPath = "";
            
            HashMap<String, String> varMap = paramMap.get(i);
            
            if (!isParamSet(content))
            {
                continue;
            }
            
            String[] lineArr = getLines(content);
            
            int lineLen = lineArr.length;
            for (int j = 0; j < lineLen; j++)
            {
                String line = lineArr[j];
                
                if (line.trim().contains("setValue(getParams(") ||
                        line.trim().contains("setValue(\"getParams("))
                {
                    int begin = 0;
                    if (-1 == line.trim().indexOf("setValue(getParams("))
                    {
                        begin = line.trim().indexOf("setValue(\"getParams(") + 20;
                    }
                    else
                    {
                        begin = line.trim().indexOf("setValue(getParams(") + 19;
                    }
                    int end = line.trim().indexOf("))");
                    if (end == -1)
                    {
                        end = line.trim().indexOf(")\")");
                    }
                    String var = line.trim().substring(begin, end);

                    if (dataPath.isEmpty())
                    {
                        dataPath = var + "=" + varMap.get(var);
                    }
                    else
                    {
                        dataPath += ", " + var + "=" + varMap.get(var);
                    }
                    
                    line = line.replace("getParams(" + var + ")", varMap.get(var));
                }
                else if (line.trim().startsWith("assertEquals") && line.contains("getParams("))
                {
                    int begin = 0;
                    if (-1 == line.trim().indexOf("getParams("))
                    {   
                        begin = line.trim().indexOf("\"getParams(") + 11;
                    }
                    else
                    {
                        begin = line.trim().indexOf("getParams(") + 10;
                    }
                    
                    int end = 0;
                    if (-1 == line.trim().indexOf("),"))
                    {
                        end = line.trim().indexOf(")\",");
                    }
                    else
                    {
                        end = line.trim().indexOf("),");
                    }
                    
                    String var = line.trim().substring(begin, end);
                    String varValue = var + "=" + varMap.get(var);
                    if (dataPath.isEmpty())
                    {
                        if (!dataPath.contains(varValue + ",") &&
                                !dataPath.endsWith(varValue))
                        {
                            dataPath += varValue;
                        }
                    }
                    else
                    {
                        if (!dataPath.contains(varValue + ",") &&
                                !dataPath.endsWith(varValue))
                        {
                            dataPath += ", " + varValue;
                        }
                    }
                    
                    line = line.replace("getParams(" + var + ")", varMap.get(var));
                }
                
                newContent += line + "\n";
            }
            
            String stepDataPath = "";
            if (!dataPath.isEmpty())
            {
                stepDataPath = "[" + dataPath + "]";
            }
            
            dataPathList.add(stepDataPath);
            
            scriptList.add(newContent);
        }
        
        retMap.put("data", dataPathList);
        retMap.put("case", scriptList);
        
        return retMap;
    }
    
    /**
     * Get scenario Parameter value
     * @param mbtXmlPath
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings("rawtypes")
    protected HashMap<Integer, HashMap<String, String>> GetScenarioParam(String content, 
                    String mbtXmlPath, String scenarioName) throws MException, DocumentException
    {
        String algo = "";
        Set<String> paramSet = new HashSet<String>();
        ArrayList<HashMap<String, String>> valueList = new ArrayList<HashMap<String, String>>();
        
        // Parse scenario parameter
        MXmlParse xmlParse = new MXmlParse(mbtXmlPath);
        Element root = xmlParse.getRoot();
        Element scenarios = xmlParse.getSubFirstElement(root, "scenarios");
        List scenarioList = xmlParse.getSubElement(scenarios, "scenario");
        
        int scenarioListSize = scenarioList.size();
        for (int i = 0; i < scenarioListSize; i++)
        {
            Element scenario = (Element) scenarioList.get(i);
            Element nameElement = xmlParse.getSubFirstElement(scenario, "name");
            Element optionElement = xmlParse.getSubFirstElement(scenario, "option");
            Element paramElement = xmlParse.getSubFirstElement(scenario, "param");
            
            String nameStr = xmlParse.getNodeData(nameElement);

            if (scenarioName.equalsIgnoreCase(nameStr))
            {
                String optionStr = xmlParse.getNodeData(optionElement);
                String paramStr = xmlParse.getNodeData(paramElement);
                
                algo = MJsonHandler.getJsonValueByKey(optionStr, "data");
                if (algo == null)
                {
                    algo = MDataConfig.MDATA_ALGORITHM_ALGIN_NAME;
                }
                
                valueList = MJsonHandler.getJsonParamList(paramStr);
                paramSet = GetParamInContent(content);
                
                break;
            }
        }
        
        HashMap<Integer, HashMap<String, String>> stepVarsMap = 
                    MDataAlgoMag.getInstance().getAlgorithm(algo).genData(valueList, paramSet);
        
        return stepVarsMap;
    }
    
    /**
     * Get parameter in the steps
     * @param content
     * @return parameter map
     */
    protected HashMap<String, Set<String>> GetParamInStep(String content)
    {
        HashMap<String, Set<String>> stepVarMap = new HashMap<String, Set<String>>();
        String stepName = "";
        
        if (!isParamSet(content))
        {
            return stepVarMap;
        }
        
        String[] lineArray = getLines(content);
        int lineNum = lineArray.length;
        for (int i = 0; i < lineNum; i++)
        {
            String line = lineArray[i];
            
            if (line.trim().startsWith("stepStart"))
            {
                int begin = line.indexOf(": ") + 2;
                int end = line.indexOf("\")");
                stepName = line.substring(begin, end).trim();
                Set<String> varList = new HashSet<String>();
                stepVarMap.put(stepName, varList);
                continue;
            }
            else if (line.trim().contains("setValue(getParams(") ||
                    line.trim().contains("setValue(\"getParams("))
            {
                int begin = 0;
                if (-1 == line.trim().indexOf("setValue(getParams("))
                {
                    begin = line.trim().indexOf("setValue(\"getParams(") + 20;
                }
                else
                {
                    begin = line.trim().indexOf("setValue(getParams(") + 19;
                }
                int end = line.trim().indexOf("))");
                if (end == -1)
                {
                    end = line.trim().indexOf(")\")");
                }
                String var = line.trim().substring(begin, end);

                if (stepVarMap.get(stepName) != null)
                {
                    stepVarMap.get(stepName).add(var);
                }
                
                continue;
            }
            else if (line.trim().startsWith("assertEquals") && line.trim().contains("getParams"))
            {
                int begin = 0;
                if (-1 == line.trim().indexOf("getParams("))
                {
                    begin = line.trim().indexOf("\"getParams(") + 11;
                }
                else
                {
                    begin = line.trim().indexOf("getParams(") + 10;
                }
                
                int end = 0;
                if (-1 == line.trim().indexOf("),"))
                {
                    end = line.trim().indexOf(")\",");
                }
                else
                {
                    end = line.trim().indexOf("),");
                }
                String var = line.trim().substring(begin, end);

                if (stepVarMap.get(stepName) != null)
                {
                    stepVarMap.get(stepName).add(var);
                }
                
                continue;
            }
        }
        
        return stepVarMap;
    }
    
    /**
     * Gets parameter in the content
     * @param content
     * @return variable set 
     */
    protected Set<String> GetParamInContent(String content)
    {
        Set<String> paramSet = new HashSet<String>();;
        
        if (!isParamSet(content))
        {
            return paramSet;
        }
        
        String[] lineArray = getLines(content);
        int lineNum = lineArray.length;
        for (int i = 0; i < lineNum; i++)
        {
            String line = lineArray[i];
                        
            if (line.trim().contains("setValue(getParams(") ||
                    line.trim().contains("setValue(\"getParams("))
            {
                int begin = 0;
                if (-1 == line.trim().indexOf("setValue(getParams("))
                {
                    begin = line.trim().indexOf("setValue(\"getParams(") + 20;
                }
                else
                {
                    begin = line.trim().indexOf("setValue(getParams(") + 19;
                }
                int end = line.trim().indexOf("))");
                if (end == -1)
                {
                    end = line.trim().indexOf(")\")");
                }
                String var = line.trim().substring(begin, end);
                paramSet.add(var);

                continue;
            }
            else if (line.trim().startsWith("assertEquals") && line.contains("getParams("))
            {
                int begin = 0;
                if (-1 == line.trim().indexOf("getParams("))
                {
                    begin = line.trim().indexOf("\"getParams(") + 11;
                }
                else
                {
                    begin = line.trim().indexOf("getParams(") + 10;
                }
                int end = line.trim().indexOf("),");
                if (end == -1)
                {
                    end = line.trim().indexOf(")\",");
                }
                String var = line.trim().substring(begin, end);
                paramSet.add(var);
                
                continue;
            }
        }
        
        return paramSet;
    }
 
    /**
     * Split content by "\n"
     * @param content
     * @return line array
     */
    private String[] getLines(String content)
    {
        StringTokenizer token = new StringTokenizer(content, "\n");
        String[] lineArr = new String[token.countTokens()]; 
        int lineCount = 0;  
        while (token.hasMoreTokens())
        {  
            lineArr[lineCount++] = token.nextToken();  
        }
        return lineArr;
    }
    
    /**
     * Check the string contain some tag
     * @param content
     * @return true if contain, otherwise false
     */
    private boolean isParamSet(String content)
    {
        if (content.contains("getParams(") ||
                content.contains("getParams("))
        {
            return true;
        }
        
        return false;
    }

    
}
