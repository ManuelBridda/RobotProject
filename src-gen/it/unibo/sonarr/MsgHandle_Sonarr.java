/* Generated by AN DISI Unibo */ 
package it.unibo.sonarr;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.akka.QActorMsgQueue;

public class MsgHandle_Sonarr extends QActorMsgQueue{
	public MsgHandle_Sonarr(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  {
		super(actorId, myCtx, outEnvView);
	}
}
