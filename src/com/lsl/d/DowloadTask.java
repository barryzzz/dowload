package com.lsl.d;

import java.io.*;

public class DowloadTask implements Runnable, IoUtils.CopyListener {

    private final IDowload dowloader;
    private final String uri;
    private final DowloadProgressListener progressListener;

    private static final int BUFFER_SIZE = 32 * 1024;

    private final File filepath;


    public DowloadTask(String uri, File filepath, IDowload dowloader, DowloadProgressListener progressListener) {
        this.dowloader = dowloader;
        this.uri = uri;
        this.progressListener = progressListener;
        this.filepath = filepath;
    }


    @Override
    public void run() {
        try {
            dowloadApk();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常了");
        }
    }


    private boolean dowloadApk() throws IOException {
        InputStream is = dowloader.getStream(uri);
        return save(uri, is, this);
    }

    private boolean save(String uri, InputStream is, IoUtils.CopyListener listener) throws IOException {
        File file = getFile();
        boolean isload = false;
        try {
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
            try {
                isload = IoUtils.copyStream(is, outputStream, listener, BUFFER_SIZE);
            } finally {
                IoUtils.closeSilently(outputStream);
            }
        } finally {
            IoUtils.closeSilently(is);
        }
        return isload;

    }

    private File getFile() {
        return filepath;
    }

    @Override
    public boolean onBytesCopied(int current, int total) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                progressListener.onProgressUpdate(uri, current, total);
            }
        };
        r.run();
        return true;
    }
}
