package it.unibo.custom;

import java.text.SimpleDateFormat;  
import java.util.Date;
import it.unibo.qactors.akka.QActor;

public class getClock {  
public static void getTime(QActor qa) {  
    SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");  
    Date date = new Date();  
    String value = formatter.format(date);
    qa.replaceRule("clock(X)","clock("+value+")");  
}  
}