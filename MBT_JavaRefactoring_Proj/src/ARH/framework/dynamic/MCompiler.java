/*
 * FileName: MCompiler.java
 * 
 * Description: create MCompiler class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-19 Create
 */


package ARH.framework.dynamic;

import java.io.*;
import java.util.*;

import javax.tools.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.exception.MException;

/**
 * this class is used to dynamically compile .java files
 * @author SHUYUFANG
 * @version 1.0
 */
public class MCompiler
{
    /**
     * constructor
     */
    private MCompiler()
    {
        
    }
    
    /**
     * compile .java file(s) into .class file(s)
     * .class file(s) have the same directory with .java file(s)
     * @param filePaths the paths of files to compile
     * @return successfully compiled or not
     */
    public static boolean compile(String... filePaths)
    {
        boolean res;
        
        // get compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        // compile the java source code by run method
        int ret = compiler.run(null, null, null, filePaths);
        
        if (0 == ret)
        {
            res = true;
        }
        else
        {
            res = false;
        }

        return res;
    }
    
    /**
     * compile .java file(s) into .class file(s)
     * .class file(s) will be generated in the specified directory
     * if .java file(s) contains "package XXX;" statement
     * SUB directory XXX will be created and .class file will be in SUB directory
     * @param outputPath the directory path of class file to generate
     * @param filePaths the paths of files to compile
     * @return successfully compiled or not
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean compileToDir(String outputPath, String... filePaths) throws MException
    {
        boolean res = false;    
        StandardJavaFileManager fileManager = null;
        try
        {   
            // get compiler
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            
            // get diagnostic object used to save diagnostic information 
            DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
            
            // get file manager object and set diagnostic for the object    
            fileManager = compiler.getStandardFileManager(collector, null, null);
            
            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(filePaths));
            
            Iterable options = Arrays.asList("-sourcepath", MBasicApi.getProjectPath() + "src/ARH/test", 
                    "-d", outputPath);
            JavaCompiler.CompilationTask task = compiler.getTask(null,
                    fileManager, collector, options, null, compilationUnits);
            
            res = task.call();
            
            /*
            // set class output location    
            Location location = StandardLocation.CLASS_OUTPUT;    
            manager.setLocation(location, Arrays.asList(new File[]{new File(outputPath)}));
            
            // get list of files to be compiled
            ArrayList<File> fileList = new ArrayList<File>();
            for (int i = 0; i < filePaths.length; ++i)
            {
                fileList.add(new File(filePaths[i]));
            }
            
            // get java file object object which specify java source files
            Iterable<? extends JavaFileObject> javaFileObject = 
                    manager.getJavaFileObjectsFromFiles(fileList);
            
            // compile java source files by using
            // CompilationTask's call method
            res = compiler.getTask(null, manager, collector, null, null, 
                    javaFileObject).call();
            
            manager.close();
            */
        }
        catch (Exception e)
        {
            throw new MException("Cannot compile \"" + filePaths + "\" to directory \"" 
                    + outputPath + "\" in MCompile.");
        }
        finally
        {
            if (fileManager != null)
            {
                try
                {
                    fileManager.close();
                }
                catch (IOException e)
                {
                    throw new MException("Fail to close file manager in MCompile.");
                }
            }
        }
        
        return res;
    }
}