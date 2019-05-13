
	timew(740).  
	timeTurn(345).	
	curGoal(0,0).   
 	
%%eval( le, X, X ) :- !. 
%%eval( le, X, V ) :- eval( lt, X , V ) .

	eval( eq, X, X ).		
	doTheMove(M) :-  moveDone(_),!,fail. 
	doTheMove(M) :-  
		move(M1), !, 
		eval(eq,M,M1), 
 		doTheFirstMove(M). 
	    
	doTheFirstMove(w) :- timew(T), 
	         replaceRule( moveDone(_), moveDone(T) ), retract( move(w) ),!.   			
	doTheFirstMove(a) :- timeTurn(T),  
	         replaceRule( moveDone(_),moveDone(T) ), retract( move(a) ),!.
	doTheFirstMove(d) :- timeTurn(T),  
	        replaceRule( moveDone(_),moveDone(T) ), retract( move(d) ),!.

explorableDiagonalGoal :- getVal(curNumExplore,N), obstacleOnExploringGoal(N),!.

stopExploring :- getVal(curNumExplore,N), N>20.