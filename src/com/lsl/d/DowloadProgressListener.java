package com.lsl.d;

public interface DowloadProgressListener {
    void onProgressUpdate(String uri, int current, int total);
}
