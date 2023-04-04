package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, List<String>> queryParams = new HashMap<>();
    private final StringBuffer body = new StringBuffer();

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
            instance.path = parts[1].split("\\?")[0];
            instance.parseQueryParams(requestLine);

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

    private void parseQueryParams(String requestLine) {
        final List<NameValuePair> pairs = URLEncodedUtils.parse(requestLine, StandardCharsets.UTF_8);
        for(NameValuePair pair : pairs) {
            List<String> values = queryParams.get(pair.getName());
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(pair.getValue());
            queryParams.put(pair.getName(), values);
        }
    }

    public List<String> getQueryParam(String name) {
        return queryParams.get(name);
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

    public Map<String, List<String>> getQueryParams() {
        return queryParams;
    }
}
