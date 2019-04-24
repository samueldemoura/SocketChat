package ufpb.srjn.socketchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class is responsible for the client's GUI.
 * @author samuel
 */
public class ClientJFrame extends javax.swing.JFrame {

	// Logger handle
	private static final Logger LOGGER = Logger.getLogger(ClientThread.class.getName());

	// Client instance
	private static ClientInstance client;

	/**
	 * Creates new form ClientJFrame
	 */
	public ClientJFrame() {
		initComponents();
	}

	/**
	 * Sends a new line to the chat text field.
	 * @param message Message to send to text field.
	 */
	public void sendToTextField(String message) {
		chatTextArea.append(message + "\n");
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        inputTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();

        jFormattedTextField1.setText("jFormattedTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });

        chatTextArea.setEditable(false);
        chatTextArea.setColumns(20);
        chatTextArea.setRows(5);
        jScrollPane1.setViewportView(chatTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                    .addComponent(inputTextField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(inputTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * This method is called whenever the client hits enter on the message input
	 * field.
	 *
	 * @param evt
	 */
    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
		// Client tried to send a message
		String input = inputTextField.getText();

		try {
			// Send to server
			client.writeOut(input);
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "*** Error sending message to server: {0}", ex.getMessage());
		}

		// Clear input field
		inputTextField.setText("");
    }//GEN-LAST:event_inputTextFieldActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ClientJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				ClientJFrame jframe = new ClientJFrame();
				jframe.setVisible(true);

				// Receive IP, port, password and username from user.
				String ip_port = JOptionPane.showInputDialog("Input the server address and port (format: <ip address>:<port>)");
				if (ip_port == null)
					System.exit(0);
				
				String password = JOptionPane.showInputDialog("Input the server password:");
				if (password == null)
					System.exit(0);
				
				String username = JOptionPane.showInputDialog("Input your desired username:");
				if (username == null)
					System.exit(0);

				// Pass along the password to the authenticator class.
				Authenticator.setPassword(password);
				
				// Connect to server.
				try {
					Socket socket = new Socket(ip_port.split(":")[0], Integer.parseInt(ip_port.split(":")[1]));
					DataInputStream in = new DataInputStream(socket.getInputStream());
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());

					client = new ClientInstance(socket, in, out, username);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(
						jframe,
						"Error while connecting to server: " + ex.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE
						);
					System.exit(-1);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(
						jframe,
						"Error: " + args[1] + " is not a valid port number.",
						"Error",
						JOptionPane.ERROR_MESSAGE
						);
					System.exit(-1);
				} catch (ArrayIndexOutOfBoundsException ex) {
					JOptionPane.showMessageDialog(
						jframe,
						"Invalid server address.",
						"Error",
						JOptionPane.ERROR_MESSAGE
						);
					System.exit(-1);
				}

				jframe.sendToTextField("*** Connected to " + ip_port + " as " + client.username);

				// Create event-handling thread
				Thread client_thread = new Thread(new ClientThread(client, jframe));
				client_thread.start();
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
