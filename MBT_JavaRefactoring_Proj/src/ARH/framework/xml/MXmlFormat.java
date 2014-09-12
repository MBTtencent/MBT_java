/*
 * FileName: MXmlFormat
 * 
 * Description: create MXmlFormat class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-21 Create
 */


package ARH.framework.xml;

import org.dom4j.io.*;

/**
 * This class used to set the XML format
 * @author MICKCHEN
 * @version 1.0
 */
public class MXmlFormat
{
    /**
     * Format object
     */
    private OutputFormat format;

    /**
     * Constructor
     * @param type
     * @param encoding
     */
    public MXmlFormat()
    {
        this.format = OutputFormat.createPrettyPrint();
        this.format.setEncoding(MXmlConfig.MXML_FORMAT_ENCODING_UTF8_TYPE);
    }
    
    /**
     * Set XML format
     * @param type format type 
     * @param encoding encoding type
     */
    public void setFormat(int type, String encoding)
    {
        if (type == MXmlConfig.MXML_FORMAT_COMPACT_PRINT)
        {
            this.format = OutputFormat.createCompactFormat();
        }
        else
        {
            this.format = OutputFormat.createPrettyPrint();
        }
        
        this.format.setEncoding(encoding);
    }
    
    /**
     * get format object
     * @return format object
     */
    public OutputFormat getFormat()
    {
        return this.format;
    }
}