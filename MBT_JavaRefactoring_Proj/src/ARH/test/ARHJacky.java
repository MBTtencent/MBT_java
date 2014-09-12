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
public class ARHJacky extends MbtBase
{
    private static final String MESSGE_LOADERMSGDIR = "C:/Users/jackyguo/Desktop/ECC/Qtest/QTest-1.3.8.0/config/wizard/message/paipai/c2c/wizard/";  
	private static final String MESSGE_LOADERNOMSGDIR = "E:/test/testData/";  
	private static final String MESSGE_LOADERDATADIR = "E:/test/test/";  
	@MTestStep(beginState = "Start", endState = "PreData")
	public void StartPreData()
	{
	    MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","1-4");
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","1-6");
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","5-8");
	}	  
	@MTestStep(beginState = "PreData", endState = "DealAoCreateDeal")
	public void PreDataCreateDeal()
	{   	
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","7,8,9,10,11,12,21");
		MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","10-16");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoCreateDeal.xml",
        		MESSGE_LOADERDATADIR+"testDealAoCreateDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","13-16");
        MQTestActions.loadScript(MESSGE_LOADERNOMSGDIR+"sctiptFunctions.xml","1","14");
	}
	@MTestStep(beginState = "DealAoCreateDeal", endState = "QueryTrade")
	public void CreateDealQueryTrade()
	{
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64}", scenParam.get("DBEntity")); 
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","17-18");
        MQTestActions.query("select Ftrade_id,Ftrade_state from unp_deal.t_trade_${varTableId} where Fdeal_id=${varDealId64} order by Ftrade_id desc", scenParam.get("DBEntity"));
        MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","19-20");
	}
////DealAoNotifyPayment
	@MTestStep(beginState = "QueryTrade", endState = "DealAoNotifyPayment")
	public void QueryTradeTwoPayment()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifyPayment.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifyPayment.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
//////DealAoMarkShipping
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoMarkShipping")
	public void PaymentMarkShipping()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoMarkShipping.xml",
	            MESSGE_LOADERDATADIR+"testDealAoMarkShipping.xml", scenParam.get("qqEntity"));
	}
////DealAoConfirmReceive
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoConfirmReceive",
			   param="[{'name':'varOperatorRole','value':'[1,2,3,1]','type':'string'}," 
		       +"{'name':'tBuyerId','value':'[634035710,634035710,634035710,634035711]','type':'string'},"
		       +"{'name':'varBResultCode','value':'[0,0,0,14]','type':'string'},"
		       +"{'name':'dbVerify1','value':'[11,22]','type':'string'}]",
		       data="align")
	public void MarkReceive()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
	            MESSGE_LOADERDATADIR+"DealAoConfirmReceiveStemParam.xml", scenParam.get("qqEntity"));
	}
	@MTestStep(beginState = "QueryTrade", endState = "DealAoConfirmReceive")
	public void QueryReceive()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
	            MESSGE_LOADERDATADIR+"DealAoConfirmReceiveNoParam.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");   
	}
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoConfirmReceive")
	public void PayReceive()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
	            MESSGE_LOADERDATADIR+"DealAoConfirmReceiveNoParam.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");   
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoConfirmReceiveForRateState")
	public void MarkReceiveRateState()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
	            MESSGE_LOADERDATADIR+"DealAoConfirmReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4");   
	}	
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoConfirmReceiveForOther")
	public void MarkReceiveForOther()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoConfirmReceive.xml",
	            MESSGE_LOADERDATADIR+"DealAoConfirmReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4");   
	}
//////DealAoUserCancelDeal
	@MTestStep(beginState = "QueryTrade", endState = "DealAoUserCancelDeal")
	public void QueryCancelDealSuccess()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13,14,15"); 
	}	
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoUserCancelDeal")
	public void PaymentCancelDealFail()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "28");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1"); 
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoUserCancelDeal")
	public void MarkCancelDealFail()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "28");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6"); 
	}
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoUserCancelDeal")
	public void ReceiveCancelDealFail()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "28");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "7"); 
	}
	@MTestStep(beginState = "DealAoHaltDealForOther", endState = "DealAoUserCancelDeal")
	public void HaltDealCancelDealFail()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "30");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
	}
	@MTestStep(beginState = "QueryTrade", endState = "DealAoUserCancelDealForOther")
	public void QueryCancelDealSuccessForOther()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoUserCancelDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoUserCancelDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13"); 
	}
