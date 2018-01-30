import java.io.*;


/*****les diff�rents types de messages qui seront �chang�s entre les
				Clients et le serveur***/

public class Chat implements Serializable {
/*************************************************************/   
    protected static final long serialVersionUID = 1L;
    static final int  IDMessage = 1;
    private int type;
    private String message;//Le message actuele
/*************************************************************/   
    Chat(int type, String message) {
        this.type = type;
        this.message = message;
    }
    Chat(String message) {
        this.message = message;
    }
/*************************************************************/   
    int getType() {
        return type;
   }
/*************************************************************/   
    String getMessage() {
        return message;
    }
}
