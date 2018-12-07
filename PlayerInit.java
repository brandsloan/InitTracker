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

public class PlayerInit extends JFrame implements Serializable{
	static ArrayList<Character> Characters;
	JPanel mainframe;
	JPanel charframe;
	JScrollBar charscroller;
	public static int width = 280;
	public static int height = 1000;
	ImageIcon ic = new ImageIcon("C:\\Users\\BR20039543\\Documents\\DandD\\remove.png");
	Image img = ic.getImage();
	//Image resizedImage = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
	ImageIcon rIcon = new ImageIcon(img);
	public PlayerInit(String header){
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
	public void reDraw(PlayerInit pi){
		pi.charframe.removeAll();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.PAGE_START;
		ActionListener enterPress = new ActionListener(){
            public void actionPerformed(ActionEvent e){
				pi.reDraw(pi);
            }
		};
		for(Character c : Characters){
			gc.gridx = 0;
			gc.gridy = Characters.indexOf(c);
			gc.gridwidth = 1;
			gc.gridheight = 1;

			JTextField NameLine = new JTextField(c.getName());
			NameLine.setMinimumSize(new Dimension(80, 10));
			NameLine.setEditable(false);
			pi.charframe.add(NameLine, gc);
			
			gc.gridx = 1;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField CurLine = new JTextField(String.valueOf(c.getCurHP()), 3);
			CurLine.setMinimumSize(new Dimension(20, 20));
			CurLine.setEditable(true);
			CurLine.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateCur();
				}
				public void removeUpdate(DocumentEvent e) {
					updateCur();
				}
				public void insertUpdate(DocumentEvent e) {
					updateCur();
				}
				public void updateCur() { 
					try{
						c.setCurHP(Integer.parseInt(CurLine.getText()));
					}
					catch(Exception e){}
				}
			});
			CurLine.addActionListener(enterPress);
			pi.charframe.add(CurLine, gc);
			

			gc.gridx = 2;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField InitLine = new JTextField(String.valueOf(c.getInit()), 3);
			InitLine.setMinimumSize(new Dimension(20, 20));
			InitLine.setEditable(false);
			pi.charframe.add(InitLine, gc);
			
			gc.gridx = 3;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			float curHP = (float)(c.getMaxHP()-c.getCurHP());
			float percentHP = curHP/c.getMaxHP();
			JTextField ColorBar = new JTextField("               ");
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
			pi.charframe.add(ColorBar, gc);
			
			gc.gridx = 4;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JButton remove = new JButton("X");
			remove.setBackground(Color.RED);
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Characters.remove(gc.gridy);
					pi.reDraw(pi);
					pi.revalidate();
					pi.repaint();
					return;
				}
			});
			pi.charframe.add(remove, gc);
			
			/*gc.gridx = 5;
			gc.gridy = 0;
			gc.gridheight = 5;
			gc.anchor = GridBagConstraints.FIRST_LINE_END;
			*/
			pi.revalidate();
			pi.repaint();
		}
	}

	private static void createAndShowGUI(){
		PlayerInit pi = new PlayerInit("PlayerInit");
		pi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pi.setPreferredSize(new Dimension(width, height));
		pi.mainframe = new JPanel();
		pi.mainframe.setLayout(new BoxLayout(pi.mainframe, BoxLayout.X_AXIS));
		pi.charframe = new JPanel();
		pi.charframe.setLayout(new GridBagLayout());
		pi.charscroller = new JScrollBar(JScrollBar.VERTICAL);
		JButton reorder = new JButton();
		JButton next = new JButton();
		JButton save = new JButton();
		JButton load = new JButton();
		
		reorder.setText("Reorder");
		reorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collections.sort(Characters, Collections.reverseOrder());
				pi.charframe.removeAll();
				pi.reDraw(pi);
				pi.charframe.revalidate();
				pi.charframe.repaint();
            }
        });
		next.setText("Next");
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				rotate(Characters, 1);
				pi.charframe.removeAll();
				pi.reDraw(pi);
				pi.charframe.revalidate();
				pi.charframe.repaint();
			}
        });
		save.setText("Save");
		save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
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
		load.setText("Load");
		load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
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
						pi.reDraw(pi);
					}
					catch(ClassCastException cc){
						System.out.println("File unable to be opened.%n  Please check file type before continuing.");
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
		pi.mainframe.add(reorder);
		pi.mainframe.add(next);
		pi.mainframe.add(save);
		pi.mainframe.add(load);
		pi.getContentPane().setLayout(new GridLayout());
		JSplitPane splitPane = new JSplitPane();
		pi.getContentPane().add(splitPane);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(20);
		splitPane.setTopComponent(pi.mainframe);
		splitPane.setBottomComponent(pi.charframe);
		pi.charframe.setBackground(new Color(235, 210, 141));
		pi.mainframe.setBackground(new Color(235, 210, 141));
		pi.pack();
		pi.setVisible(true);
	}
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}