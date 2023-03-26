package bankServer;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Iterator;
import javax.swing.*;

import bankServer.banking.domain.*;

public class QueryHandler implements ActionListener {
	JFrame queryFrame;
	JButton enter,cancel;
	JLabel lastName,firstName;
	JTextField lastText,firstText;
	TextArea textArea;
	JPanel enterPanel,lastPanel,firstPanel;
	Box box;
	JComboBox<String> choiceBox;
	private String[] accounts = {
			"Checking Account","Savings Account"
	};
	private Bank bank;
	
	public QueryHandler() {
		bank = Bank.getBank();
		lastText = new JTextField(20);
		firstText = new JTextField(20);
		queryFrame = new JFrame();
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		lastName = new JLabel("Please enter your last name: ");
		firstName = new JLabel("Please enter your first name:");
		enterPanel = new JPanel();
		lastPanel = new JPanel();
		firstPanel = new JPanel();
		textArea = new TextArea(20,40);
		choiceBox = new JComboBox<String>(accounts);
		
		box = Box.createVerticalBox();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		lastText.setText("");
		firstText.setText("");
		textArea.setText("");
		
		textArea.setEditable(false);
		textArea.setBackground(Color.WHITE);
		
		firstPanel.add(firstName);
		firstPanel.add(firstText);
		
		lastPanel.add(lastName);
		lastPanel.add(lastText);
		
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		box.add(firstPanel);
		box.add(lastPanel);
		box.add(choiceBox);
		
		box.setBorder(BorderFactory.createEmptyBorder(0,0,40,0));
		
		queryFrame.add(box);
		queryFrame.add(textArea);
		queryFrame.add(enterPanel);
		
		queryFrame.setLayout(new FlowLayout());
		queryFrame.setBounds(200,200,500,550);
		queryFrame.setTitle("Query an Account");
		queryFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{
		
		private String first,last;
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean findAcct = false,findCus = false;
			
			NumberFormat currency_format = NumberFormat.getCurrencyInstance();
			
			textArea.setText("");
			
			if(!firstText.getText().equals("") && !lastText.getText().equals("")) {
				
				first = firstText.getText();
				last = lastText.getText();
				
				Iterator<Customer> customers = bank.getCustomers();
				
				while(customers.hasNext()) {
					Customer customer = (Customer) customers.next();
					if(first.equals(customer.getFirstName()) && last.equals(customer.getLastName())) { 
						findCus = true;
						
						Iterator<Account> accounts = customer.getAccounts();
						
						while(accounts.hasNext()) {
							Account account = accounts.next();
							if(choiceBox.getSelectedIndex() == 0 && account instanceof CheckingAccount) { 
								findAcct = true;
								
								textArea.append("Dear "+last+", "+first+":\r\n"+
								"UserID: "+customer.getID()+"\r\n"+
								"Your current balance of the Checking Account is: "+currency_format.format(account.getBalance())+"\r\n"+
								"And your Overdraft Proection is: "+currency_format.format(account.getOverdraft())+"\r\n\r\n");
							}else if(choiceBox.getSelectedIndex() == 1 && account instanceof SavingsAccount) { 
								findAcct = true;
								
								textArea.append("Dear "+last+", "+first+":\r\n"+
								"UserID: "+customer.getID()+"\r\n"+
								"Your current balance of the Savings Account is: "+currency_format.format(account.getBalance())+"\r\n"+
								"And your Interest Rate is: "+account.getInterestRate()+"\r\n\r\n");
							}
						}
					}
				}
				
				if(!findCus) {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client hasn't registered at our bank ! !","Search failure",JOptionPane.WARNING_MESSAGE);
				}else if(!findAcct) {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Client doesn't have this account ! !","Search failure",JOptionPane.WARNING_MESSAGE);
				}
			}else { 
				JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Please enter client's name ! !","Query failure",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	private class CancelHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			queryFrame.dispose();
		}
	}
}
