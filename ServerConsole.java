import java.io.*;

public class ServerConsole extends Thread {
    private String console;

    public ServerConsole(){
        this.start();
    }

    public void run(){
        while(true){
            try {
                System.out.print("Console: ");
                BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ) );
                console = keyboard.readLine();
                if (console.equals("//stop")){
                    Server.stopServer();
                    break;
                }
                if (console.equals("//list")){
                    Server.getUsersList().getClientsList();
                }
            } catch ( IOException e ) { e.printStackTrace(); }
        }
    }
}

