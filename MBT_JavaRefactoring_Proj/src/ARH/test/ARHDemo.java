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
public class ARHDemo extends MbtBase
{
    private static final String MESSGE_LOADERMSGDIR = "C:/Users/jackyguo/Desktop/ECC/Qtest/QTest-1.3.8.0/config/wizard/message/paipai/c2c/wizard/";  
	private static final String MESSGE_LOADERNOMSGDIR = "E:/test/testData/";  
	private static final String MESSGE_LOADERDATADIR = "E:/test/testStatexmlfull/";  
	@MTestStep(beginState = "DealAoCreateDeal", endState = "WaitPayment")
	public void CreateDeal()
	{
	    MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","1-4");
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","1,2,3,4,5,6,33");
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","5-8");  	
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","7,8,9,10,11,12,21");
		MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","10-16");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoCreateDeal.xml",
        		MESSGE_LOADERDATADIR+"testDealAoCreateDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","13-16");
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","14");
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64}", scenParam.get("DBEntity")); 
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","17-18");
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64} order by Ftrade_id desc", scenParam.get("DBEntity"));
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","19-20");
	}
////DealAoModifyPrice
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment",
			   param="[{'name':'vRole','value':'[1]','type':'string'}," 
			       +"{'name':'tPrice','value':'[50]','type':'string'},"
			       +"{'name':'tFee','value':'[50]','type':'string'},"
			       +"{'name':'vResult','value':'[0]','type':'string'}," 
			       +"{'name':'dFee','value':'[50]','type':'string'}]",
			       data="align")
	public void ModPrice()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyPrice.xml",
        		MESSGE_LOADERDATADIR+"testDealAoModifyPrice.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "23");
	}
////DealAoModifyBuyRemark
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
	public void ModRemark()
	{
     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyBuyRemark.xml",
     		MESSGE_LOADERDATADIR+"testDealAoModifyBuyRemark.xml", scenParam.get("qqEntity"));
	}	
////DealAoModifyRecvInfo
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
	public void ModifyRecv()
	{
     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRecvInfo.xml",
  		MESSGE_LOADERDATADIR+"testDealAoModifyRecvInfo.xml", scenParam.get("qqEntity"));
	}	
//////DealAoModifyNotipaymentFail
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
	public void PayFail()
	{
		 MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
		            MESSGE_LOADERDATADIR+"testDealAoNotifyPaymentFail.xml", scenParam.get("qqEntity"));
		 MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","24,18,19,20");
	     MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");
	}	
//////DealAoUserCancelDeal
	@MTestStep(beginState = "WaitPayment", endState = "CancelDeal")
	public void CancelDeal()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13,14,15"); 
	}
	@MTestStep(beginState = "CancelDeal", endState = "CancelDeal")
	public void SetHide()
	{
      MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSetVisibility.xml", scenParam.get("qqEntity"));
      MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13,14,15"); 
	}
////DealAoUserCancelPayment
	@MTestStep(beginState = "CancelDeal", endState = "WaitMarkShip")
	public void CancelPayment()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
////////DealAoNotifyPayment
	@MTestStep(beginState = "WaitPayment", endState = "WaitMarkShip")
	public void NotifyPayment()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
//////////ModifyRemark
	@MTestStep(beginState = "WaitMarkShip", endState = "WaitMarkShip")
	public void WshipModRemark()
	{
	     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyBuyRemark.xml",
 		 MESSGE_LOADERDATADIR+"testDealAoModifyBuyRemark.xml", scenParam.get("qqEntity"));
	}	
////DealAoMarkShippingPrepare
	@MTestStep(beginState = "WaitMarkShip", endState = "WaitMarkShip")
		public void WshipPrepare()
			{
		     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoMarkShippingPrepare.xml",
		     		MESSGE_LOADERDATADIR+"testDealAoMarkShippingPrepare.xml", scenParam.get("qqEntity"));
			}	
////DealAoApplyFullRefund
	@MTestStep(beginState = "WaitMarkShip", endState = "WaitFullRefund")
	public void FApplyRefund()
	{
	 MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoApplyFullRefund.xml",
  	 MESSGE_LOADERDATADIR+"testDealAoApplyFullRefund.xml", scenParam.get("qqEntity"));
	}
