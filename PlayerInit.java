import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlayerInit extends JFrame implements Serializable{
	static Vector<Character> Characters;
	JMenuBar menubar;
	JMenu file, menu, sock;
	JPanel charframe;
	JScrollBar charscroller;
	Socket socket;
	public static int width = 280;
	public static int height = 1000;
	public int port = 8080;
	ImageIcon ic = new ImageIcon("C:\\Users\\BR20039543\\Documents\\DandD\\remove.png");
	Image img = ic.getImage();
	//Image resizedImage = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
	ImageIcon rIcon = new ImageIcon(img);
	public PlayerInit(String header){
		super(header);
		Characters = new Vector<Character>();
	}
	public static <T> Vector<T> rotate(Vector<T> aL, int shift){
		if (aL.size() == 0)
			return aL;

		T element = null;
		for(int i = 0; i < shift; i++){
			// remove first element, add it to back of the Vector
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
					Characters.remove(Characters.indexOf(c));
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
		pi.charframe = new JPanel();
		pi.charframe.setLayout(new GridBagLayout());
		pi.menubar = new JMenuBar();
		pi.menu = new JMenu("menu");
		pi.file = new JMenu("file");
		pi.sock = new JMenu("connect");
		pi.menubar.add(pi.file);
		pi.menubar.add(pi.menu);
		pi.menubar.add(pi.sock);
		pi.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				try{
					pi.socket.close();
				}
				catch(IOException io){
					System.out.println(io.toString());
				}
				finally{
					System.exit(0);
				}
			}
		});
		
		pi.charscroller = new JScrollBar(JScrollBar.VERTICAL);
		JMenuItem reorder = new JMenuItem();
		JMenuItem next = new JMenuItem();
		JMenuItem save = new JMenuItem();
		JMenuItem load = new JMenuItem();
		JMenuItem look = new JMenuItem();
		
		reorder.setText("Reorder");
		reorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Collections.sort(Characters, Collections.reverseOrder());
				pi.reDraw(pi);
            }
        });
		next.setText("Next");
		next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
				rotate(Characters, 1);
				pi.reDraw(pi);
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
						Characters = (Vector<Character>)ois.readObject();
						pi.reDraw(pi);
					}
					catch(ClassCastException cc){
						System.out.println("File unable to be opened.  Please check file type before continuing.");
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
							System.out.println(e.toString());
						}
					}
				}
			}
		});
		look.setText("Connect");
		look.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					JFrame ipDialog = new JFrame("IP Dialog");
					JPanel jp = new JPanel();
					jp.setLayout(new GridLayout(1,1));
					
					JTextField ipaddress = new JTextField("", 20);
					ipaddress.setUI(new JTextFieldHintUI("ip address", Color.gray));
					JTextField portnum = new JTextField("", 10);
					portnum.setUI(new JTextFieldHintUI("host port", Color.gray));
					JButton submit = new JButton("Submit");
					submit.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent act){
							System.out.println("Attempting to connect");
							try{
								pi.socket = new Socket(ipaddress.getText(), Integer.parseInt(portnum.getText()));
								new SocketListener(pi.socket, pi).start();
								System.out.println("Connected Successfully");
							}
							catch(Exception ex){
								System.out.println(ex.toString());
							}
							
						}
					});
					jp.add(ipaddress);
					jp.add(portnum);
					jp.add(submit);
					ipDialog.getContentPane().add(jp);
					ipDialog.pack();
					ipDialog.setVisible(true);
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
				finally{}
			}
		});
		
		pi.menu.add(reorder);
		pi.menu.add(next);
		pi.file.add(save);
		pi.file.add(load);
		pi.sock.add(look);
		pi.charframe.setBackground(new Color(235, 210, 141));
		pi.setJMenuBar(pi.menubar);
		pi.add(pi.charframe);
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
	public void run(){}
}