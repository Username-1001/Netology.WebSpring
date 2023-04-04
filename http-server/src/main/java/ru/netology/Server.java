package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final String PATH = "src/main/resources/config.properties";
    private final int DEFAULT_PORT = 9999;
    private List<String> validPaths;
    private int port;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(64);
    private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();

    public Server() {
        try (FileReader fileReader = new FileReader(PATH)) {
            Properties properties = new Properties();
            properties.load(fileReader);
            port = Integer.parseInt(properties.getProperty("port"));
            validPaths = List.of(properties.getProperty("paths").split(";"));
        } catch (Exception e) {
            port = DEFAULT_PORT;
        }
    }

    public void start() {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }

    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers = handlers.get(method);
        if (methodHandlers == null) {
            methodHandlers = new ConcurrentHashMap<>();
            methodHandlers.put(path, handler);
            handlers.put(method, methodHandlers);
            return;
        }
        methodHandlers.put(path, handler);
    }

    private void handleConnection(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {

            StringBuilder requestText = new StringBuilder();
            String requestLine = in.readLine();
            while (requestLine != null) {
                requestText.append(requestLine);
                requestLine = in.readLine();
            }

            Request request = Request.parse(requestText.toString());
            if (request == null) {
                out.write((
                        "HTTP/1.1 400 Bad request\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                return;
            }
//          Далее предполагается, что на каждый метод и путь есть обработчик
//          Иначе зарегистрируем, например, дефолтный обработчик, который будет отвечать, что не удалось обработать запрос
//            server.addHandler("default", "default", new Handler() {
//                public void handle(Request request, BufferedOutputStream responseStream) {
//                    // TODO: handlers code
//                }
            handlers.get(request.getMethod()).get(request.getPath()).handle(request, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
