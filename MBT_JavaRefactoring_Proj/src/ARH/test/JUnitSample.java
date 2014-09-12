package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class JUnitSample extends MbtBase
{
    @MTestStep(beginState = "A", endState = "B")
    public void testDiv()
    {
        MJUnitActions.setUp("");
        MJUnitActions.before();
        MJUnitActions.testFun("clearCalculator", "calculator.clear();");
        MJUnitActions.after();
        MJUnitActions.tearDown("");
        MJUnitActions.test("timeout=400");
        MJUnitActions.testFun("divide", "calculator.divide(0);");
        MJUnitActions.ignore("not ready yet");
        MJUnitActions.testFun("multiply", "assertEquals(calculator.getResult(50 + 40), 100);");
    }
    
    @MTestStep(beginState = "A", endState = "C")
    public void testSub()
    {
        MJUnitActions.setUp("");
        MJUnitActions.before();
        MJUnitActions.testFun("clearCalculator", "calculator.clear();");
        MJUnitActions.after();
        MJUnitActions.tearDown("");
        MJUnitActions.test("timeout=400");
        MJUnitActions.testFun("subtraction", "calculator.subtraction(10);");
        MJUnitActions.ignore("not ready yet");
        MJUnitActions.testFun("multiply", "assertEquals(calculator.getResult(50 + 40), 100);");
    }
    
    @MTestStep(beginState = "A", endState = "c")
    public void testAdd()
    {
        MJUnitActions.setUp("");
        MJUnitActions.before();
        MJUnitActions.testFun("clearCalculator", "calculator.clear();");
        MJUnitActions.after();
        MJUnitActions.tearDown("");
        MJUnitActions.test("timeout=400");
        MJUnitActions.testFun("add", "calculator.add(23);");
        MJUnitActions.ignore("not ready yet");
        MJUnitActions.testFun("multiply", "assertEquals(calculator.getResult(50 + 40), 100);");
    }

    @MScenario ()
    public void JUnitSampleA()
    {      
    }
    
    public static void main(String[] args)
    {
        MTestLoader.run("MJUnitActions", "MJUnitView", "JUnitSample", "e:\\test", false);
    }
}
