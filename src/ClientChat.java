import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientChat extends JFrame implements ActionListener {
/*************************************************************/  
    private static final long serialVersionUID = 1L;
    private JTextField nom;
    private JTextField serveur, nport;
    private JButton sign;
    private JTextArea disc;
    private boolean connection;
    private Client client;
    private int Port;
    private String Host;
/*************************************************************/  
    ClientChat(String host, int port) {
        super("Le client");
        Port = port;
        Host = host;
        JPanel jpn1 = new JPanel(new GridLayout(3,1));
        JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
        serveur = new JTextField(host);
        nport = new JTextField("" + port);
        nport.setHorizontalAlignment(SwingConstants.RIGHT);
        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(serveur);
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(nport);
        serverAndPort.add(new JLabel(""));
        jpn1.add(serverAndPort);
        jpn1.setBackground(Color.PINK);
        nom = new JTextField("Votre Nom ici...");
        nom.setBackground(Color.WHITE);
        jpn1.add(nom);
        add(jpn1, BorderLayout.NORTH);
         disc = new JTextArea("Discussion...", 80, 80);
        JPanel panl2 = new JPanel(new GridLayout(1,1));
        panl2.setBackground(Color.PINK);
        panl2.add(new JScrollPane(disc));
        disc.setEditable(false);
        add(panl2, BorderLayout.CENTER);
        sign = new JButton("Sign");
        sign.addActionListener(this);
        JPanel panl3 = new JPanel();
        panl3.setBackground(Color.PINK);
        panl3.add(sign);
        add(panl3, BorderLayout.SOUTH);
        setSize(600, 600);
        setVisible(true);
    }
/*************************************************************/  
    void ajouter(String str) {
    	disc.append(str);
    } 
/*************************************************************/  
    public void actionPerformed(ActionEvent e) {
        Object clt = e.getSource();
        if(connection) {
            client.Envoyer(new Chat(Chat.IDMessage, nom.getText()));            
            nom.setText("");
           // return;
        }
        if(clt == sign) {
            String name = nom.getText();
            String server = serveur.getText();
            String Nport = nport.getText();
             int port = 0;
            try {

                port = Integer.parseInt(Nport);
            }
            catch(Exception en) {
                return;   
            }
            client = new Client(server, port, name, this);
            if(!client.LancerConnectionCS())
                return;
            nom.setText("");
            connection = true;           
            sign.setEnabled(false);
            nom.addActionListener(this);
        }
    }
/*************************************************************/    
    public static void main(String[] args) {
        new ClientChat("localhost", 1234);
    }
/*************************************************************/  
}
