package Test2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;


class ClientThread extends Thread {
    private static int IDUser;
    Socket socket;
    Server ser;
    ObjectInputStream in;
    ObjectOutputStream out;
    int id;
    String nom;
    Chat cm;
    ClientThread(Socket socket) {
        id = ++IDUser;
        this.socket = socket;
        try
        {
        	//le flux d'entre
        	in  = new ObjectInputStream(socket.getInputStream());
        	//le flux de sortie
        	out = new ObjectOutputStream(socket.getOutputStream());
            // avoir le nom
            nom = (String) in.readObject();
         System.out.println("l'utilisateur :"+nom+ "vient de se connecter");
        }
        catch (IOException e) {
            System.out.println("erreuuur sur les flux");
            return;
        }
        catch (ClassNotFoundException e) {

        }
    }

    public void run() {
        boolean lance = true;
        while(lance) {
            try {
                cm = (Chat) in.readObject();
            }
            catch (IOException e) {
            	System.err.println("Erreuur de la part :"+nom);
                break;             
           }
            catch(ClassNotFoundException e2) {
               break;
            }
            String message = cm.getMessage();          
        }
        ser.remove(id);
        FermerCnx();
    }
    private void FermerCnx() {
       try {
            if(out != null)
            	out.close();
        }
        catch(Exception e) {}
        try {

            if(in != null)
            	in.close();
        }

        catch(Exception e) {};

        try {

            if(socket != null) 
            	socket.close();

        }

        catch (Exception e) {}

    }
public boolean LireMessage(String message) {

        // if Client is still connected send the message to it

        if(!socket.isConnected()) {
        	FermerCnx();
            return false;
        }
        try {
            out.writeObject(message);
        }
        catch(IOException e) {
        System.out.println("Probleme chez" + nom);
        }
        return true;
    }
}