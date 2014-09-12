/**
 * FileName: MGenQTestHttpMsg.java
 * 
 * Description: create MGenQTestHttpMsg.java class
 * 
 * History:
 * 1.0 MICKCHEN 2013-11-27 Create file
 */

package ARH.common.view.qtest;

import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import ARH.framework.exception.MException;
import ARH.framework.file.MFile;
import ARH.framework.xml.MXmlCreate;
import ARH.framework.xml.MXmlParse;

public class MGenQTestHttpMsg
{
    private static final String MGEN_QTEST_HTTP = "http";
    private static final String MGEN_QTEST_HTTP_REQ = "request";
    private static final String MGEN_QTEST_HTTP_RESP = "response";
    private static final String MGEN_QTEST_HTTP_REQUEST_LINE = "request-line";
    private static final String MGEN_QTEST_HTTP_STATUS_LINE = "status-line";
    private static final String MGEN_QTEST_HTTP_HEADER = "header";
    private static final String MGEN_QTEST_HTTP_DATA = "data";
    private static final String MGEN_QTEST_HTTP_METHOD = "method";
    private static final String MGEN_QTEST_HTTP_URL = "url";
    
    /**
     * Data file path
     */
    private String confiPath;
    
    /**
     * Constructor
     */
    public MGenQTestHttpMsg(String configPath)
    {
        this.confiPath = configPath;
    }
    
    /**
     * Generate Http message
     * @throws MException 
     * @throws DocumentException 
     */
    public String genMsg() throws MException, DocumentException
    {
        String content = "";
        if (this.confiPath.isEmpty())
        {
            throw new MException("The config path is Empty.");
        }
        
        if (MFile.isExisted(this.confiPath) == false)
        {
            createConfig(this.confiPath);
        }
        
        MXmlParse xmlParse = new MXmlParse(this.confiPath);
        Element root = xmlParse.getRoot();
        if (root != null)
        {
            content += "";
            content += genMsgPy(root, xmlParse);
        }
        
        return content;
    }
    
