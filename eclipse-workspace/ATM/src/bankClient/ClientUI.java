package bankClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;

public class ClientUI {
	private CustomerEnterUI customer;
	private JFrame mainFrame;
	private JPanel buttonPanel,accountPanel,displayPanel,balancePanel,overOrInPanel;
	private JButton query,changePass,deposit,withdraw,transfer,quit;
	private JLabel balance,overOrIn,account,customerName;
	private JTextField balanceT,overOrInT;
	private JComboBox<String> accountBox;
	private String[] accounts;
	private String name;
	
	public ClientUI(CustomerEnterUI customer,String name,String[] accounts) {
		this.customer = customer;
		this.name = name;
		this.accounts = accounts;
		balance = new JLabel("Balance: ");
		account = new JLabel("Accounts: ");
		overOrIn = new JLabel("Overdraft:");
		customerName=new JLabel("Name: "+name);
		query = new JButton("Query");
		changePass = new JButton("Change Password");
		deposit = new JButton("Deposit");
		withdraw = new JButton("Withdraw");
		transfer=new JButton("Transfer");
		quit = new JButton("Quit");
		balanceT = new JTextField();
		overOrInT = new JTextField();
		accountPanel = new JPanel();
		buttonPanel = new JPanel();
		displayPanel = new JPanel();
		balancePanel=new JPanel();
		overOrInPanel=new JPanel();
		accountBox = new JComboBox<String>(accounts);
	}
	
	public void launchFrame() {
		query.setBackground(new Color(176,196,222));
		changePass.setBackground(new Color(176,196,222));
		deposit.setBackground(new Color(176,196,222));
		withdraw.setBackground(new Color(176,196,222));
		transfer.setBackground(new Color(176,196,222));
		quit.setBackground(new Color(176,196,222));
		accountBox.setBackground(new Color(240,248,255));
		
		balanceT.setEditable(false);
		balanceT.setColumns(10);
		overOrInT.setEditable(false);
		overOrInT.setColumns(10);
		
		accountBox.setSelectedIndex(0);
		if(accounts[0].equals("Checking Account")) {
			overOrIn.setText("Overdraft:");
		}else if(accounts[0].equals("Savings Account")) {
			overOrIn.setText("Interest Rate:");
		}
		
		buttonPanel.add(query);
		buttonPanel.add(deposit);
		buttonPanel.add(withdraw);
		buttonPanel.add(changePass);
		buttonPanel.add(transfer);
		buttonPanel.add(quit);
		
		accountPanel.add(account);
		accountPanel.add(accountBox);
		
		balancePanel.add(Box.createRigidArea(new Dimension(30,0)));
		balancePanel.add(balance);
		balancePanel.add(balanceT);
		balancePanel.add(Box.createRigidArea(new Dimension(30,0)));
		balancePanel.setLayout(new BoxLayout(balancePanel,BoxLayout.X_AXIS));
		
		overOrInPanel.add(Box.createRigidArea(new Dimension(30,0)));
		overOrInPanel.add(overOrIn);
		overOrInPanel.add(overOrInT);
		overOrInPanel.add(Box.createRigidArea(new Dimension(30,0)));
		overOrInPanel.setLayout(new BoxLayout(overOrInPanel,BoxLayout.X_AXIS));
		
		customerName.setAlignmentX(Component.CENTER_ALIGNMENT);
		displayPanel.add(customerName);
		accountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		displayPanel.add(accountPanel);
		balancePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		displayPanel.add(balancePanel);
		overOrInPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		displayPanel.add(overOrInPanel);
		
		buttonPanel.setLayout(new GridLayout(3,2));
		buttonPanel.setSize(80,200);
		displayPanel.setLayout(new BoxLayout(displayPanel,BoxLayout.Y_AXIS));
		displayPanel.setBounds(20, 20, 5, 5);
		
		
		mainFrame = new JFrame("Welcome "+name);
		mainFrame.setSize(400,250);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		mainFrame.add(displayPanel,BorderLayout.NORTH);
		mainFrame.add(buttonPanel,BorderLayout.SOUTH);
		
		
		query.addActionListener(new QueryHandler());
		quit.addActionListener(new QuitHandler());
		quit.addActionListener(new ButtonHandler());
		deposit.addActionListener(new ButtonHandler());
		deposit.addActionListener(new DepositHandler(customer, this));
		withdraw.addActionListener(new ButtonHandler());
		withdraw.addActionListener(new WithdrawHandler(customer, this));
		changePass.addActionListener(new ButtonHandler());
		changePass.addActionListener(new ChangePassHandler(customer));
		transfer.addActionListener(new ButtonHandler());
		transfer.addActionListener(new transferHandler(customer, this));
		
		mainFrame.setVisible(true);
	}
	
	public String getSelectedAccount() {
		return (String) accountBox.getSelectedItem();
	}
	
	private class QuitHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	private class QueryHandler implements ActionListener{
		private String acct,balance,overIn;
		@Override
		public void actionPerformed(ActionEvent e) {
			
			acct = (String) accountBox.getSelectedItem();
			
			if(acct.equals("Checking Account")) {
				overOrIn.setText("Overdraft:");
				customer.serverOut.println("Query Checking");
			}else if(acct.equals("Savings Account")) {
				overOrIn.setText("Interest Rate:");
				customer.serverOut.println("Query Savings");
			}
			try {	
				balance = customer.serverIn.readLine();
				overIn = customer.serverIn.readLine();
				
				balanceT.setText(balance);
				overOrInT.setText(overIn);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class ButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			overOrInT.setText("");
			balanceT.setText("");
		}
	}
}
