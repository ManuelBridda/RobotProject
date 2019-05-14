%====================================================================================
% Context ctxDemoObstacle  SYSTEM-configuration: file it.unibo.ctxDemoObstacle.demoObstacle.pl 
%====================================================================================
context(ctxdemoobstacle, "localhost",  "TCP", "5558" ).  		 
%%% -------------------------------------------
qactor( obstacledemo , ctxdemoobstacle, "it.unibo.obstacledemo.MsgHandle_Obstacledemo"   ). %%store msgs 
qactor( obstacledemo_ctrl , ctxdemoobstacle, "it.unibo.obstacledemo.Obstacledemo"   ). %%control-driven 
%%% -------------------------------------------
%%% -------------------------------------------

