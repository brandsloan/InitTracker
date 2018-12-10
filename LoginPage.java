import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.*;

public class LoginPage extends JFrame{
	public String[] userTypes = {"DM", "Player"};
	JPanel mainframe;
	public LoginPage(String header){
		super(header);
		
	}
	public static void createAndShowGUI(){
		JComboBox cb;
		JTextField ip;
		JButton  con;
		
		LoginPage lp = new LoginPage("Login Page");
		lp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lp.setPreferredSize(new Dimension(400, 400));
		
		ip = new JTextField();
		ip.setEditable(false);
		cb = new JComboBox(lp.userTypes);
		con = new JButton("Connect");
		
		lp.mainframe = new JPanel();
		lp.mainframe.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.PAGE_START;
		gc.ipadx = 40;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;
		
		JLabel roleLabel = new JLabel("Role: ");
		lp.mainframe.add(roleLabel, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;
		
		JLabel serverLabel = new JLabel("IP Server: ");
		lp.mainframe.add(serverLabel, gc);
		
		gc.gridx = 1;
		gc.gridy = 0;
		
		cb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				try{
					String role = cb.getSelectedItem().toString();
					if (role.equals("DM") == true)
						ip.setEditable(false);
					else
						ip.setEditable(true);
				}
				catch(Exception e){
					return;
				}
			}
		});
		lp.mainframe.add(cb, gc);

		gc.gridx = 1;
		gc.gridy = 1;
		
		ip.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
			}
		});
		lp.mainframe.add(ip, gc);
		
		gc.gridx = 0;
		gc.gridy = 2;
		gc.gridwidth = 2;
		lp.mainframe.add(con, gc);
		
		lp.add(lp.mainframe);
		lp.pack();
		lp.setVisible(true);
	}
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				createAndShowGUI();
			}
		});
	}
}