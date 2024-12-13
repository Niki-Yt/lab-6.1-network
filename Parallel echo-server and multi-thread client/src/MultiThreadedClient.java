import java.io.*;
import java.net.*;
import java.util.Random;

public class MultiThreadedClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        int clientCount = 1000;
        Thread[] threads = new Thread[clientCount];

        for (int i = 0; i < clientCount; i++) {
            threads[i] = new Thread(new ClientSession());
            threads[i].start();
        }

        for (int i = 0; i < clientCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Усі клієнтські сесії завершено.");
    }

    private static class ClientSession implements Runnable {
        @Override
        public void run() {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String randomString = generateRandomString(10);
                out.println(randomString);

                String processedData = in.readLine();
                String duration = in.readLine();

                System.out.println("Original: " + randomString);
                System.out.println(processedData);
                System.out.println(duration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String generateRandomString(int length) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            return sb.toString();
        }
    }
}