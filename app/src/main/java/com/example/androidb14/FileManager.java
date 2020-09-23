package com.example.androidb14;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileManager {

    private InputStream in;

    public File[] getFiles(String path) {
        File f = new File(path);
        if (f.isDirectory()) {
            return f.listFiles();
        }

        return null;
    }

    private String download(String link, String type) throws IOException {

        URL url = new URL(link);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();

        String path = Environment.getExternalStorageDirectory().getPath() + "/demo/" + System.currentTimeMillis() + type;
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] b = new byte[1024];
        int count = in.read(b);
        while (count > 0) {
            outputStream.write(b, 0, count);
            count = in.read();
        }

        in.close();
        outputStream.close();
        return path;
    }

    public void download(final String link, final String type, final FileDownloadCallBack callBack){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = download(link, type);
                    callBack.onSuccess(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onFail(e);
                }
            }
        });
        thread.start();
    }

    public interface FileDownloadCallBack {
        void onSuccess(String path);

        void onFail(Exception ex);
    }
}
