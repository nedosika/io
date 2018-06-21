import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int mLevel;
    private int mHelth;
    private Vector2 mPosition;

    public Client(Socket socket){
        this.socket = socket;
        this.mHelth = 100;
        this.mLevel = 0;
        this.mPosition = new Vector2();
    }

    public Client(Socket socket , ObjectOutputStream oos , ObjectInputStream ois ){
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public ObjectOutputStream getThisObjectOutputStream() {
        return this.oos;
    }

    public ObjectInputStream getThisObjectInputStream() {
        return this.ois;
    }

    public void setThisObjectOutputStream(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public void setThisObjectInputStream(ObjectInputStream ois) {
        this.ois = ois;
    }

    public Vector2 getmPosition() {
        return mPosition;
    }
    public void setmPosition(Vector2 mPosition) {
        this.mPosition = mPosition;
    }

    public int getmLevel() {
        return mLevel;
    }

    public void setmLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public int getmHelth() {
        return mHelth;
    }

    public void setmHelth(int mHelth) {
        this.mHelth = mHelth;
    }

}
