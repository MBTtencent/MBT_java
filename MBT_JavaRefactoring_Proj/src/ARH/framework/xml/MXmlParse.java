/*
 * FileName: MXmlParse.xml
 * 
 * Description: create MXmlParse class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-21 Create
 */


package ARH.framework.xml;

import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

import ARH.framework.exception.*;
import ARH.framework.file.MFile;

/**
 * the MXmlParse class used to parse XML file
 * @author MICKCHEN
 * @version 1.0
 */
public class MXmlParse
{
    /**
     * File name
     */
    private String fileName;
    
    /**
     * Document object of XML
     */
    private Document doc;
    
    /**
     * Constructor
     * @param doc document object
     * @throws MException if the document is null
     */
    public MXmlParse(Document doc) throws MException
    {
        if (doc == null)
        {
            throw new MException("Document object is null.");
        }
        else
        {
            this.doc = doc;
        }
    }
   
    /**
     * Constructor
     * @param fileName absolute path of the file
     * @throws MException if the path not exist
     * @throws DocumentException if read the file error
     */
    public MXmlParse(String fileName) throws MException, DocumentException
    {
        if (MFile.isExisted(fileName) == false)
        {
            throw new MException("The file \"" + fileName + "\" does not exist.");
        }
        else
        {
            this.fileName = fileName;
            
            SAXReader reader = new SAXReader();
            
            this.doc = reader.read(fileName);
        }
    }
    
    /**
     * Get document object
     * @return document object
     */
    public Document getDoc()
    {
        return this.doc;
    }
    
    /**
     * Get file name
     * @return file name
     */
    public String getFileName()
    {
        return this.fileName;
    }
    
    /**
     * Get xml's root element
     * @return null while the node is not exist,otherwise the root
     */
    public Element getRoot()
    {
        if (this.doc == null)
        {
            return null;
        }
        else
        {
            Element root = this.doc.getRootElement();
            if (root == null)
            {
                return null;
            }
            else
            {
                return root;
            }
            
        }
    }
    
    /**
     * get the list of sub elements by name
     * @param parElement parent element
     * @param name sub elements' name
     * @return sub element list of parElement 
     */
    @SuppressWarnings("rawtypes")
    public List getSubElement(Element parElement, String name)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            return parElement.elements(name);
        }
    }
    
    /**
     * get the list of sub elements by name
     * @param parElement parent element
     * @param name sub elements' name
     * @return sub element list of parElement 
     */
    @SuppressWarnings("rawtypes")
    public List getSubElementByKey(Element parElement, String key, String value)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            List<Element> retNodeList = new ArrayList<Element>();
            List nodeList = parElement.elements();
            for (int i = 0; i < nodeList.size(); i++)
            {
                Element node = (Element) nodeList.get(i);
                
                if (node.attributeValue(key).equalsIgnoreCase(value))
                {
                    retNodeList.add(node);
                }
            }
            
            return retNodeList;
        }
    }
    
    /**
     * Get the first sub element by name
     * @param parElement parent element
     * @param name sub elements' name
     * @return first sub element
     */
    @SuppressWarnings("rawtypes")
    public static Element getFirstSubElementByKey(Element parElement, String key, String value)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            List nodeList = parElement.elements();
            for (int i = 0; i < nodeList.size(); i++)
            {
                Element node = (Element) nodeList.get(i);
                
                if (node.attributeValue(key).equalsIgnoreCase(value))
                {
                    return node;
                }
            }
            
            return null;
        }
    }
    
    /**
     * get the list of sub elements
     * @param parElement parent element
     * @return element list 
     */
    @SuppressWarnings("rawtypes")
    public List getSubElement(Element parElement)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            return parElement.elements();
        }
    }
    
    /**
     * get attribute
     * @param element
     * @param name
     * @return
     * @throws MException
     */
    public Attribute getAttribute(Element element, String name)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return element.attribute(name);
        }
    }
    
    /**
     * get attribute value
     * @param element
     * @param name
     * @return
     * @throws MException
     */
    public String getAttrValue(Element element, String name)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            if (element.attribute(name) != null)
            {
                return element.attribute(name).getValue();
            }
            else
            {
                return "";
            }
        }
    }
    
    /**
     * get node text
     * @param element
     * @return
     * @throws MException
     */
    public String getNodeText(Element element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return element.getText();
        }
    }
    
    /**
     * Get node data
     * @param element
     * @return
     * @throws MException
     */
    public String getNodeData(Element element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return (String) element.getData();
        }
    }
    
    /**
     * get first sub element
     * @param parElement
     * @return first sub element
     */
    public Element getSubFirstElement(Element parElement)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            return (Element) getSubElement(parElement).get(0);
        }
    }
    
    /**
     * get first sub element
     * @param parElement
     * @param name
     * @return first sub element
     */
    public Element getSubFirstElement(Element parElement, String name)
    {
        if (parElement == null)
        {
            return null;
        }
        else
        {
            return (Element) parElement.elements(name).get(0);
        }
    }
    
    /**
     * get elements by XPATH
     * @param xPath
     * @return list of elements
     */
    @SuppressWarnings("unchecked")
    public List<Element> getElements(String xPath)
    {
        try
        {
            DocumentHelper.createXPath(xPath);
        }
        catch (InvalidXPathException e)
        {
            return null;
        }
        
        return this.doc.selectNodes(xPath);
    }
    
    /**
     * get element path
     * @param element
     * @return
     * @throws MException
     */
    public String getElementPath(Element element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return element.getPath();
        }
    }
    
    /**
     * get element name
     * @param element
     * @return element name
     * @throws MException
     */
    public String getElementName(Element element)
    {
        if (element == null)
        {
            return null;
        }
        else
        {
            return element.getName();
        }
    }
}

