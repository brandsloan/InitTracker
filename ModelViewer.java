import java.awt.*
import java.awt.event.*;
import javax.swing.*;

public class ModelViewer extends JFrame{
	public ModelViewer(Container pane){
		super("Initiative Tracker");
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		Jbutton button = new JButton("STUFF");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(button);
	}
}