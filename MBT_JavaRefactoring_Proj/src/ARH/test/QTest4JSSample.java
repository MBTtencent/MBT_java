package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class QTest4JSSample extends MbtBase
{
    @MTestStep(beginState = "A", endState = "B")
    public void testAdd()
    {
        MQTest4JSActions.testSuite("BasicSuite");
        MQTest4JSActions.funName("BasicSuite.prototype.testAdd");
        MQTest4JSActions.setVar("para1", "1");
        MQTest4JSActions.setVar("para2", "2");
        MQTest4JSActions.setVar("result", "1 + 2");
        MQTest4JSActions.assertSame("1 + 2", "result");
    }
    
    @MTestStep(beginState = "A", endState = "C")
    public void setUp()
    {
        MQTest4JSActions.testSuite("BasicSuite");
        MQTest4JSActions.funName("ComplexFirSuite.prototype.setUp");
        MQTest4JSActions.log("ComplexFirSuite setUp!");
    }
    
    @MTestStep(beginState = "C", endState = "D")
    public void testFir()
    {
        MQTest4JSActions.funName("ComplexFirSuite.prototype.testFir");
        MQTest4JSActions.log("ComplexFirSuite first test function!");
        MQTest4JSActions.assertTrue("true");
    }
    
    @MTestStep(beginState = "D", endState = "E")
    public void testSec()
    {
        MQTest4JSActions.funName("ComplexFirSuite.prototype.testSec");
        MQTest4JSActions.log("ComplexFirSuite second test function!");
        MQTest4JSActions.assertTrue("true");
    }

    @MTestStep(beginState = "D", endState = "E")
    public void tearDown()
    {
        MQTest4JSActions.funName("ComplexFirSuite.prototype.tearDown");
        MQTest4JSActions.log("ComplexTestCase tearDown!");
    }

    @MScenario ()
    public void QTest4JSSampleA()
    {      
    }
    
    public static void main(String[] args)
    {
        MTestLoader.run("MQTest4JSActions", "MQTest4JSView", "QTest4JSSample", "e:\\test", false);
    }
}
