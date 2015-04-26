package mypackage;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

/**
 * Created by saikat on 4/24/15.
 */
public class ServerProgram {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(5050);
        Socket socket = serverSocket.accept();
        GZIPInputStream gzipin = new GZIPInputStream(socket.getInputStream());
        ObjectInputStream objReader = new ObjectInputStream(gzipin);
        message number = (message) objReader.readObject();


        message number1 = (message) objReader.readObject();
        System.out.println(number.data);
        System.out.println(number1.data);
    }

}
