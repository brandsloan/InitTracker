import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class InitTracker extends JFrame{
	static ArrayList<Character> Characters;
	JPanel mainframe;
	JPanel charframe;
	public static int width = 400;
	public static int height = 1000;
	ImageIcon ic = new ImageIcon("C:\\Users\\BR20039543\\Documents\\DandD\\remove.png");
	Image img = ic.getImage();
	Image resizedImage = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
	ImageIcon rIcon = new ImageIcon(resizedImage);
	
	public InitTracker(String header){
		super(header);
		Characters = new ArrayList<Character>();
	}
	public static <T> ArrayList<T> rotate(ArrayList<T> aL, int shift){
		if (aL.size() == 0)
			return aL;

		T element = null;
		for(int i = 0; i < shift; i++){
			// remove first element, add it to back of the ArrayList
			element = aL.remove(0);
			aL.add(aL.size(), element);
		}
		return aL;
	}
	public void reDraw(int ac, int max, int cur, String name, int init){
		JTextField NameLine = new JTextField(name);
		NameLine.setEditable(false);
		this.charframe.add(NameLine);
		
		JTextField CurLine = new JTextField(String.valueOf(cur));
		CurLine.setEditable(true);
		this.charframe.add(CurLine);

		JTextField InitLine = new JTextField(String.valueOf(init));
		InitLine.setEditable(false);
		this.charframe.add(InitLine);
		
		JButton remove = new JButton();
		remove.setIcon(this.rIcon);
		remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				System.out.printf("remove %d", 1);
			}
		});
		this.charframe.add(remove);
		
		float curHP = (float)(max-cur);
		float percentHP = curHP/max;
		JTextField ColorBar = new JTextField(String.valueOf(curHP)+"/"+String.valueOf(max));
		if(percentHP < .25)
			ColorBar.setBackground(Color.RED);
		else if (percentHP < .5)
			ColorBar.setBackground(Color.ORANGE);
		else if (percentHP < .75)
			ColorBar.setBackground(Color.YELLOW);
		else
			ColorBar.setBackground(Color.GREEN);
		ColorBar.setEditable(false);
		this.charframe.add(ColorBar);
		
		this.revalidate();
		this.repaint();
	}
	public void addCharacter(int ac, int max, int cur, String name, int init){
		Character C = new Character(ac, max, cur, name, init);
		Characters.add(C);

		this.reDraw(ac, max, cur, name, init);
	}
	private static void createAndShowGUI(){
		InitTracker it = new InitTracker("InitTracker");
		it.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		it.setPreferredSize(new Dimension(width, height));
		it.mainframe = new JPanel();
		it.mainframe.setLayout(new BoxLayout(it.mainframe, BoxLayout.Y_AXIS));
		it.charframe = new JPanel();
		it.charframe.setLayout(new GridLayout(0, 5));
		JButton addChar = new JButton();
		JButton reorder = new JButton();
		JButton next = new JButton();
		addChar.setText("Add Character (+)");
		addChar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame AddFrame = new JFrame("Character");
				JPanel grid = new JPanel();
				grid.setLayout(new GridLayout());
				
				JTextField cname = new JTextField("NAME", 20);
				JTextField ccur = new JTextField("Missing Health", 3);
				JTextField cmax = new JTextField("Maximum Health", 3);
				JTextField cac = new JTextField("AC", 2);
				JTextField cinit = new JTextField("Initiative", 2);
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						try{
							it.addCharacter(Integer.parseInt(cac.getText()), Integer.parseInt(cmax.getText()), Integer.parseInt(ccur.getText()), cname.getText(), Integer.parseInt(cinit.getText()));
							//AddFrame.dispose();
						}
						catch (Exception e){
							JOptionPane.showMessageDialog(it, e.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				grid.add(cname);
				grid.add(ccur);
				grid.add(cmax);
				grid.add(cac);
				grid.add(cinit);
				grid.add(submit);
				
				AddFrame.getContentPane().add(grid);
				AddFrame.pack();
				AddFrame.setVisible(true);
            }
        });
		reorder.setText("Reorder");
		reorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collections.sort(it.Characters, Collections.reverseOrder());
				it.charframe.removeAll();
				for(Character c : it.Characters){
					it.reDraw(c.getAC(), c.getMaxHP(), c.getCurHP(), c.getName(), c.getInit());
				}
				it.charframe.revalidate();
				it.charframe.repaint();
            }
        });
		next.setText("Next");
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				rotate(it.Characters, 1);
				it.charframe.removeAll();
				for(Character c : it.Characters){
					it.reDraw(c.getAC(), c.getMaxHP(), c.getCurHP(), c.getName(), c.getInit());
				}
				it.charframe.revalidate();
				it.charframe.repaint();
			}
        });
		it.mainframe.add(addChar);
		it.mainframe.add(reorder);
		it.mainframe.add(next);
		it.getContentPane().setLayout(new GridLayout());
		JSplitPane splitPane = new JSplitPane();
		it.getContentPane().add(splitPane);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(height/5);
		splitPane.setTopComponent(it.mainframe);
		splitPane.setBottomComponent(it.charframe);
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