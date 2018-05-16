package com.lsl.d;

import java.io.File;

public class Maintest {

    public static void main(String args[]) {

        DowloadTask task = new DowloadTask("https://github.com/shadowsocks/ShadowsocksX-NG/releases/download/v1.7.1/ShadowsocksX-NG.1.7.1.zip",
                new File("ShadowsocksX-NG.1.7.1.zip"), new DowloadImpl(),
                new DowloadProgressListener() {
                    @Override
                    public void onProgressUpdate(String uri, int current, int total) {
                        System.out.println("current:" + current + " total:" + total);
                    }
                });

        new Thread(task).start();
    }
}
