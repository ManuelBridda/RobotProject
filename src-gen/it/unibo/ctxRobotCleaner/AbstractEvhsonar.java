/* Generated by AN DISI Unibo */ 
package it.unibo.ctxRobotCleaner;
import alice.tuprolog.Term;
import it.unibo.contactEvent.interfaces.IEventItem;
import it.unibo.qactors.platform.EventHandlerComponent;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.QActorUtils;

public abstract class AbstractEvhsonar extends EventHandlerComponent { 
protected IEventItem event;
	public AbstractEvhsonar(String name, QActorContext myCtx, IOutputEnvView outEnvView, String[] eventIds ) throws Exception {
		super(name, myCtx, eventIds, outEnvView);
  	}
	@Override
	public void doJob() throws Exception {	}
	
	public void handleCurrentEvent() throws Exception {
		event = this.currentEvent; //AKKA getEventItem();
		if( event == null ) return;
		{
		Term msgt       = Term.createTerm(event.getMsg());
		Term msgPattern = Term.createTerm("cmd(CATEG,NAME,VALUE)");
				boolean b = this.pengine.unify(msgt, msgPattern);
				if( b ) {
			  		sendMsg("sonarCtrl","mind", QActorContext.dispatch, msgt.toString() ); 
				}else{
					println("non unifiable");
				}
		}
	}//handleCurrentEvent
	
	@Override
	protected void handleQActorEvent(IEventItem ev) {
		super.handleQActorEvent(ev);
 		try {
			handleCurrentEvent();
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}//handleQActorEvent
	
}