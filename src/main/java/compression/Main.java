package compression;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by saikat on 4/26/15.
 */
public class Main {
    static Socket s;
    private static class ServerWorker extends Thread {
        Socket s = null;
        public ServerWorker(Socket s) {
            this.s = s;
            setDaemon(false);
            start();
        }

        public void run() {
            try {
                // Build a Reader object that wraps the
                // (decompressed) socket input stream.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new CompressedBlockInputStream(
                                        s.getInputStream())));
//                String line = in.readLine();
//                while (line != null) {
//                    System.out.println(line);
//                    line = in.readLine();
//                }

                ObjectInputStream objReader = new ObjectInputStream(new CompressedBlockInputStream(s.getInputStream()));
                try {
                    Message number = (Message) objReader.readObject();
                    System.out.println(number.toString());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.flush();
                //System.out.println(number.toString());
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void initClientConn(int port) {
        try {
            s = new Socket("localhost", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeClientConn(int port) {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendData(int port, int i) throws IOException {
        // Connect to the server
        Socket s = new Socket("localhost", port);

        // Make a stream that compresses outgoing data,
        // sending a compressed block for every 1K worth of
        // uncompressed data written.
        //        CompressedBlockOutputStream compressed =
        //                new CompressedBlockOutputStream(
        //                        s.getOutputStream(), 1024);

        CompressedBlockOutputStream compressed =
                new CompressedBlockOutputStream(
                        s.getOutputStream(), 1024);


        /*
        // Build a writer that wraps the (compressed) socket
        // output stream
        PrintWriter out =
                new PrintWriter(new OutputStreamWriter(compressed));

        // Send across 1000 lines of output
        for (int i = 0; i < 1000; i++) {
            out.println("This is line " + (i + 1) +
                    " of the test output.");
        }

        // Note that if we don't close the stream, the last
        // block of data may not be sent across the connection. We
        // could also force the last block to be sent by calling
        // flush(), which would leave the socket connection open.
        out.close();
        */

        // my implementation
        ObjectOutputStream objWriter = new ObjectOutputStream(compressed);
        //Integer i = 10;
        Message m = new Message(0, MessageType.HW, i);
        objWriter.writeObject(m);
    //            objWriter.close();
        objWriter.flush();
        s.close();
    }

    private static class Server extends Thread {
        int port;
        public Server(int port) {
            this.port = port;
            setDaemon(true);
            start();
        }

        public void run() {
            try {
                // Accept connections, spawning a worker thread
                // when we get one.
                ServerSocket ss = new ServerSocket(port);
                while (true) {
                    ServerWorker worker =
                            new ServerWorker(ss.accept());
                }
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Pick an obscure default port. An alternate port
        // can be given as the first command line argument.
        int port = 11538;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        Server s = new Server(port);
//        initClientConn(port);
        sendData(port, 1);
        sendData(port, 2);
        try {
            s.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeClientConn(port);
    }
}
