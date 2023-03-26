package bankServer.banking.domain;

import java.util.*;

public class Bank {
	
	private List<Customer> customers;
	private static Bank bank = new Bank(); 
	
	private Bank() {
		customers = new ArrayList<Customer>();
	}
	
	public static Bank getBank() {
		return bank;
	}
	
	public void addCustomer(String f,String l,String userID,String password) {
		Customer customer = new Customer(f,l,userID,password);
		customers.add(customer);
	}
	
	public void addCustomer(Customer customer) {
		customers.add(customer);
	}
	
	public int getNumOfCustomers() {
		return customers.size();
	}
	
	public Customer getCustomer(int index) {
		return (Customer) customers.get(index);
	}
	
	public Iterator<Customer> getCustomers() {
		Iterator<Customer> it = customers.iterator();
		return it;
	}
	
	public void deleteCustomer(Iterator<Customer> it) {
		it.remove();
	}
	public void deleteCustomer(int index) {
		customers.remove(index);
	}
	
	public void sortCustomers() {
		Collections.sort(customers);
	}
}
