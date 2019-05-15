%====================================================================================
% Context ctxRobotCleaner  SYSTEM-configuration: file it.unibo.ctxRobotCleaner.robotCleaner.pl 
%====================================================================================
context(ctxrobotcleaner, "localhost",  "TCP", "4448" ).  		 
%%% -------------------------------------------
qactor( console , ctxrobotcleaner, "it.unibo.console.MsgHandle_Console"   ). %%store msgs 
qactor( console_ctrl , ctxrobotcleaner, "it.unibo.console.Console"   ). %%control-driven 
qactor( tempsensor , ctxrobotcleaner, "it.unibo.tempsensor.MsgHandle_Tempsensor"   ). %%store msgs 
qactor( tempsensor_ctrl , ctxrobotcleaner, "it.unibo.tempsensor.Tempsensor"   ). %%control-driven 
qactor( sonars , ctxrobotcleaner, "it.unibo.sonars.MsgHandle_Sonars"   ). %%store msgs 
qactor( sonars_ctrl , ctxrobotcleaner, "it.unibo.sonars.Sonars"   ). %%control-driven 
qactor( clocksensor , ctxrobotcleaner, "it.unibo.clocksensor.MsgHandle_Clocksensor"   ). %%store msgs 
qactor( clocksensor_ctrl , ctxrobotcleaner, "it.unibo.clocksensor.Clocksensor"   ). %%control-driven 
qactor( conditioncontrol , ctxrobotcleaner, "it.unibo.conditioncontrol.MsgHandle_Conditioncontrol"   ). %%store msgs 
qactor( conditioncontrol_ctrl , ctxrobotcleaner, "it.unibo.conditioncontrol.Conditioncontrol"   ). %%control-driven 
qactor( robot , ctxrobotcleaner, "it.unibo.robot.MsgHandle_Robot"   ). %%store msgs 
qactor( robot_ctrl , ctxrobotcleaner, "it.unibo.robot.Robot"   ). %%control-driven 
qactor( led , ctxrobotcleaner, "it.unibo.led.MsgHandle_Led"   ). %%store msgs 
qactor( led_ctrl , ctxrobotcleaner, "it.unibo.led.Led"   ). %%control-driven 
qactor( cmdrobotconverter , ctxrobotcleaner, "it.unibo.cmdrobotconverter.MsgHandle_Cmdrobotconverter"   ). %%store msgs 
qactor( cmdrobotconverter_ctrl , ctxrobotcleaner, "it.unibo.cmdrobotconverter.Cmdrobotconverter"   ). %%control-driven 
qactor( mind , ctxrobotcleaner, "it.unibo.mind.MsgHandle_Mind"   ). %%store msgs 
qactor( mind_ctrl , ctxrobotcleaner, "it.unibo.mind.Mind"   ). %%control-driven 
%%% -------------------------------------------
eventhandler(evhsonar,ctxrobotcleaner,"it.unibo.ctxRobotCleaner.Evhsonar","sonarEvent").  
eventhandler(evhs2,ctxrobotcleaner,"it.unibo.ctxRobotCleaner.Evhs2","sonar").  
eventhandler(evh,ctxrobotcleaner,"it.unibo.ctxRobotCleaner.Evh","sensorEvent,mindEvent").  
%%% -------------------------------------------

