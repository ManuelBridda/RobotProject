/* Generated by AN DISI Unibo */ 
package it.unibo.led;
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
public abstract class AbstractLed extends QActor { 
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
		public AbstractLed(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/led/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/led/plans.txt";
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
	    	stateTab.put("off",off);
	    	stateTab.put("ledOn",ledOn);
	    	stateTab.put("ledOff",ledOff);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "led tout : stops");  
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
	    	it.unibo.custom.hue.hueClient.init( myself  );
	    	//switchTo off
	        switchToPlanAsNextState(pr, myselfName, "led_"+myselfName, 
	              "off",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun off = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_off",0);
	     pr.incNumIter(); 	
	    	String myselfName = "off";  
	    	it.unibo.custom.hue.hueClient.off( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"led_"+myselfName,false,
	          new StateFun[]{stateTab.get("off"), stateTab.get("ledOn") }, 
	          new String[]{"true","M","ledCmdOff", "true","M","ledCmdOn" },
	          36000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_off){  
	    	 println( getName() + " plan=off WARNING:" + e_off.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//off
	    
	    StateFun ledOn = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("ledOn",-1);
	    	String myselfName = "ledOn";  
	    	it.unibo.custom.hue.hueClient.on( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"led_"+myselfName,false,
	          new StateFun[]{stateTab.get("off") }, 
	          new String[]{"true","M","ledCmdOff" },
	          500, "ledOff" );//msgTransition
	    }catch(Exception e_ledOn){  
	    	 println( getName() + " plan=ledOn WARNING:" + e_ledOn.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//ledOn
	    
	    StateFun ledOff = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("ledOff",-1);
	    	String myselfName = "ledOff";  
	    	it.unibo.custom.hue.hueClient.off( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"led_"+myselfName,false,
	          new StateFun[]{stateTab.get("off") }, 
	          new String[]{"true","M","ledCmdOff" },
	          500, "ledOn" );//msgTransition
	    }catch(Exception e_ledOff){  
	    	 println( getName() + " plan=ledOff WARNING:" + e_ledOff.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//ledOff
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
