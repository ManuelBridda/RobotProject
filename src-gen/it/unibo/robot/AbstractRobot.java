/* Generated by AN DISI Unibo */ 
package it.unibo.robot;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractRobot extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractRobot(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/robot/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/robot/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("waitingCmd",waitingCmd);
	    	stateTab.put("handleCmd",handleCmd);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "robot tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	    	temporaryStr = "\"Robot STARTED\"";
	    	println( temporaryStr );  
	    	it.unibo.utils.clientTcp.initClientConn( myself ,"localhost", "8999"  );
	    	//switchTo waitingCmd
	        switchToPlanAsNextState(pr, myselfName, "robot_"+myselfName, 
	              "waitingCmd",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitingCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitingCmd",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitingCmd";  
	    	//bbb
	     msgTransition( pr,myselfName,"robot_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleCmd") }, 
	          new String[]{"true","M","robotCmd" },
	          36000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitingCmd){  
	    	 println( getName() + " plan=waitingCmd WARNING:" + e_waitingCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitingCmd
	    
	    StateFun handleCmd = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleCmd",-1);
	    	String myselfName = "handleCmd";  
	    	temporaryStr = "\"[Robot] eseguo mossa:\"";
	    	println( temporaryStr );  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("move(MOSSA,TIME)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("move(DIR,TIME)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		{/* JavaLikeMove */ 
	    		String arg1 = "MOSSA" ;
	    		arg1 =  updateVars( Term.createTerm("move(DIR,TIME)"), Term.createTerm("move(MOSSA,TIME)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg1 );	                
	    		//end arg1
	    		String arg2 = "TIME" ;
	    		arg2 =  updateVars( Term.createTerm("move(DIR,TIME)"), Term.createTerm("move(MOSSA,TIME)"), 
	    			                Term.createTerm(currentMessage.msgContent()),  arg2 );	                
	    		//end arg2
	    		it.unibo.utils.clientTcp.sendMsg(this,arg1,arg2 );
	    		}
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("move(MOSSA,TIME)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("move(DIR,TIME)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "MOSSA";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("move(DIR,TIME)"), 
	    		                    Term.createTerm("move(MOSSA,TIME)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("move(MOSSA,TIME)");
	    	if( currentMessage != null && currentMessage.msgId().equals("robotCmd") && 
	    		pengine.unify(curT, Term.createTerm("move(DIR,TIME)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "TIME";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("move(DIR,TIME)"), 
	    		                    Term.createTerm("move(MOSSA,TIME)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	temporaryStr = "\"\"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"robot_"+myselfName,false,true);
	    }catch(Exception e_handleCmd){  
	    	 println( getName() + " plan=handleCmd WARNING:" + e_handleCmd.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleCmd
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