////DealAoAgreeFullRefund
	@MTestStep(beginState = "WaitFullRefund", endState = "AgreeFullRefund")
	public void FAgreeRefund()
	{
	 MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoAgreeFullRefund.xml",
	 MESSGE_LOADERDATADIR+"testDealAoAgreeFullRefund.xml", scenParam.get("qqEntity"));
	}
	@MTestStep(beginState = "AgreeFullRefund", endState = "DealDone")
	public void FRefundSuccess()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "31-32");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
	} 	
//////DealAoMarkShipping
	@MTestStep(beginState = "WaitMarkShip", endState = "WaitReceiveShip")
	public void MarkShipping()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoMarkShipping.xml",
	            MESSGE_LOADERDATADIR+"testDealAoMarkShipping.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}	
//DealAoApplyRefund
	@MTestStep(beginState = "WaitReceiveShip", endState = "WaitRefund")
	public void WRecApplyRefund()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoApplyRefund.xml",
	            MESSGE_LOADERDATADIR+"testDealAoApplyRefund.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}
////DealAoApplyRefund
	@MTestStep(beginState = "WaitRefund", endState = "AgreeRefund")
	public void AgreeRefund()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoAgreeRefund.xml",
	            MESSGE_LOADERDATADIR+"testDealAoAgreeRefund.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}	
	@MTestStep(beginState = "AgreeRefund", endState = "DealDone")
	public void RefundSuccess()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "31-32");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
	} 	
////DealAoConfirmReceive
	@MTestStep(beginState = "WaitReceiveShip", endState = "ConfirmReceive")
	public void WRecvReceive()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
                MESSGE_LOADERDATADIR+"DealAoConfirmReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4"); 
	}		
////DealAoModifyRateState
	@MTestStep(beginState = "ConfirmReceive", endState = "ConfirmReceive")
	public void ModRateState()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateState.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2");   
	}
/////DealAoNotifySettlement
	@MTestStep(beginState = "ConfirmReceive", endState = "DealDone")
	public void ReceiveSettle()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "25-26");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8,9,10");   
	}	
//////DealAoSetVisibility
	@MTestStep(beginState = "DealDone", endState = "DealDone")
	public void SetVisible()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSetVisibility.xml", scenParam.get("qqEntity")); 
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","34");   
	}
//	@MTestStep(beginState = "WaitPayment", endState = "DealDone")
//	public void setHide()
//	{
//        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
//	            MESSGE_LOADERDATADIR+"testDealAoSetVisibilityFail.xml", scenParam.get("qqEntity"));
//        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","33,18,19,20");
//	}
//DealAoHaltDeal
	@MTestStep(beginState = "WaitMarkShip", endState = "HaltDeal")
	public void WMarkHaltDeal()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "16");
	}	
	@MTestStep(beginState = "WaitReceiveShip", endState = "HaltDeal")
	public void WReceiveHaltDeal()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "16");
	}
	@MTestStep(beginState = "HaltDeal", endState = "DealDone",repeatTime=3)
	public void HaltSuccess()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "31-32");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
	} 
////demo_StateCase
//    @MScenario (option = "{'start' : 'DealAoCreateDeal', 'path' : 'all', 'data' : 'align'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARH()
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
  @MScenario (option = "{'start' : 'DealAoCreateDeal','stop':'AgreeRefund','throughState':'ApplyRefund','throughStep':'RefundSuccess','data' : 'align'}", 
          param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
                  + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]",
                  route="[ {'state' : '', 'step' : '','cov':'partial'}," +
                  		"{'state' : 'DealAoCreateDeal,WaitPayment,WaitMarkShip,HaltDeal,DealDone', 'step' : 'CreateDeal,ModifyRemark,NotifyPayment,WMarkHaltDeal,HaltSuccess','cov':'overall'}]")
	public void ARHSampleB()
	{
	}                 
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
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "ARHDemo", "e:\\test\\ARHCaseDemo",true);
    }
}
