package mypackage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

/**
 * Created by saikat on 4/25/15.
 */
public class ClientProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5050);
        GZIPOutputStream gzipout = new GZIPOutputStream(socket.getOutputStream());
        ObjectOutputStream objWriter = new ObjectOutputStream(gzipout);
        message m =  new message();
        m.data = 10;
        objWriter.writeObject(m);
        gzipout.finish();

        GZIPOutputStream gzipout1 = new GZIPOutputStream(socket.getOutputStream());
        ObjectOutputStream objWriter1 = new ObjectOutputStream(gzipout1);
        message m1 =  new message();
        m1.data = 20;
        objWriter1.writeObject(m1);
        gzipout.finish();
    }
}


