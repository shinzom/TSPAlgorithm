package bankServer;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Iterator;
import javax.swing.*;

import bankServer.banking.domain.*;

public class ModifyHandler implements ActionListener {
	JFrame modifyFrame;
	JButton enter,cancel,modify;
	JLabel lastName,firstName,user;
	JTextField lastText,firstText,userID;
	TextArea textArea;
	JPanel enterPanel,lastPanel,firstPanel,idPanel;
	JComboBox<String> choiceBox;
	Box box;
	CheckingAccount modiCheck;
	SavingsAccount modiSave;
	private String[] accounts = {
			"Checking Account","Savings Account"
	};
	private Bank bank;
	private ServerNet server;
	private int modifyChoice = 0;
	
	public ModifyHandler() {
		server = ServerNet.getServer();
		bank = Bank.getBank();
		lastText = new JTextField(20);
		firstText = new JTextField(20);
		userID = new JTextField(20);
		modifyFrame = new JFrame();
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		modify = new JButton("Modify");
		user = new JLabel("Please enter user ID:        ");
		lastName = new JLabel("Please enter your last name: ");
		firstName = new JLabel("Please enter your first name:");
		idPanel = new JPanel();
		enterPanel = new JPanel();
		lastPanel = new JPanel();
		firstPanel = new JPanel();
		textArea = new TextArea(20,40);
		choiceBox = new JComboBox<String>(accounts);
		box = Box.createVerticalBox();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
		modify.addActionListener(new ModiAccountHandler(this));
		
		modiCheck = null;
		modiSave = null;
	}
	
	public int getModiChoice() {
		return modifyChoice;
	}
	
	public void setModiChoice(int choice) {
		modifyChoice = choice;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		lastText.setText("");
		firstText.setText("");
		userID.setText("");
		
		textArea.setText("After completing the information,\r\n"+
		"press 'Enter' button to search the account\r\n"+
		"you want to modify.");
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		
		idPanel.add(user);
		idPanel.add(userID);
		
		firstPanel.add(firstName);
		firstPanel.add(firstText);
		
		lastPanel.add(lastName);
		lastPanel.add(lastText);
		
		enterPanel.add(modify);
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		box.setBorder(BorderFactory.createEmptyBorder(0,0,40,0));
		
		box.add(idPanel);
		box.add(firstPanel);
		box.add(lastPanel);
		box.add(choiceBox);
		
		modifyFrame.add(box);
		modifyFrame.add(textArea);
		modifyFrame.add(enterPanel);
		
		modifyFrame.setLayout(new FlowLayout());
		modifyFrame.setBounds(200,200,500,550);
		modifyFrame.setTitle("Modify an Account");
		modifyFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{
		private String first,last,id;

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean findAcct = false,findCus = false;
			boolean space = true;
			modifyChoice = 0;
			NumberFormat currency_format = NumberFormat.getCurrencyInstance();
			
			if(!userID.getText().equals("") && !firstText.getText().equals("") && !lastText.getText().equals("")) {
				first = firstText.getText();
				last = lastText.getText();
				id = userID.getText();
				
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
						if(id.equals(customer.getID()) && first.equals(customer.getFirstName()) && last.equals(customer.getLastName())) { // if find the customers
							findCus = true;
							
							Iterator<Account> accounts = customer.getAccounts();
							
							while(accounts.hasNext()) {
								Account account = accounts.next();
								if((choiceBox.getSelectedItem()).equals("Checking Account") && account instanceof CheckingAccount) { // find the account
									findAcct = true;
									
									textArea.setText("Dear "+last+", "+first+"\r\n"+
									"Your current balance of the Checking Account is: "+currency_format.format(account.getBalance())+"\r\n"+
									"And your Overdraft Proection is: "+currency_format.format(account.getOverdraft())+"\r\n"+"\r\n"+
									"Press 'Modify' to modify it.");
									
									modifyChoice = 1;
									modiCheck = (CheckingAccount) account;
								}else if((choiceBox.getSelectedItem()).equals("Savings Account") && account instanceof SavingsAccount) {
									findAcct = true;
									
									textArea.setText("Dear "+last+", "+first+"\r\n"+
									"Your current balance of the Savings Account is: "+currency_format.format(account.getBalance())+"\r\n"+
									"And your Interest Rate is: "+account.getInterestRate()+"\r\n"+"\r\n"+
									"Press 'Modify' to modify it.");
									
									modifyChoice = 2;
									modiSave = (SavingsAccount) account;
								}
							}
						}
					}
					
					if(!findCus) {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client haven't registered at our bank ! !","Search failure",JOptionPane.WARNING_MESSAGE);
					}else if(!findAcct) {
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client don't have this account ! !","Search failure",JOptionPane.WARNING_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"This client is taking the server ! !","Operation failure",JOptionPane.WARNING_MESSAGE);
				}
				
			}else { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please enter client's ID and name ! !","Modify failure",JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	private class CancelHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			server.saveData();
			modifyFrame.dispose();
		}
		
	}

}

