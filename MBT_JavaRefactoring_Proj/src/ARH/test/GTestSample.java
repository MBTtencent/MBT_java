package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class GTestSample extends MbtBase
{
    @MTestStep(beginState = "Start", endState = "A")
    public void header()
    {
        MGTestActions.include("<gtest/gtest.h>");
        MGTestActions.include("\"sample.h\"");
    }
    
    @MTestStep(beginState = "A", endState = "B")
    public void testAdd()
    {
        MGTestActions.funInfo("MCalcTC", "Add");
        MGTestActions.exceptEq("2", "Add(1, 1)");
        MGTestActions.exceptGt("3", "Add(1, 1)");
    }
    
    @MTestStep(beginState = "B", endState = "C")
    public void testSub()
    {
        MGTestActions.funInfo("MCalcTC", "Sub");
        MGTestActions.exceptEq("2", "Sub(3, 1)");
        MGTestActions.exceptGt("3", "Sub(4, 3)");
    }
    
    @MTestStep(beginState = "B", endState = "D")
    public void testMul()
    {
        MGTestActions.funInfo("MCalcTC", "Mul");
        MGTestActions.exceptEq("2", "Mul(2, 1)");
        MGTestActions.exceptGt("9", "Mul(3, 3)");
    }
    
    @MTestStep(beginState = "B", endState = "E")
    public void testDiv()
    {
        MGTestActions.funInfo("MCalcTC", "Div");
        MGTestActions.exceptEq("2", "Div(2, 1)");
        MGTestActions.exceptGt("9", "Div(3, 3)");
    }

    @MScenario ()
    public void GTestSampleA()
    {      
    }
    
    public static void main(String[] args)
    {
        MTestLoader.run("MGTestActions", "MGTestView", "GTestSample", "e:\\test", false);
    }
}
