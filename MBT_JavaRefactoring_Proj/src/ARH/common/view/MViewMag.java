/*
 * FileName: MViewMag.java
 * 
 * Description: create MViewMag class
 * 
 * History:
 * 1.0 MICKCHEN 2013-08-27 Create
 */


package ARH.common.view;

import java.util.HashMap;

import ARH.framework.basic.MBasicApi;
import ARH.framework.logger.MLogMag;

/**
 * This class is used to manage view
 * @author MICKCHEN
 * @version 1.0
 */
public class MViewMag
{
    /**
     * MViewMag instance
     */
    private static MViewMag instance = new MViewMag();
    
    /**
     * view map 
     * HashMap<view-name, view-object>
     */
    private HashMap<String, MViewBase> viewMap = new HashMap<String, MViewBase>();
    
    /**
     * private constructor, forbidden to use
     */
    private MViewMag()
    {
    }
    
    /**
     * get instance of this class
     * @return instance of this class
     */
    public static MViewMag getInsctance()
    {
        return instance;
    }
    
    /**
     * add view into the map
     * @param viewName  the name of view
     */
    public void addView(String viewName)
    {
        MViewBase view;
        try
        {
            view = (MViewBase) Class.forName(MViewConfig.MVIEW_PACKAGE_NAME + "." + viewName).
                    newInstance();
        }
        catch (Exception e) 
        {
            MLogMag.getInstance().getLogger().warning(MBasicApi.getLineInfo() + 
                    e.getMessage());
            view = null;
        }

        viewMap.put(viewName, view);
    }
    
    /**
     * get view
     * @param viewName
     * @return view object
     */
    public MViewBase getView(String viewName)
    {
        return viewMap.get(viewName);
    }
}