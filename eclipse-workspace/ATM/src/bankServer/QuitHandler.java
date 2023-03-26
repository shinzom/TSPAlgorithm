package bankServer;

import java.awt.event.*;

public class QuitHandler implements ActionListener{
	ServerNet server = ServerNet.getServer();

	public void actionPerformed(ActionEvent arg0) {
		server.saveData();
		System.exit(0);
	}
}
