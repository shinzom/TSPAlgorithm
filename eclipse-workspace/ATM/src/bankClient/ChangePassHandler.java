package bankClient;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class ChangePassHandler implements ActionListener{
	private CustomerEnterUI customer;
	private JPasswordField oldPass,newPass,confirmPass;
	private JLabel oldL,newL,confirmL;
	private JButton enter,cancel;
	private JFrame changeFrame;
	private JPanel inforPanel,buttonPanel;
	
	public ChangePassHandler(CustomerEnterUI customer) {
		
		this.customer = customer;
		oldL = new JLabel("Please enter original password: ");
		newL = new JLabel("Please enter new password: ");
		confirmL = new JLabel("Confirm password again: ");
		oldPass = new JPasswordField(20);
		newPass = new JPasswordField(20);
		confirmPass = new JPasswordField(20);
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		
		buttonPanel = new JPanel();
		inforPanel = new JPanel(new GridLayout(3,2,10,10));
		changeFrame = new JFrame("Change Password");
		
		oldPass.setEchoChar('*');
		oldPass.setColumns(20);
		newPass.setEchoChar('*');
		newPass.setColumns(20);
		oldPass.setEchoChar('*');
		confirmPass.setColumns(20);
		confirmPass.setEchoChar('*');
		
		enter.addActionListener(new EnterHandler());
		cancel.addActionListener(new CancelHandler());
		changeFrame.addWindowListener(new CloseWidget());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		customer.serverOut.println("Change");
		
		launchFrame();
	}
	
	public void launchFrame() {
		
		oldPass.setText("");
		newPass.setText("");
		confirmPass.setText("");
		
		buttonPanel.add(enter);
		buttonPanel.add(cancel);
		inforPanel.add(oldL);
		inforPanel.add(oldPass);
		inforPanel.add(newL);
		inforPanel.add(newPass);
		inforPanel.add(confirmL);
		inforPanel.add(confirmPass);
		
		inforPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		
		changeFrame.add(inforPanel,BorderLayout.CENTER);
		changeFrame.add(buttonPanel,BorderLayout.SOUTH);
		
		changeFrame.setBounds(200,300,450,250);
		changeFrame.setVisible(true);
	}
	
	private class EnterHandler implements ActionListener{
		
		private String oldT,newT,confirmT,message;
		@Override
		public void actionPerformed(ActionEvent e) {
			
			oldT = new String(oldPass.getPassword());
			newT = new String(newPass.getPassword());
			confirmT = new String(confirmPass.getPassword());
			
			customer.serverOut.println("Old");
			customer.serverOut.println(oldT);
			try {
				
				message = customer.serverIn.readLine();
				
				if(message.equals("Yes")) {
					
					if(newT.equals(confirmT)) {
						
						customer.serverOut.println("New");
						customer.serverOut.println(newT);
						
						changeFrame.dispose();
					}else {
						
						customer.serverOut.println("");
						JOptionPane.showMessageDialog(new JFrame().getContentPane(),"The password input is inconsistent ! !","Change Password failure",JOptionPane.WARNING_MESSAGE);
					}
				}else if(message.equals("No")){ 
					JOptionPane.showMessageDialog(new JFrame().getContentPane(),"Wrong original password ! !","Change Password failure",JOptionPane.WARNING_MESSAGE);
				}
			}catch(IOException e1) {
			}
			
			oldPass.setText("");
			newPass.setText("");
			confirmPass.setText("");
		}
	}
	
	private class CloseWidget extends WindowAdapter implements WindowListener{
		@Override
		public void windowClosing(WindowEvent e) {
			
			customer.serverOut.println("Cancel");
			
			changeFrame.dispose();
		}
	}
	
	private class CancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			customer.serverOut.println("Cancel");
			
			changeFrame.dispose();
		}
	}
}
