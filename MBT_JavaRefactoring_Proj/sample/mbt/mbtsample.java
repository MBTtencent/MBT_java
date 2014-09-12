package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class mbt extends MbtBase
{
    @MTestStep(beginStep = "A", endStep = "B",
            param = "[ {'name' : 'c', 'value' : '[1, 2, 3]'}, "
                    + "{'name' : 'b', 'value' : '[4, 5, 6]'} ]",
            data = "discards")
    public void AB()
    {
        String sql = "select name,word from employee where id = " + stepParam.get("b");
        String entity = "DBEntity" + stepParam.get("c");
        MQTestActions.query(sql, entity);
        MQTestActions.loadMsg("D:\\Program Files\\QTest-1.3.8.2\\config\\wizard\\message\\paipai\\c2c\\wizard\\CategoryApiAoAddIcsonDic.xml"
                , "C:\\Users\\Administrator\\Desktop\\data.xml", "DBEntity");
    }
    
    @MTestStep(beginStep = "B", endStep = "C")
    public void BC()
    {
        String sql = "select name,word from employee where id = " + scenParam.get("b");
        String entity = "DBEntity";
        MQTestActions.query(sql, entity);
        MQTestActions.loadScript("E:\\MBT_JavaRefactoring_Proj\\sample\\xml\\loadscript.xml", "0", "a");
        MQTestActions.loadVar("E:\\MBT_JavaRefactoring_Proj\\sample\\xml\\loadvar.xml", "1", "1,5,4");
        MQTestActions.loadDbVerify("E:\\MBT_JavaRefactoring_Proj\\sample\\xml\\loaddbverify.xml", "1", "1-2");
    }
    
    @MTestStep(beginStep = "C", endStep = "D",
            param = "[ {'name' : 'c', 'value' : '[aa, bb, cc]'} ]",
            data = "align")
    public void CD()
    {
        String sql = "select name,word from employee where name = " + stepParam.get("c") + scenParam.get("c");
        String entity = "DBEntity";
        MQTestActions.query(sql, entity);
    }

    @MScenario (option = "{'start' : 'B', 'stop' : 'D', 'path' : 'all', 'data' : 'align', 'through' : 'A'}", 
            param = "[ {'name' : 'a', 'value' : '[1, 2, 3]'}, "
                    + "{'name' : 'b', 'value' : '[44, 55, 66]'}, "
                    + "{'name' : 'c', 'value' : '[a, b, c]'} ]")
    public void MbtSampleA()
    {
        
    }

    @MScenario (option = "{'start' : 'A', 'stop' : 'D', 'path' : 'all', 'data' : 'discards', 'through' : 'A'}", 
            param = "[ {'name' : 'a', 'value' : '[1, 2, 3]'}, "
                    + "{'name' : 'b', 'value' : '[44, 55, 66]'}, "
                    + "{'name' : 'c', 'value' : '[a, b, c]'} ]")
    public void MbtSampleB()
    {
        
    }
    
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "mbt", "e:\\test");
    }
}
