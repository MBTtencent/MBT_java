/*
 * FileName: ARHJacky.java
 * 
 * Description: create case
 * 
 * History:
 * 1.0 Jackyguo 2013-10-10 Create
 */

package ARH.test;
import ARH.framework.annotation.*;
import ARH.loader.MTestLoader;
@Model
//DealAoCreateDeal
public class ARHDemoUse extends MbtBase
{
    private static final String MESSGE_LOADERMSGDIR = "C:/Users/jackyguo/Desktop/test/ECC/Qtest/QTest-1.3.8.0/config/wizard/message/paipai/c2c/wizard/";  
	private static final String MESSGE_LOADERNOMSGDIR = "E:/test/testDatause/";  
	private static final String MESSGE_LOADERDATADIR = "E:/test/testStatexmluse/";  
	@MTestStep(beginState = "DealAoCreateDeal", endState = "WaitPayment")
	public void CreateDeal()
	{	
		MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","1-6");//定义了处理seqId自增的方法，兼容业务用
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","1-10");//定义的变量用来LoadMsg()消息输入和校验用
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoCreateDeal.xml",
        		MESSGE_LOADERDATADIR+"testDealAoCreateDeal.xml", scenParam.get("qqEntity"));//消息的发送和返回校验 
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","11-15");//添加了后续接口都可能用到的一些公用变量
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","7-10");//实现了分表的方法，以便后续查询用   
        //查询订单的第一个商品子单
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64}", scenParam.get("DBEntity")); 
        //将第一个商品子单保存到变量中 
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","16-17");
        //查询订单的第二个商品子单
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64} order by Ftrade_id desc", scenParam.get("DBEntity"));
      //将第二个商品子单保存到变量中 
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","18-19");
	}
///DealAoNotifyPayment
	@MTestStep(beginState = "WaitPayment", endState = "WaitMarkShip")
	public void NotifyPayment()
	{
	        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
		            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
	        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
////demo_StateCase
    @MScenario (option = "{'start' : 'DealAoCreateDeal', 'path' : 'all', 'data' : 'align'}", 
            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
	public void ARH()
	{
	}
///main
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "ARHDemoUse", "e:\\test\\ARHDemouse",true);
    }
	 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//////DealAoModifyPrice
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void ModPrice()
//	{
//	}
//////DealAoModifyBuyRemark
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void ModRemark()
//	{ 
//	}
//////////DealAoNotifyPayment
//	@MTestStep(beginState = "WaitPayment", endState = "WaitMarkShip")
//	public void NotifyPayment()
//	{
//	}		
////////DealAoMarkShipping
//	@MTestStep(beginState = "WaitMarkShip", endState = "WaitReceiveShip")
//	public void MarkShipping()
//	{
//	}	
//////DealAoConfirmReceive
//	@MTestStep(beginState = "WaitReceiveShip", endState = "ConfirmReceive")
//	public void ReceiveShip()
//	{
//	}		
///////DealAoNotifySettlement
//	@MTestStep(beginState = "ConfirmReceive", endState = "DealDone")
//	public void ReceiveSettle()
//	{
//	}	
////////DealAoSetVisibility
//	@MTestStep(beginState = "DealDone", endState = "DealDone")
//	public void SetVisible()
//	{
//	}
//	@MTestStep(beginState = "DealAoCreateDeal", endState = "WaitPayment")
//	public void CreateDeal()
//	{
//	}
//////DealAoModifyPrice
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void ModPrice()
//	{
//	}
//////DealAoModifyBuyRemark
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void ModRemark()
//	{ 
//	}	
//////DealAoModifyRecvInfo
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void ModifyRecv()
//	{
//	}	
////////DealAoModifyNotipaymentFail
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
//	public void PayFail()
//	{
//	}	
////////DealAoUserCancelDeal
//	@MTestStep(beginState = "WaitPayment", endState = "CancelDeal")
//	public void CancelDeal()
//	{
//	}
//	@MTestStep(beginState = "CancelDeal", endState = "CancelDeal")
//	public void SetHide()
//	{
//	}
//////DealAoUserCancelPayment
//	@MTestStep(beginState = "CancelDeal", endState = "WaitMarkShip")
//	public void CancelPayment()
//	{
//	}
//////////DealAoNotifyPayment
//	@MTestStep(beginState = "WaitPayment", endState = "WaitMarkShip")
//	public void NotifyPayment()
//	{
//	}
////////////ModifyRemark
//	@MTestStep(beginState = "WaitMarkShip", endState = "WaitMarkShip")
//	public void WshipModRemark()
//	{
//	}	
//////DealAoMarkShippingPrepare
//	@MTestStep(beginState = "WaitMarkShip", endState = "WaitMarkShip")
//		public void WshipPrepare()
//			{
//			}	
//////DealAoApplyFullRefund
//	@MTestStep(beginState = "WaitMarkShip", endState = "WaitFullRefund")
//	public void FApplyRefund()
//	{
//	}
//////DealAoAgreeFullRefund
//	@MTestStep(beginState = "WaitFullRefund", endState = "AgreeFullRefund")
//	public void FAgreeRefund()
//	{
//	}
//	@MTestStep(beginState = "AgreeFullRefund", endState = "DealDone")
//	public void FRefundSuccess()
//	{
//	} 	
////////DealAoMarkShipping
//	@MTestStep(beginState = "WaitMarkShip", endState = "WaitReceiveShip")
//	public void MarkShipping()
//	{
//	}	
////DealAoApplyRefund
//	@MTestStep(beginState = "WaitReceiveShip", endState = "WaitRefund")
//	public void WRecApplyRefund()
//	{
//	}
//////DealAoApplyRefund
//	@MTestStep(beginState = "WaitRefund", endState = "AgreeRefund")
//	public void AgreeRefund()
//	{
//	}	
//	@MTestStep(beginState = "AgreeRefund", endState = "DealDone")
//	public void RefundSuccess()
//	{
//	} 	
//////DealAoConfirmReceive
//	@MTestStep(beginState = "WaitReceiveShip", endState = "ConfirmReceive")
//	public void WRecvReceive()
//	{
//	}		
//////DealAoModifyRateState
//	@MTestStep(beginState = "ConfirmReceive", endState = "ConfirmReceive")
//	public void ModRateState()
//	{
//	}
///////DealAoNotifySettlement
//	@MTestStep(beginState = "ConfirmReceive", endState = "DealDone")
//	public void ReceiveSettle()
//	{
//	}	
////////DealAoSetVisibility
//	@MTestStep(beginState = "DealDone", endState = "DealDone")
//	public void SetVisible()
//	{
//	}
////DealAoHaltDeal
//	@MTestStep(beginState = "WaitMarkShip", endState = "HaltDeal")
//	public void WMarkHaltDeal()
//	{
//	}	
//	@MTestStep(beginState = "WaitReceiveShip", endState = "HaltDeal")
//	public void WReceiveHaltDeal()
//	{
//	}
//	@MTestStep(beginState = "HaltDeal", endState = "DealDone",repeatTime=3)
//	public void HaltSuccess()
//	{
//	} 

    
 ////through
