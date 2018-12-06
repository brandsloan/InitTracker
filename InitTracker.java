import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.io.*;

public class InitTracker extends JFrame implements Serializable{
	static ArrayList<Character> Characters;
	JPanel mainframe;
	JPanel charframe;
	JScrollBar charscroller;
	public static int width = 400;
	public static int height = 1000;
	ImageIcon ic = new ImageIcon("C:\\Users\\BR20039543\\Documents\\DandD\\remove.png");
	Image img = ic.getImage();
	//Image resizedImage = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
	ImageIcon rIcon = new ImageIcon(img);
	
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
	public void reDraw(InitTracker it){
		it.charframe.removeAll();
		for(Character c : Characters){
			GridBagConstraints gc = new GridBagConstraints();
			gc.fill = GridBagConstraints.BOTH;
			gc.anchor = GridBagConstraints.FIRST_LINE_START;
			gc.gridx = 0;
			gc.gridy = Characters.indexOf(c);
			gc.gridwidth = 1;
			gc.gridheight = 1;

			JTextField NameLine = new JTextField(c.getName(), 12);
			NameLine.setMinimumSize(new Dimension(80, 10));
			NameLine.setEditable(false);
			it.charframe.add(NameLine, gc);
			
			gc.gridx = 1;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField CurLine = new JTextField(String.valueOf(c.getCurHP()), 4);
			CurLine.setMinimumSize(new Dimension(40, 10));
			CurLine.setEditable(true);
			CurLine.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				//updateCur();
			}
			public void removeUpdate(DocumentEvent e) {
				//updateCur();
			}
			public void insertUpdate(DocumentEvent e) {
				updateCur();
			}
            public void updateCur() {
				System.out.println("Updating");
				c.setCurHP(Integer.parseInt(CurLine.getText()));
				it.reDraw(it);
			  }
			});
			it.charframe.add(CurLine, gc);

			gc.gridx = 2;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField InitLine = new JTextField(String.valueOf(c.getInit()), 4);
			InitLine.setMinimumSize(new Dimension(40, 10));
			InitLine.setEditable(false);
			it.charframe.add(InitLine, gc);
			
			gc.gridx = 3;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			float curHP = (float)(c.getMaxHP()-c.getCurHP());
			float percentHP = curHP/c.getMaxHP();
			JTextField ColorBar = new JTextField(String.valueOf((int)curHP)+"/"+String.valueOf(c.getMaxHP()));
			ColorBar.setMinimumSize(new Dimension(40, 10));
			if(percentHP < .25)
				ColorBar.setBackground(Color.RED);
			else if (percentHP < .5)
				ColorBar.setBackground(Color.ORANGE);
			else if (percentHP < .75)
				ColorBar.setBackground(Color.YELLOW);
			else
				ColorBar.setBackground(Color.GREEN);
			ColorBar.setEditable(false);
			it.charframe.add(ColorBar, gc);
			
			gc.gridx = 4;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JButton remove = new JButton("remove");
			remove.setBackground(Color.RED);
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					//Characters.set(gc.gridy, null);
					Characters.remove(gc.gridy);
					it.reDraw(it);
					it.revalidate();
					it.repaint();
					return;
				}
			});
			it.charframe.add(remove, gc);
			
			/*gc.gridx = 5;
			gc.gridy = 0;
			gc.gridheight = 5;
			gc.anchor = GridBagConstraints.FIRST_LINE_END;
			*/
			it.revalidate();
			it.repaint();
		}
	}
	public void addCharacter(int ac, int max, int cur, String name, int init){
		Character c = new Character(ac, max, cur, name, init);
		Characters.add(c);

		this.reDraw(this);
	}
	private static void createAndShowGUI(){
		InitTracker it = new InitTracker("InitTracker");
		it.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		it.setPreferredSize(new Dimension(width, height));
		it.mainframe = new JPanel();
		it.mainframe.setLayout(new BoxLayout(it.mainframe, BoxLayout.Y_AXIS));
		it.charframe = new JPanel();
		it.charframe.setLayout(new GridBagLayout());
		it.charscroller = new JScrollBar(JScrollBar.VERTICAL);
		JButton addChar = new JButton();
		JButton reorder = new JButton();
		JButton next = new JButton();
		JButton save = new JButton();
		JButton load = new JButton();
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
                Collections.sort(Characters, Collections.reverseOrder());
				it.charframe.removeAll();
				//for(Character c : Characters){
				it.reDraw(it);
				//}
				it.charframe.revalidate();
				it.charframe.repaint();
            }
        });
		next.setText("Next");
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				rotate(Characters, 1);
				it.charframe.removeAll();
				//for(Character c : Characters){
				it.reDraw(it);
				//}
				it.charframe.revalidate();
				it.charframe.repaint();
			}
        });
		save.setText("Save Initiative");
		save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				System.out.println("Saving");
				String sc = "ThisInit";
				FileOutputStream fos = null;
				ObjectOutputStream oos = null;
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File("/home/Documents/"));
				int ret = jfc.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try{
						fos = new FileOutputStream(jfc.getSelectedFile()+".init");
						oos = new ObjectOutputStream(fos);
						//for(Character c: Characters)
						//	oos.writeObject(c);
						oos.writeObject(Characters);
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
					finally{
						try{
							oos.close();
							fos.close();
						}
						catch (Exception e){
							
						}
					}
				}
			}
		});
		load.setText("Load Initiative");
		load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				System.out.println("Loading");
				String sc = "ThisInit";
				FileInputStream fis = null;
				ObjectInputStream ois = null;
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Load");
				jfc.setCurrentDirectory(new File("/home/Documents/"));
				int ret = jfc.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try{
						fis = new FileInputStream(jfc.getSelectedFile());
						ois = new ObjectInputStream(fis);
						Characters = (ArrayList<Character>)ois.readObject();
						it.reDraw(it);
					}
					catch(ClassCastException cc){
						System.out.println("File not of .init format.%n  Please check file type before continuing.");
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
					finally{
						try{
							ois.close();
							fis.close();
						}
						catch (Exception e){
							
						}
					}
				}
			}
		});
		it.mainframe.add(addChar);
		it.mainframe.add(reorder);
		it.mainframe.add(next);
		it.mainframe.add(save);
		it.mainframe.add(load);
		it.getContentPane().setLayout(new GridLayout());
		JSplitPane splitPane = new JSplitPane();
		it.getContentPane().add(splitPane);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(130);
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