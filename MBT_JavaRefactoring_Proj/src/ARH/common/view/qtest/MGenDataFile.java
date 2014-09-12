/*
 * FileName: MGenDataFile.java
 * 
 * Description: create MGenDataFile class
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-09 Create file
 */


package ARH.common.view.qtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import ARH.common.view.MQTestView;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;
import ARH.framework.file.MFile;
import ARH.framework.logger.MLogMag;
import ARH.framework.xml.MXmlCreate;
import ARH.framework.xml.MXmlFileWriter;
import ARH.framework.xml.MXmlFormat;
import ARH.framework.xml.MXmlParse;

/**
 * This class is used to generate data file
 * @author MICKCHEN
 * @version  1.0
 */
public class MGenDataFile
{
    /**
     * Define some constants
     */
    private static String XML_CONFIG_WIZARD_WIZARD      = "wizard";
    private static String XML_CONFIG_MSG_NAME           = "name";
//    private static String XML_CONFIG_MSG_ACTION         = "action";
    private static String XML_CONFIG_WIZARD_MSG         = "message";
    private static String XML_CONFIG_FIELD_NAME         = "name";
    private static String XML_CONFIG_FIELD_SUB_CLASS    = "sub-class";
    private static String XML_CONFIG_FIELD_CLASS        = "class";
    private static String XML_CONFIG_FIELD_TYPE         = "type";
    private static String XML_CONFIG_FIELD_VALUE        = "value";
    private static String XML_CONFIG_FIELD_VALUE_TYPE   = "value-type";
    private static String XML_CONFIG_WIZARD_FIELD       = "field";
    private static String XML_CONFIG_WIZARD_FIELD_NAME  = "name";
    private static String XML_CONFIG_WIZARD_FIELD_VERIFY_TYPE  = "verify-type";
    
    public static String QTEST_XML_CONFIG_PATH_TAG = "config/wizard/message";
    
    /**
     * Wizard file
     */
    private String wizardFile;
    
    /**
     * Data file
     */
    private String dataFile;
    
    /**
     * The base path
     */
    private String basePath;
    
