package bankServer.banking.domain;

import java.io.*;

public class Account implements Serializable {
	
	protected double balance; 
	
	public Account(double init_balance) { 
		balance = init_balance;
	}
	
	public double getBalance() { 
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getOverdraft() {
		return 0;
	}
	
	public double getInterestRate() {
		return 0;
	}
	
	public boolean deposit(double amt) { 
		balance += amt;
		return true;
	}
	
	public boolean withdraw(double amt) { 
		if(balance < amt) {
			return false;
		}else {
			balance -= amt;
			return true;
		}
	}
}
