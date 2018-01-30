package Test2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormClient1 extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    private JTextField tfname;
    private JTextField tfServer, tfPort;
    private JButton login, logout, whoIsIn;
    private JTextArea ta;
    private boolean connection;
    private Client client;

    // the default port number

    private int defaultPort;

    private String defaultHost;

    // Constructor connection receiving a socket number

    FormClient1(String host, int port) {


        super("Chat Client");

        defaultPort = port;
     defaultHost = host;

        
        // The NorthPanel with:
        JPanel northPanel = new JPanel(new GridLayout(3,1));
        // the server name anmd the port number
        JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
        // the two JTextField with default value for server address and port number
        tfServer = new JTextField(host);
        tfPort = new JTextField("" + port);
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);
        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(tfServer);
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(tfPort);
        serverAndPort.add(new JLabel(""));

        // adds the Server an port field to the GUI
        northPanel.add(serverAndPort);

 

        // the Label and the TextField

        label = new JLabel("Enter your username below", SwingConstants.CENTER);
        northPanel.add(label);
        tfname = new JTextField("Anonymous");
        tfname.setBackground(Color.WHITE);
        northPanel.add(tfname);
        add(northPanel, BorderLayout.NORTH);
 
        // The CenterPanel which is the chat room
        ta = new JTextArea("Welcome to the Chat room\n", 80, 80);

        JPanel centerPanel = new JPanel(new GridLayout(1,1));

        centerPanel.add(new JScrollPane(ta));

        ta.setEditable(false);

        add(centerPanel, BorderLayout.CENTER);

 

        // the 3 buttons

        login = new JButton("Login");

        login.addActionListener(this);

        logout = new JButton("Logout");

        logout.addActionListener(this);

        logout.setEnabled(false);       // you have to login before being able to logout
        whoIsIn = new JButton("Who is in");

        whoIsIn.addActionListener(this);

        whoIsIn.setEnabled(false);      // you have to login before being able to Who is in
 
        JPanel southPanel = new JPanel();
        southPanel.add(login);
        southPanel.add(logout);

        southPanel.add(whoIsIn);
        add(southPanel, BorderLayout.SOUTH);
 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        tfname.requestFocus();
 
    }
 
    // called by the Client to append text in the TextArea
    void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }
    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    void connectionFailed() {
        login.setEnabled(true);
        logout.setEnabled(false);
        whoIsIn.setEnabled(false);
     label.setText("Enter your username below");
     tfname.setText("Anonymous");
        // reset port number and host name as a construction time
        tfPort.setText("" + defaultPort);
        tfServer.setText(defaultHost);
        // let the user change them
        tfServer.setEditable(false);
        tfPort.setEditable(false);
        // don't react to a <CR> after the username
        tfname.removeActionListener(this);
        connection = false;

    }

         

    /*

    * Button or JTextField clicked

    */

    public void actionPerformed(ActionEvent e) {
		 Object obj = e.getSource();
		 if(connection) {
	            client.EnvoyerMssg(new Chat( ""));              
	           // return;
	        }
		 else if(obj== logout) {
	            client.EnvoyerMssg(new Chat( ""));
	            tfname.setText("");
	        }
		 else if(obj == login) {
			 String nom = tfname.getText().trim();
			 if(nom==null)
	                return;
			 String server = tfServer.getText().trim();
	            if(server.length() == 0)
	                return;
			 String Nport = tfPort.getText().trim();
			 if(Nport==null)
	                return;
	            int port = 0;
	            try {
	                port = Integer.parseInt(Nport);
	            }
	            catch(Exception en) {
	                return;   //le port est invalid
	            }
	            // instancier un nv user
	            client = new Client(server, port, nom, this);
	            // si le client peu se connecter

	            if(!client.start())
	                return;
	            tfname.setText("");
	            label.setText("Veuillez saisir votre nom");
	            connection = true;
	            // Lancer l'action lorsque le client saisi son nom
	            tfname.addActionListener(this);
		 }	
	}
 

    // to start the whole thing the server

    public static void main(String[] args) {

        new FormClient1("localhost", 1234);

    }

 
}