//////DealAoHaltDeal
	@MTestStep(beginState = "QueryTrade", endState = "DealAoHaltDeal")
	public void QueryHaltDeal()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");
	}
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoHaltDeal")
	public void PaymentHaltDeal()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
		            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoHaltDeal")
	public void MarkHaltDeal()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
		            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");
	}
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoHaltDeal")
	public void ReceiveHaltDeal()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
		            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "7");
	}
	@MTestStep(beginState = "DealAoUserCancelDealForOther", endState = "DealAoHaltDeal")
	public void CancelHaltDeal()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
		            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "13");
	}
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoHaltDealForOther")
	public void QueryHaltDealForOther()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoHaltDeal.xml",
	            MESSGE_LOADERDATADIR+"testDealAoHaltDeal.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "7");
	}	
/////DealAoNotifySettlement
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoNotifySettlement")
	public void ReceiveSettle()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "25-26");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8,9,10");   
	}
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoNotifySettlementForRateState")
	public void ReceiveSettleForRate()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "25-26");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"DealAoNotifySettlementForReceive.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8,9,10");   
	}
////getScenParam
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoNotifySettlementForVisibility")
	public void ReceiveSettleForOther()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoNotifySettlement.xml",
	            MESSGE_LOADERDATADIR+"testDealAoNotifySettlementForScenParam.xml", scenParam.get("qqEntity"));  
	}		
//////DealAoSysQueryDealDetail
	@MTestStep(beginState = "PreData", endState = "DealAoSysQueryDealDetail",
			   param="[{'name':'varInfoType','value':'[463,2,16,512,463]','type':'string'}," 
				     +"{'name':'varHistoryFlag','value':'[0,0,0,0, ,]','type':'string'},"
				     +"{'name':'tBuyerId','value':'[634035710,634035710,634035710,634035711,634035710]','type':'string'},"
				     +"{'name':'varBResultCode','value':'[0,0,0,255,0]','type':'string'}]",
			   data="align")
	public void PreSysQueryDealDetail()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "23-24");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSysQueryDealDetail.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSysQueryDealDetail.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4");  
	}
////DealListAoSysGetDealList
	@MTestStep(beginState = "PreData", endState = "DealListAoSysGetDealList",
			   param="[{'name':'varInfoType','value':'[463,1,2,4,463,4]','type':'string'}," 
				     +"{'name':'varTotalNumFlag','value':'[2,2,2,2,2, ,]','type':'string'},"
				     +"{'name':'varHistoryFlag','value':'[0,0,0,0,1,1]','type':'string'},"
				     +"{'name':'varTotalNum','value':'[7,7,7,7,7,7]','type':'string'},"
				     +"{'name':'varBResultCode','value':'[0,0,0,0,65522,65522]','type':'string'}]",
			   data="align")
	public void PreSysQueryList()
	{
		MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml", "1", "23-24");
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealListAoSysGetDealList.xml",
	            MESSGE_LOADERDATADIR+"testDealListAoSysGetDealList.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "2-4");  
	}	
//DealAoSyncBankResult
	@MTestStep(beginState = "QueryTrade", endState = "DealAoSyncBankResult")
	public void QueryCreateSyncBank()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSyncBankResult.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSyncBankResult.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");  
	}
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoSyncBankResult")
	public void QueryPaySyncBank()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSyncBankResult.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSyncBankResult.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");  
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoSyncBankResult")
	public void QueryMarkSyncBank()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSyncBankResult.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSyncBankResult.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");  
	}
	@MTestStep(beginState = "DealAoConfirmReceiveForOther", endState = "DealAoSyncBankResult")
	public void QueryReceiveSyncBank()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSyncBankResult.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSyncBankResult.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "7");  
	}	
