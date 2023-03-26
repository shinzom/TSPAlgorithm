package bankServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import bankServer.banking.domain.Bank;
import bankServer.banking.domain.Customer;

public class ServerNet {
	private JFrame bankFrame;
	private JPanel buttonPanel;
	private JLabel label;
	private Font font;
	private JButton create,del,modify,quit,display,query;
	private JMenu m1,m2;
	private JMenuBar menuBar;
	private JMenuItem menuItem1,menuItem2;
	private Bank bank;
	private FileInputStream fis = null;
	private ObjectInputStream in = null;
	private String filename = "Data/customers.ser";
	private Customer readData = null;
	private File file;
	private FileOutputStream fos = null;
	private ObjectOutputStream out = null;
	public TextArea textArea;
	
	private static ServerNet server = new ServerNet(); 
	public static final int PORT = 2020;
	public static LinkedList<Customer> currentCustomers = new LinkedList<Customer>();
	
	private ServerNet(){
		bank = Bank.getBank();
		font = new Font("Georgia",Font.PLAIN,25);
		label = new JLabel("Welcome to Bank Server");
		bankFrame = new JFrame("Bank Server");
		buttonPanel = new JPanel();
		menuBar = new JMenuBar();
        m1 = new JMenu("Quit");
        m2 = new JMenu("Help");
        menuItem1 = new JMenuItem("quit");      
        menuItem2 = new JMenuItem("about");
		textArea = new TextArea(20,50);
		create = new JButton("Create a customer");
		del = new JButton("Delete an account");
		modify = new JButton("Modify an account");
		display = new JButton("Display all accounts");
		quit = new JButton("Quit");
		query = new JButton("Query an account");
		file = new File(filename); 
	}
	
	public static ServerNet getServer() {
		return server;
	}
	
	public void launchFrame() {		
		label.setFont(font);
		textArea.setFont(new Font("Serif",Font.BOLD|Font.ITALIC,14));
		textArea.setEditable(false);
		textArea.setText("Welcome to Bank Server!\n"
		                 +"Click on the buttons to do what you want to do.");
		textArea.setBackground(new Color(225,255,255));
		
		create.setBackground(new Color(173,216,230));
		del.setBackground(new Color(173,216,230));
		query.setBackground(new Color(173,216,230));
		modify.setBackground(new Color(173,216,230));
		display.setBackground(new Color(173,216,230));
		quit.setBackground(new Color(173,216,230));
		
		menuBar.add(m1);
 	    menuBar.add(m2);
 	    m1.add(menuItem1);
 	    m2.add(menuItem2);
		
		buttonPanel.add(create);
		buttonPanel.add(del);
		buttonPanel.add(query);
		buttonPanel.add(modify);
		buttonPanel.add(display);
		buttonPanel.add(quit);
		
		quit.addActionListener(new QuitHandler());
		display.addActionListener(new DisplayHandler(this));
		create.addActionListener(new CreateHandler());
		query.addActionListener(new QueryHandler());
		del.addActionListener(new DeleteHandler());
		modify.addActionListener(new ModifyHandler());
		
		menuItem1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		ServerNet server = ServerNet.getServer();
	    		server.saveData();
	    		System.exit(0);
	    	}
	    });
	    
	    menuItem2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		JOptionPane.showMessageDialog(null, "You can use this system to create a Customer with new account,\r\n"
	    				+ "delete a account,\r\n"
	    				+ "query an account by customer,\r\n"
	    				+ "modify a specified account and\r\n"
	    				+ "display all account information.", "About",JOptionPane.INFORMATION_MESSAGE );
	    	}
	    });
		
		bankFrame.setBounds(200,300,850,450);
		bankFrame.setTitle("Bank Server");
		bankFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bankFrame.setLayout(new FlowLayout());
		bankFrame.setJMenuBar(menuBar);
		bankFrame.add(label);
		bankFrame.add(buttonPanel);
		bankFrame.add(textArea);
		bankFrame.setSize(800,500);
		bankFrame.setVisible(true);
	}
	
	public void loadData() {
		if(!file.exists()) {
			System.out.println("Creating new data file...");
			try {
				File dir = new File("Data/");
				dir.mkdir();
				file.createNewFile();
			}catch(IOException e){ 
				System.out.println("Failed to create new file");
			}
			System.out.println("Creating successfully.");
		}else {
			System.out.println("Loading data from files...");
			try { 
				fis = new FileInputStream(file);
				in = new ObjectInputStream(fis);
				try {
					while(true) {
						readData = (Customer) in.readObject();
						bank.addCustomer(readData);
					}
				}catch(EOFException e) { 
					in.close();
					fis.close();
				}
			} catch(IOException ex) { 
				ex.printStackTrace();
			} catch(ClassNotFoundException ex) { 
				ex.printStackTrace();
			}
			System.out.println("Loading successfully."); 
		}
	}
	
	public void saveData() {
		try { 
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			Iterator<Customer> cus = bank.getCustomers();
			while(cus.hasNext()) {
				Customer customer = cus.next();
				out.writeObject(customer);
			}
			out.close();
			fos.close();
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ServerNet bankServer = ServerNet.getServer();
		bankServer.loadData();
		System.out.println("Welcome to bank server.");
		javax.swing.SwingUtilities.invokeLater(new Runnable() { 
            public void run() {
            	bankServer.launchFrame();
            }
        });
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			Socket client = null;
			System.out.println("The server is waiting for the client to connect");
			while(true) {
				client = serverSocket.accept();
				ServerThread serverThread = new ServerThread(client);
				serverThread.start();
			}
		} catch (IOException e) {
			System.out.println("Server stop. "+e.getMessage());
		}
	}
}