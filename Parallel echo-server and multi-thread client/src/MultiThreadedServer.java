import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Сервер запущено...");
        ExecutorService pool = Executors.newFixedThreadPool(50); // Обмеження на 50 потоків
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String input = in.readLine();
                if (input != null) {
                    long startTime = System.currentTimeMillis();
                    String processedData = new StringBuilder(input).reverse().toString(); // Реверс рядка
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    out.println("Processed Data: " + processedData);
                    out.println("Processing Time (ms): " + duration);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}