    /**
     * Generate python script of the message 
     * @param elementList
     * @return python script
     */
    private String genMsgPy(Element root, MXmlParse xmlParse)
    {
        String content = "";
        
        Element subElementReq = xmlParse.getSubFirstElement(root, MGEN_QTEST_HTTP_REQ);
        Element requestLine = xmlParse.getSubFirstElement(subElementReq, MGEN_QTEST_HTTP_REQUEST_LINE);
        Element headerReq = xmlParse.getSubFirstElement(subElementReq, MGEN_QTEST_HTTP_HEADER);
        Element dataReq = xmlParse.getSubFirstElement(subElementReq, MGEN_QTEST_HTTP_DATA);
        
        content += "oHttpReq = HttpRequest();\n";
        List subRequestLine = xmlParse.getSubElement(requestLine);
        int sunRequestLineSize = subRequestLine.size();
        for (int i = 0; i < sunRequestLineSize; i++)
        {
            Element element = (Element) subRequestLine.get(i);
            
            String name = xmlParse.getElementName(element);
            String value = xmlParse.getNodeData(element);
            if (name.equalsIgnoreCase(MGEN_QTEST_HTTP_METHOD))
            {
                content += "oHttpReq.getRequestLine().setName(" + value + ");\n";
            }
            else if (name.equalsIgnoreCase(MGEN_QTEST_HTTP_URL))
            {
                content += "oHttpReq.getRequestLine().setUrl(" + value + ");\n";
            }
        }
        
        List subHeaderReq = xmlParse.getSubElement(headerReq);
        int subHeaderReqSize = subHeaderReq.size();
        for (int i = 0; i < subHeaderReqSize; i++)
        {
            Element element = (Element) subHeaderReq.get(i);

            String subHeaderReqName = xmlParse.getElementName(element);
            String subHeaderReqValue = xmlParse.getNodeData(element);
            
            if (!subHeaderReqValue.isEmpty())
            {
                content += "oHttpReq.getHeaderList().setHeader('" + 
                        subHeaderReqName + "', " + subHeaderReqValue + ");\n";
            }
        }
        
        String value = xmlParse.getNodeData(dataReq);
        if (!value.isEmpty())
        {
            content += "oHttpReq.getBody().setValue(" + value + ");\n";
        }
        
        content += "oHttpEnt.sendMsg(oHttpReq);\n\n";
        content += "oHttpResp = HttpResponse();\n";
        
        // response
        Element subElementResp = xmlParse.getSubFirstElement(root, MGEN_QTEST_HTTP_RESP);
        Element statusLine = xmlParse.getSubFirstElement(subElementResp, MGEN_QTEST_HTTP_STATUS_LINE);
        Element headerRsp = xmlParse.getSubFirstElement(subElementResp, MGEN_QTEST_HTTP_HEADER);
        Element dataRsp = xmlParse.getSubFirstElement(subElementResp, MGEN_QTEST_HTTP_DATA);
        
        String statusLineValue = xmlParse.getNodeData(statusLine);
        if (!statusLineValue.isEmpty())
        {
            content += "oHttpResp.getStatusLine().setName(" + statusLineValue + ");\n";
        }
        
        List subHeaderRsp = xmlParse.getSubElement(headerRsp);
        int subHeaderRspSize = subHeaderRsp.size();
        for (int i = 0; i < subHeaderRspSize; i++)
        {
            Element element = (Element) subHeaderRsp.get(i);
            
            String subHeaderNameRsp = xmlParse.getElementName(element);
            String subHeaderValueRsp = xmlParse.getNodeData(element);
            
            if (!subHeaderValueRsp.isEmpty())
            {
                content += "oHttpResp.getHeaderList().setHeader('" +
                           subHeaderNameRsp + "', " + subHeaderValueRsp +");\n";
            }
        }
        
        String dataResp = xmlParse.getNodeData(dataRsp);
        if (!dataResp.isEmpty())
        {
            content += "oHttpResp.getBody().setValue(" + dataResp + ");\n";
        }
        
        content += "oHttpRecvd = oHttpEnt.receiveMsg(oHttpResp);\n";
        
        return content;
    }
    
    /**
     * Create configure file of http
     * @param configPath file Path
     * @throws MException 
     */
    void createConfig(String configPath) throws MException
    {
        MXmlCreate xmlCreate = new MXmlCreate(configPath);

        Element root = xmlCreate.addElement(null, MGEN_QTEST_HTTP);
        Element request = xmlCreate.addElement(root, MGEN_QTEST_HTTP_REQ);
        Element response = xmlCreate.addElement(root, MGEN_QTEST_HTTP_RESP);
        
        Element requestLine = xmlCreate.addElement(request, MGEN_QTEST_HTTP_REQUEST_LINE);
        Element url = xmlCreate.addElement(requestLine, MGEN_QTEST_HTTP_URL);
        xmlCreate.addElementData(url, "");
        Element method = xmlCreate.addElement(requestLine, MGEN_QTEST_HTTP_METHOD);
        xmlCreate.addElementData(method, "");
        Element headerReq = xmlCreate.addElement(request, MGEN_QTEST_HTTP_HEADER);
        xmlCreate.addElementCont(headerReq, "");
        Element dataReq = xmlCreate.addElement(request, MGEN_QTEST_HTTP_DATA);
        xmlCreate.addElementData(dataReq, "");
        
        Element statusLine = xmlCreate.addElement(response, MGEN_QTEST_HTTP_STATUS_LINE);
        xmlCreate.addElementData(statusLine, "");
        Element headerRsp = xmlCreate.addElement(response, MGEN_QTEST_HTTP_HEADER);
        xmlCreate.addElementCont(headerRsp, "");
        Element dataRsp = xmlCreate.addElement(response, MGEN_QTEST_HTTP_DATA);
        xmlCreate.addElementData(dataRsp, "");
        
        xmlCreate.createXml();
    }
}
