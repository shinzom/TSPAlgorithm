package bankServer;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

import bankServer.banking.domain.Account;
import bankServer.banking.domain.Bank;
import bankServer.banking.domain.CheckingAccount;
import bankServer.banking.domain.Customer;
import bankServer.banking.domain.SavingsAccount;
import bankServer.banking.exception.ClientStopException;

public class ServerThread extends Thread {
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private Bank bank = Bank.getBank();
	private Customer customer = null;
	private ServerNet server = ServerNet.getServer();
	
	public ServerThread(Socket client) {
		this.client = client;
		
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(),true);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		outer:
		while(true) {
			try {
				boolean same = false;
				
				String ID = in.readLine();
				String password = in.readLine();
				
				Iterator<Customer> it = bank.getCustomers();
				
				while(it.hasNext()) {
					Customer cus = (Customer) it.next();
					if(cus.getID().equals(ID) && cus.getPassword().equals(password)) { 
						for(Customer c : ServerNet.currentCustomers) {
							if(cus.equals(c)) {
								out.println("Same");
								
								same = true;
								break;
							}
						}
						
						if(!same) {
							customer = cus;
							ServerNet.currentCustomers.add(customer);
						
							out.println("Correct");
							out.println(customer.getLastName()+", "+customer.getFirstName());
							
							Iterator<Account> acct = cus.getAccounts();
							while(acct.hasNext()) {
								Account account = (Account)acct.next();
								if(account instanceof CheckingAccount) {
									out.print("Checking Account\t\t");
								}else if(account instanceof SavingsAccount) {
									out.print("Savings Account\t\t");
								}
							}
							out.println();
							
							break outer;
						}
					}
				}
				
				if(!same) {
					out.println("Wrong");
				}
			} catch (IOException e) {
				return;
			}
		}
		
		System.out.println("Client "+customer.getFirstName()+" "+customer.getLastName()+" connect successfully");
		System.out.println("From IP: "+client.getInetAddress());
		
	}
	
	@Override
	public void run() {
		try {
			String message;
			while((message = in.readLine()) != null) {
				System.out.println("[Message received from client "+customer.getFirstName()+" "+customer.getLastName()+"]: "+message);
				
				String[] order = message.split(" ");
				
				switch(order[0]) {
				case "Query": 
					if(order[1].equals("Checking")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof CheckingAccount) {
								out.println(account.getBalance());
								out.println(account.getOverdraft());
								break;
							}
						}
					}else if(order[1].equals("Savings")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof SavingsAccount) {
								out.println(account.getBalance());
								out.println(account.getInterestRate());
								break;
							}
						}
					}
					break;
				case "Deposit": 
					if(order[1].equals("Checking")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof CheckingAccount) {
								deposit(account);
								break;
							}
						}
					}else if(order[1].equals("Savings")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof SavingsAccount) {
								deposit(account);
								break;
							}
						}
					}
					break;
				case "Withdraw": 
					if(order[1].equals("Checking")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof CheckingAccount) {
								withdraw(account);
								break;
							}
						}
					}else if(order[1].equals("Savings")) {
						Iterator<Account> acct = customer.getAccounts();
						while(acct.hasNext()) {
							Account account = (Account)acct.next();
							if(account instanceof SavingsAccount) {
								withdraw(account);
								break;
							}
						}
					}
					break;
				case "Change": 
					changePass();
					break;
				default:
					break;
				}
			}
			
		}catch (IOException e) { 
			if(customer == null) { 
				System.out.println("Client "+client.getInetAddress()+" quit.");
			}else {
				server.saveData();
				ServerNet.currentCustomers.remove(customer);
				
				System.out.println("Client "+customer.getFirstName()+" "+customer.getLastName()+" finish its service.");
			}
		} catch (ClientStopException e) {
			ServerNet.currentCustomers.remove(customer);
			
			System.out.println("Client "+customer.getFirstName()+" "+customer.getLastName()+e.getMessage());
		}
	}
	
	public void withdraw(Account account) throws ClientStopException {
		String message;
		double money;
		boolean result;
		
		try {
			out.println(account.getBalance());
			
			while(true) {
				message = in.readLine();
				
				if(!message.equals("Cancel")) {
					money = Double.parseDouble(message);
					
					result = account.withdraw(money);
					
					if(result) {
						out.println("Success");
						break;
					}else {
						out.println("Fail");
					}
				}else { 
					break;
				}
			}
		} catch (IOException e) {
			throw new ClientStopException();
		}
	}
	
	public void deposit(Account account) throws ClientStopException {
		String message;
		double money;
		boolean result;
		
		try {
			out.println(account.getBalance());
			
			while(true) {
				message = in.readLine();
				
				if(!message.equals("Cancel")) {
					money = Double.parseDouble(message);
					
					result = account.deposit(money);
					
					if(result) {
						out.println("Success");
						break;
					}else {
						out.println("Fail");
					}
				}else { 
					break;
				}
			}
		} catch (IOException e) {
			throw new ClientStopException();
		}
	}
	
	public void changePass() throws ClientStopException {
		String message,password,newPassword;
		
		try {
			while(!(message = in.readLine()).equals("Cancel")) {
				password = in.readLine();
				
				if(password.equals(customer.getPassword())) {
					out.println("Yes");
					
					message = in.readLine();
					if(message.equals("New")) {
						newPassword = in.readLine();
						
						customer.setPassword(newPassword);
						break;
					}
				}else{ 
					out.println("No");
				}
			}
		} catch (IOException e) {
			throw new ClientStopException();
		}
	}

}