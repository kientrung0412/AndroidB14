package com.example.androidb14;

import android.os.Environment;

import java.io.File;

public class FileManager {

    public File[] getFiles(String path) {
        File f = new File(path);
        if (f.isDirectory()){
            return  f.listFiles();
        }

        return null;
    }
}
