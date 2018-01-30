import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.ServerSocket;


public class ServeurChat extends JFrame implements ActionListener, WindowListener {
/*************************************************************/   
	public ServeurChat() {
	}
/*************************************************************/   
    private static final long serialVersionUID = 1L;
    private JButton Lancer;
    private JTextArea chat;
    private JTextField Nport;
    private Serveur server;
/*************************************************************/      
    ServeurChat(int port) {
        super("Le serveur");
        server = null;
        JPanel jpn1 = new JPanel();
        jpn1.add(new JLabel("Port number: "));
        jpn1.setBackground(Color.PINK);
        Nport = new JTextField("  " + port);
        jpn1.add(Nport);
        Lancer = new JButton("Lancer");
        Lancer.addActionListener(this);
        jpn1.add(Lancer);
        getContentPane().add(jpn1, BorderLayout.NORTH);
        JPanel center = new JPanel();
        chat = new JTextArea(80,80);
       // chat.setBackground(Color.CYAN);
     center.add(new JScrollPane(chat));
        getContentPane().add(center);
     addWindowListener(this);
        setSize(400, 600);
        setVisible(true);
    } 
/*************************************************************/      
    void AfficherDscussion(String str) {
       chat.append(str);
    }
/*************************************************************/  
    void appendEvent(String str) { }
/*************************************************************/  
    public void actionPerformed(ActionEvent e) {
        if(server != null) {
            server.ArretServeur();
            server = null;
            Nport.setEditable(true);
            Lancer.setText("Lancer");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(Nport.getText().trim());
        }
        catch(Exception er) {
            appendEvent("Le numero de port est invalid");
            return;

        }
        //Initialiser le serveur avec le port et l'interface Server
        server = new Serveur(port, this);
        //Starter le Thread
        new ServerThread().start();
        Nport.setEditable(false);
    }
/*************************************************************/  
    public static void main(String[] arg) {
        new ServeurChat(1234);
    }
/*************************************************************/  
    public void windowClosing(WindowEvent e) {
        if(server != null) {
            try {
                server.ArretServeur();        
            }
           catch(Exception eClose) {
            }
            server = null;
        }
        dispose();
        System.exit(0);
    }
/*************************************************************/  
   public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
/*************************************************************/  
    class ServerThread extends Thread {
        public void run() {
            server.start();      
            Lancer.setText("Lancer");
            Nport.setEditable(true); 
            appendEvent("Server crashed\n");
        
        }
    }
 
}
