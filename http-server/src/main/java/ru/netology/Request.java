package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private StringBuffer body = new StringBuffer();

    public static Request parse(String requestText) {
        Request instance;
        try(BufferedReader reader = new BufferedReader(new StringReader(requestText))) {
            instance = new Request();
            String requestLine = reader.readLine();
            final var parts = requestLine.split(" ");
            if (parts.length != 3) {
                return null;
            }
            instance.method = parts[0];
            instance.path = parts[1];

            String header = reader.readLine();
            while (header.length() > 0) {
                instance.addHeader(header);
                header = reader.readLine();
            }

            String bodyLine = reader.readLine();
            while (bodyLine != null) {
                instance.appendBodyLine(bodyLine);
                bodyLine = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return instance;
    }
    private Request() {
    }

    private void addHeader(String header) {
        int idx = header.indexOf(":");
        if (idx == -1) {
            return;
        }
        headers.put(header.substring(0, idx), header.substring(idx + 1));
    }

    private void appendBodyLine(String bodyLine) {
        body.append(bodyLine).append("\r\n");
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body.toString();
    }

    public String getPath() {
        return path;
    }
}
