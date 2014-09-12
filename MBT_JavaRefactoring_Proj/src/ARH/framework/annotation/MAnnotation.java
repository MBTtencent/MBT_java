/*
 * FileName: MAnnotation.java
 * 
 * Description: create MAnnotation class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-19 Create
 */


package ARH.framework.annotation;

import java.lang.reflect.*;

import ARH.framework.basic.MBasicApi;
import ARH.framework.logger.MLogMag;

/**
 * this class provides some methods to get value
 * of specified annotations for methods
 * @author SHUYUFANG
 * @version 1.0
 */
public class MAnnotation
{
    /**
     * constructor
     */
    private MAnnotation()
    {
        
    }
    
    /**
     * get "BEGINSTATE" value of "TESTSTEP" annotation
     * @param method the method which contains "TESTSTEP" annotation
     * @return value of "BEGINSTATE" as String
     */
    public static String getTestStepBeginState(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MTestStep.class))
        {
            res = method.getAnnotation(MTestStep.class).beginState();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get begin step in MAnnotation");
        }
        return res;
    }
    
    /**
     * get "ENDSTATE" value of "TESTSTEP" annotation
     * @param method the method which contains "TESTSTEP" annotation
     * @return value of "ENDSTATE" as String
     */
    public static String getTestStepEndState(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MTestStep.class))
        {
            res = method.getAnnotation(MTestStep.class).endState();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get end step in MAnnotation");
        }
        return res;        
    }
    
    /**
     * get "REPEATTIME" value of "TESTSTEP" annotation
     * @param method the method which contains "TESTSTEP" annotation
     * @return value of "REPEATTIME" as integer
     */
    public static int getTestStepRepeatTime(Method method)
    {
        int res = -1;
        if (null != method && method.isAnnotationPresent(MTestStep.class))
        {
            res = method.getAnnotation(MTestStep.class).repeatTime();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get repeat time in MAnnotation");
        }
        return res;
    }
    
    /**
     * get "PARAM" value of "TESTSTEP" annotation
     * @param method the method which contains "PARAM" annotation
     * @return value of "PARAM" as String
     */
    public static String getTestStepParam(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MTestStep.class))
        {
            res = method.getAnnotation(MTestStep.class).param();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get param in MAnnotation");
        }
        return res;    
    }
    
    /**
     * get "DATA" value of "TESTSTEP" annotation
     * @param method the method which contains "DATA" annotation
     * @return value of "DATA" as String
     */
    public static String getTestStepData(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MTestStep.class))
        {
            res = method.getAnnotation(MTestStep.class).data();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get data in MAnnotation");
        }
        return res;    
    }
    
    /**
     * get "OPTION" value of "SCENARIO" annotation
     * @param method the method which contains "SCENARIO" annotation
     * @return value of "OPTION" as String
     */
    public static String getScenOption(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MScenario.class))
        {
            res = method.getAnnotation(MScenario.class).option();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get option in MAnnotation");
        }
        return res;
    }
    
    /**
     * get "PARAM" value of "SCENARIO" annotation
     * @param method the method which contains "SCENARIO" annotation
     * @return value of "PARAM" as String
     */
    public static String getScenParam(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MScenario.class))
        {
            res = method.getAnnotation(MScenario.class).param();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get param in MAnnotation");
        }
        return res;
    }
    
    /**
     * get "ROUTE" value of "SCENARIO" annotation
     * @param method the method which contains "SCENARIO" annotation
     * @return value of "ROUTE" as String
     */
    public static String getScenPath(Method method)
    {
        String res = null;
        if (null != method && method.isAnnotationPresent(MScenario.class))
        {
            res = method.getAnnotation(MScenario.class).route();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get path in MAnnotation");
        }
        return res;
    }
    
    /**
     * get "VALUE" value of "STEPNUMBER" annotation
     * @param method the method which contains "STEPNUMBER" annotation
     * @return value of "VALUE" as integer
     */
    public static int getTestStepValue(Method method)
    {
        int res = -1;
        if (null != method && method.isAnnotationPresent(MStepNumber.class))
        {
            res = method.getAnnotation(MStepNumber.class).value();
        }
        else
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + "\n" +
                    "Cannot get value in MAnnotation");
        }
        return res;
    }
}