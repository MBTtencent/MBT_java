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
public class ARHJackyState extends MbtBase
{
    private static final String MESSGE_LOADERMSGDIR = "C:/Users/jackyguo/Desktop/ECC/Qtest/QTest-1.3.8.0/config/wizard/message/paipai/c2c/wizard/";  
	private static final String MESSGE_LOADERNOMSGDIR = "E:/test/testData/";  
	private static final String MESSGE_LOADERDATADIR = "E:/test/testState/";  
	@MTestStep(beginState = "DealAoCreateDeal", endState = "WaitPayment")
	public void CreateDeal()
	{
	    MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","1-4");
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","1-6");
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
//DealAoModifyPrice
//	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment",
//			   param="[{'name':'varOpRole','value':'[1]','type':'string'}," 
//			       +"{'name':'tModPrice','value':'[50]','type':'string'},"
//			       +"{'name':'tModShipFee','value':'[50]','type':'string'},"
//			       +"{'name':'varBResult','value':'[0]','type':'string'}]",
//			       data="align")
//	public void ModifyPrice()
//	{
//        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyPrice.xml",
//        		MESSGE_LOADERDATADIR+"testDealAoModifyPrice.xml", scenParam.get("qqEntity"));
//	}
//DealAoModifyBuyRemark
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment",
			   param="[{'name':'varOpRole','value':'[1]','type':'string'}," 
			       +"{'name':'varBuyRemark','value':'[jacky111]','type':'string'},"
			       +"{'name':'varBResult','value':'[0]','type':'string'}]",
			       data="align")
	public void ModifyRemark()
	{
     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyBuyRemark.xml",
     		MESSGE_LOADERDATADIR+"testDealAoModifyBuyRemark.xml", scenParam.get("qqEntity"));
	}	
//DealAoModifyRecvInfo
	@MTestStep(beginState = "WaitPayment", endState = "WaitPayment")
	public void ModifyRecv()
	{
     MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRecvInfo.xml",
  		MESSGE_LOADERDATADIR+"testDealAoModifyRecvInfo.xml", scenParam.get("qqEntity"));
	}	
////DealAoUserCancelDeal
	@MTestStep(beginState = "WaitPayment", endState = "CancelDeal")
	public void CancelDeal()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13,14,15"); 
	}
//////DealAoUserCancelDeal
	@MTestStep(beginState = "CancelDeal", endState = "WaitMarkShip")
	public void CancelPayment()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
////DealAoNotifyPayment
	@MTestStep(beginState = "WaitPayment", endState = "WaitMarkShip")
	public void NotifyPayment()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
	
////DealAoApplyFullRefund
	@MTestStep(beginState = "WaitMarkShip", endState = "WaitRefund")
	public void FApplyRefund()
	{
	 MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoApplyFullRefund.xml",
  	 MESSGE_LOADERDATADIR+"testDealAoApplyFullRefund.xml", scenParam.get("qqEntity"));
	}
	
////DealAoAgreeFullRefund
	@MTestStep(beginState = "WaitRefund", endState = "AgreeRefund")
	public void FAgreeRefund()
	{
	 MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoAgreeFullRefund.xml",
	 MESSGE_LOADERDATADIR+"testDealAoAgreeFullRefund.xml", scenParam.get("qqEntity"));
	}
	@MTestStep(beginState = "AgreeRefund", endState = "DealSuccess")
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
////DealAoApplyRefund
	@MTestStep(beginState = "WaitReceiveShip", endState = "ApplyRefund")
	public void WRecApplyRefund()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoApplyRefund.xml",
	            MESSGE_LOADERDATADIR+"testDealAoApplyRefund.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}

////DealAoApplyRefund
	@MTestStep(beginState = "ApplyRefund", endState = "AgreeRefund")
	public void AgreeRefund()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoAgreeRefund.xml",
	            MESSGE_LOADERDATADIR+"testDealAoAgreeRefund.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}	
	
	@MTestStep(beginState = "AgreeRefund", endState = "DealSuccess")
	public void RefundSuccess()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "31-32");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
	} 	
//DealAoConfirmReceive
	@MTestStep(beginState = "WaitReceiveShip", endState = "ConfirmReceive")
	public void WRecvReceive()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
                MESSGE_LOADERDATADIR+"DealAoConfirmReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4"); 
	}	
///////DealAoNotifySettlement
	@MTestStep(beginState = "ConfirmReceive", endState = "DealSuccess")
	public void ReceiveSettle()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "25-26");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8,9,10");   
	}
//////DealAoHaltDeal
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
	@MTestStep(beginState = "HaltDeal", endState = "DealSuccess")
	public void HaltSuccess()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "31-32");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
	} 
    @MScenario (option = "{'start' : 'DealAoCreateDeal', 'path' : 'all', 'data' : 'align'}", 
            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
	public void ARHSampleB()
	{
	}   
////through
//    @MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','data' : 'discards','through':'DealAoSyncBankResult'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                    +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//				    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//				    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//				    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//				    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//				    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//				    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                    + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//    public void ARHSampleC()
//	{
//	}
                    
                    
////long  
//    @MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'long','through':'Start'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                    +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//				    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//				    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//				    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//				    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//				    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//				    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                    + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//	public void ARHSampleD()
//	{
//	}
                    
                    
////short   
//    @MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'short','throughState':'Start'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                    +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//				    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//				    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//				    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//				    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//				    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//				    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                    + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//	public void ARHSampleE()
//	{
//	}  
                    
                    
////repeat  
//    @MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'repeat','through':'Start'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                    +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//				    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//				    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//				    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//				    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//				    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//				    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                    + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//	public void ARHSampleF()
//	{
//	}  
                    

////n2nshort
//@MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'n2nshort','through':'Start'}", 
//        param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//			    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//			    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//			    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//			    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//			    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//			    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//public void ARHSampleG()
//{
//}   
                    
////n2nlong
//@MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'n2nlong','through':'Start'}", 
//        param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//			    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//			    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//			    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//			    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//			    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//			    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//public void ARHSampleH()
//{
//}  
                    
                    
////least
//@MScenario (option = "{'start' : 'Start','stop':'DealAoSetVisibility','path' : 'least','through':'Start'}", 
//        param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
//                +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
//			    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
//			    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
//			    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
//			    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
//			    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
//			    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
//                + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
//public void ARHSampleI()
//{
//}  
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "ARHJackyState", "e:\\test",true);
    }
}
