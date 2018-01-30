package Test2;

import java.net.*;
import java.io.*;
import java.util.*;
public class Client  {
    private ObjectInputStream in;       
    private ObjectOutputStream out; 
    private Socket socket;
    private FormClient1 fc;
    private String server, username;
    private ServerThread svTh;
    private int port;

    Client(String server, int port, String username, FormClient1 fc) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.fc = fc;
    }
    
    
    public boolean start() {
        try {
            socket = new Socket(server, port);
        }

        catch(Exception ec) {

           System.out.println("Probleme de connection avec le serveur");
            return false;
        }
         
     System.out.println("l'acceptation de la connection" + socket.getInetAddress() + ":" + socket.getPort());

        try
        {
            in  = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException eIO) {      
            System.out.println("erreuuur avec le in et out");
            return false;

        }

        svTh.start();
        try
        {

            out.writeObject(username);
        }
        catch (IOException eIO) {
           // disconnect();
            return false;

        }
        return true;

    }
   public void EnvoyerMssg(Chat message) {

        try {
            out.writeObject(message);
        }
        catch(IOException e) {       
        }
    }
   private void display(String msg) {

       if(fc == null)
           System.out.println(msg);      // println in console mode
       else
           fc.append(msg + "\n");      // append to the ClientGUI JTextArea (or whatever)
   }
//    private void disconnect() {
//
//        try {
//            if(in != null) in.close();
//        }
//        catch(Exception e) {} 
//        try {
//            if(out != null) out.close();
//        }
//        catch(Exception e) {} 
//        try{
//
//            if(socket != null) socket.close();
//
//        }
//        catch(Exception e) {} 
//
//      
//    }



}
