package com.example.webworks.galleryapp.util;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileUtil {

    public ArrayList arrImage = new ArrayList();
    File file;
    String picturePath;
    String folderName;

    public ArrayList<String> getFilesFromDir(String folderName) {
        this.folderName=folderName;
        file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        Log.i("filename", String.valueOf(file));
        if (file.isDirectory()) {
            File[] dirFile = file.listFiles();
            for (int i = 0; i < dirFile.length; i++) {
                arrImage.add(dirFile[i].toString());
            }
        }
        return arrImage;
    }

    public boolean deletFiles() {
        File files = new File(picturePath);
        if (files.exists()) {
            files.delete();
            return true;
        }
        return false;
    }

    public boolean copyFile(String picturePath, String lastName) {//sourcefile=moveFile
        this.picturePath=picturePath;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(picturePath);
            out = new FileOutputStream(this.file + "/" + lastName);
            String newPath = this.file + "/" + lastName;
            arrImage.add(newPath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hideCurrentFolder(){

        File hideFile = new File(Environment.getExternalStorageDirectory() + "/"+ "."+folderName);
        boolean sucess = this.file.renameTo(hideFile);
        if (sucess){
            return true;
        }
        return false;
    }
}
