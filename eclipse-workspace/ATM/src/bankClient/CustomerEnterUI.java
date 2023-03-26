package bankClient;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class CustomerEnterUI {
	
	private JFrame signFrame;
	private JPanel numberPanel,passwordPanel,buttonPanel;
	private JLabel userID,password;
	private JTextField number;
	private JPasswordField passwordT;
	private JButton enter,cancel;
	private Socket connection;
	private Box box;
	private ClientUI mainFrame;
	
	public PrintStream serverOut;
	public BufferedReader serverIn;
	
	public CustomerEnterUI() {
		signFrame = new JFrame("Log in");
		userID = new JLabel("    User ID: ");
		password = new JLabel("Password: ");
		number = new JTextField(20);
		passwordT = new JPasswordField(10);
		numberPanel = new JPanel();
		passwordPanel = new JPanel();
		buttonPanel = new JPanel();
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		box = Box.createVerticalBox();
	}
	
	public void launchFrame() {
		enter.setBackground(new Color(240,255,255));
		cancel.setBackground(new Color(240,255,255));
		
		numberPanel.add(userID);
		numberPanel.add(number);
		passwordPanel.add(password);
		passwordPanel.add(passwordT);
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		
		box.add(numberPanel);
		box.add(passwordPanel);
		box.add(buttonPanel);
		
		passwordT.setEchoChar('*');
		passwordT.setColumns(20);
		
		signFrame.add(box);
		
		signFrame.setBounds(600, 300, 100, 100);
		signFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		signFrame.pack();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler(this));
		passwordT.addKeyListener(new EnterKeyHandler(this));
		
		try { 
			connection = new Socket("127.0.0.1", 2020);
			serverIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			serverOut = new PrintStream(connection.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		signFrame.setVisible(true);
	}
	
	private class EnterKeyHandler extends KeyAdapter implements KeyListener{
		
		private CustomerEnterUI cus;
		public EnterKeyHandler(CustomerEnterUI cus) {
			this.cus = cus;
		}
		@Override
		
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				
				String password = new String(passwordT.getPassword());
				serverOut.println(number.getText());
				serverOut.println(password);
				try {
					
					String message = serverIn.readLine();
					
					if(message.equals("Correct")) {
						
						String name = serverIn.readLine();
						
						String[] accounts = serverIn.readLine().split("\t\t");
						
						mainFrame = new ClientUI(cus,name,accounts);
						mainFrame.launchFrame();
						signFrame.dispose();
					}else if(message.equals("Wrong")) {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Wrong UserID or password ! !","Login failure",JOptionPane.WARNING_MESSAGE);
					}else if(message.equals("Same")) { 
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"This ID has already been logged in ! !","Login failure",JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private class EnterHandler implements ActionListener{
		
		private CustomerEnterUI cus;
		public EnterHandler(CustomerEnterUI cus) {
			this.cus = cus;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String password = new String(passwordT.getPassword());
			serverOut.println(number.getText());
			serverOut.println(password);
			try {
				
				String message = serverIn.readLine();
				
				if(message.equals("Correct")) {
					
					String name = serverIn.readLine();
					
					String[] accounts = serverIn.readLine().split("\t\t");
					
					mainFrame = new ClientUI(cus,name,accounts);
					mainFrame.launchFrame();
					signFrame.dispose();
				}else if(message.equals("Wrong")) {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Wrong UserID or password ! !","Login failure",JOptionPane.WARNING_MESSAGE);
				}else if(message.equals("Same")) { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"This ID has already been logged in ! !","Login failure",JOptionPane.WARNING_MESSAGE);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class CancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		CustomerEnterUI customer = new CustomerEnterUI();
		javax.swing.SwingUtilities.invokeLater(new Runnable() { 
            public void run() {
            	customer.launchFrame();
            }
        });
	}
}
