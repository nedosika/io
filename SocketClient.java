import java.net.Socket;
import java.net.InetAddress;
import java.io.*;

public class SocketClient {
    private final static String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа
    private final static int serverPort = Config.PORT; // здесь обязательно нужно указать порт к которому привязывается сервер

    private static String userName = "";
    static Socket socket = null;

    public static void main( String[] args ) {
        System.out.println("Вас приветствует клиент чата!");
        System.out.println("Введите свой ник и нажмите \"Enter\"");

        // Создаем поток для чтения с клавиатуры
        BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ) );
        try {
        // Ждем пока пользователь введет свой ник и нажмет кнопку Enter
            userName = keyboard.readLine();
            System.out.println();
        } catch ( IOException e ) { e.printStackTrace(); }

        try {
            try {
                InetAddress ipAddress = InetAddress.getByName( address ); // создаем объект который отображает вышеописанный IP-адрес
                socket = new Socket( ipAddress, serverPort ); // создаем сокет используя IP-адрес и порт сервера

                ObjectOutputStream objectOutputStream = new ObjectOutputStream( socket.getOutputStream() );
                ObjectInputStream objectInputStream = new ObjectInputStream(  socket.getInputStream() );

                new ServerListenerThread( objectOutputStream, objectInputStream );

                objectOutputStream.writeObject( new Message( userName, "User join to the chat(Auto-message)" ) );

                // Создаем поток для чтения с клавиатуры
                String message = null;
                System.out.println("Наберите сообщение и нажмите \"Enter\"");

                while (true) { // Бесконечный цикл

                    message = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.

                    if (message.equals("//list")){
                        for (String user : ServerListenerThread.usersList){System.out.println(user);}
                        message.
                    } else {
                        objectOutputStream.writeObject( new Message( userName, message ) ); // отсылаем введенную строку текста серверу.
                    }

                    if (message.equals("//end")){
                        break;
                    }
                }
            } catch ( Exception e ) { e.printStackTrace(); }
        } finally {
            try {
                if ( socket != null ) { socket.close(); }
            } catch ( IOException e ) { e.printStackTrace(); }
        }
    }
}
