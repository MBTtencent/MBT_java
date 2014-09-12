/*
 * FileName: MDot.java
 * 
 * Description: create MDot class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-09-05 Create
 */


package ARH.framework.dot;

import java.io.IOException;
import java.util.*;

import ARH.common.config.*;
import ARH.framework.basic.*;
import ARH.framework.exception.MException;
import ARH.framework.file.*;
import ARH.framework.logger.*;

/**
 * this class is used to generate graph file
 * @author SHUYUFANG
 * @version 1.0
 */
public class MDot
{
    /** the directory path to generate graph file */
    private String fileDir;
    
    /** the name of the graph file */
    private String fileName;
    
    /** the map of node */
    private HashMap<String, String> nodeMap;
    
    /** the map of edge */
    private HashMap<String, ArrayList<String>> edgeMap;
    
    /**
     * constructor
     */
    public MDot()
    {
        this.nodeMap = new HashMap<String, String>();
        this.edgeMap = new HashMap<String, ArrayList<String>>();
    }
    
    /**
     * add a node to the node map
     * @param nodeName name of the node
     * @param nodeShape shape of the node in the graph
     * @param nodeStyle style of the node in the graph
     * @param nodeColor color of the node in the graph
     */
    public void addNode(String nodeName, String nodeShape, String nodeStyle, String nodeColor)
    {
        String nodeDetail = "[style=" + nodeStyle + ",shape=" + nodeShape + ",color=" + nodeColor + "]";
        this.nodeMap.put(nodeName, nodeDetail);
    }
    
    /**
     * add an edge to the edge map
     * @param beginNode name of the begin edge
     * @param endNode name of the end edge
     * @param edgeLabel label of the edge in the graph
     * @param edgeType type of the edge in the graph
     * @param edgeColor color of the edge in the graph
     * @param edgeStyle style of the edge in the graph
     */
    public void addEdge(String beginNode, String endNode, String edgeLabel, String edgeType, 
            String edgeColor, String edgeStyle)
    {
        String edgeDesc = beginNode + " " + edgeType + " " + endNode;
        String edgeDetail = "[label=\"" + edgeLabel + "\",color=" + edgeColor
                + ",style=" + edgeStyle + "]";
        
        if (!this.edgeMap.containsKey(edgeDesc))
        {
            this.edgeMap.put(edgeDesc, new ArrayList<String>());
        }
        this.edgeMap.get(edgeDesc).add(edgeDetail);
    }
    
    /**
     * delete all nodes in the node map
     */
    public void clearNodeMap()
    {
        this.nodeMap.clear();
    }
    
    /**
     * delete all edges in the edge map
     */
    public void clearEdgeMap()
    {
        this.edgeMap.clear();
    }
    
    /**
     * set the path of the directory to generate file
     * @param fileDir the directory path
     */
    public void setFileDir(String fileDir)
    {
        this.fileDir = fileDir;
    }
    
    /**
     * set the name of the graph file
     * @param fileName the name of the file
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    /**
     * generate dot file
     */
    public void genDotFile() throws MException
    {
        String dotFilePath = this.fileDir + "/" + this.fileName + ".dot";
        String dotFileContent = "digraph\n{\n";
        
        for (String nodeName : this.nodeMap.keySet())
        {
            dotFileContent += "    " + nodeName + " " + this.nodeMap.get(nodeName) + ";\n";
        }
        
        dotFileContent += "\n";
        
        for (String edgeDesc : this.edgeMap.keySet())
        {
            for (String edgeDetail : this.edgeMap.get(edgeDesc))
            {
                dotFileContent += "    " + edgeDesc + " " + edgeDetail + ";\n";
            }
        }
        
        dotFileContent += "}\n";
        
        if (!MFile.writeFile(dotFilePath, dotFileContent))
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" 
                    + "Cannot generate dot file \"" + dotFilePath + "\" in MDot");
        }
    }
    
    /**
     * delete dot file
     */
    public void delDotFile() throws MException
    {
        String dotFilePath = this.fileDir + "/" + this.fileName + ".dot";
        MFile.delFile(dotFilePath);
    }
    
    /**
     * generate graph file
     */
    public void genGraph() throws MException
    {
        String dotFilePath = this.fileDir + "/" + this.fileName 
                + MCommonConfig.MBT_SUFFIX_DOT;
        String graphFilePath = this.fileDir + "/" + this.fileName 
                + MCommonConfig.MBT_SUFFIX_PNG;
        
//        String cmd = MBasicApi.getProjectPath() + "/tools/dot/dot.exe -Tpng " + dotFilePath 
//                + " -o " + graphFilePath;
        
        // abel machine
//        String cmd = "D:/Program Files/dot/dot.exe -Tpng " + dotFilePath + " -o " + graphFilePath;
        
        String cmd = "dot -Tpng " + dotFilePath + " -o " + graphFilePath;
        
        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            
//            InputStream is = process.getInputStream();
//            InputStream err = process.getErrorStream();
//            while()
            int exitValue = process.waitFor();
            if (0 != exitValue)
            {
                MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" 
                        + "Cannot execute cmd \"" + cmd + "\" in MDot");
            }
        }
        catch (InterruptedException e)
        {
            throw new MException("The cmd is interrupted when executing.");
        }
        catch (IOException e)
        {
//            throw new MException("IO error occurs when executing cmd.");
            e.printStackTrace();
        }
    }
}
