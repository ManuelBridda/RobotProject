/* Generated by AN DISI Unibo */ 
package it.unibo.mind;
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
public abstract class AbstractMind extends QActor { 
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
		public AbstractMind(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/mind/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/mind/plans.txt";
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
	    	stateTab.put("statusNotWorking",statusNotWorking);
	    	stateTab.put("initializingExplore",initializingExplore);
	    	stateTab.put("statusExploring",statusExploring);
	    	stateTab.put("doActions",doActions);
	    	stateTab.put("handleSonar",handleSonar);
	    	stateTab.put("handleMoveForward",handleMoveForward);
	    	stateTab.put("forwardOk",forwardOk);
	    	stateTab.put("handleObstacle",handleObstacle);
	    	stateTab.put("consumePendingCollisions",consumePendingCollisions);
	    	stateTab.put("handleCollision",handleCollision);
	    	stateTab.put("backToHome",backToHome);
	    	stateTab.put("exploreUncovered",exploreUncovered);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "mind tout : stops");  
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
	    	temporaryStr = "\"Robot Mind AI (robot autonomous driving) STARTED\"";
	    	println( temporaryStr );  
	    	//switchTo statusNotWorking
	        switchToPlanAsNextState(pr, myselfName, "mind_"+myselfName, 
	              "statusNotWorking",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun statusNotWorking = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_statusNotWorking",0);
	     pr.incNumIter(); 	
	    	String myselfName = "statusNotWorking";  
	    	temporaryStr = "\"Status Not Working\"";
	    	println( temporaryStr );  
	    	it.unibo.planning.planUtil.initAI( myself  );
	    	parg = "retractall(sonar2(X))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "retractall(move(X))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "retractall(moveDone(X))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "retractall(obstacleOnExploringGoal(X))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "retractall(sonar2position(X,Y))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "consult(\"./robotRules.pl\")";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "retractall(statusExploring(X))";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"ledCmdOff","ledCmdOff", guardVars ).toString();
	    	sendMsg("ledCmdOff","led", QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("statusNotWorking"), stateTab.get("initializingExplore") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","startExploring", "true","M","endAction", "true","M","sonarCtrl", "true","M","moveForward", "true","M","endExploring", "true","E","obstacleEvent", "true","M","internalCmdOn" },
	          3600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_statusNotWorking){  
	    	 println( getName() + " plan=statusNotWorking WARNING:" + e_statusNotWorking.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//statusNotWorking
	    
	    StateFun initializingExplore = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_initializingExplore",0);
	     pr.incNumIter(); 	
	    	String myselfName = "initializingExplore";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"ledCmdOn","ledCmdOn", guardVars ).toString();
	    	sendMsg("ledCmdOn","led", QActorContext.dispatch, temporaryStr ); 
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"move(DIR,TIME)","move(turnLeft,T)", guardVars ).toString();
	    	sendMsg("robotCmd","robot", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(500,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "initializingExplore";
	    	if( ! aar.getGoon() ) return ;
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"move(DIR,TIME)","move(turnLeft,T)", guardVars ).toString();
	    	sendMsg("robotCmd","robot", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(500,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "initializingExplore";
	    	if( ! aar.getGoon() ) return ;
	    	parg = "assign(curNumExplore,0)";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	parg = "assign(nstep,0)";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	it.unibo.planning.planUtil.cleanQa( myself  );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"startExploring","startExploring", guardVars ).toString();
	    	sendMsg("startExploring",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("statusExploring") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","startExploring" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_initializingExplore){  
	    	 println( getName() + " plan=initializingExplore WARNING:" + e_initializingExplore.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//initializingExplore
	    
	    StateFun statusExploring = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_statusExploring",0);
	     pr.incNumIter(); 	
	    	String myselfName = "statusExploring";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "exploreStep(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	it.unibo.planning.planUtil.extendSpaceToexplore( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?inc(curNumExplore,1,E)" )) != null ){
	    	it.unibo.planning.planUtil.setGoal( myself ,guardVars.get("E"), guardVars.get("E")  );
	    	}
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?stopExploring" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "cmd(CATEG,NAME,VALUE)","cmd(ias,ia1,offobstacle)", guardVars ).toString();
	    	emit( "mindEvent", temporaryStr );
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking") }, 
	          new String[]{"true","M","internalCmdOff" },
	          1000, "doActions" );//msgTransition
	    }catch(Exception e_statusExploring){  
	    	 println( getName() + " plan=statusExploring WARNING:" + e_statusExploring.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//statusExploring
	    
	    StateFun doActions = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doActions",-1);
	    	String myselfName = "doActions";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?move(M)" )) != null ){
	    	temporaryStr = "doActions_doingTheMove(M)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " not !?move(M)" )) != null )
	    	{
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	temporaryStr = "moveDone(_)";
	    	removeRule( temporaryStr );  
	    	replaceRule("moveDuration(_)", "moveDuration(moveWDuration(0))");
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(a)" )) != null ){
	    	{//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    	it.unibo.utils.movePlanUtil.move( myself ,"a", guardVars.get("T")  );
	    	}
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(d)" )) != null ){
	    	{//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?timeTurn(T)" )) != null ){
	    	it.unibo.utils.movePlanUtil.move( myself ,"d", guardVars.get("T")  );
	    	}
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?doTheMove(w)" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"moveForward","moveForward", guardVars ).toString();
	    	sendMsg("moveForward",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	it.unibo.planning.planUtil.showMap( myself  );
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("handleSonar"), stateTab.get("handleMoveForward"), stateTab.get("backToHome") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","sonarCtrl", "true","M","moveForward", "true","M","endAction" },
	          1000, "doActions" );//msgTransition
	    }catch(Exception e_doActions){  
	    	 println( getName() + " plan=doActions WARNING:" + e_doActions.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doActions
	    
	    StateFun handleSonar = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleSonar",-1);
	    	String myselfName = "handleSonar";  
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(sonar2s,sonar2,detectedbysonar2)");
	    	if( currentMessage != null && currentMessage.msgId().equals("sonarCtrl") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="sonar2(reached)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("cmd(CATEG,NAME,VALUE)"),  Term.createTerm("cmd(sonar2s,sonar2,detectedbysonar2)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(sonar2s,sonar2,detectedbysonar2)");
	    	if( currentMessage != null && currentMessage.msgId().equals("sonarCtrl") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		//println("WARNING: variable substitution not yet fully implemented " ); 
	    		{//actionseq
	    		if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    		temporaryStr = "sonar2position(X,Y)";
	    		temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    		addRule( temporaryStr );  
	    		}
	    		};//actionseq
	    	}
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("cmd(sonar2s,sonar2,detectedbysonar2)");
	    	if( currentMessage != null && currentMessage.msgId().equals("sonarCtrl") && 
	    		pengine.unify(curT, Term.createTerm("cmd(CATEG,NAME,VALUE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg = "\"raggiunto sonar 2\"";
	    		/* Print */
	    		parg =  updateVars( Term.createTerm("cmd(CATEG,NAME,VALUE)"), 
	    		                    Term.createTerm("cmd(sonar2s,sonar2,detectedbysonar2)"), 
	    			    		  	Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) println( parg );
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking") }, 
	          new String[]{"true","M","internalCmdOff" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_handleSonar){  
	    	 println( getName() + " plan=handleSonar WARNING:" + e_handleSonar.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleSonar
	    
	    StateFun handleMoveForward = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleMoveForward",-1);
	    	String myselfName = "handleMoveForward";  
	    	it.unibo.utils.movePlanUtil.startTimer( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?timew(T)" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"move(DIR,TIME)","move(moveForward,T)", guardVars ).toString();
	    	sendMsg("robotCmd","robot", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("handleObstacle") }, 
	          new String[]{"true","M","internalCmdOff", "true","E","obstacleEvent" },
	          740, "forwardOk" );//msgTransition
	    }catch(Exception e_handleMoveForward){  
	    	 println( getName() + " plan=handleMoveForward WARNING:" + e_handleMoveForward.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleMoveForward
	    
	    StateFun forwardOk = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("forwardOk",-1);
	    	String myselfName = "forwardOk";  
	    	it.unibo.planning.planUtil.doMove( myself ,"w"  );
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("handleSonar") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","sonarCtrl" },
	          1000, "doActions" );//msgTransition
	    }catch(Exception e_forwardOk){  
	    	 println( getName() + " plan=forwardOk WARNING:" + e_forwardOk.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//forwardOk
	    
	    StateFun handleObstacle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleObstacle",-1);
	    	String myselfName = "handleObstacle";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"move(DIR,TIME)","move(moveForward,0)", guardVars ).toString();
	    	sendMsg("robotCmd","robot", QActorContext.dispatch, temporaryStr ); 
	    	it.unibo.utils.movePlanUtil.getDuration( myself  );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	//switchTo consumePendingCollisions
	        switchToPlanAsNextState(pr, myselfName, "mind_"+myselfName, 
	              "consumePendingCollisions",false, false, null); 
	    }catch(Exception e_handleObstacle){  
	    	 println( getName() + " plan=handleObstacle WARNING:" + e_handleObstacle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleObstacle
	    
	    StateFun consumePendingCollisions = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("consumePendingCollisions",-1);
	    	String myselfName = "consumePendingCollisions";  
	    	temporaryStr = "\"CONSUMO COLLISIONE\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("consumePendingCollisions"), stateTab.get("handleCollision") }, 
	          new String[]{"true","M","internalCmdOff", "true","E","obstacleEvent", "true","M","endAction" },
	          3000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_consumePendingCollisions){  
	    	 println( getName() + " plan=consumePendingCollisions WARNING:" + e_consumePendingCollisions.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//consumePendingCollisions
	    
	    StateFun handleCollision = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleCollision",-1);
	    	String myselfName = "handleCollision";  
	    	it.unibo.planning.planUtil.markCellAsObstacle( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??moveWDuration(T)" )) != null ){
	    	it.unibo.utils.movePlanUtil.moveNoMap( myself ,"s", guardVars.get("T")  );
	    	}
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(2000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "handleCollision";
	    	if( ! aar.getGoon() ) return ;
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?explorableDiagonalGoal" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	}
	    	else{ {//actionseq
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("handleSonar"), stateTab.get("backToHome") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","sonarCtrl", "true","M","endAction" },
	          1000, "doActions" );//msgTransition
	    }catch(Exception e_handleCollision){  
	    	 println( getName() + " plan=handleCollision WARNING:" + e_handleCollision.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleCollision
	    
	    StateFun backToHome = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("backToHome",-1);
	    	String myselfName = "backToHome";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?sonar2(reached)" )) != null ){
	    	{//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(0,0,D)" )) != null ){
	    	{//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?sonar2position(X,Y)" )) != null ){
	    	{//actionseq
	    	it.unibo.planning.planUtil.markCellsAsObstacle( myself ,guardVars.get("X"), guardVars.get("Y")  );
	    	};//actionseq
	    	}
	    	};//actionseq
	    	}
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endExploring","endExploring", guardVars ).toString();
	    	sendMsg("endExploring",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " not !?sonar2(reached)" )) != null )
	    	{
	    	{//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(0,0,D)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "\"AT HOME \"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"endAction","endAction", guardVars ).toString();
	    	sendMsg("endAction",getNameNoCtrl(), QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	it.unibo.planning.planUtil.setGoal( myself ,"0", "0"  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?curPos(X,Y,D)" )) != null ){
	    	temporaryStr = "backToHome(X,Y,D)";
	    	temporaryStr = QActorUtils.substituteVars(guardVars,temporaryStr);
	    	println( temporaryStr );  
	    	}
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	};//actionseq
	    	}it.unibo.planning.planUtil.showMap( myself  );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking"), stateTab.get("statusExploring"), stateTab.get("exploreUncovered") }, 
	          new String[]{"true","M","internalCmdOff", "true","M","endAction", "true","M","endExploring" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_backToHome){  
	    	 println( getName() + " plan=backToHome WARNING:" + e_backToHome.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//backToHome
	    
	    StateFun exploreUncovered = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("exploreUncovered",-1);
	    	String myselfName = "exploreUncovered";  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?noOpGoal(yes)" )) != null ){
	    	{//actionseq
	    	it.unibo.planning.planUtil.findNextCellUncovered( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?uncovered(X,Y)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "uncovered(X,Y)";
	    	removeRule( temporaryStr );  
	    	it.unibo.planning.planUtil.markGoalCellAsObstacle( myself ,guardVars.get("X"), guardVars.get("Y")  );
	    	temporaryStr = "noOpGoal(yes)";
	    	removeRule( temporaryStr );  
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(200,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "exploreUncovered";
	    	if( ! aar.getGoon() ) return ;
	    	};//actionseq
	    	}
	    	};//actionseq
	    	}
	    	it.unibo.planning.planUtil.findNextCellUncovered( myself  );
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?uncovered(X,Y)" )) != null ){
	    	{//actionseq
	    	temporaryStr = "uncovered(X,Y)";
	    	removeRule( temporaryStr );  
	    	it.unibo.planning.planUtil.setGoal( myself ,guardVars.get("X"), guardVars.get("Y")  );
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?sonar2position(Z,W)" )) != null ){
	    	it.unibo.planning.planUtil.setGoal( myself ,guardVars.get("Z"), guardVars.get("W")  );
	    	}
	    	it.unibo.planning.planUtil.doPlan( myself  );
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"mind_"+myselfName,false,
	          new StateFun[]{stateTab.get("statusNotWorking") }, 
	          new String[]{"true","M","internalCmdOff" },
	          100, "doActions" );//msgTransition
	    }catch(Exception e_exploreUncovered){  
	    	 println( getName() + " plan=exploreUncovered WARNING:" + e_exploreUncovered.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//exploreUncovered
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
