package ARH.test;

import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;

@Model
public class PHPUnitSample extends MbtBase
{
    @MTestStep(beginState = "A", endState = "B")
    public void testAdd()
    {
        MPHPUnitActions.script("public $o;");
        MPHPUnitActions.setUp("$this->o = new calculator();");
        MPHPUnitActions.tearDown("unset($this->o);");
        MPHPUnitActions.testFun("testadd", "$this->assertEquals($this->o->add(1, 2), 5);");
        MPHPUnitActions.testFun("testadd2", "$this->assertTrue($this->o->add(102, 106) == 208);");
    }
    
    @MTestStep(beginState = "A", endState = "C")
    public void testSub()
    {
        MPHPUnitActions.script("public $o;");
        MPHPUnitActions.setUp("$this->o = new calculator();");
        MPHPUnitActions.tearDown("unset($this->o);");
        MPHPUnitActions.testFun("testsub", "$this->assertEquals($this->o->sub(1, 2), 5);");
        MPHPUnitActions.testFun("testsub2", "$this->assertTrue($this->o->sub(102, 106) == 208);");
    }

    @MScenario ()
    public void PHPUnitSampleA()
    {      
    }
    
    public static void main(String[] args)
    {
        MTestLoader.run("MPHPUnitActions", "MPHPUnitView", "PHPUnitSample", "e:\\test", false);
    }
}
