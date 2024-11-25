import java.io.*;
import java.net.*;


public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 8030;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("Ваш ход")) {
                    String move = console.readLine();
                    out.println(move);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
