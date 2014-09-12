/*
 * FileName: MXmlUpdate.xml
 * 
 * Description: create MXmlUpdate class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-21 Create
 */


package ARH.framework.xml;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

import ARH.framework.exception.*;
import ARH.framework.file.MFile;

/**
 * this class used to update XML file
 * @author MICKCHEN
 * @version 1.0
 */
public class MXmlUpdate
{
    /**
     * Private constructor, forbidden to use
     */
    private MXmlUpdate() {}
    
    /**
     * Update XML file
     * @param doc document object
     * @param fileName absolutely path of the file
     * @param format format object
     * @throws MException if doc is null,or if file is not exist
     */
    public static void updateXml(Document doc, String fileName, MXmlFormat xmlFormat)
            throws MException
    {
        if (doc == null)
        {
            throw new MException("The Document is null.");
        }
        
        if(MFile.isFileDirExist(fileName) == false)
        {
            throw new MException("The file is not exist.");
        }
        
        OutputFormat format = null;
        if (xmlFormat == null)
        {
            format = new MXmlFormat().getFormat();
        }
        else
        {
            format = xmlFormat.getFormat();
        }

        try
        {
            MXmlFileWriter.writeXml(doc, fileName, format);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Update node data value
     * @param element element object
     * @param value data value
     * @throws MException if the element is null
     */
    public static void updateNodeData(Element element, String value) 
            throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            element.setData(value);
        }
    }
    
    /**
     * Add CDATA to the element
     * @param element element object
     * @param value data value
     * @throws MException if the element is null
     */
    public static void addNodeData(Element element, String value) throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            element.addCDATA(value);
        }
    }
    
    /**
     * Update node content
     * @param element element object
     * @param value value of element
     * @throws MException if element is null
     */
    public static void updateNodeCont(Element element, String value) throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            element.setText(value);
        }
    }
    
    /**
     * Update attribute value
     * @param element element object
     * @param name attribute name
     * @param value attribute value
     * @throws MException if element is null
     */
    public static void updateAttrValue(Element element, String name, String value) 
            throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            Attribute attribute= element.attribute(name);

            if (attribute != null)
            {
                attribute.setValue(value);
            }
        }
    }
    
    /**
     * Remove attribute by name
     * @param element element object
     * @param name attribute name
     * @throws MException if element is null
     */
    public static void removeAttr(Element element, String name) 
            throws MException
    {
        if (element == null)
        {
            throw new MException("The element if null.");
        }
        else
        {
            Attribute attribute = element.attribute(name);
            
            if (attribute != null)
            {
                element.remove(attribute);
            }
        }
    }
    
    /**
     * remove the element by it's name
     * @param parElement parent name
     * @param Name element's name
     * @throws MException  if parent name is null
     */
    @SuppressWarnings("rawtypes")
    public static void removeElement(Element parElement, String name) 
            throws MException
    {    
        if (parElement == null)
        {
            throw new MException("The element if null.");
        }
        else
        {
            List elements = parElement.elements(name);
            Iterator it = elements.iterator();
            while (it.hasNext())
            {
                Element element = (Element) it.next();
                parElement.remove(element);
            }
        }
    }
}
