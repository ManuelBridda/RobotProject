package it.unibo.planning;

import java.util.Iterator;
import java.util.List;

import aima.core.agent.Action;
 

public class MainPlanUtilDemo {

	public void demo() {
		System.out.println("===== demo");
		try {
			planUtil.initAI(null);
			planUtil.cleanQa(null);
			System.out.println("===== initial map");
			planUtil.showMap(null);
			doSomeMOve();
			System.out.println("===== map after some move");
			planUtil.showMap(null);
			List<Action> actions = planUtil.doPlan(null);
			System.out.println("===== plan actions: " + actions);
			executeMoves( planUtil.doPlan(null) );
			System.out.println("===== map after plan");
			planUtil.showMap(null);
		} catch (Exception e) {
 			e.printStackTrace();
		}		
	}
	
	protected void doSomeMOve() throws Exception {
		planUtil.doMove(null,"w");
		planUtil.doMove(null,"a");
		planUtil.doMove(null,"w");
		planUtil.doMove(null,"w");
		planUtil.doMove(null,"d");
		planUtil.doMove(null,"w");
		planUtil.doMove(null,"a");
		planUtil.doMove(null,"obstacleOnRight");
	}
	
	
	protected void executeMoves(List<Action> actions) throws Exception {
		Iterator<Action> iter = actions.iterator();
		while( iter.hasNext() ) {
			planUtil.doMove(null,iter.next().toString());
		}
	}
	public static void main(String[] args) {
		new MainPlanUtilDemo().demo();
	}
}
