package bankServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Iterator;

import bankServer.banking.domain.*;

public class DisplayHandler implements ActionListener{
	ServerNet bankUser;
	Bank bank = Bank.getBank();
	
	public DisplayHandler(ServerNet bankUser) {
		this.bankUser = bankUser;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		bankUser.textArea.setText("");
		
		NumberFormat currency_format = NumberFormat.getCurrencyInstance();
		
		bank.sortCustomers();
		
		bankUser.textArea.append("\t\t\tCUSTOMERS REPORT\r\n");
		bankUser.textArea.append("\t\t\t================\r\n");
		
		Iterator<Customer> cus = bank.getCustomers();
		
		while(cus.hasNext()) {
			
			Customer customer = cus.next();
			
			bankUser.textArea.append("\r\n");
			bankUser.textArea.append("UserID: "+customer.getID()+"\r\n");
			bankUser.textArea.append("Customer: "
			+ customer.getLastName() + ", "
			+ customer.getFirstName()+"\r\n");
			
			Iterator<Account> acct = customer.getAccounts();
			
			while(acct.hasNext()) {
				
				Account account = acct.next();
				String  account_type = "";
				
				if(account instanceof SavingsAccount) {
					account_type = new String("    Savings Account");
				}else if(account instanceof CheckingAccount) {
					account_type = new String("    Checking Account");
				}
				
				bankUser.textArea.append(account_type+": current balance is "+currency_format.format(account.getBalance())+"\r\n");
			}
		}
	}
}