/*
 * FileName: MClass.java
 * 
 * Description: create MClass class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-19 Create
 */


package ARH.framework.dynamic;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import ARH.framework.exception.MException;

/**
 * this class is used to dynamically load class
 * @author SHUYUFANG
 * @version 1.0
 */
public class MClass
{
    /**
     * constructor
     */
    private MClass()
    {
        
    }
    
    /**
     * load specified class with specified URLs
     * @param className the name of the class to load
     * @param urls the URL of files to search
     * @return loaded class if found, null if not found
     */
    public static Class<?> loadClass(String className, URL[] urls) throws MException
    { 
        Class<?> retClass = null;
        try
        {
            URLClassLoader classLoader = new URLClassLoader(urls);
            retClass = Class.forName(className, true, classLoader);
        }
        catch (ClassNotFoundException e)
        {
            throw new MException("Cannot find class \"" + className + "\" in MClass.");
        }
        return retClass;
    }
    
    /**
     * load specified class with no URLs
     * @param className the name of the class to load
     * @return loaded class if found, null if not found
     */
    public static Class<?> loadClass(String className) throws MException
    {
        URL[] urls = new URL[0];
        return MClass.loadClass(className, urls);
    }
    
    /**
     * get a new instance of a class
     * @param c the class of the object to generate
     * @return the new instance if class existed, null if not
     */
    public static Object getInstance(Class<?> c) throws MException
    {
        Object obj = null;
        if (null == c)
        {
            return obj;
        }
        try
        {
            obj = c.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new MException("Cannot get instance of class \"" + c.getName() + "\" in MClass.");
        }
        catch (IllegalAccessException e)
        {
            throw new MException("Cannot get instance of class \"" + c.getName() + "\" in MClass.");
        }
        return obj;
    }
    
    /**
     * get a new instance of a class specified by class name
     * @param className the name of class to generate an instance
     * @return the new instance if class existed, null if not
     */
    public static Object getInstance(String className) throws MException
    {
        Class<?> c = MClass.loadClass(className);
        if (null == c)
        {
            return null;
        }
        return MClass.getInstance(c);
    }
    
    /**
     * invoke a method and get its return object
     * @param method the method to invoke
     * @param instance the object to which the method belong
     * @param args the arguments of the method
     * @return return object of a method
     */
    public static Object invokeMethod(Method method, Object instance, Object[] args) throws MException
    {
        Object obj = null;
        if (null == method || null == instance || null == args)
        {
            return obj;
        }
        try
        {
            obj = method.invoke(instance, args);
        }
        catch (InvocationTargetException e)
        {
            throw new MException("Cannot invoke method in MClass.");            
        }
        catch (IllegalAccessException e)
        {
            throw new MException("Cannot access instance in MClass.");                
        }
        return obj;
    }

    /**
     * get methods of specified class with URLs
     * @param className the name of the class to get methods
     * @param urls the URL of files to search
     * @return methods of the class
     */
    public static ArrayList<Method> getMethods(String className, URL[] urls) throws MException
    {
        ArrayList<Method> methods = new ArrayList<Method>();
        Class<?> tmpClass = loadClass(className, urls);
        if (null != tmpClass)
        {
            Collections.addAll(methods, tmpClass.getMethods());
        }
        return methods;
    }
    
    /**
     * get methods of specified class
     * @param className the name of the class to get methods
     * @return methods of the class
     */
    public static ArrayList<Method> getMethods(String className) throws MException
    {
        ArrayList<Method> methods = new ArrayList<Method>();
        Class<?> tmpClass = loadClass(className);
        if (null != tmpClass)
        {
            Collections.addAll(methods, tmpClass.getMethods());
        }
        return methods;
    }

    /**
     * get methods which has specified annotation with URLs
     * @param className the name of the class to get methods
     * @param annotationClass the class of the annotation
     * @param urls the URL of files to search
     * @return methods which has specified annotation
     */
    public static ArrayList<Method> getMethodsWithAnnotation(String className, 
            Class<? extends Annotation> annotationClass, URL[] urls) throws MException
    {
        ArrayList<Method> methods = new ArrayList<Method>();
        Class<?> tmpClass = loadClass(className, urls);
        if (null != tmpClass)
        {
            for (Method method : tmpClass.getMethods())
            {
                if (method.isAnnotationPresent(annotationClass))
                {
                    methods.add(method);
                }
            }
        }
        return methods;
    }
    
    /**
     * get methods which has specified annotation
     * @param className the name of the class to get methods
     * @param annotationClass the class of the annotation
     * @return methods which has specified annotation
     */
    public static ArrayList<Method> getMethodsWithAnnotation(String className, 
            Class<? extends Annotation> annotationClass) throws MException
    {
        ArrayList<Method> methods = new ArrayList<Method>();
        Class<?> tmpClass = loadClass(className);
        if (null != tmpClass)
        {
            for (Method method : tmpClass.getMethods())
            {
                if (method.isAnnotationPresent(annotationClass))
                {
                    methods.add(method);
                }
            }
        }
        return methods;
    }
}