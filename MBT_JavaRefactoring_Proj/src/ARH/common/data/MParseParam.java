/*
 * FileName: MParseParam.java
 * 
 * Description: parse the parameters in the MBT XML
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-25 Create
 */


package ARH.common.data;

import java.util.*;

import org.dom4j.*;

import ARH.common.config.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.*;
import ARH.framework.file.*;
import ARH.framework.json.*;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.*;

/**
 * This class is used to parse the parameter
 * @author MICKCHEN
 * @version 1.0
 */
public class MParseParam
{
    /**
     * file name
     */
    private String fileName;
    
    /**
     * map that contain the parameter
     */
    @SuppressWarnings("unused")
    private HashMap<String, HashMap<String, String>> params;
    
    /**
     * constructor
     * @param fileName
     * @throws MException 
     */
    public MParseParam(String fileName) throws MException
    {
        if (!MFile.isExisted(fileName))
        {
            throw new MException("construct function fail.");
        }
        else
        {
            this.fileName = fileName;
            this.params = new HashMap<String, HashMap<String, String>>();
        }
    }
    
    /**
     * get file name
     * 
     * @return file name
     */
    public String getFileName()
    {
        return this.fileName;
    }
    
    /**
     * get parameter from the MBT XML
     * @return the map of parameter
     */
    @SuppressWarnings("rawtypes")
    public HashMap<String, ArrayList<HashMap<String, String>>> parseParam()
    {
        HashMap<String, ArrayList<HashMap<String, String>>> paramMap = 
            new HashMap<String, ArrayList<HashMap<String, String>>>();
        
        try
        {
            MXmlParse xmlParse = new MXmlParse(this.fileName);
            
            Element root = xmlParse.getRoot();
            Element scenarios = xmlParse.getSubFirstElement(root, 
                    MCommonConfig.MBT_XML_SCENARIOS);
            List scenarioList  = xmlParse.getSubElement(scenarios);
            
            int size = scenarioList.size();
            for (int i = 0; i < size; i++)
            {
                Element e = (Element) scenarioList.get(i);
                
                Element name = xmlParse.getSubFirstElement(e, MCommonConfig.MBT_XML_NAME);
                String nameStr = xmlParse.getNodeData(name);
                
                Element param = xmlParse.getSubFirstElement(e, MCommonConfig.MBT_XML_PARAM);
                String paramStr = xmlParse.getNodeData(param);
                
                paramMap.put(nameStr, MJsonHandler.getJsonParamList(paramStr));
            }
        }
        catch (Exception e) 
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
                    e.getMessage());
            paramMap = null;
        }
        
        return paramMap;
    }
}