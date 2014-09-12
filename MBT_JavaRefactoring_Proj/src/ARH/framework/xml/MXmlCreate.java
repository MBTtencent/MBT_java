/*
 * FileName: MXmlCreate.java
 * 
 * Description: create MXmlCreate class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-20 Create
 */


package ARH.framework.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.*;
import ARH.framework.file.MFile;
import ARH.framework.logger.MLogMag;

/**
 * This Class is used to create XML file
 * @author MICKCHEN
 * @version 1.0
 */
public class MXmlCreate extends MXmlFileWriter
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
     * XML format object
     */
    private MXmlFormat format; 
    
    /**
     * Constructor
     */
    public MXmlCreate()
    {
        this.doc = DocumentHelper.createDocument();
        this.format = new MXmlFormat();
    }
    
    /**
     * Constructor
     * @param fileName
     * @param isCover is cover the old file
     * @throws MException
     */
    public MXmlCreate(String fileName, int isCover) throws MException
    {
        if (fileName.trim().isEmpty())
        {
            throw new MException("The file name is empty.");
        }
        
        if (MFile.isExisted(fileName))
        {
            if (isCover == 1)
            {
                MFile.delFile(fileName);
            }
        }
        
        if (!MFile.isFileDirExist(fileName))
        {
            String dir = MFile.getParentDir(fileName);
            if (!MFile.makeDir(dir))
            {
                throw new MException("Create directory false.");
            }
        }
        
        this.fileName = fileName;
        this.doc = DocumentHelper.createDocument();
        this.format = new MXmlFormat();
    }
    
    /**
     * Constructor
     * @param fileName
     * @throws MException
     */
    public MXmlCreate(String fileName) throws MException
    {
        if (fileName.trim().isEmpty())
        {
            throw new MException("The file name is empty.");
        }
        
        if (MFile.isExisted(fileName))
        {
            MFile.delFile(fileName);
            //throw new MException("The file is exist.");
        }
        
        if (!MFile.isFileDirExist(fileName))
        {
            String dir = MFile.getParentDir(fileName);
            if (!MFile.makeDir(dir))
            {
                throw new MException("Create directory false.");
            }
        }
        
        this.fileName = fileName;
        this.doc = DocumentHelper.createDocument();
        this.format = new MXmlFormat();
    }
    
    /**
     * Get document
     * @return document object
     */
    public Document getDoc()
    {
        return this.doc;
    }
    
    /**
     * Set format type and encoding type
     * @param type
     * @param encoding
     */
    public void setFormat(int type, String encoding)
    {
        this.format.setFormat(type, encoding);
    }
    
    /**
     * Add element as child node of the parElement 
     * @param element The parent element
     * @param element child
     */
    public void addElement(Element parElement, Element element)
    {
        parElement.add(element);
    }
    
    /**
     * Add element.Add element to root,while the element is null
     * @param element The parent element
     * @param name Element name
     */
    public Element addElement(Element element, String name)
    {
        if (element == null)
        {
            return this.doc.addElement(name);
        }
        else
        {
            return element.addElement(name);
        }
    }
    
    /**
     * Add attribute to element
     * @param element The element 
     * @param name Attribute names
     * @param value Attribute value
     * @throws MException While the element is null
     */
    public void addAttr(Element element, String name, String value) throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            element.addAttribute(name, value);
        }
    }
    
    /**
     * Add element's data
     * @param element The Element
     * @param value Data value
     * @throws MException While the element is null
     */
    public void addElementData(Element element, String value) throws MException
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
     * Add element's content
     * @param element The element
     * @param cont The content would be added
     * @throws MException While the element is null
     */
    public void addElementCont(Element element, String cont) throws MException
    {
        if (element == null)
        {
            throw new MException("The element is null.");
        }
        else
        {
            element.addText(cont);
        }
    }
    
    /**
     * Create XML file
     * @return Return true while the XMl file created success, otherwise false
     */
    public boolean createXml()
    { 
        boolean res = true;
        
        try
        {
            MXmlFileWriter.writeXml(this.doc, this.fileName, this.format.getFormat());
        } 
        catch (Exception e) 
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
                    e.getMessage());
            res = false;
        }
        
        return res;
    }
    
    /**
     * Set XML file path
     * @param fileName the path of the file
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
}
