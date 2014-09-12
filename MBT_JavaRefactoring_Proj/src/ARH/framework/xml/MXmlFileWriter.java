/*
 * FileName: MXmlFileWriter
 * 
 * Description: create MXmlFileWriter class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-21 Create
 */


package ARH.framework.xml;

import java.io.*;
import org.dom4j.*;
import org.dom4j.io.*;
import ARH.framework.exception.*;

/**
 * This class is used to write XML file
 * @author MICKCHEN
 * @version 1.0
 */
public class MXmlFileWriter
{
    /**
     * Write XML file
     * @param document Document object
     * @param fileName the file name of result 
     * @throws IOException if there is some wrong with the file
     * @throws MException if the document object is null, 
     * or the string of file name is null,
     * or the format object is null. 
     */
    public static void writeXml(Document document, String fileName, OutputFormat format) 
            throws IOException, MException
    {
        if (document == null)
        {
            throw new MException("The document is null.");
        }
        
        if (fileName == null)
        {
            throw new MException("The fileName is null.");
        }
        
        if (format == null)
        {
            throw new MException("The format is null.");
        }
        
        FileWriter fileWrite = new FileWriter(new File(fileName));
        XMLWriter writer = new XMLWriter(fileWrite, format);
        writer.write(document);
        writer.close();
    }
}