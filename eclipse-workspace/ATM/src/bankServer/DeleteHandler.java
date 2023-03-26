package bankServer;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;

import bankServer.banking.domain.*;

public class DeleteHandler implements ActionListener {
	JFrame deleteFrame;
	JButton enter,cancel;
	JLabel lastName,firstName,user;
	JTextField lastText,firstText,userID;
	JPanel enterPanel,lastPanel,firstPanel,idPanel;
	JComboBox<String> choiceBox;
	Box box;
	private Bank bank;
	private ServerNet server;
	private String[] accounts = {
			"Checking Account","Savings Account"
	};
	
	public DeleteHandler() {
		server = ServerNet.getServer();
		bank = Bank.getBank();
		userID = new JTextField(20);
		lastText = new JTextField(20);
		firstText = new JTextField(20);
		deleteFrame = new JFrame();
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		user = new JLabel("Please enter user ID:        ");
		lastName = new JLabel("Please enter your last name: ");
		firstName = new JLabel("Please enter your first name:");
		idPanel = new JPanel();
		enterPanel = new JPanel();
		lastPanel = new JPanel();
		firstPanel = new JPanel();
		choiceBox = new JComboBox<String>(accounts);
		box = Box.createVerticalBox();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
	}
	
	public void actionPerformed(ActionEvent e) {
		lastText.setText("");
		firstText.setText("");
		userID.setText("");
		
		idPanel.add(user);
		idPanel.add(userID);
		
		firstPanel.add(firstName);
		firstPanel.add(firstText);
		
		lastPanel.add(lastName);
		lastPanel.add(lastText);
		
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		box.add(idPanel);
		box.add(firstPanel);
		box.add(lastPanel);
		box.add(choiceBox);
		
		box.setBorder(BorderFactory.createEmptyBorder(0,0,40,0));

		deleteFrame.add(box);
		deleteFrame.add(enterPanel);
		
		deleteFrame.setLayout(new FlowLayout());
		deleteFrame.setBounds(200,300,500,500);
		deleteFrame.setTitle("Delete an Account");

		deleteFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{
		private String first,last,id;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean findAcct = false,findCus = false;
			boolean space = true;
			if(!userID.getText().equals("")&& !firstText.getText().equals("") && !lastText.getText().equals("")) { 
				id = userID.getText();
				first = firstText.getText();
				last = lastText.getText();
				
				for(Customer cus:ServerNet.currentCustomers) {
					if(id.equals(cus.getID())) {
						space = false;
						break;
					}
				}
				
				if(space) {
					Iterator<Customer> customers = bank.getCustomers();
					
					while(customers.hasNext()) {
						Customer customer = (Customer) customers.next();
						if(id.equals(customer.getID()) && first.equals(customer.getFirstName()) && last.equals(customer.getLastName())) { // if customer is found
							findCus = true;
							
							Iterator<Account> accounts = customer.getAccounts();
							
							while(accounts.hasNext()) {
								Account account = accounts.next();
								if(choiceBox.getSelectedIndex() == 0 && account instanceof CheckingAccount) { 
									findAcct = true;
									customer.deleteAccount(accounts); 
									if(customer.getNumOfAccounts() == 0) {
										bank.deleteCustomer(customers);
										break;
									}
								}else if(choiceBox.getSelectedIndex() == 1 && account instanceof SavingsAccount) { 
									findAcct = true;
									customer.deleteAccount(accounts);
									if(customer.getNumOfAccounts() == 0) {
										bank.deleteCustomer(customers);
										break;
									}
								}
							}
						}
					}
					
					if(!findCus) {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client hasn't registered at our bank ! !","Search failure",JOptionPane.WARNING_MESSAGE);
					}else if(!findAcct) {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client doesn't have this account ! !","Search failure",JOptionPane.WARNING_MESSAGE);
					}else {
						server.saveData();
						deleteFrame.dispose();
					}
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"This client is taking the server ! !","Operation failure",JOptionPane.WARNING_MESSAGE);
				}
				
				
			}else { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please enter client's ID and name ! !","Delete failure",JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	private class CancelHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			server.saveData();
			deleteFrame.dispose();
		}
		
	}

}