//DealAoModifyRateState 
	@MTestStep(beginState = "DealAoConfirmReceiveForRateState", endState = "DealAoModifyRateState",
			   param="[{'name':'varOperatorRole','value':'[1,1,2,2]','type':'string'}," 
				     +"{'name':'varRateState','value':'[2,3,3,2]','type':'string'},"
				     +"{'name':'varBResultCode','value':'[0,0,0,0]','type':'string'}]",
			   data="align")
	public void ReceiveRateState()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateStateStepParam.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "7");  
	}
	@MTestStep(beginState = "QueryTrade", endState = "DealAoModifyRateState")
	public void QueryRateState()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateState.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");  
	}
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoModifyRateState")
	public void paymentRateState()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateState.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");  
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoModifyRateState")
	public void markRateState()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateState.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");  
	}
	
	@MTestStep(beginState = "DealAoNotifySettlementForRateState", endState = "DealAoModifyRateState")
	public void SettleRateState()
	{
		MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoModifyRateState.xml",
	            MESSGE_LOADERDATADIR+"testDealAoModifyRateState.xml", scenParam.get("qqEntity"));
		MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8");  
	}
////DealAoSetVisibility 
	@MTestStep(beginState = "DealAoNotifySettlement", endState = "DealAoSetVisibility",
			   param="[{'name':'varOperatorRole','value':'[2,2,1,1,1]','type':'string'}," 
				     +"{'name':'varVisibleFlag','value':'[5,7,5,7,1]','type':'string'},"
				     +"{'name':'varBResultCode','value':'[14,14,14,14,14]','type':'string'}]",
			   data="align")
	public void SettVisibility()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSetVisibilityStepParam.xml", scenParam.get("qqEntity"));
        MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "8-10");  
	}
	
	@MTestStep(beginState = "QueryTrade", endState = "DealAoSetVisibility")
   public void QueryVisibility()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","27-29");
	    MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
		     MESSGE_LOADERDATADIR+"testDealAoSetVisibility.xml", scenParam.get("qqEntity"));
	    MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "5");   
	}
	@MTestStep(beginState = "DealAoNotifyPayment", endState = "DealAoSetVisibility")
   public void PaymentVisibility()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","27-29");
	    MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
			     MESSGE_LOADERDATADIR+"testDealAoSetVisibility.xml", scenParam.get("qqEntity"));
	    MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "1");   
	}
	@MTestStep(beginState = "DealAoMarkShipping", endState = "DealAoSetVisibility")
	public void MarkVisibility()
	{
	    MQTestActions.loadVar(MESSGE_LOADERNOMSGDIR+"setVar.xml","1","27-29");
	    MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
				     MESSGE_LOADERDATADIR+"testDealAoSetVisibility.xml", scenParam.get("qqEntity"));
	    MQTestActions.loadDbVerify(MESSGE_LOADERNOMSGDIR+"dbverify.xml", "1", "6");   
	}	
	//getScenParam
	@MTestStep(beginState = "DealAoNotifySettlementForVisibility", endState = "DealAoSetVisibility")
	public void SettleVisibility()
	{
        MQTestActions.loadMsg(MESSGE_LOADERMSGDIR+"DealAoSetVisibility.xml",
	            MESSGE_LOADERDATADIR+"testDealAoSetVisibilityScenParam.xml", scenParam.get("qqEntity"));  
	}	
    @MScenario (option = "{'start' : 'Start', 'path' : 'all', 'data' : 'align'}", 
            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'},"
                    +"{'name':'varSellerSettleFee','value':'[18500,0,18500]','type':'string'},"
				    +"{'name':'varBuyerSettleFee','value':'[0,18500,0]','type':'string'},"
				    +"{'name':'tSellerId','value':'[1105788698,1105788698,634035710]','type':'string'},"
				    +"{'name':'varBResultSett','value':'[0,0,14]','type':'string'},"
				    +"{'name':'varOperator','value':'[2,1,1]','type':'string'},"
				    +"{'name':'varVisible','value':'[1,5,1]','type':'string'},"
				    +"{'name':'varBResultVis','value':'[0,14,10]','type':'string'}," 
                    + "{'name' : 'varFkuj', 'value' : '[110, 111, 112]', 'type' : 'string'} ]")
	public void ARHSampleA()
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

                    
       
//    @MScenario (option = "{'start' : 'PreData', 'path' : 'all', 'data' : 'align'}", 
//            param = "[ {'name' : 'qqEntity', 'value' : 'paipai_appPlatform_server6', 'type' : 'string'},"
//                    + "{'name' : 'DBEntity', 'value' : 'mysql_db', 'type': 'string'}]")
//	public void ARHSampleB()
//	{
//	}
    
    
    
    public static void main(String[] args)
    {
    	MTestLoader.run("MQTestActions", "MQTestView", "ARHJacky", "e:\\test",true);
    }
}
