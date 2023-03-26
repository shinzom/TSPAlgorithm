package bankServer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import bankServer.banking.domain.Bank;
import bankServer.banking.domain.CheckingAccount;
import bankServer.banking.domain.Customer;
import bankServer.banking.domain.SavingsAccount;

public class CreateHandler implements ActionListener {
	private ServerNet server;
	private Bank bank;
	private Customer customer;
	private JFrame createFrame;
	JLabel userID,password,again,lastName,firstName,checking,saving,cbalance,sbalance,interestRate,overdraft;
	JTextField ID,lastText,firstText,checkBalance,checkOver,savingRate,savingBalance;
	JPasswordField passText,againText;
	JPanel userPanel,checkPanel,savingPanel,enterPanel;
	JButton enter,cancel;
	Box box;
	
	public CreateHandler() {
		server =ServerNet.getServer();
		bank = Bank.getBank();
		createFrame = new JFrame("Create a new Customer");
		userID = new JLabel("Please enter your User ID:");
		password = new JLabel("Please enter the password:");
		again = new JLabel("Please enter again:");
		lastName = new JLabel("Please enter your last name:");
		firstName = new JLabel("Please enter your first name:");
		checking = new JLabel("Create Checking Account:  ");
		saving = new JLabel("Create Saving Account:  ");
		cbalance = new JLabel("Balance:");
		sbalance = new JLabel("Balance:");
		interestRate = new JLabel("Interest Rate:");
		overdraft = new JLabel("Overdraft:");
		ID = new JTextField(20);
		passText = new JPasswordField(20);
		againText = new JPasswordField(20);
		lastText = new JTextField(20);
		firstText = new JTextField(20);
		checkOver = new JTextField(10);
		checkBalance = new JTextField(10);
		savingRate = new JTextField(10);
		savingBalance = new JTextField(10);
		enterPanel = new JPanel();
		userPanel = new JPanel(new GridLayout(5,2,0,10));
		checkPanel = new JPanel(new GridLayout(5,1));
		savingPanel = new JPanel(new GridLayout(5,1));
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		box = Box.createVerticalBox();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
	}

	public void actionPerformed(ActionEvent e) {
		ID.setText("");;
		passText.setText("");;
		againText.setText("");;
		lastText.setText("");
		firstText.setText("");
		checkOver.setText("");
		checkBalance.setText("");
		savingRate.setText("");
		savingBalance.setText("");
		
		passText.setEchoChar('*');
		passText.setColumns(20);
		againText.setEchoChar('*');
		againText.setColumns(20);
		
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		checkPanel.add(checking);
		checkPanel.add(cbalance);
		checkPanel.add(checkBalance);
		checkPanel.add(overdraft);
		checkPanel.add(checkOver);
		
		savingPanel.add(saving);
		savingPanel.add(sbalance);
		savingPanel.add(savingBalance);
		savingPanel.add(interestRate);
		savingPanel.add(savingRate);
		
		checkPanel.setBorder(BorderFactory.createEmptyBorder(10,200,10,200));
		savingPanel.setBorder(BorderFactory.createEmptyBorder(10,200,10,200));
		
		userPanel.add(userID);
		userPanel.add(ID);
		
		userPanel.add(password);
		userPanel.add(passText);
		
		userPanel.add(again);
		userPanel.add(againText);
		
		userPanel.add(firstName);
		userPanel.add(firstText);
		
		userPanel.add(lastName);
		userPanel.add(lastText);
		userPanel.setBorder(BorderFactory.createEmptyBorder(20, 200, 20, 200));

		box.add(userPanel);
		box.add(savingPanel);
		box.add(checkPanel);
		box.add(enterPanel);
		
		createFrame.add(box,BorderLayout.CENTER);
		
		createFrame.setBounds(200,300,800,550);
		createFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{
		private double cBalance;
		private double cOver;
		private double sBalance;
		private double sRate;
		
		public void actionPerformed(ActionEvent e) {
			boolean same = false;
			boolean hasacct = false;
			String user = ID.getText();
			
			if(!user.equals("")) {
				
				Iterator<Customer> cus = bank.getCustomers();
				while(cus.hasNext()) {
					Customer findSame = cus.next();
					if(findSame.getID().equals(user)) {
						same = true;
						break;
					}
				}
				
				if(!same) {
					String pass = new String(passText.getPassword());
					String again = new String(againText.getPassword());
					
					if(pass.equals(again)) {
						if(!firstText.getText().equals("") && !lastText.getText().equals("")) { 
							bank.addCustomer(new Customer(firstText.getText(),lastText.getText(),user,pass));
							customer = bank.getCustomer(bank.getNumOfCustomers()-1);
							
							if(!savingBalance.getText().equals("")) {
								sBalance = Double.parseDouble(savingBalance.getText());
								if(!savingRate.getText().equals("")) {
									sRate = Double.parseDouble(savingRate.getText());
								}else {
									sRate = 0.0;
								}
								customer.addAccount(new SavingsAccount(sBalance, sRate));
								hasacct = true;
							}
							
							if(!checkBalance.getText().equals("")) {
								cBalance = Double.parseDouble(checkBalance.getText());
								if(!checkOver.getText().equals("")) {
									cOver = Double.parseDouble(checkOver.getText());
									customer.addAccount(new CheckingAccount(cBalance, cOver));
								}else {
									customer.addAccount(new CheckingAccount(cBalance));
								}
								hasacct = true;
							}
							
							if(hasacct) {
								server.saveData();
								createFrame.dispose();
							}else {
								JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please add account ! !","Create failure",JOptionPane.WARNING_MESSAGE);
								bank.deleteCustomer(bank.getNumOfCustomers()-1);
							}
							
						}else { 
							JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please enter client name ! !","Create failure",JOptionPane.WARNING_MESSAGE);
						}
					}else { 
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"The password input is inconsistent ! !","Create failure",JOptionPane.WARNING_MESSAGE);
					}
				}else { 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"The user name has been used ! !","Create failure",JOptionPane.WARNING_MESSAGE);
				}
			}else { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please enter user ID ! !","Create failure",JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	private class CancelHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			server.saveData();
			createFrame.dispose();
		}
		
	}

}