package com.lsl.d;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DowloadImpl implements IDowload {

    private static final int CONNECT_TIMEOUT = 5 * 1000;
    private static final int READ_TIMEOUT = 5 * 1000;
    private static final int BUFFER_SIZE = 32 * 1024;

    @Override
    public InputStream getStream(String uri) throws IOException {
        HttpURLConnection connection = createConnection(uri);
        int retry = 0;
        while (connection.getResponseCode() / 100 == 3 && retry < 3) {
            connection = createConnection(connection.getHeaderField("Location"));
            retry++;
            System.out.println("重试" + retry);
        }

        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            connection.getErrorStream().close();
            throw e;
        }
        return new ContentLengthInputStream(new BufferedInputStream(inputStream, BUFFER_SIZE),
                connection.getContentLength());
    }


    private HttpURLConnection createConnection(String uri) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        return connection;
    }
}
