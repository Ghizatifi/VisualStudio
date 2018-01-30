import java.net.*;
import java.io.*;
import java.util.*;

public class Client  {

    private ObjectInputStream in;//Lire a partir de la socket    
    private ObjectOutputStream out; //ecrire a partir de la socket     
    private Socket socket;
    //pour tester l'utilisation de l'interface graphique ou pas
    private ClientChat clientchat; 
    private String server;//le serveur "localhost"
    private String login;//login pour se connecter
    private int port;//le port utilise 1234
    
/*************************************************************/
    Client(String server, int port, String username) {
        this(server, port, username, null);}
    Client() {}
/*************************************************************/  
    Client(String server, int port, String login, ClientChat clientchat) {
        this.server = server;
        this.port = port;
        this.login = login;
        this.clientchat = clientchat;}
/*************************************************************/   
    public boolean LancerConnectionCS() {
        try {
           socket = new Socket(server, port);
        }
        catch(Exception ec) {

        	Afficher("probleme de connection avec le serveur:" + ec);
            return false;
        }
        String msg = "Connection acceptee sur :***: " + socket.getInetAddress() + ":***:" + socket.getPort();
        Afficher(msg);//Afficher le message sur l'interface graphique
        try
        {
            in  = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO) {
            return false;
        }
        ClientThread clThread= new ClientThread();
        		clThread.start();
        try
        {
        	//envoyer le login 
            out.writeObject(login);
        }
        catch (IOException eIO) {     
            return false;
        }
        return true;
    }
/*************************************************************/   
     public void Afficher(String msg) {
        if(clientchat == null)
            System.out.println(msg);      
        else
        	clientchat.ajouter(msg + "\n");      
    }
/*************************************************************/   
    public void Envoyer(Chat msg) {
        try {
            out.writeObject(msg);
        }
        catch(IOException e) {
        	Afficher("Exception au niveau du serveur: " + e);
        }
    }
/*************************************************************/    
    public static void main(String[] args) {
        int portNumber = 1234;
        String serverAddress = "localhost";
        String userName = "";
        Client client = new Client(serverAddress, portNumber, userName);
        Scanner scan = new Scanner(System.in);
        while(true) {
            String msg = scan.nextLine();
             client.Envoyer(new Chat(msg));       
        }      
    }
/*************************************************************/      
    class ClientThread extends Thread {
    	
        public void run() {
            while(true) {
                try {
                    String msg = (String) in.readObject();
                   if(clientchat == null) {
                        System.out.println(msg);
                   }
                   else {
                    	clientchat.ajouter(msg);//declarer au niveau de GUI du client
                    }
                }
                catch(IOException e) {
                	Afficher("La connection est arrete: " + e);                    
                }
                catch(ClassNotFoundException e2) {
                }
            }
        }      
    }
/*************************************************************/ 
}
