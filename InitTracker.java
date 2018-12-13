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
import java.net.Socket;
import java.net.ServerSocket;

public class InitTracker extends JFrame implements Serializable{
	static ArrayList<Character> Characters;
	JMenuBar menuBar;
	JMenu file, menu, sock;
	JPanel charframe;
	JScrollBar charscroller;
	ServerSocket ss;
	Socket socket;
	ObjectInputStream clientU = null;
	PrintWriter serverU = null;
	public static final int PORT = 8080;
	public static int width = 450;
	public static int height = 1000;
	boolean connect = false;
	ImageIcon ic = new ImageIcon("C:\\Users\\BR20039543\\Documents\\DandD\\remove.png");
	Image img = ic.getImage();
	//Image resizedImage = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
	ImageIcon rIcon = new ImageIcon(img);
	
	public InitTracker(String header){
		super(header);
		Characters = (new ArrayList<Character>());
	}
	public static <T> ArrayList<T> rotate(ArrayList<T> aL, int shift){
		if (aL.size() == 0)
			return aL;

		T element = null;
		for(int i = 0; i < shift; i++){
			element = aL.remove(0);
			aL.add(aL.size(), element);
		}
		return aL;
	}
	public void post(Character c, String command){
		try{
			this.serverU.flush();
			this.serverU.write(command + " " + c.getName() + " " + c.getCurHP() + " " + c.getMaxHP() + " " + c.getInit() + "\n");
			this.serverU.flush();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	public void reDraw(InitTracker it){
		String command = null;
		it.charframe.removeAll();
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.anchor = GridBagConstraints.PAGE_START;
		ActionListener enterPress = new ActionListener(){
            public void actionPerformed(ActionEvent e){
				/*if(it.serverU != null){
					it.post(c, "update");
				}*/
				it.reDraw(it);
				it.revalidate();
				it.repaint();
            }
		};
		for(Character c : Characters){
			gc.gridx = 0;
			gc.gridy = Characters.indexOf(c);
			gc.gridwidth = 1;
		gc.gridheight = 1;
		JTextField NameLine = new JTextField(c.getName());
			NameLine.setMinimumSize(new Dimension(80, 10));
			NameLine.setEditable(true);
			NameLine.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateName();
				}
				public void removeUpdate(DocumentEvent e) {
					updateName();
				}
				public void insertUpdate(DocumentEvent e) {
					updateName();
				}
				public void updateName() { 
					try{
						c.setName(NameLine.getText());
						if(it.serverU != null){
							it.post(c, "update");
						}
					}
					catch(Exception e){}
				}
			});
			NameLine.addActionListener(enterPress);
			it.charframe.add(NameLine, gc);
			
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
						if(it.serverU != null){
							it.post(c, "update");
						}
					}
					catch(Exception e){}
				}
			});
			CurLine.addActionListener(enterPress);
			it.charframe.add(CurLine, gc);
			
			gc.gridx = 2;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField MaxLine = new JTextField(String.valueOf(c.getMaxHP()), 3);
			MaxLine.setMinimumSize(new Dimension(20, 20));
			MaxLine.setEditable(true);
			MaxLine.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateMax();
				}
				public void removeUpdate(DocumentEvent e) {
					updateMax();
				}
				public void insertUpdate(DocumentEvent e) {
					updateMax();
				}
				public void updateMax() { 
					try{
						c.setMaxHP(Integer.parseInt(MaxLine.getText()));
						if(it.serverU != null){
							it.post(c, "update");
						}
					}
					catch(Exception e){}
				}
			});
			CurLine.addActionListener(enterPress);
			it.charframe.add(MaxLine, gc);

			gc.gridx = 3;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JTextField InitLine = new JTextField(String.valueOf(c.getInit()), 3);
			InitLine.setMinimumSize(new Dimension(20, 20));
			InitLine.setEditable(true);
			InitLine.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					updateInit();
				}
				public void removeUpdate(DocumentEvent e) {
					updateInit();
				}
				public void insertUpdate(DocumentEvent e) {
					updateInit();
				}
				public void updateInit() { 
					try{
						c.setInit(Integer.parseInt(InitLine.getText()));
						if(it.serverU != null){
							it.post(c, "update");
						}
					}
					catch(Exception e){}
				}
			});
			InitLine.addActionListener(enterPress);
			it.charframe.add(InitLine, gc);
			
			gc.gridx = 4;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			float curHP = (float)(c.getMaxHP()-c.getCurHP());
			float percentHP = curHP/c.getMaxHP();
			JTextField ColorBar = new JTextField(String.valueOf((int)curHP)+"/"+String.valueOf(c.getMaxHP()));
			ColorBar.setMinimumSize(new Dimension(40, 10));
			if (percentHP <= 0.0)
				ColorBar.setBackground(Color.BLACK);
			else if(percentHP < .25)
				ColorBar.setBackground(Color.RED);
			else if (percentHP < .5)
				ColorBar.setBackground(Color.ORANGE);
			else if (percentHP < .75)
				ColorBar.setBackground(Color.YELLOW);
			else
				ColorBar.setBackground(Color.GREEN);
			ColorBar.setEditable(false);
			it.charframe.add(ColorBar, gc);
			
			gc.gridx = 5;
			gc.gridwidth = 1;
			gc.gridheight = 1;
			
			JButton remove = new JButton("remove");
			remove.setBackground(Color.RED);
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Characters.remove(Characters.indexOf(c));
					if(it.serverU != null){
						it.post(c, "remove");
					}
					it.reDraw(it);
					it.revalidate();
					it.repaint();
					return;
				}
			});
			it.charframe.add(remove, gc);
			
			/*gc.gridx = 6;
			gc.gridy = 0;
			gc.gridheight = 5;
			gc.anchor = GridBagConstraints.FIRST_LINE_END;
			*/
			it.revalidate();
			it.repaint();
		}
	}
	public void addCharacter(int max, int cur, String name, int init){
		Character c = new Character(max, cur, name, init);
		Characters.add(c);
		if(this.serverU != null){
			this.post(c, "add");
		}
		this.reDraw(this);
	}
	private static void createAndShowGUI(){
		InitTracker it = new InitTracker("InitTracker");
		it.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		it.setPreferredSize(new Dimension(width, height));
		it.charframe = new JPanel();
		it.charframe.setLayout(new GridBagLayout());
		it.menuBar = new JMenuBar();
		it.menu = new JMenu("menu");
		it.file = new JMenu("file");
		it.sock = new JMenu("connect");
		it.menuBar.add(it.file);
		it.menuBar.add(it.menu);
		it.menuBar.add(it.sock);
		it.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				try{
					it.socket.close();
					it.clientU.close();
					it.serverU.close();
					it.ss.close();
				}
				catch(IOException io){
					System.out.println(io.toString());
				}
				finally{
					System.exit(0);
				}
			}
		});
		
		it.charscroller = new JScrollBar(JScrollBar.VERTICAL);
		JMenuItem addChar = new JMenuItem();
		JMenuItem loadChar = new JMenuItem();
		JMenuItem reorder = new JMenuItem();
		JMenuItem next = new JMenuItem();
		JMenuItem save = new JMenuItem();
		JMenuItem load = new JMenuItem();
		JMenuItem look = new JMenuItem();
		addChar.setText("Add Character");
		addChar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFrame AddFrame = new JFrame("Character");
				JPanel grid = new JPanel();
				grid.setLayout(new GridLayout(3, 2));
				
				JTextField cname = new JTextField("", 20);
				cname.setUI(new JTextFieldHintUI("Name", Color.gray));
				
				JTextField ccur = new JTextField("", 3);
				ccur.setUI(new JTextFieldHintUI("Missing Health", Color.gray)); 
				
				JTextField cmax = new JTextField("", 3);
				cmax.setUI(new JTextFieldHintUI("Maximum Health", Color.gray)); 
				
				JTextField cinit = new JTextField("", 2);
				cinit.setUI(new JTextFieldHintUI("Init", Color.gray)); 
				
				JButton submit = new JButton("Submit");
				submit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						try{
							it.addCharacter(Integer.parseInt(cmax.getText()), Integer.parseInt(ccur.getText()), cname.getText(), Integer.parseInt(cinit.getText()));
						}
						catch (Exception e){
							JOptionPane.showMessageDialog(it, e.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				JButton charSave = new JButton("Save Initiative");
				charSave.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ae){
						String sc = "ThisInit";
						FileOutputStream fos = null;
						ObjectOutputStream oos = null;
						JFileChooser jfc = new JFileChooser();
						jfc.setCurrentDirectory(new File("/home/Documents/"));
						int ret = jfc.showSaveDialog(null);
						if(ret == JFileChooser.APPROVE_OPTION){
							try{
								fos = new FileOutputStream(jfc.getSelectedFile()+".char");
								oos = new ObjectOutputStream(fos);
								oos.writeObject(new Character(Integer.parseInt(cmax.getText()), Integer.parseInt(ccur.getText()), cname.getText(), Integer.parseInt(cinit.getText())));
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
				grid.add(cname);
				grid.add(ccur);
				grid.add(cmax);
				grid.add(cinit);
				grid.add(submit);
				grid.add(charSave);
				
				AddFrame.getContentPane().add(grid);
				AddFrame.pack();
				AddFrame.setVisible(true);
            }
        });
		loadChar.setText("Load Character");
		loadChar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				String sc = "CharLoad";
				FileInputStream fis = null;
				ObjectInputStream ois = null;
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Load Character");
				jfc.setCurrentDirectory(new File("/home/Documents/"));
				int ret = jfc.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try{
						fis = new FileInputStream(jfc.getSelectedFile());
						ois = new ObjectInputStream(fis);
						Character c = (Character)ois.readObject();
						Characters.add(c);
						if(it.serverU != null){
							it.post(c, "add");
						}
						it.reDraw(it);
					}
					catch(ClassCastException cc){
						System.out.printf("File not of .char format.\n  Please check file type before continuing.\n");
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
					finally{
						try{
							ois.close();
							fis.close();
						}
						catch (Exception e){}
					}
				}				
			}
		});
		reorder.setText("Reorder");
		reorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collections.sort(Characters, Collections.reverseOrder());
				if(it.serverU != null){
					it.post(new Character(), "reorder");
				}
				it.reDraw(it);
            }
        });
		next.setText("Next");
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				rotate(Characters, 1);
				if(it.serverU != null){
					it.post(new Character(), "next");
				}
				it.reDraw(it);
			}
        });
		save.setText("Save Initiative");
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
						catch (Exception e){}
					}
				}
			}
		});
		load.setText("Load Initiative");
		load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				String sc = "ThisInit";
				FileInputStream fis = null;
				ObjectInputStream ois = null;
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Load Initiative");
				jfc.setCurrentDirectory(new File("/home/Documents/"));
				int ret = jfc.showSaveDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					try{
						fis = new FileInputStream(jfc.getSelectedFile());
						ois = new ObjectInputStream(fis);
						//Characters.clear();
						Characters = (ArrayList<Character>)ois.readObject();
						for(Character c : Characters){
							if(it.serverU != null){
								it.post(c, "add");
							}
						}
						it.reDraw(it);
					}
					catch(ClassCastException cc){
						System.out.printf("File not of .init format.\n  Please check file type before continuing.\n");
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
					finally{
						try{
							ois.close();
							fis.close();
						}
						catch (Exception e){}
					}
				}
			}
		});
		look.setText("Connect");
		look.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				it.connect = true;
				if(it.connect == true){
					try{						
						System.out.println("Waiting for connection");
						it.ss = new ServerSocket(0);
						JOptionPane.showMessageDialog(it, "Hosting on port: " + it.ss.getLocalPort());
						it.socket = it.ss.accept();
						System.out.println("Accepting");
						//it.clientU = new ObjectInputStream(it.socket.getInputStream());
						it.serverU = new PrintWriter(it.socket.getOutputStream(), true);
						look.setEnabled(false);
					}
					catch(IOException e){
						System.out.print(e.toString());
					}
					finally{
						System.out.println("Connection found");
					}
				}
			}
		});
		
		it.menu.add(addChar);
		it.file.add(loadChar);
		it.menu.add(reorder);
		it.menu.add(next);
		it.file.add(save);
		it.file.add(load);
		it.sock.add(look);
		it.setJMenuBar(it.menuBar);
		it.getContentPane().setLayout(new GridLayout());
		it.charframe.setBackground(new Color(235, 210, 141));
		it.add(it.charframe);
		it.pack();
		it.setVisible(true);	
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}