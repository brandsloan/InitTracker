import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//TODO: implements ActionLister
public class InitTracker extends JFrame{
	public InitTracker(String header){
		super(header);
	}
	private static void createAndShowGUI(){
		JFrame it = new InitTracker("InitTracker");
		it.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		it.pack();
		it.setVisible(true);
	}
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}