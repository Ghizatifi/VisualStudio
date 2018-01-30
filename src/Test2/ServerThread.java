package Test2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ServerThread extends Thread {
	 private FormClient fc;
	   private ObjectInputStream in;       
	    private ObjectOutputStream out; 
	 public void run() {
         while(true) {
             try {
                 String msg = (String) in.readObject();
                 // if console mode print the message and add back the prompt
                 if(fc == null) {

                     System.out.println(msg);

                     System.out.print("}}");

                 }
      

             }

             catch(IOException e) {
               
             }
             catch(ClassNotFoundException e2) {
             }
         }
     }
 }

