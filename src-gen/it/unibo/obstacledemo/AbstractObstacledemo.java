/* Generated by AN DISI Unibo */ 
package it.unibo.obstacledemo;
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
public abstract class AbstractObstacledemo extends QActor { 
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
		public AbstractObstacledemo(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/obstacledemo/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/obstacledemo/plans.txt";
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
	    	stateTab.put("waitingForCollision",waitingForCollision);
	    	stateTab.put("handleEvent",handleEvent);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "obstacledemo tout : stops");  
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
	    	it.unibo.custom.arduino.realRobotAdapter.init( myself ,"COM3"  );
	    	temporaryStr = "\"demo STARTED\"";
	    	println( temporaryStr );  
	    	//switchTo waitingForCollision
	        switchToPlanAsNextState(pr, myselfName, "obstacledemo_"+myselfName, 
	              "waitingForCollision",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun waitingForCollision = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_waitingForCollision",0);
	     pr.incNumIter(); 	
	    	String myselfName = "waitingForCollision";  
	    	//bbb
	     msgTransition( pr,myselfName,"obstacledemo_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleEvent") }, 
	          new String[]{"true","E","obstacleEvent" },
	          36000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_waitingForCollision){  
	    	 println( getName() + " plan=waitingForCollision WARNING:" + e_waitingForCollision.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//waitingForCollision
	    
	    StateFun handleEvent = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleEvent",-1);
	    	String myselfName = "handleEvent";  
	    	printCurrentEvent(false);
	    	repeatPlanNoTransition(pr,myselfName,"obstacledemo_"+myselfName,false,true);
	    }catch(Exception e_handleEvent){  
	    	 println( getName() + " plan=handleEvent WARNING:" + e_handleEvent.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleEvent
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}