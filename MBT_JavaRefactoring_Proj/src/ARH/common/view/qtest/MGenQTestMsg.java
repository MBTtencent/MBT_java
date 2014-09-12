/*
 * FileName: MGenQTestMsg.java
 * 
 * Description: create MGenQTestMsg class
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-09 Create file
 */


package ARH.common.view.qtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.MXmlParse;

/**
 * This class is used to generate QTest message script
 * @author MICKCHEN
 * @version 1.0
 * @see
 */
public class MGenQTestMsg
{
    /**
     * Define TAB
     */
    private static final String MQTEST_MSG_SCRIPT_TAB = "";//"    ";
    
    /**
     * Data file path
     */
    private static String dataFile;
    
    /**
     * Base path of the wizard file
     */
    private static String basePath;
    
    /**
     * Generate Message script
     * @param wizard
     * @param data
     * @param entity
     * @param dataList
     * @return Message script
     * @throws MException
     * @throws DocumentException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public String genMsg(String wizard, String data, String entity, 
            ArrayList<String> dataList) throws MException, DocumentException, IOException
    {
        String script = "";

        wizard = wizard.replaceAll("\\\\", "/");
        
        MGenDataFile genDataFile = new MGenDataFile(wizard, data);
        
        genDataFile.genDataFile(dataList);
        
        dataList.add(data);

        int idx = wizard.indexOf(MGenDataFile.QTEST_XML_CONFIG_PATH_TAG);
        basePath = wizard.substring(0, idx) + "/" + MGenDataFile.QTEST_XML_CONFIG_PATH_TAG;

        dataFile = data;
        
        MXmlParse xmlParse = new MXmlParse(wizard);
        Element root = xmlParse.getRoot();
        List msgList  = xmlParse.getSubElement(root, "message");
        
        // add entity
        script += MQTEST_MSG_SCRIPT_TAB + "mVarEntity = ResObjFactory.getObject('" + entity + "')\n\n";
        
        for (int i = 0; i < msgList.size(); i++)
        {
            Element msg = (Element) msgList.get(i);
            String name = xmlParse.getAttrValue(msg, "name");
            String action = xmlParse.getAttrValue(msg, "action");

            String msgPath = basePath + "/" + name.replaceAll("\\.", "\\/") + ".xml";

            script += genMsgPy(msgPath, action);
        }
        
        return script;
    }
    
    /**
     * Generate message PY script
     * @param msgPath message path
     * @param action
     * @return message script content
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings("rawtypes")
    private String genMsgPy(String msgPath, String action) throws MException, DocumentException
    {
        String script = "";
        
        MXmlParse xmlParse = new MXmlParse(msgPath);
        Element msg = xmlParse.getRoot();
        String msgName = xmlParse.getAttrValue(msg, "name");
        List fieldList = xmlParse.getSubElement(msg);

        MXmlParse xmlParseData = new MXmlParse(dataFile);
        Element dataRoot = xmlParseData.getRoot();
        
        List msgList = xmlParseData.getSubElementByKey(dataRoot, "name", msgName);

        script += MQTEST_MSG_SCRIPT_TAB + "mVar" + msgName + " = " + msgName + "()\n";
        
        String fieldScript = "";
        
        for (int i = 0; i < fieldList.size(); i++)
        {
            Element field = (Element) fieldList.get(i);
            
            String name = xmlParse.getAttrValue(field, "name");
            if (name == null) name = "";
            String classStr = xmlParse.getAttrValue(field, "class");
            if (classStr == null) classStr = "";
            String subClass = xmlParse.getAttrValue(field, "sub-class");
            if (subClass == null) subClass = ""; 
            String verifyType = xmlParse.getAttrValue(field, "verify-type");
            if (verifyType == null) verifyType = "";
            String type = xmlParse.getAttrValue(field, "type");
            if (type == null) type = "";
            String value = xmlParse.getAttrValue(field, "value");
            if (value == null) value = "";
            
            List fieldNodeList = new ArrayList<Element>();
            if (msgList.size() != 0)
            {
                fieldNodeList = xmlParseData.getSubElementByKey((Element) msgList.get(0), "name", name);
            }
            
            String head = MQTEST_MSG_SCRIPT_TAB + "mVar" + msgName;
            
            int msgType;
            if (action.toLowerCase().equalsIgnoreCase("send"))
            {
                msgType = 1;
            }
            else
            {
                msgType = 0;
            }
            
            int index = 0;
            
            if (fieldNodeList.size() == 0)
            {
                fieldScript += genFieldPy(head, name, type, classStr, subClass, 
                        verifyType, xmlParseData, null, index, msgType);
            }
            
            for (int j = 0; j < fieldNodeList.size(); j++)
            {
                Element fieldNode = (Element) fieldNodeList.get(j);
                fieldScript += genFieldPy(head, name, type, classStr, subClass, 
                        verifyType, xmlParseData, fieldNode, index, msgType);
                index++;
            }
        }

        script += fieldScript;
        
        if (action.equalsIgnoreCase("send"))
        {
            script += MQTEST_MSG_SCRIPT_TAB + "mVarEntity." + "sendMsg(mVar" + msgName + ")\n\n";
        }
        else if (action.equalsIgnoreCase("receive"))
        {
            script += "\n" + MQTEST_MSG_SCRIPT_TAB + "mVarEntity.setTimeout(1000 * 30)\n\n";
            script += MQTEST_MSG_SCRIPT_TAB + "mVarRecv" + msgName + " = mVarEntity.receiveMsg(mVar" + msgName + ")\n";
        }
        
        return script;
    }
    
    /**
     * generate script from XML file of field
     * @return
     * @throws MException 
     * @throws DocumentException 
     */
    @SuppressWarnings("rawtypes")
    private String genFieldPy(String head, String name, String type, 
            String classStr, String subClass, String verifyType, 
            MXmlParse xmlParseData, Element fieldNode, int index, int msgType) throws MException, DocumentException
    {
        String fieldScript = "";
        
        if (xmlParseData == null)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + 
                    "MXmlParse object is null.");

