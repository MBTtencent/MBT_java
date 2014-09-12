package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class mbt extends MbtBase
{
    @MTestStep(beginState = "A", endState = "B", repeatTime = 1)
    public void AB1()
    {
        MQTestActions.script("ab1");
    }

    @MTestStep(beginState = "B", endState = "C", repeatTime = 3)
    public void BC1()
    {
        MQTestActions.script("bc1");
//        MQTestActions.loadScript("E:\\ARH_proj\\trunk\\MBT_JavaRefactoring_Proj\\ARHTest\\testData\\function.xml");
        MQTestActions.script("    test");
    }

    @MScenario ()
    public void MbtSampleB()
    {      
    }
    
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "mbt", "e:\\test", false);
    }
}
