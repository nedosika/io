import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Time;

public class ClientThread extends Thread {

    private final static int DELAY = 30000;

    private Socket socket;
    private Message message;
    private String login;
    private int inPacks = 0;
    private int outPacks = 0;
    private boolean flag = false;
    private Timer timer;

    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }

    public void run() {
		try {
			final ObjectInputStream inputStream   = new ObjectInputStream(this.socket.getInputStream());
			final ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream());

			this.message = (Message) inputStream.readObject();
			this.login = this.message.getLogin();

			if (!this.message.getMessage().equals(Config.HELLO_MESSAGE)) {
				System.out.println("[" + this.message.getLogin() + "]: " + this.message.getMessage());
				Server.getChatHistory().addMessage(this.message);
			} else {
				for(Message message : Server.getChatHistory().getHistory()){
					outputStream.writeObject(message);
				}

				this.broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been connect"));
			}
			Server.getUsersList().addUser(login, socket, outputStream, inputStream);

			this.message.setOnlineUsers(Server.getUsersList().getUsers());
			this.broadcast(Server.getUsersList().getClientsList(), this.message);

			this.timer = new Timer(DELAY, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (inPacks == outPacks) {
							outputStream.writeObject(new Ping());
							outPacks++;
							System.out.println(outPacks + " out");
						} else {
							throw new SocketException();
						}
					} catch (SocketException ex1) {
						System.out.println("packages not clash");
						System.out.println(login + " disconnected!");
						Server.getUsersList().deleteUser(login);
						broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been disconnect", Server.getUsersList().getUsers()));
						flag = true;
						timer.stop();
					} catch (IOException ex2) {
						ex2.printStackTrace();
					}
				}
			});

			this.timer.start();
			outputStream.writeObject(new Ping());
			this.outPacks++;
			System.out.println(outPacks + " out");

			while (true) {
				if(this.flag) {
					this.flag = false;
					break;
				}
				this.message = (Message) inputStream.readObject();

				if (this.message instanceof Ping) {
					this.inPacks++;
					System.out.println(this.inPacks + " in");
				} else if (!this.message.getMessage().equals(Config.HELLO_MESSAGE) && !this.message.getMessage().equals(Config.END_MESSAGE)) {
					System.out.println("[" + login + "]: " + this.message.getMessage());
					Server.getChatHistory().addMessage(this.message);
				} else {
					for(Message message : Server.getChatHistory().getHistory()){
						outputStream.writeObject(message);
					}

					this.broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been connect"));
				}
					this.message.setOnlineUsers(Server.getUsersList().getUsers());

				if (!(this.message instanceof Ping) && !this.message.getMessage().equals(Config.HELLO_MESSAGE)) {
					if (this.message.getMessage().equals(Config.END_MESSAGE)){
						System.out.println(login + " disconnected!");
						Server.getUsersList().deleteUser(login);
						broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been disconnected!", Server.getUsersList().getUsers()));
						timer.stop();
						flag = true;
					} else {
						System.out.println("Send broadcast Message: " + this.message.getMessage() );
						this.broadcast(Server.getUsersList().getClientsList(), this.message);
					}
				}
			}

		} catch (SocketException e) {
			System.out.println(login + " disconnected!");
			Server.getUsersList().deleteUser(login);
			broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been disconnect", Server.getUsersList().getUsers()));
			this.timer.stop();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

    private void broadcast(ArrayList<Client> clientsArrayList, Message message) {
		try {
			for (Client client : clientsArrayList) {
				client.getThisObjectOutputStream().writeObject(message);
			}
		} catch (SocketException e) {
			System.out.println("in broadcast: " + login + " disconnected!");
			Server.getUsersList().deleteUser(login);
			this.broadcast(Server.getUsersList().getClientsList(), new Message("Server-Bot", "The user " + login + " has been disconnected", Server.getUsersList().getUsers()));
			timer.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
