/*
 * FileName: MGenGraph.java
 * 
 * Description: create MGenGraph class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-20 Create
 */


package ARH.common.generate;

import java.lang.reflect.*;
import java.util.*;

import ARH.common.config.*;
import ARH.framework.annotation.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.dynamic.*;
import ARH.framework.exception.MException;
import ARH.framework.graph.*;
import ARH.framework.logger.*;
import ARH.framework.xml.*;

import org.dom4j.*;

/**
 * this class is used to generate graph object 
 * according to MBT file written by user
 * @author SHUYUFANG
 * @version 1.0
 */
public class MGenGraph
{
    /** the graphs to be generated */
    private MGraph graph;
    
    /**
     * constructor
     */
    public MGenGraph()
    {
        this.graph = new MGraph();
    }

    /**
     * generate graph from MBT XML file
     * @param mbtXmlFilePath MBT XML file path
     * @return graph generated or not
     */
    @SuppressWarnings("unchecked")
    public boolean genGraphFromXML(String mbtXml)
    {
        boolean res = false;
        // the object to parse XML file
        MXmlParse xmlParser;
        try
        {
            xmlParser = new MXmlParse(mbtXml);
         
            // get root element called "MODEL"
            Element modelElem = xmlParser.getRoot();
            // get sub element called "TESTSTEPS"
            Element testStepsElem = xmlParser.getSubFirstElement(modelElem, MCommonConfig.MBT_XML_TEST_STEPS);
            // get the list of sub element called "TESTSTEP"
            List<Element> testStepElemList = xmlParser.getSubElement(testStepsElem, MCommonConfig.MBT_XML_TEST_STEP);
            for (Element testStepElem : testStepElemList)
            {
                // get sub element called "NAME"
                Element nameElem = xmlParser.getSubFirstElement(testStepElem, MCommonConfig.MBT_XML_NAME);
                String name = xmlParser.getNodeData(nameElem);
                // get sub element called "STEPNUMBER"
                Element stepNumberElem = xmlParser.getSubFirstElement(testStepElem, MCommonConfig.MBT_XML_STEP_NUMBER);
                int stepNumber = Integer.valueOf(xmlParser.getNodeData(stepNumberElem));
                // get sub element called "BEGINSTATE"
                Element beginStateElem = xmlParser.getSubFirstElement(testStepElem, MCommonConfig.MBT_XML_BEGIN_STATE);
                String beginState = xmlParser.getNodeData(beginStateElem);
                // get sub element called "ENDSTATE"
                Element endStateElem = xmlParser.getSubFirstElement(testStepElem, MCommonConfig.MBT_XML_END_STATE);
                String endState = xmlParser.getNodeData(endStateElem);
                // get sub element called "REPEATTIME"
                Element repeatTimeElem = xmlParser.getSubFirstElement(testStepElem, MCommonConfig.MBT_XML_REPEAT_TIME);            
                int repeatTime = Integer.valueOf(xmlParser.getNodeData(repeatTimeElem));
                // add a new edge to the graph
                this.addEdge(beginState, endState, name, stepNumber, repeatTime);
            }
            
            res = true;
        }
        catch (DocumentException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" + 
                    "Cannot generate graph from XML \"" + mbtXml + "\" in MGenGraph" + 
                    "\n" + e.getMessage());            
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" + 
                    "Cannot generate graph from XML \"" + mbtXml + "\" in MGenGraph" + 
                    "\n" + e.getMessage());
        }
        
        return res;
    }
    
    /**
     * generate graph from MBT class
     * @param className the class name of MBT script file
     * @return graph generated or not
     */
    public boolean genGraphFromClass(String className)
    {
        boolean res = false;
        
        // get TestStep method list of the MBT class
        ArrayList<Method> stepMethods = null;
        try
        {
            stepMethods = MClass.getMethodsWithAnnotation(className, 
                    MTestStep.class);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(MBasicApi.getLineInfo() + "\n" + 
                    "Cannot generate graph from class \"" + className + "\" in MGenGraph" + 
                    "\n" + e.getMessage());
            return res;
        }
        
        // no step was found
        if (0 == stepMethods.size())
        {
            return res;
        }
        
        for (Method method : stepMethods)
        {
            // get name of method
            String stepName = method.getName();
            // get value of "beginState" annotation
            String beginState = MAnnotation.getTestStepBeginState(method);
            // get value of "endState" annotation
            String endState = MAnnotation.getTestStepEndState(method);
            // get value of "repeatTime" annotation
            int repeatTime = MAnnotation.getTestStepRepeatTime(method);
            // get value of "stepNumber" annotation 
            int stepNumber = MAnnotation.getTestStepValue(method);     
            // add a new edge to the graph
            this.addEdge(beginState, endState, stepName, stepNumber, repeatTime);
        }
        res = true;
        
        return res;
    }
    
    /**
     * add a new edge to the graph
     * @param beginStep begin node(s) of the edge
     * @param endStep end node(s) of the edge
     * @param stepNumber the number of the step
     * @param repeatTime the repeat time of the step
     */
    private void addEdge(String beginStep, String endStep, String stepName, int stepNumber, 
            int repeatTime)
    {
        String[] beginStepArray = null;
        String[] endStepArray = null;
        // no repeatTime specified means one time
        if (-1 == repeatTime)
        {
            repeatTime = 1;
        }
        
        // more than one begin node
        if (-1 != beginStep.indexOf(","))
        {
            beginStepArray = beginStep.split(",");
        }
        // just one begin node
        else
        {
            beginStepArray = new String[]{beginStep};
        }
        
        // more than one end node
        if (-1 != endStep.indexOf(","))
        {
            endStepArray = endStep.split(",");
        }
        // just one end node
        else
        {
            endStepArray = new String[]{endStep};
        }
        
        for (String tmpBeginStep : beginStepArray)
        {
            for (String tmpEndStep : endStepArray)
            {
                // add edge from each begin node to the end node
                this.graph.addEdge(tmpBeginStep.trim(), tmpEndStep.trim(), stepName, stepNumber, repeatTime);
            }
        }
    }
    
    /**
     * get the graph
     * @return the graph generated
     */
    public MGraph getGraph()
    {
        return this.graph;
    }
}