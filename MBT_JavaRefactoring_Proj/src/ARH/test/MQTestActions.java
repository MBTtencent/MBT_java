/*
 * FileName: MQTestActions.java
 * 
 * Description: create MQTestActions class
 * 
 * History:
 * 1.0 MICKCHEN 2013-09-06 Create file
 */


package ARH.test;

import ARH.framework.annotation.*;

public class MQTestActions
{
    /**
     * SetUp action. Set the step as setup
     */
    @MAction
    public static void setup()
    {
    }
    
    /**
     * CleanUp action. Set the step as cleanup
     */
    @MAction
    public static void cleanup()
    {
    }
    
    /**
     * Set variable action 
     * @param name variable name
     * @param value variable value
     * @param type variable type
     * @param domain variable domain
     */
    @MAction
    public static void setVar(String name, String value, String type, String domain)
    {
    }
    
    /**
     * Script action
     * @param script script content
     */
    @MAction
    public static void script(String script)
    {
    }
    
    /**
     * Remote call action
     * @param script script content
     * @param result the except result
     * @param entity socket entity
     */
    @MAction
    public static void remoteCall(String script, String expRet, String entity)
    {
    }
    
    /**
     * Query action
     * @param sql SQL
     * @param dbEntity database entity
     */
    @MAction
    public static void query(String sql, String dbEntity)
    {
    }
    
    /**
     * Database verify action
     * @param sql SQL
     * @param exceptVars the except values
     * @param entity data base entity
     */
    @MAction
    public static void dbVerify(String sql, String exceptValues, String entity)
    {
    }
    
    /**
     * Load message action
     * @param wizard wizard file path
     * @param data data file
     * @param entity entity name
     */
    @MAction
    public static void loadMsg(String wizard, String data, String entity)
    {
    }
    
    /**
     * load variables action
     * @param filePath scripts file path
     * @param type 0:tag 1:id
     * @param regex "1,2,3" or "2-4"
     */
    @MAction
    public static void loadVar(String filePath, String type, String regex)
    {
    }

    /**
     * load variables action
     * @param filePath variables definition file path
     */
    @MAction
    public static void loadVar(String filePath)
    {
    }
    
    /**
     * load scripts action
     * @param filePath scripts file path
     * @param type 0:tag 1:id
     * @param regex "1,2,3" or "2-4"
     */
    @MAction
    public static void loadScript(String filePath, String type, String regex)
    {
    }

    /**
     * load scripts action
     * @param filePath scripts file path
     */
    @MAction
    public static void loadScript(String filePath)
    {
    }
    
    /**
     * load database verify action
     * @param filePath DbVerify definition file path
     * @param type 0:tag 1:id
     * @param regex "1,2,3" or "2-4"
     */
    @MAction
    public static void loadDbVerify(String filePath, String type, String regex)
    {
    }
    
    /**
     * load database verify action
     * @param filePath DbVerify definition file path
     */
    @MAction
    public static void loadDbVerify(String filePath)
    {
    }
    
    /**
     * Load message action, include script, loadVar, loadScript, loadDbVerify
     * @param type action type
     * @param var1 var1 is the script, script file or variable file 
     */
    @MAction
    public static void loadNonMsg(int type, String var1)
    {
    }
    
    /**
     * Load message action, include query
     * @param type action type
     * @param var1 var1 is the SQL
     * @param var2 var2 is the entity of DB
     */
    @MAction
    public static void loadNonMsg(int type, String var1, String var2)
    {
    }
    
    /**
     * Load message action, include remoteCall, dbVerify, loadVar, loadScript, loadDbVerify
     * @param type action type
     * @param var1 var1 is the script or SQL
     * @param var2 var2 is the except result or except values
     * @param var3 var3 is the remote entity or DB entity
     */
    @MAction
    public static void loadNonMsg(int type, String var1, String var2, String var3)
    {
    }
    
    /**
     * Load message action, include setVar 
     * @param type action type
     * @param var1 var1 is the variable name
     * @param var2 var2 is the variable value
     * @param var3 var3 is the variable type
     * @param var4 var4 is the variable domain
     */
    @MAction
    public static void loadNonMsg(int type, String var1, String var2, String var3, String var4)
    {
    }
    
    /**
     * Load HTTP message action
     * @param sFilePath configure file path
     * @param sIP IP
     * @param sPort port
     */
    @MAction
    public static void loadHttpMsg(String filePath, String sIP, String port)
    {
    }
    
    /**
     * QQ Login
     * @param uin
     * @param password
     * @param skey variable
     * @param key variable
     */
    @MAction
    public static void QQLogin(String uin, String password, String skey, String key)
    {
    }
    
    /**
     * Get login key, Only for PaiPai
     * @param uin
     * @param password
     * @param appId
     * @param skey
     * @param psKey
     */
    @MAction
    public static void getLoginKey(String uin, String password, String appId, String skey, 
            String cuin, String psKey)
    {
    }
}