//    @MScenario (option = "{'start' : 'DealAoCreateDeal','data' : 'align','throughState':'AgreeRefund','throughStep':'RefundSuccess'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//    public void ARHSampleThrough()
//	{
//	}
	
////demo_Param_Route
//  @MScenario (option = "{'start' : 'DealAoCreateDeal','stop':'AgreeRefund','throughState':'ApplyRefund','throughStep':'RefundSuccess','data' : 'align'}", 
//          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]",
//                  route="[ {'state' : 'DealAoCreateDeal,WaitPayment,WaitMarkShip,WaitReceiveShip', 'step' : 'CreateDeal,ModifyRemark,NotifyPayment,MarkShipping','cov':'partial'}," +
//                  		"{'state' : 'DealAoCreateDeal,WaitPayment,WaitMarkShip,HaltDeal,DealDone', 'step' : 'CreateDeal,ModifyRemark,NotifyPayment,WMarkHaltDeal,HaltSuccess','cov':'overall'}]")
//	public void ARHSampleB()
//	{
//	}                 
////long  
//    @MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path' : 'long','throughState':'AgreeRefund','throughStep':'FRefundSuccess'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]",
//            route="[{'state':'DealAoCreateDeal,WaitPayment,CancelDeal,WaitMarkShip,WaitReceiveShip,ApplyRefund,AgreeRefund','step':'CreateDeal,ModifyRemark,CancelDeal,CancelPayment,MarkShipping,WRecApplyRefund,AgreeRefund,RefundSuccess','cov':'partial'}]")
//	public void ARHSampleLong()
//	{
//	}               
////short   
//    @MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path':'short'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSampleShort()
//	{
//	} 
	
////repeat   
//  @MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path':'repeat'}", 
//          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSamplerepeat()
//	{
//	}  
////shortn2n   
//  @MScenario (option = "{'start' : 'DealAoCreateDeal,CancelDeal','stop':'AgreeRefund,ConfirmReceive','data':'align','path':'shortn2n'}", 
//          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSamplerepeat()
//	{
//	} 
////longn2n   
//  @MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path':'longn2n'}", 
//          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSamplelongn2n()
//	{
//	} 
//////long 
//  @MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path' : 'long'}", 
//	        param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//	                + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSampleLong()
//	{
//	} 
////longn2n   
//  @MScenario (option = "{'start' : 'DealAoCreateDeal,CancelDeal','stop':'AgreeRefund,ConfirmReceive','data':'align','path':'longn2n'}", 
//          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSamplelongn2n()
//	{
//	} 
////least   
//@MScenario (option = "{'start' : 'DealAoCreateDeal','data':'align','path':'least'}", 
//        param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSampleleast()
//	{
//	} 
 
}
