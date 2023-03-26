package bankServer.banking.domain;

import java.io.Serializable;
import java.util.*;

public class Customer implements Comparable<Customer>,Serializable{
	
	private String userID;
	private String password;
	private String firstName;
	private String lastName;
	private List<Account> account;
	
	public Customer(String f,String l,String userID,String password) {
		firstName = f;
		lastName = l;
		account = new ArrayList<Account>();
		this.userID = userID;
		this.password = password;
	}
	
	public String getID() {
		return userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public Account getAccount(int index) {
		return (Account) account.get(index);
	}
	
	public void addAccount(Account acct) {
		account.add(acct);
	}
	
	public void setID(String userID) {
		this.userID = userID; 
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getNumOfAccounts() {
		return account.size();
	}
	
	public Iterator<Account> getAccounts() {
		Iterator<Account> it = account.iterator();
		return it;
	}
	
	public void deleteAccount(Iterator<Account> it) {
		it.remove();
	}
	@Override
	
	public int compareTo(Customer cus) {
		if(this.lastName.compareTo(cus.lastName) > 0) { 
			return 1; 
		}else if(this.lastName.compareTo(cus.lastName) == 0) {
			if(this.firstName.compareTo(cus.firstName) > 0) { 
				return 1; 
			}else if(this.firstName.compareTo(cus.lastName) == 0) {
				return 0; 
			}else {
				return -1; 
			}
		}else {
			return -1; 
		}
	}
}
