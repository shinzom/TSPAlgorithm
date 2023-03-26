package bankClient;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class WithdrawHandler implements ActionListener {
	private JFrame withdrawFrame;
	private JButton enter,cancel;
	private JPanel buttonPanel,eventPanel;
	private JTextField showBalance,money;
	private JLabel accountName,balanceLabel,withdraw;
	private String acct,balance;
	private ClientUI mainUI;
	private CustomerEnterUI customer;
	private Font font;
	
	public WithdrawHandler(CustomerEnterUI customer,ClientUI mainUI) {
		this.mainUI = mainUI;
		this.customer = customer;
		withdrawFrame = new JFrame("Withdraw");
		font = new Font("Georgia",Font.PLAIN,20);
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		accountName = new JLabel();
		balanceLabel = new JLabel("Your current balance: ");
		withdraw = new JLabel("withdraw");
		showBalance = new JTextField(20);
		money = new JTextField(20);
		eventPanel = new JPanel(new GridLayout(2,2,10,10));
		buttonPanel = new JPanel();
		
		showBalance.setEditable(false);
		
		cancel.addActionListener(new CancelHandler());
		withdrawFrame.addWindowListener(new CloseWidget());
		enter.addActionListener(new EnterHandler());
		money.addKeyListener(new EnterKeyHandler());
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
		
		withdrawFrame.setLayout(new FlowLayout());
		
		accountName.setFont(font);
		showBalance.setText(balance);
		money.setText("");
		
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		eventPanel.add(balanceLabel);
		eventPanel.add(showBalance);
		eventPanel.add(withdraw);
		eventPanel.add(money);
		
		eventPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		
		withdrawFrame.add(accountName);
		withdrawFrame.add(eventPanel);
		withdrawFrame.add(buttonPanel);
		
		withdrawFrame.setBounds(200,300,450,250);
		withdrawFrame.setVisible(true);
	}
	
	private class CloseWidget extends WindowAdapter implements WindowListener{
		@Override
		public void windowClosing(WindowEvent e) {
			
			customer.serverOut.println("Cancel");
			
			withdrawFrame.dispose();
		}
	}
	
	private class EnterHandler implements ActionListener{
		
		private String message;
		private Double getMoney;
		@Override
		public void actionPerformed(ActionEvent e) {
			
			message = money.getText();
			try {
				
				getMoney = Double.parseDouble(message);
				customer.serverOut.println(getMoney);
				
				if(customer.serverIn.readLine().equals("Success")) {
					
					withdrawFrame.dispose();
				}else { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"You do not have enough money! !","Withdraw failure",JOptionPane.WARNING_MESSAGE);
				}
			}catch(Exception e1) { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please input numbers ! !","Withdraw failure",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	private class EnterKeyHandler extends KeyAdapter implements KeyListener{
		
		private String message;
		private Double getMoney;
		public void keyPressed(KeyEvent e) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				
				message = money.getText();
				try {
					
					getMoney = Double.parseDouble(message);
					customer.serverOut.println(getMoney);
					
					if(customer.serverIn.readLine().equals("Success")) {
						
						withdrawFrame.dispose();
					}else { 
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"You do not have enough money! !","Withdraw failure",JOptionPane.WARNING_MESSAGE);
					}
				}catch(Exception e1) { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please input numbers ! !","Withdraw failure",JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	private class CancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			customer.serverOut.println("Cancel");
			
			withdrawFrame.dispose();
		}
	}
}
