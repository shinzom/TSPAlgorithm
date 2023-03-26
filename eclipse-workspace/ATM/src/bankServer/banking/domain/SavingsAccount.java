package bankServer.banking.domain;

public class SavingsAccount extends Account {
	
	private double interestRate;
	
	public SavingsAccount(double balance,double interest_rate) {
		super(balance);
		interestRate = interest_rate;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
}
