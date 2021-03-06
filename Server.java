import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static UsersList list = new UsersList();
    private static ChatHistory chatHistory = new ChatHistory();

    public static void main(String[] args) {
        try {
            //Создаем слушатель
            ServerSocket socketListener = new ServerSocket(Config.PORT);
            System.out.println("Server started on " + Config.PORT );
            //Запускаем консоль управления сервером
            new ServerConsole();

            //Ждем подключения нового клиента
            while (true) {
                Socket client = null;
                while (client == null) {
                    client = socketListener.accept();
                }
                //Создаем новый поток, которому передаем сокет
                new ClientThread(client);
            }
        } catch (SocketException e) {
            System.err.println("Socket exception");
            e.printStackTrace();
        } catch (IOException e) {
                System.err.println("I/O exception");
            e.printStackTrace();
        }
    }

    public synchronized static UsersList getUsersList() {
	return list;
    }

    public synchronized static ChatHistory getChatHistory() {
        return chatHistory;
    }

    public static void stopServer(){
        System.exit(0);
    }
}
