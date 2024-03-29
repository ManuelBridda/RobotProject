/* Generated by AN DISI Unibo */ 
package it.unibo.conditioncontrol;
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
public abstract class AbstractConditioncontrol extends QActor { 
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
		public AbstractConditioncontrol(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/conditioncontrol/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/conditioncontrol/plans.txt";
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
	    	stateTab.put("init2",init2);
	    	stateTab.put("handleMsg",handleMsg);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "conditioncontrol tout : stops");  
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
	    	parg = "consult(\"./resourceModel.pl\")";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	temporaryStr = "\"waiting for msg\"";
	    	println( temporaryStr );  
	    	//switchTo init2
	        switchToPlanAsNextState(pr, myselfName, "conditioncontrol_"+myselfName, 
	              "init2",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun init2 = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_init2",0);
	     pr.incNumIter(); 	
	    	String myselfName = "init2";  
	    	//bbb
	     msgTransition( pr,myselfName,"conditioncontrol_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleMsg"), stateTab.get("handleMsg") }, 
	          new String[]{"true","M","internalcmd", "true","M","usercmd" },
	          36000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init2){  
	    	 println( getName() + " plan=init2 WARNING:" + e_init2.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init2
	    
	    StateFun handleMsg = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleMsg",-1);
	    	String myselfName = "handleMsg";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(CATEG,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="changeModelItem(CATEG,NAME,VALUE)";
	    		/* PHead */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    			if( parg != null ) {
	    			    aar = QActorUtils.solveGoal(this,myCtx,pengine,parg,"",outEnvView,86400000);
	    				//println(getName() + " plan " + curPlanInExec  +  " interrupted=" + aar.getInterrupted() + " action goon="+aar.getGoon());
	    				if( aar.getInterrupted() ){
	    					curPlanInExec   = "handleMsg";
	    					if( aar.getTimeRemained() <= 0 ) addRule("tout(demo,"+getName()+")");
	    					if( ! aar.getGoon() ) return ;
	    				} 			
	    				if( aar.getResult().equals("failure")){
	    					if( ! aar.getGoon() ) return ;
	    				}else if( ! aar.getGoon() ) return ;
	    			}
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(CATEG,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="changeModelItem(CATEG,NAME,VALUE)";
	    		/* PHead */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    			if( parg != null ) {
	    			    aar = QActorUtils.solveGoal(this,myCtx,pengine,parg,"",outEnvView,86400000);
	    				//println(getName() + " plan " + curPlanInExec  +  " interrupted=" + aar.getInterrupted() + " action goon="+aar.getGoon());
	    				if( aar.getInterrupted() ){
	    					curPlanInExec   = "handleMsg";
	    					if( aar.getTimeRemained() <= 0 ) addRule("tout(demo,"+getName()+")");
	    					if( ! aar.getGoon() ) return ;
	    				} 			
	    				if( aar.getResult().equals("failure")){
	    					if( ! aar.getGoon() ) return ;
	    				}else if( ! aar.getGoon() ) return ;
	    			}
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(temperature,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"Ricevuta temperatura:\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(temperature,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(temperature,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "VALUE";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(temperature,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(clocktime,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"Ricevuto orario:\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(clocktime,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(clocktime,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "VALUE";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(clocktime,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(ias,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"Ricevuto evento da Mind/AI:\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(ias,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(ias,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("internalcmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "VALUE";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(ias,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(console,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"Ricevuto comando da console:\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(console,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(console,NAME,VALUE)");
	    	if( currentMessage != null && currentMessage.msgId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "VALUE";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(console,NAME,VALUE)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?model(state(X))" )) != null ){
	    	temporaryStr = "X";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	temporaryStr = "\"---------------\"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"conditioncontrol_"+myselfName,false,true);
	    }catch(Exception e_handleMsg){  
	    	 println( getName() + " plan=handleMsg WARNING:" + e_handleMsg.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleMsg
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
