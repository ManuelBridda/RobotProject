/*
resourceModel.pl
*/

%%%+++++++++++++++++++++++++++++++++ STATE OF THE SYSTEM ++++++++++++++++++++++++++++++++++++++++++++++++

model( state(notworking) ).

%%%+++++++++++++++++++++++++++++++++ STATE OF THE RESOURCES ++++++++++++++++++++++++++++++++++++++++++++++++
%%%For a given type and category, priority is given to the entity that is on top of other with different names.

model( type(sensor, temperature), 	name(tempmock),  	value(800)   ). %%%temp sens
model( type(sensor, clocktime), 	name(clockmock),   value(300) ).	%%%inserire valore adatto
model( type(sensor, console), 		name(consmock),   	value(off)  ). 	%%%console start cmd
model( type(sensor, ias), 		name(ia1), 	value(off)  ). 	%%% ia manda al robot un messaggio per comunicare lo stato di fine lavoro (o se trova un ostacolo invalicabile)


%%%%NOT NEEDED%%%%%
%%%model( type(actuator, leds),      	name(led1), 	value(off)  ). 	%%%led1 -> robot
%%%model( type(actuator, leds),      	name(hue), 	value(off)  ).  %%%led hue lamp
%%%%%%%%%%%%%%%%model( type(sensor, sonars), 		name(sonar1), 	value(off)  ). 	%%%sonar1 - start-point
%%%%%%%%%%%%%%%%model( type(sensor, sonars), 		name(sonar2), 	value(off)  ). 	%%%sonar2 - end-point
%%%%%%%%%%%%%%%%model( type(sensor, robsonars), 	name(rsonar1),  value(off)  ). 	%%%robot sonar

%%%++++++++++++++++++++++++++++++++ THRESHOLDS +++++++++++++++++++++++++++++++++++++++++

temperatureThreshold(25).
clockInfLimit(000000).
clockSupLimit(235959).

%%%+++++++++++++++++++++++ CONDITIONS TO CHANGE STATE +++++++++++++++++++++++

%%%+++++ TO GO IN state(working) ++++++

%%% R-TempOk
rTempOk(T) :- 	temperatureThreshold( LIMIT),
		model( type(sensor, temperature), name(T), value(VALUE) ), 
		eval( ge, LIMIT, VALUE).

%%% R-TimeOk
rTimeOk(C):- 	clockInfLimit(INF), 
		clockSupLimit(SUP), 
		model( type(sensor, clocktime), name(C), value(VALUE)), 
		eval(ge, VALUE, INF), 
		eval(ge, SUP, VALUE).


%%% R-Start
changedModelAction(console, CONS, on):- rTempOk(T), 
					rTimeOk(C), 
					startworking.  %%emette tutti gli events del caso
					
%%%+++++ TO GO IN state(notworking) ++++++

%%%R-Stop
changedModelAction(console, CONS, off):- stopworking. %%emette tutti gli events del caso

changedModelAction(temperature, T, V):- temperatureThreshold(LIMIT), 
					eval( gt, V , LIMIT ), 
					stopworking. %%sarebbe consono usare il not sulle condizioni già esistenti

changedModelAction(clocktime, C, V):- clockSupLimit(SUP),
				 eval( gt, V , SUP ), 
				 !, stopworking.

changedModelAction(clocktime, C, V):- clockInfLimit(INF), 
				 eval( gt, INF , V ), 
				 stopworking.
				 
%%%R-End
changedModelAction(ias, I, offclean):- stopworking.
%%%R-Obstacle
changedModelAction(ias, I, offobstacle):- stopworking. %% nel caso in cui abbia finito di pulire la stanza o nel caso dell'ostacolo


%%%++++++++++++++++++++++ POSSIBLE STATES (with the relative events) +++++++++++++++++++++++++

startworking :-  
	model(state(notworking)),
	replaceRule(
		model(state(notworking)), 
		model(state(working))
		), 
	%%NB IA1 SOLO COSMETICO, NO ARGOMENTI. %%Forse sarebbe più opportuno evento con nome generale, per tutti, non solo "ias"
	emitevent( ctrlEvent, ctrlEvent(on) ).
	%%%Led...?
stopworking :- 
	model(state(working)), 
	replaceRule(
		model(state(working)), 
		model(state(notworking))
		),
	emitevent( ctrlEvent, ctrlEvent(off) ).
	 %%Forse sarebbe più opportuno evento con nome generale, per tutti, non solo "ias"

%%%ChangeStateAndEmit(newstate) con eventualmente anche altri parametri %%%Non vale la pena, perché dobbiamo inviare vari ctrlEventi a diversi componenti, difficile generalizzare (servirebbe come argomento lista dei componenti e relativi valori da mandare).
%%%In alternativa inviare singolo evento generico, e i vari componenti devono adoperarsi conoscerne il significato.

	
%%%+++++++++++++++++++++++++++++++ LOGIC ENGINE ++++++++++++++++++++++++++++++++

getModelItem( TYPE, CATEG, NAME, VALUE ) :-
		model( type(TYPE, CATEG), name(NAME), value(VALUE) ).

changeModelItem( CATEG, NAME, VALUE ) :-
 		replaceRule( 
			model( type(TYPE, CATEG), name(NAME), value(_) ),  
			model( type(TYPE, CATEG), name(NAME), value(VALUE) ) 		
		),!,
		%%output( changedModelAction(CATEG, NAME, VALUE) ),
		( changedModelAction(CATEG, NAME, VALUE) %%to be defined by the appl designer
		  ; true ).		%%to avoid the failure if no changedModelAction is defined
		
eval( ge, X, X ) :- !. 
eval( ge, X, V ) :- eval( gt, X , V ) .

emitevent( EVID, EVCONTENT ) :- 
	actorobj( Actor ), 
	%%output( emit( Actor, EVID, EVCONTENT ) ),
	Actor <- emit( EVID, EVCONTENT ).


%%%  initialize
initResourceTheory :- output("initializing the initResourceTheory ...").
:- initialization(initResourceTheory).
