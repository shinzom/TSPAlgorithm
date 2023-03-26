package bankClient;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class transferHandler implements ActionListener {
	
	private JFrame frame;
	private JButton enter,cancel;
	private JPanel buttonPanel,eventPanel;
	private JTextField showCurrentBalance,showMoney,showID;
	private JLabel currentBalance,money,ID,accountName;
	private String acct,balance;
	private ClientUI mainUI;
	private CustomerEnterUI customer;
	private Font font;
	
	public transferHandler(CustomerEnterUI customer,ClientUI mainUI) {
		this.mainUI = mainUI;
		this.customer = customer;
		font = new Font("Georgia",Font.PLAIN,20);
		frame=new JFrame("Transfer");
		enter=new JButton("Enter");
		cancel=new JButton("Cancel");
		buttonPanel=new JPanel();
		eventPanel=new JPanel(new GridLayout(3,2,10,10));
		showCurrentBalance=new JTextField(20);
		showMoney=new JTextField(20);
		showID=new JTextField(20);
		currentBalance=new JLabel("Your current balance:");
		money=new JLabel("Money you want to transfer:");
		ID=new JLabel("User ID you want to transfer:");
		accountName=new JLabel();
		
        showCurrentBalance.setEditable(false);
        
		cancel.addActionListener(new CancelHandler());
		frame.addWindowListener(new CloseWidget());
		enter.addActionListener(new EnterHandler());
		showID.addKeyListener(new EnterKeyHandler());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		acct = (String) mainUI.getSelectedAccount();
		accountName.setText(acct);
		
		if(acct.equals("Checking Account")) {
			customer.serverOut.println("Withdraw Checking");
		}else if(acct.equals("Savings Account")) {
			customer.serverOut.println("Withdraw Savings");
		}
		try {
			
			balance = customer.serverIn.readLine();
			
			launchFrame();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void launchFrame() {
		
		frame.setLayout(new FlowLayout());
		
		accountName.setFont(font);
		showCurrentBalance.setText(balance);
		showMoney.setText("");
		
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		
		eventPanel.add(currentBalance);
		eventPanel.add(showCurrentBalance);
		eventPanel.add(money);
		eventPanel.add(showMoney);
		eventPanel.add(ID);
		eventPanel.add(showID);
		eventPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		frame.add(accountName);
		frame.add(eventPanel);
		frame.add(buttonPanel);
		
		frame.setBounds(200,300,450,250);
		frame.setVisible(true);
	}
	
	private class CloseWidget extends WindowAdapter implements WindowListener{
		@Override
		public void windowClosing(WindowEvent e) {
			
			customer.serverOut.println("Cancel");
			
			frame.dispose();
		}
	}
	
	private class EnterHandler implements ActionListener{
		
		private String message;
		private Double getMoney;
		//private String transferID;
		@Override
		public void actionPerformed(ActionEvent e) {
			message = showMoney.getText();
			try {
				
				getMoney = Double.parseDouble(message);
				customer.serverOut.println(getMoney);
				
				if(customer.serverIn.readLine().equals("Success")) {
					frame.dispose();
				}else { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"You do not have enough money! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
				}
			}catch(Exception e1) { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please input numbers ! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
			}
           
			/*customer1=new CustomerEnterUI();
			try {
				
				getMoney = Double.parseDouble(message);
				customer1.serverOut.println(getMoney);
				
				if(customer1.serverIn.readLine().equals("Success")) {
					
					frame.dispose();
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Something wrong! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
				}
			}catch(Exception e2) { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please input numbers ! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
			}*/
		}
	}
	private class EnterKeyHandler extends KeyAdapter implements KeyListener{
		
		private String message;
		private Double getMoney;
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				
				message = showMoney.getText();
				try {
					
					getMoney = Double.parseDouble(message);
					customer.serverOut.println(getMoney);
					
					if(customer.serverIn.readLine().equals("Success")) {
						
						frame.dispose();
					}else { 
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"You do not have enough money! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception e1) { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please input numbers ! !","Transfer failure",JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	private class CancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			customer.serverOut.println("Cancel");
			
			frame.dispose();
		}
	}
}

