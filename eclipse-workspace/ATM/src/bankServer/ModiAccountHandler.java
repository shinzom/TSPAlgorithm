package bankServer;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ModiAccountHandler implements ActionListener {	
	JFrame modiCheckFrame,modiSaveFrame;
	JLabel checking,saving,cbalance,sbalance,interestRate,overdraft;
	JTextField checkBalance,checkOver,savingRate,savingBalance;
	JPanel checkPanel,savingPanel,enterPanel;
	JButton enter,cancel;
	Box cbox,sbox;
	private ServerNet server;
	private ModifyHandler modify;
	
	public ModiAccountHandler(ModifyHandler modify) {
		server = ServerNet.getServer();
		this.modify = modify;
		modiCheckFrame = new JFrame("Modify an account");
		modiSaveFrame = new JFrame("Modify an account");
		checking = new JLabel("Modify your Checking Account:  ");
		saving = new JLabel("Modify your Saving Account:  ");
		cbalance = new JLabel("Balance:");
		sbalance = new JLabel("Balance:");
		interestRate = new JLabel("Interest Rate:");
		overdraft = new JLabel("Overdraft:");
		checkOver = new JTextField(10);
		checkBalance = new JTextField(10);
		savingRate = new JTextField(10);
		savingBalance = new JTextField(10);
		enterPanel = new JPanel();
		checkPanel = new JPanel(new GridLayout(5,1));
		savingPanel = new JPanel(new GridLayout(5,1));
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		
		cbox = Box.createVerticalBox();
		sbox = Box.createVerticalBox();
		
		cancel.addActionListener(new CancelHandler());
		enter.addActionListener(new EnterHandler());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		checkOver.setText("");
		checkBalance.setText("");
		savingRate.setText("");
		savingBalance.setText("");
		
		int choice = modify.getModiChoice();
		
		switch(choice) {
		case 0:
			break;
		case 1:
			launchCheck(); 
			break;
		case 2:
			launchSave(); 
			break;
		default:
			
			System.out.println("Something Wrong!!");
		}
	}
	
	public void launchCheck() {
		
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		checkPanel.add(checking);
		checkPanel.add(cbalance);
		checkPanel.add(checkBalance);
		checkPanel.add(overdraft);
		checkPanel.add(checkOver);
		
		checkPanel.setBorder(BorderFactory.createEmptyBorder(10,200,10,200));
		
		cbox.add(checkPanel);
		cbox.add(enterPanel);
		
		modiCheckFrame.add(cbox);
		
		modiCheckFrame.setBounds(200,300,800,450);
		modiCheckFrame.setVisible(true);
	}
	
	public void launchSave() {
		
		enterPanel.add(enter);
		enterPanel.add(cancel);
		
		savingPanel.add(saving);
		savingPanel.add(sbalance);
		savingPanel.add(savingBalance);
		savingPanel.add(interestRate);
		savingPanel.add(savingRate);
		
		savingPanel.setBorder(BorderFactory.createEmptyBorder(10,200,10,200));
		
		sbox.add(savingPanel);
		sbox.add(enterPanel);
		
		modiSaveFrame.add(sbox);
		
		modiSaveFrame.setBounds(200,300,800,450);
		modiSaveFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{	
		private double cBalance;
		private double cOver;
		private double sBalance;
		private double sRate;
		@Override
		public void actionPerformed(ActionEvent e) {
			modify.textArea.setText("");
			modify.textArea.append("Modified successfully.\r\n"+
					"Press 'Cancel' to return menu\r\n,"+
					"Press 'Enter' to search for another account");
			
			int choice = modify.getModiChoice();
			
			switch(choice) {
			case 0:
				break;
			case 1: 
				if(!checkBalance.getText().equals("")) { 
					cBalance = Double.parseDouble(checkBalance.getText());
					if(!checkOver.getText().equals("")) {
						cOver = Double.parseDouble(checkOver.getText());
						modify.modiCheck.setOverdraft(cOver);
					}
					modify.modiCheck.setBalance(cBalance);
				}
				
				modify.setModiChoice(0);
				server.saveData();
				modiCheckFrame.dispose();
				break;
			case 2: 
				if(!savingBalance.getText().equals("")) { 
					sBalance = Double.parseDouble(savingBalance.getText());
					if(!savingRate.getText().equals("")) {
						sRate = Double.parseDouble(savingRate.getText());
						modify.modiSave.setInterestRate(sRate);
					}
					modify.modiSave.setBalance(sBalance);
				}
				
				modify.setModiChoice(0);
				server.saveData();
				modiSaveFrame.dispose();
				break;
			default: 
				System.out.println("Something Wrong!!");
			}
		}
	}
	
	private class CancelHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = modify.getModiChoice();
			
			switch(choice) {
			case 0:
				break;
			case 1:
				server.saveData();
				
				modiCheckFrame.dispose();
				break;
			case 2:
				server.saveData();
				
				modiSaveFrame.dispose();
				break;
			default:
				
				System.out.println("Something Wrong!!");
			}
		}
	}
}
