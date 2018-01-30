package Test2;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;

import java.util.*;

public class Server {
    private ArrayList<ClientThread> list;
    private ServeurForm SF;
    private int port;
    private boolean go;
    private Socket socket;
    private SimpleDateFormat sdf;
    public Server(int port) {
      this.port=port;
   }
    public Server(int port, ServeurForm SF) {
        this.SF = SF;
        this.port = port;
        list = new ArrayList<ClientThread>();
   }
    private void display(String msg) {

        String time = sdf.format(new Date()) + " " + msg;

        if(SF == null)

            System.out.println(time);

        else

        	SF.appendEvent(time + "\n");

    }
    public void start() {
        go = true;
        try

        {
            ServerSocket serverSocket = new ServerSocket(port);
            while(go)
            {
                System.out.println("Le serveur a l'ecoute sur le port : " + port);
                display("Le serveur a l'ecoute sur le port : " + port);
                Socket socket = serverSocket.accept();      // accept connection
                if(!go)
                    break;
                ClientThread t = new ClientThread(socket);  // make a thread of it
                list.add(t);                                  // save it in the ArrayList
                t.start();
            }
            try {
                serverSocket.close();
                for(int i = 0; i < list.size(); ++i) {
                   ClientThread tc = list.get(i);
                    try {
                    tc.in.close();
                    tc.out.close();
                    tc.socket.close();
                    }
                    catch(IOException ioE) {
                    }
               }
            }
            catch(Exception e) {
            }
        }
        catch (IOException e) {

          
        }
    }      

    protected void stop() {

        go = false;
        try {

            new Socket("localhost", port);
        }

        catch(Exception e) {
        }

    }

    private synchronized void broadcast(String message) {
        String msg =  " Dit :" + message + "\n";
        ClientThread ctr = new ClientThread(socket);
        if(SF == null)
            System.out.print(msg);
        else
        	SF.appendRoom(msg); 
        for(int i = list.size(); --i >= 0;) {

            ClientThread ct = list.get(i);
            if(!ct.LireMessage(msg)) {
            	list.remove(i);
            }
        }
    }
    synchronized void remove(int id) {
        for(int i = 0; i < list.size(); ++i) {
            ClientThread ct = list.get(i);
            if(ct.id == id) {
            	list.remove(i);
                return;
            }
        }
    }

    public static void main(String[] args) {
        int PortN = 1234;

        switch(args.length) {

            case 1:

//                try {
//                	PortN = Integer.parseInt(args[0]);
//                }
//
//                catch(Exception e) {

                    System.out.println("Le numero de port est invalid");


                    return;
               // }

            case 0:

                break;                        
        }

 Server server = new Server(PortN);
        server.start();
    }

    

}