    /**
     * Constructor
     * @param wizardFile wizard file path
     * @param dataFile data file path
     */
    public MGenDataFile(String wizardFile, String dataFile)
    {
        dataFile = dataFile.replaceAll("\\\\", "/");
        wizardFile = wizardFile.replaceAll("\\\\", "/");
        
        if (!MBasicApi.isXMLFileValid(wizardFile))
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + 
                    "wizard file is not valid.");
            return;
        }
        
        if (!dataFile.endsWith(".xml"))
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() +
                    "data file is not end with 'XML'.");
            return;
        }
        
        this.wizardFile = wizardFile;
        this.dataFile = dataFile;

        int idx = wizardFile.indexOf(QTEST_XML_CONFIG_PATH_TAG);
        if (idx > 0)
        {
            this.basePath = wizardFile.substring(0, idx) + "/" + QTEST_XML_CONFIG_PATH_TAG; 
        }
        else
        {
            this.basePath = "";
        }
    }
    
    /**
     * Get base path
     * @return base path
     */
    public String getBasePath()
    {
        return this.basePath;
    }
    
    /**
     * Generate data files
     * @param dataFiles data file list
     * @throws DocumentException 
     * @throws MException 
     * @throws IOException 
     */
    public void genDataFile(ArrayList<String> dataFiles) throws MException, DocumentException, IOException
    {
        if (MQTestView.dataFileList.contains(this.dataFile))
        {
            return;
        }
        MQTestView.dataFileList.add(this.dataFile);
        
        MXmlCreate xmlCreate = new MXmlCreate(this.dataFile, 0);
        
        this.genDataWizard(xmlCreate);

        for (int i = 0; i < dataFiles.size(); i++)
        {
            if (dataFiles.get(i).equalsIgnoreCase(this.dataFile))
            {
                return;
            }
        }

        if (MFile.isExisted(this.dataFile))
        {
            // update
            Document doc = xmlCreate.getDoc();
            
            MXmlParse xmlParse = updateXmlFile(this.dataFile, doc);
            MXmlFormat format = new MXmlFormat();
            MXmlFileWriter.writeXml(xmlParse.getDoc(), this.dataFile, format.getFormat());
        }
        else
        {
            xmlCreate.createXml();
        }
    }
    
    /**
     * Generate data in wizard file
     * @param xmlCreate MXmlCreate object
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings("rawtypes")
    private void genDataWizard(MXmlCreate xmlCreate) throws MException, DocumentException
    {
        if (xmlCreate == null)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + " MXmlCreate object is null.");
            return;
        }
        
        Element wizardNode = xmlCreate.addElement(null, XML_CONFIG_WIZARD_WIZARD);
        
        MXmlParse xmlParse = new MXmlParse(this.wizardFile);
        Element node = xmlParse.getRoot();
        List msgList = xmlParse.getSubElement(node, XML_CONFIG_WIZARD_MSG);
        
        for (int i = 0; i < msgList.size(); i++)
        {
            Element element = (Element) msgList.get(i);
            if (element != null)
            {
                String name = xmlParse.getAttrValue(element, XML_CONFIG_MSG_NAME).replaceAll("\\.", "/");
                
                //String action = xmlParse.getAttrValue(element, XML_CONFIG_MSG_ACTION);
                
                String msgPath = this.basePath + "/" + name + ".xml";
                this.genDataMsg(xmlCreate, wizardNode, msgPath);
            }
        }
    }
    
    /**
     * Generate Data in message file
     * @param xmlCreate
     * @param node
     * @param msgPath
     * @throws DocumentException 
     * @throws MException 
     */
    @SuppressWarnings("rawtypes")
    private void genDataMsg(MXmlCreate xmlCreate, Element node, String msgPath) throws MException, DocumentException
    {
        MXmlParse MsgXmlParse = new MXmlParse(msgPath);
        Element msgNode = MsgXmlParse.getRoot();
        Element msg = null;
        if (msgNode != null)
        {
            String msgName = MsgXmlParse.getAttrValue(msgNode, XML_CONFIG_MSG_NAME);
            msg = xmlCreate.addElement(node, XML_CONFIG_WIZARD_MSG);
            xmlCreate.addAttr(msg, XML_CONFIG_MSG_NAME, msgName);
        }
        else
        {
            MLogMag.getInstance().getLogger().info(MBasicApi.getLineInfo() + " message node is null.");
            return;
        }
        
        List fileNodes = MsgXmlParse.getSubElement(msgNode);
        
        for (int i = 0; i < fileNodes.size(); i++)
        {
            Element field = (Element) fileNodes.get(i);
            
            if (field != null)
            {
                String name = field.attributeValue(XML_CONFIG_FIELD_NAME);
                if (name == null) name = "";
                String subClass = field.attributeValue(XML_CONFIG_FIELD_SUB_CLASS);
                if (subClass == null) subClass = "";
                String classStr = field.attributeValue(XML_CONFIG_FIELD_CLASS);
                if (classStr == null) classStr = "";
                String type = field.attributeValue(XML_CONFIG_FIELD_TYPE);
                if (type == null) type = "";
                String value = field.attributeValue(XML_CONFIG_FIELD_VALUE);
                if (value == null) value = "";
                String valueType = field.attributeValue(XML_CONFIG_FIELD_VALUE_TYPE);
                if (valueType == null) valueType = "0";
                
                this.genDataField(xmlCreate, msg, classStr, type, name, subClass, value, valueType);
            }
        }
    }
    
    /**
     * Generate data in field field
     * @param xmlCreate
     * @param parNode
     * @param classStr
     * @param type
     * @param name
     * @param subClass
     * @param value
     * @param valueType
     * @throws MException 
     * @throws DocumentException 
     */
    @SuppressWarnings("rawtypes")
    private void genDataField(MXmlCreate xmlCreate, Element parNode, String classStr,
            String type, String name, String subClass, String value, String valueType) throws MException, DocumentException
    {
        Element fieldNode = xmlCreate.addElement(parNode, XML_CONFIG_WIZARD_FIELD);
        xmlCreate.addAttr(fieldNode, XML_CONFIG_WIZARD_FIELD_NAME, name);
        
        if (subClass != null && !subClass.isEmpty())
        {
            xmlCreate.addAttr(fieldNode, XML_CONFIG_FIELD_SUB_CLASS, subClass);
            if (classStr != null && classStr.isEmpty())
            {
                if (Integer.parseInt(valueType) == 0)
                {
                    xmlCreate.addElementData(fieldNode, "\"" + value + "\"");
                }
                else
                {
                    xmlCreate.addElementData(fieldNode, value);
                }
            }
        }
        else
        {
            if (classStr != null && classStr.isEmpty())
            {
                if (Integer.parseInt(valueType) == 0)
                {
                    xmlCreate.addElementData(fieldNode, "\"" + value + "\"");
                }
                else
                {
                    xmlCreate.addElementData(fieldNode, value);
                }
            }
        }
        
        if (classStr != null && !classStr.isEmpty())
        {
            String fieldPath = this.basePath + "/" + classStr.replaceAll("\\.", "/") + ".xml";

            MXmlParse FieldXmlParse = new MXmlParse(fieldPath);
            Element fieldRoot = FieldXmlParse.getRoot();
            
            List fieldList = FieldXmlParse.getSubElement(fieldRoot);
            
            int size = fieldList.size();
            if (size == 0)
            {
                if (Integer.parseInt(valueType) == 0)
                {
                    xmlCreate.addElementData(fieldNode, "\"" + value + "\"");
                }
                else
                {
                    xmlCreate.addElementData(fieldNode, value);
                }
            }
            
            for (int i = 0; i < size; i++)
            {
                Element field = (Element) fieldList.get(i);
                if (field != null)
                {
                    String fieldName = field.attributeValue(XML_CONFIG_FIELD_NAME);
                    if (fieldName == null) fieldName = "";
                    String fieldClassStr = field.attributeValue(XML_CONFIG_FIELD_CLASS);
                    if (fieldClassStr == null) fieldClassStr = "";
                    String fieldSubClass = field.attributeValue(XML_CONFIG_FIELD_SUB_CLASS);
                    if (fieldSubClass == null) fieldSubClass = "";
                    String fieldVerifyType = field.attributeValue(XML_CONFIG_WIZARD_FIELD_VERIFY_TYPE);
                    if (fieldVerifyType == null) fieldVerifyType = "";
                    String fieldType = field.attributeValue(XML_CONFIG_FIELD_TYPE);
                    if (fieldType == null) fieldType = "";
                    String fieldValue = field.attributeValue(XML_CONFIG_FIELD_VALUE);
                    if (fieldValue == null) fieldValue = "";
                    String fieldValueType = field.attributeValue(XML_CONFIG_FIELD_VALUE_TYPE);
                    if (fieldValueType == null) fieldValueType = "0";
                    
                    this.genDataField(xmlCreate, fieldNode, fieldClassStr, fieldType, fieldName, 
                            fieldSubClass, fieldValue, fieldValueType);
                }
            }
        }
    }
    
    /**
     * update data file
     * @param dataFile original data file
     * @param doc Document object
     * @return true if update success, otherwise false 
     * @throws DocumentException 
     * @throws MException 
     */
    public MXmlParse updateXmlFile(String dataFile, Document doc) throws MException, DocumentException
    {
        MXmlParse xmlParse = new MXmlParse(dataFile);
        Element wizardRoot = xmlParse.getRoot();

        if (wizardRoot == null)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + 
                    "wizard root element is null.");
            return null;
        }

        MXmlParse xmlParse2 = new MXmlParse(doc);
        Element wizardRoot2 = xmlParse2.getRoot();

        for (int i = 0; i < xmlParse2.getSubElement(wizardRoot2).size(); i++)
        {
            handleField((Element) xmlParse.getSubElement(wizardRoot).get(i), 
                    (Element) xmlParse2.getSubElement(wizardRoot2).get(i));
        }

        bakFile(dataFile);
        
        return xmlParse;
    }
    
    /**
     * Handle field
     * @param node1
     * @param node2
     * @return true if success,otherwise false
     */
    private void handleField(Element node1, Element node2)
    {
        int size1 = node1.elements().size();
        int size2 = node2.elements().size();

        if ((size1 == 0) || (size2 == 0))
        {
            return;
        }
        else
        {
            Element node1Tmp = (Element) node1.clone();
            for (int i = 0; i < node1Tmp.elements().size(); i++)
            {
                String value1 = ((Element) node1Tmp.elements().get(i)).attributeValue("name");
                
                int index = 0;
                for (int j = 0; j < node2.elements().size(); j++)
                {
                    index++;
                    if (value1.equalsIgnoreCase(((Element) node2.elements().get(j)).attributeValue("name")))
                    {
                        handleField(((Element) node2.elements().get(j)), 
                                (Element) node1Tmp.elements().get(i));
                        break;
                    }
                    else
                    {
                        if (index == size2)
                        {
                            String name = ((Element) node1Tmp.elements().get(i)).attributeValue("name");
                            
                            Element nodeTmp = MXmlParse.getFirstSubElementByKey(node1, "name", name);
                            node1.remove(nodeTmp);
                            break;
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
            }
            
            Element node1Tmp2 = (Element) node1.clone();
            
            for (int i = 0; i < node2.elements().size(); i++)
            {
                String value2 = ((Element) node2.elements().get(i)).attributeValue("name");
                
                int index = 0;
                
                for (int j = 0; j < node1Tmp2.elements().size(); j++)
                {
                    index++;

                    if (value2.equalsIgnoreCase(((Element) node1Tmp2.elements().get(j)).attributeValue("name")))
                    {
                        handleField((Element) node2.elements().get(i), 
                                ((Element) node1Tmp2.elements().get(j)));
                        break;
                    }
                    else
                    {
                        if (index == size1)
                        {
                            Element field = (Element) ((Element) node2.elements().get(i)).clone();
                            node1.add(field);
                            break;
                        }
                        else
                        {
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Bake file
     * @param filePath file path
     */
    private static void bakFile(String filePath) throws MException
    {
        if (!MFile.isExisted(filePath))
        {
            return;
        }
        
        if (MFile.isExisted(filePath + ".old"))
        {
            MFile.delFile(filePath + ".old");
        }

        MFile.copyFile(filePath, filePath + ".old");
    }
}