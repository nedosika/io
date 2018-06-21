import java.io.*;
import java.net.SocketException;

public class ServerListenerThread implements Runnable {
    private Thread thread = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;
    public static String[] usersList;

    public ServerListenerThread( ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream ) {
        this.objectOutputStream = objectOutputStream;
        this.objectInputStream = objectInputStream;

        thread = new Thread( this );
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message messageIn = (Message) objectInputStream.readObject();
                if ( messageIn instanceof Ping ) {
                    Ping ping = (Ping) messageIn;
                    objectOutputStream.writeObject( new Ping() );
                } else {
                    System.out.println(messageIn.getDate().toString() + "{" + 0 + "]" + messageIn.getLogin() + " : " + messageIn.getMessage() );
                    usersList = messageIn.getUsers();
                }
            }
        }
        catch ( SocketException e ) { e.getMessage(); }
        catch ( ClassNotFoundException e ) { e.getMessage(); }
        catch ( IOException e ) { e.getMessage(); }
    }
}