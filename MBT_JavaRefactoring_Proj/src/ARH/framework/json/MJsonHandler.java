/*
 * FileName: MJsonHandler.java
 * 
 * Description: create MJsonHandler class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-23 Create
 */


package ARH.framework.json;

import java.io.IOException;
import java.util.*;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;

import ARH.common.config.*;
import ARH.framework.exception.*;

/**
 * This class is used to handle the JSON
 * @author MICKCHEN
 * @version 1.0
 */
public class MJsonHandler
{
    /**
     * Define ObjectMapper, could reused
     */
    private static ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Constructor, forbidden to use
     */
    private MJsonHandler()
    {
    }
    
    /**
     * Get value by JSON key
     * only support simple JSON string
     * @param jsonStr JSON String
     * @param key 
     * @return value the value of the key
     */
    public static String getJsonValueByKey(String jsonStr, String key) throws MException
    {
        String value = null;
        if (null == jsonStr || null == key || jsonStr.trim().equals("") || key.trim().equals(""))
        {
            return value;
        }
        
        try
        {
            JsonNode rootNode = mapper.readValue(jsonStr, JsonNode.class);
            //JsonNode node = rootNode.path(key);
            value = rootNode.findValue(key).getTextValue();
        }
        catch (NullPointerException e)
        {
//            throw new MException("Error occurs when getting key \"" + key + "\" from json string \""
//                    + jsonStr + "\".");
        }
        catch (JsonMappingException e)
        {
            throw new MException("Error occurs when mapping json string \"" + jsonStr + "\".");
        }
        catch (JsonParseException e)
        {
            throw new MException("Error occurs when parsing json string \"" + jsonStr + "\".");
        }
        catch (IOException e)
        {
            throw new MException("Error occurs when getting key \"" + key + "\" from json string \""
                    + jsonStr + "\".");
        }
        
        return value;
    }
    
    /**
     * Convert path JSON string to map
     * @param jSonStr String type of JSON
     * @return map of JSON's value
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<HashMap<String, String>> getJsonPathList(String jsonStr)
            throws MException
    {
        ArrayList<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        if (null == jsonStr || jsonStr.trim().equals(""))
        {
            return retList;
        }
        
        try
        {
            List<LinkedHashMap<String, Object>> list = mapper.readValue(jsonStr, List.class);
            
            for (int i = 0; i < list.size(); ++i)
            {
                LinkedHashMap<String, Object> map = list.get(i);
                
                String stepStr = (String) map.get(MCommonConfig.MBT_XML_STEP);
                String stateStr = (String) map.get(MCommonConfig.MBT_XML_STATE);
                String covStr = (String) map.get(MCommonConfig.MBT_XML_COV);
                
                HashMap<String, String> retMap = new HashMap<String, String>();
                retMap.put(MCommonConfig.MBT_XML_STEP, stepStr);
                retMap.put(MCommonConfig.MBT_XML_STATE, stateStr);
                retMap.put(MCommonConfig.MBT_XML_COV, covStr);
                retList.add(retMap);
            }
        }
        catch (JsonMappingException e)
        {
            throw new MException("Error occurs when mapping json string \"" + jsonStr + "\".");
        }
        catch (JsonParseException e)
        {
            throw new MException("Error occurs when parsing json string \"" + jsonStr + "\".");
        }
        catch (IOException e)
        {
            throw new MException("Error occurs when reading from json string \"" + jsonStr + "\".");
        }
        
        return retList;
    }
    
    /**
     * Convert parameter JSON string to map
     * @param jSonStr String type of JSON
     * @return map of JSON's value
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<HashMap<String, String>> getJsonParamList(String jsonStr)
            throws MException
    {
        ArrayList<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        if (null == jsonStr || jsonStr.trim().equals(""))
        {
            return retList;
        }
        
        try 
        {
            List<LinkedHashMap<String, Object>> list= mapper.readValue(jsonStr, List.class);
            
            for (int i = 0; i < list.size(); i++) 
            {
                LinkedHashMap<String, Object> map = list.get(i);
                
                String nameStr = (String) map.get(MCommonConfig.MBT_XML_NAME);
                String valueStr = (String) map.get(MCommonConfig.MBT_XML_VALUE);
                //String typeStr = (String) map.get(MCommonConfig.MBT_XML_TYPE);
                
                HashMap<String, String> retMap = new HashMap<String, String>();
                retMap.put(MCommonConfig.MBT_XML_NAME, nameStr);
                retMap.put(MCommonConfig.MBT_XML_VALUE, valueStr);
                retMap.put(MCommonConfig.MBT_XML_TYPE, "string");
                retList.add(retMap);
            }
        }
        catch (JsonMappingException e)
        {
            throw new MException("Error occurs when mapping json string \"" + jsonStr + "\".");
        }
        catch (JsonParseException e)
        {
            throw new MException("Error occurs when parsing json string \"" + jsonStr + "\".");
        }
        catch (IOException e)
        {
            throw new MException("Error occurs when reading from json string \"" + jsonStr + "\".");
        }
        
        return retList;
    }
}
