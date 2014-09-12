/*
 * FileName: MTestLoader.java
 * 
 * Description: create MTestLoader class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-29 Create
 */


package ARH.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Method;

import org.dom4j.Document;

import ARH.common.cases.*;
import ARH.common.config.MCommonConfig;
import ARH.common.generate.*;
import ARH.common.path.*;
import ARH.common.view.*;
import ARH.common.draw.*;
import ARH.framework.basic.MBasicApi;
import ARH.framework.dynamic.*;
import ARH.framework.exception.MException;
import ARH.framework.file.MFile;
import ARH.framework.graph.*;
import ARH.framework.logger.*;

/**
 * This class is used by users to create cases
 * @author MICKCHEN
 * @version 1.0
 */
public class MTestLoader
{
    /**
     * This function is used to generate cases
     * @param viewName  view's name
     * @param dir  direction of the cases
     */
    public static void run(String actionName, String viewName, String mbtName, String dir, 
            boolean showEdgeName)
    {
        // Define logger object
        MConsoleAppender consoleAppender = new MConsoleAppender();
        MFileAppender fileAppender = new MFileAppender();
        MLogMag.getInstance().getLogger().addAppender(consoleAppender);
        MLogMag.getInstance().getLogger().addAppender(fileAppender);
        
        String sysMbtPath = null;
        String sysActionPath = null;
        String sysActionClassPath = null;
        String sysMbtClassPath = null;
        String mbtXmlPath = null;
        String varCombXmlPath = null;
        String stepVarXmlPath = null;
        
        try
        {
            // Pre_process action file
            String packageName = MLoaderConfig.MLOAD_PACKAGE_PATH;
            String projectPath = MBasicApi.getProjectPath();
            String userActionClass = packageName + "." + actionName;
            String sysActionClass = actionName + MBasicApi.getLocalTime();
            String srcPath = projectPath + "/src/";
            String packagePath = srcPath + packageName.replace(".", "/");
            String stepLogPath = packagePath + "/" + actionName + "Step.log";
            sysActionPath = packagePath + "/" + sysActionClass + 
                    MLoaderConfig.MLOAD_FILE_EXT_JAVA;
            sysActionClassPath = packagePath + "/" + sysActionClass + 
                    MLoaderConfig.MLOAD_FILE_EXT_CLASS;
            
            if (!MGenAction.genAction(userActionClass, packageName, sysActionClass, sysActionPath, 
                    stepLogPath))
            {
                MLogMag.getInstance().getLogger().error("Fail to generate action file...");
                DelTmpFile(null, sysActionPath, sysActionClassPath, null, null, null, null);
                return;
            }
            
            MLogMag.getInstance().getLogger().info("Finish to Pre_process action file...");
            
            // Pre_process MBT file
            String userMbtPath = packagePath + "/" + mbtName + 
                    MLoaderConfig.MLOAD_FILE_EXT_JAVA;
            String userMbtClass = mbtName;
            // String userActionClass = actionName;
            String sysMbtClass = mbtName + MBasicApi.getLocalTime();
            sysMbtPath = packagePath + "/" + sysMbtClass + 
                    MLoaderConfig.MLOAD_FILE_EXT_JAVA;
            sysMbtClassPath = packagePath + "/" + sysMbtClass + 
                    MLoaderConfig.MLOAD_FILE_EXT_CLASS;
            
            if (!MGenMbt.genMbt(userMbtPath, userMbtClass, actionName, sysMbtPath, sysMbtClass, 
                    sysActionClass))
            {
                MLogMag.getInstance().getLogger().error("Fail to generate MBT file...");
                DelTmpFile(sysMbtPath, sysActionPath, sysActionClassPath, sysMbtClassPath, null, null, null);
                return;
            }
            
            MLogMag.getInstance().getLogger().info("Finish to Pre_process MBT file...");
            
            // Dynamic load MBT file and action file
            if (!MCompiler.compileToDir(srcPath, sysActionPath, sysMbtPath))
            {
                MLogMag.getInstance().getLogger().error("Fail to generate compile files...");
                DelTmpFile(sysMbtPath, sysActionPath, sysActionClassPath, sysMbtClassPath, null, null, null);
                return;
            }
            
            // Wait, make sure the class file is generated
            String mbtClassName = MLoaderConfig.MLOAD_PACKAGE_PATH  + "." + sysMbtClass;

            Class<?> obj = null;
            do
            {
                try
                {
                    Thread.sleep(2000);
                    obj = MClass.loadClass(mbtClassName);
                }
                catch (InterruptedException e)
                {
                    MLogMag.getInstance().getLogger().warning("Fail to sleep for 2 seconds.\n");                    
                }
                catch (MException e)
                {
                    MLogMag.getInstance().getLogger().warning("Fail to load MBT class \"" + mbtClassName + 
                            "\".\n");
                }
            }
            while (null == obj);
            
            MLogMag.getInstance().getLogger().info("Finish to generate class file...");
            
            // Generate MBT XML
            mbtXmlPath = packagePath + "/" + mbtName + 
                    MLoaderConfig.MLOAD_FILE_EXT_XML;
            
            if (!MGenMbtXml.genMbtXmlFile(mbtClassName, stepLogPath, mbtXmlPath))
            {
                MLogMag.getInstance().getLogger().error("Fail to generate MBT XMl file.");
                DelTmpFile(sysMbtPath, sysActionPath, sysActionClassPath, sysMbtClassPath, mbtXmlPath, null, null);
                return;
            }
            
            MLogMag.getInstance().getLogger().info("Finish to generate MBT XML file...");
            
            // Generate graph
            MGenGraph graphGenner = new MGenGraph();
            graphGenner.genGraphFromXML(mbtXmlPath);
            MGraph graph = graphGenner.getGraph();
                  
            // Draw sequence graph
            MDraw.drawSeqGraph(mbtXmlPath, graph, dir, showEdgeName);
            
            MLogMag.getInstance().getLogger().info("Finish to draw graph...");
            
            // Add view
            String viewPath = MViewConfig.MVIEW_PACKAGE_NAME + "." + viewName;
            Object viewObj = MClass.getInstance(viewPath);

            ArrayList<Method> methodList = MClass.getMethods(viewPath);
            for (int i = 0; i < methodList.size(); i++)
            {
                
                Method method = methodList.get(i);
                if (method.getName() == MViewConfig.MVIEW_CONFIG_ADD_VIEW)
                {
                    MLogMag.getInstance().getLogger().info(method.getName());
                    MClass.invokeMethod(method, viewObj, new Object[]{});
                }
            }
            
            // Generate path
            HashMap<String, HashMap<String, ArrayList<ArrayList<Integer>>>> scenariosPathsMap = 
                    MGenPath.genPath(mbtXmlPath, graph);

            // Draw coverage graph
            MDraw.drawCovGraph(mbtXmlPath, graph, dir, scenariosPathsMap, showEdgeName);
            
            varCombXmlPath = packagePath + "/" + MLoaderConfig.MLOAD_FILE_COMB_XML_FILE;
            stepVarXmlPath = packagePath + "/" + MLoaderConfig.MLOAD_FILE_VAR_XML_FILE;
            MGenStepXml.genStepXml(varCombXmlPath, stepVarXmlPath, mbtXmlPath);
            
            ArrayList<HashMap<String, Object>> scenInfoList = MGenScenParser.parseScenario(mbtXmlPath);
            for (int i = 0; i < scenInfoList.size(); ++i)
            {
                String scenName = (String)scenInfoList.get(i).get(MCommonConfig.MBT_XML_NAME);
                String dataAlgo = (String)scenInfoList.get(i).get(MCommonConfig.MBT_SCENARIO_DATA);
                ArrayList<ArrayList<Integer>> edgePathList = scenariosPathsMap.get(scenName).get(MPathConfig.EDGE_PATH);
                ArrayList<ArrayList<Integer>> stepPathList = scenariosPathsMap.get(scenName).get(MPathConfig.STEP_PATH);
                
                MGenDocMap docGenner = new MGenDocMap(mbtXmlPath, varCombXmlPath, scenName, dataAlgo, edgePathList, stepPathList);
                docGenner.genDocMap();
                for (String pathName : docGenner.getScriptDocMap().keySet())
                {
                    for (int j = 0; j < docGenner.getScriptDocMap().get(pathName).size(); j++)
                    {
                        Document pathDoc = docGenner.getScriptDocMap().get(pathName).get(j);
                        HashMap<Integer,String> nameMap = docGenner.getScriptNameMap().get(pathName).get(j);
                        
                        MGenCase genCase = new MGenCase(viewName, dir);
                        genCase.setDoc(pathDoc);
                        genCase.setCaseName(pathName);
                        genCase.setNameMap(nameMap);
                        genCase.setVarPath(stepVarXmlPath);
                        genCase.setMbtXmlPath(mbtXmlPath);
                        
                        genCase.run();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            MLogMag.getInstance().getLogger().fatal(e.getMessage());
        }
        finally
        {
            // Delete temporary files
            DelTmpFile(sysMbtPath, sysActionPath, sysActionClassPath, sysMbtClassPath, mbtXmlPath, varCombXmlPath, stepVarXmlPath);
            MLogMag.getInstance().getLogger().info("Finish to generate Cases...");
        }
    }
    
    /**
     * Delete file
     * @param sysMbtPath temporary MBT file
     * @param sysActionPath temporary action file
     * @param sysActionClassPath temporary action class file
     * @param sysMbtClassPath temporary MBT class file
     * @param mbtXmlPath temporary MBT XML file
     */
    public static void DelTmpFile(String sysMbtPath, String sysActionPath, String sysActionClassPath, 
            String sysMbtClassPath, String mbtXmlPath, String varCombXmlPath, String stepVarXmlPath)
    {
        try
        {
            MFile.delFile(sysMbtPath);
            MFile.delFile(sysActionPath);
            MFile.delFile(sysActionClassPath);
            MFile.delFile(sysMbtClassPath);
            MFile.delFile(mbtXmlPath);
            MFile.delFile(varCombXmlPath);
            MFile.delFile(stepVarXmlPath);
        }
        catch (MException e)
        {
            MLogMag.getInstance().getLogger().error(e.getMessage());
        }
    }
}