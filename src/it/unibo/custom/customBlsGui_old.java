package it.unibo.custom;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import it.unibo.qactors.akka.QActor;


public class customBlsGui_old {
public static final boolean ledGuiOn  = true;
public static final boolean ledGuiOff = false;
private static final Dimension smallGui = new Dimension(20, 20);
private static final Dimension largeGui = new Dimension(50, 50);

private static customBlsGui_old curBlsGui  = null;	//singleton
private static customBlsGui_old curLedGui  = null;	//singleton
private static customBlsGui_old curLBtnGui = null;	//singleton

	private QActor myActor;
    private JFrame frm       = new JFrame();
    private JPanel pnl       = new JPanel();
    private JButton btnGui   = new JButton("Click");
    private JPanel ledGui    = new JPanel();
    private int count        = 1;    
    
    //Factory methods
    public static synchronized customBlsGui_old createCustomBlsGui(QActor myActor) {
    	if( curBlsGui == null ) curBlsGui = new customBlsGui_old(myActor);
     	return curBlsGui;
    }
    public static synchronized customBlsGui_old createCustomLedGui(QActor myActor) {
    	if( curLedGui == null ) curLedGui = new customBlsGui_old(myActor, "led");
    	return  curLedGui;
     }
    public static synchronized customBlsGui_old createCustomButtonGui(QActor myActor) {
    	if( curLBtnGui == null ) curLBtnGui =  new customBlsGui_old(myActor, "button");
    	return  curLBtnGui;
    }
    public static void setLed(QActor myActor, String value) {
      	curLedGui.setLedGui( value.equals("on") ? true : false );
    }    
    /*
     * CONSTRUCTORS
     */
    public  customBlsGui_old(QActor myActor) {
    	this.myActor = myActor;
    	initAll();
    }
    public  customBlsGui_old(QActor myActor, String device) {
    	this.myActor = myActor;
    	if( device == "led") initLedGui();
    	else if( device == "button") initButtonGui();
    }
    
    protected void initAll() {
    	initFrame("all");
		initLedGui();
		initButtonGui();
      	System.out.println("CustomBlsGui initAll done   "    );
    }
    
    protected void initFrame(String device) {
     	 pnl.setLayout( new BorderLayout() );
         if( device == "button") {
            pnl.setPreferredSize(new Dimension(140, 100));
        	pnl.add( btnGui, BorderLayout.SOUTH);
            frm.setLocation(150, 100);
        }
        else if( device == "led") {
            pnl.setPreferredSize(new Dimension(140, 100));
        	pnl.add( ledGui, BorderLayout.NORTH); 
            frm.setLocation(350, 100);
        }
        else if( device == "all")  {
          	pnl.setPreferredSize(new Dimension(340, 200));
          	pnl.add(btnGui, BorderLayout.SOUTH);
            pnl.add( ledGui, BorderLayout.NORTH);        	
            frm.setLocation(350, 100);
        }
        frm.add(pnl, BorderLayout.CENTER);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EDIT
        frm.setResizable(false);
        frm.pack();
        frm.setVisible(true);
        System.out.println("CustomBlsGui initFrame done for " + device  );  //muActor could be null
    }
    
    protected void initButtonGui() {
    	initFrame("button");
        btnGui.setPreferredSize(new Dimension(100, 40));
        btnGui.addActionListener(new ActionListener() {
             @Override
            public void actionPerformed(ActionEvent e) {               
                if(myActor!=null) {
                	//myActor.println("actionPerformed " + e.getActionCommand());
                	myActor.emit("local_click", "clicked("+count++ +")");
                }else System.out.println("actionPerformed " + e.getActionCommand());
             }
        });    	  
    }
    
    protected void initLedGui() {
       	initFrame("led");
       	ledGui.setBackground(Color.RED);
       	ledGui.setPreferredSize(smallGui);
       	setLedGui(ledGuiOff); 	
    }
    
    public void setLedGui(boolean on) {
//    	System.out.println("setLedGui " + on);
     	if(on) ledGui.setSize(largeGui);
    	else ledGui.setSize(smallGui);
    	ledGui.repaint();
    }
    
    /*
     * Just to test	  
     */    
    public static void main(String[] args) throws InterruptedException {
    	customBlsGui_old blsGui = customBlsGui_old.createCustomBlsGui(null);  
    	for( int i=1; i<=3; i++) {
			Thread.sleep(1000);
			blsGui.setLedGui(ledGuiOn);
			Thread.sleep(1000);
			blsGui.setLedGui(ledGuiOff);
    	}
    }
}    
/*
CONTENT ASSIST
preferences / java / editor / content assist / advanded. 
Only check in first and second list the option: - Java Proposals
*/