package bankServer.banking.domain;

public class CheckingAccount extends Account {
	private double overdraftProtection;
	
	public CheckingAccount(double balance) {
		super(balance);
	}
	
	public double getOverdraft() {
		return overdraftProtection;
	}
	
	public CheckingAccount(double balance,double protect) {
		super(balance);
		overdraftProtection = protect;
	}
	
	public void setOverdraft(double overdraftProtection) {
		this.overdraftProtection = overdraftProtection;
	}
	
	public boolean withdraw(double amt) {
		if(amt <= balance) {
			balance -= amt;
			return true;
		}else if(amt-balance <= overdraftProtection) {
			overdraftProtection -= (amt-balance);
			balance = 0.0;
			return true;
		}else {
			return false;
		}
	}
}