            throw new MException("MXmlParse object is null.");
        }
        
        if (type.equalsIgnoreCase("normal"))
        {
            if (classStr.isEmpty())
            {
                String data = "";
                if (fieldNode != null)
                {
                    String verify = xmlParseData.getAttrValue(fieldNode, "isVerify");
                    if ((verify != null && verify.toLowerCase().equalsIgnoreCase("true")) || (msgType == 1))
                    {
                        data = xmlParseData.getNodeData(fieldNode);
                        fieldScript += head + ".get" + name + "().setValue(" + data + ")\n";
                    }
                }
            }
            else
            {
                String fieldPath = basePath + "/" + classStr.replaceAll("\\.", "\\/") + ".xml";
                MXmlParse fieldXmlParse = new MXmlParse(fieldPath);
                Element fieldRoot = fieldXmlParse.getRoot();
                List fieldList = fieldXmlParse.getSubElement(fieldRoot);
                
                if (fieldList.size() == 0)
                {
                    String data = "";
                    
                    if (fieldNode != null)
                    {
                        String verify = xmlParseData.getAttrValue(fieldNode, "isVerify");
                        if ((verify != null && verify.toLowerCase().equalsIgnoreCase("true")) || (msgType == 1))
                        {
                            data = xmlParseData.getNodeData(fieldNode);
                            fieldScript += head + ".get" + name + "().setValue(" + data + ")\n";
                        }
                    }
                }
                else
                {
                    head += ".get" + name + "()";

                    for (int i = 0; i < fieldList.size(); i++)
                    {
                        Element field = (Element) fieldList.get(i);
                        String nameTmp = field.attributeValue("name");
                        if (nameTmp == null) nameTmp = "";
                        String classStrTmp = field.attributeValue("class");
                        if (classStrTmp == null) classStrTmp = "";
                        String subClassTmp = field.attributeValue("sub-class");
                        if (subClassTmp == null) subClassTmp = "";
                        String verifyTypeTmp = field.attributeValue("verify-type");
                        if (verifyTypeTmp == null) verifyTypeTmp = "";
                        String typeTmp = field.attributeValue("type");
                        if (typeTmp == null) typeTmp = "";
                        String valueTmp = field.attributeValue("value");
                        if (valueTmp == null) valueTmp = "";
                        
                        List subFieldList = new ArrayList<Element>();
                        if (fieldNode != null)
                        {
                            subFieldList = xmlParseData.getSubElementByKey(fieldNode, "name", nameTmp);
                        }
                        
                        int idx = 0;
                        if (subFieldList.size() == 0)
                        {
                            fieldScript += genFieldPy(head, nameTmp, typeTmp, classStrTmp, subClassTmp, 
                                    verifyTypeTmp, xmlParseData, null, idx, msgType);
                        }
                        
                        for (int j = 0; j < subFieldList.size(); j++)
                        {
                            Element fieldTmp = (Element) subFieldList.get(j);
                            fieldScript += genFieldPy(head, nameTmp, typeTmp, classStrTmp, subClassTmp, 
                                    verifyTypeTmp, xmlParseData, fieldTmp, idx, msgType);
                            idx++;
                        }
                    }
                }
            }
        }
        else if (type.equalsIgnoreCase("array"))
        {
            if (fieldNode != null)
            {
                String verify = xmlParseData.getAttrValue(fieldNode, "isVerify");
                if ((verify != null && verify.toLowerCase().equalsIgnoreCase("true")) || (msgType == 0))
                {
                    return fieldScript;
                }
            }
            
            head += ".get" + name + "()";
            String arrayAdd = head + ".add(" + subClass + "('" + name + "'))\n";

            if (classStr.isEmpty())
            {
                String data = "";
                fieldScript += arrayAdd;
                
                if (fieldNode != null)
                {
                    data = xmlParseData.getNodeData(fieldNode);
                    fieldScript += head + ".get(" + String.valueOf(index) + ")" + 
                            ".setValue(" + data + ")\n";
                }
            }
            else
            {
                head += ".get(" + String.valueOf(index) + ")";
                String fieldPath = basePath + "/" + classStr.replaceAll("\\.", "\\/") + ".xml";
                MXmlParse fieldXmlParse = new MXmlParse(fieldPath);
                Element fieldRoot = fieldXmlParse.getRoot();
                List fieldList = fieldXmlParse.getSubElement(fieldRoot);
                fieldScript += arrayAdd;

                for (int i = 0; i < fieldList.size(); i++)
                {
                    Element field = (Element) fieldList.get(i);
                    
                    String nameTmp = fieldXmlParse.getAttrValue(field, "name");
                    String classTmp = fieldXmlParse.getAttrValue(field, "class");
                    String subClassTmp = fieldXmlParse.getAttrValue(field, "sub-class");
                    String verifyTypeTmp = fieldXmlParse.getAttrValue(field, "verify-type");
                    String typeTmp = fieldXmlParse.getAttrValue(field, "type");
                    //String valueTmp = fieldXmlParse.getAttrValue(field, "value");
                    
                    List subFieldNodeList = new ArrayList<Element>();
                    if (fieldNode != null)
                    {
                        subFieldNodeList = xmlParseData.getSubElementByKey(fieldNode, "name", nameTmp);
                    }
                    
                    int idx = 0;
                    if (subFieldNodeList.size() == 0)
                    {
                        fieldScript += genFieldPy(head, nameTmp, typeTmp, classTmp, subClassTmp, 
                                verifyTypeTmp, xmlParseData, null, idx, msgType);
                    }
                    
                    for (int j = 0; j < subFieldNodeList.size(); j++)
                    {
                        Element fieldTmp = (Element) subFieldNodeList.get(j);
                        fieldScript += genFieldPy(head, nameTmp, typeTmp, classTmp, subClassTmp, 
                                verifyTypeTmp, xmlParseData, fieldTmp, idx, msgType);
                        idx++;
                    }
                }
            }
        }
        
        return fieldScript;
    }
}
