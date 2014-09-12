/*
 * FileName: MSingleton.java
 * 
 * Description: create MSingleton class
 * 
 * History: 
 * 1.0 MICKCHEN 2013-08-16 Create
 */


package ARH.framework.singleton;

import java.util.*;

/**
 *  the base class used to create unique instance
 * @author MICKCHEN
 * @version 1.0
 */
public class MSingleton 
{
    /**
     * define map to contain the object
     */
    private static Map<String, MSingleton> map = new HashMap<String, MSingleton>();

    static 
    {
        MSingleton single = new MSingleton();
        map.put(single.getClass().getName(), single);
    }

    /**
     * constructor
     */
    private MSingleton() {}
    
    /**
     * get instant of a class
     * @return object
     */
    public static MSingleton getInstance(String name)
    {
        if (name == null)
        {
            return null;
        }
        
        if (map.get(name) == null)
        {
			try 
			{
				map.put(name, (MSingleton)Class.forName(name).newInstance());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				return null;
			} 
        }
        
        return map.get(name);
    }
}