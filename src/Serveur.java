import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import java.util.*;

public class Serveur {
/*************************************************************/   
    private ArrayList<ClientThread> Liste;//une ArrayList pour conserver la liste du client
    private ServeurChat SC;//je test si je suis dans l'intaface serveur
    private SimpleDateFormat dat;//pour identifier chaque message envoye
    private int port;//port
    private boolean ok;//Activer si le serveur em marche sinon je le desactive
/*************************************************************/   
    public Serveur(int port) {
        this(port, null);
    }
/*************************************************************/     
    public Serveur(int port, ServeurChat SC) {
        this.SC = SC;
        this.port = port;
        dat = new SimpleDateFormat("HH:mm:ss");
        Liste = new ArrayList<ClientThread>();
   }
/*************************************************************/       
    public void start() {
    	ok = true;//le serveur en marche
        try
        {
        	//créer un serveur socket et attendre les demandes de connexion
            ServerSocket serverSocket = new ServerSocket(port);
            while(ok)//boucle infini pour etre a l'ecoute tout le temps
            {
            	
                display("Server est a l'ecoute : " + port + ".");  
                Socket socket = serverSocket.accept();      
                if(!ok)//le cas ou on arrete le serveur
                    break;
                ClientThread t = new ClientThread(socket); //creaion d'un thread 
                Liste.add(t); //ajouter le thread a la liste                                 
                t.start();//lancer le thread
            }
            //Pour arreter le serveur
            try {
                serverSocket.close();
            }
            catch(Exception e) {
            }
        }
        catch (IOException e) {
            String msg = dat.format(new Date()) + " Exception sur le ServerSocket: " + e + "\n";
            display(msg);
        }
    }      
/*************************************************************/  
    //On l'appele au niveau du Serveur GUI
    protected void ArretServeur() {
    	ok = false;
    }
    private void display(String msg) {
       String disc = dat.format(new Date()) + "" + msg;
            System.out.println(disc);
        	SC.appendEvent(disc + "\n");
    }
    
    
    
    
/*************************************************************/  
    private void broadcast(String mess) {
        String time = dat.format(new Date());
        String message = time + "  " + mess + "\n";
            System.out.print(message);
        	SC.AfficherDscussion(message);
        
        for(int i =0; i<Liste.size(); i++) {
            ClientThread ct = Liste.get(i);
            if(ct.writeMsg(message)==false) {
            	Liste.remove(i);
/**********************EN cas de deconnection du client***************************************/     
                display("Client est deconnecte " + ct.username);
            }
        }
/*************************************************************/  
    }
    
/************************Suppression du client deconnecter de la liste*************************************/  
    private synchronized void remove(int id) {
        for(int i = 0; i < Liste.size(); ++i) {
            ClientThread ct = Liste.get(i);
            if(ct.id == id) {
            	Liste.remove(i);
                return;
            }

        }

    }


/*********************ClientThread****************************************/  
    class ClientThread extends Thread {
        private Socket socket;// la socket où écouter / ecrire
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private int id;
        private String username;
        private Chat cm;
        private String date;
        
        //constructor
        private ClientThread(Socket socket) {
            this.socket = socket;
            System.out.println("Connection d'un nouveau client");
            try
            {
                out = new ObjectOutputStream(socket.getOutputStream());
                in  = new ObjectInputStream(socket.getInputStream());
                username = (String) in.readObject();
              display(username + "*******vient de se connecter");
            }
            catch (IOException e) {
                display("Exception sur les Input/Output: " + e);
            }
           catch (ClassNotFoundException e) {
            }
            date = new Date().toString() + "\n";
        }

        public void run() {
            boolean ok = true;
            while(ok) {
                try {
                    cm = (Chat) in.readObject();
                }
                catch (IOException e) {
                    display(username + " Error sur le InputStream: " + e);//Serveur GUI          
                }

                catch(ClassNotFoundException e2) {
                }
                String message = cm.getMessage();
               // switch(cm.getType()) {
               // case Chat.IDMessage:
                    broadcast(username + ": " + message);
                  //  break;
               // }
            }
            remove(id);
            try {
				out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    private boolean writeMsg(String msg) {
            try {
                out.writeObject(msg);
            }
            catch(IOException e) {
                display("Probleme de d'envoyer le message a " + username);          
            }
            return true;
        }
    }
}
