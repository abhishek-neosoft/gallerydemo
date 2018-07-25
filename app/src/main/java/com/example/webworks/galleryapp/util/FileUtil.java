package com.example.webworks.galleryapp.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class FileUtil {

    public ArrayList arrImage = new ArrayList();
    File file;
    public ArrayList<String> getFilesFromDir(String folderName){

        file= new File(Environment.getExternalStorageDirectory() + "/"+folderName);
        if (file.isDirectory())
        {
            File[] dirFile = file.listFiles();
            for (int i=0;i<dirFile.length;i++)
            {
                arrImage.add(dirFile[i].toString());
            }
        }
        return arrImage;
    }

    public Boolean deletFiles(String path){
        File files=new File(path);

        if (files.exists())
        {
            return files.delete();
        }
        return false;
    }
    public void copyFile(File sourceFile, File destFile) throws IOException {
        OutputStream out;
        File file = new File(destFile + "Folder Name" + File.separator + sourceFile);
        file.createNewFile();
        out = new FileOutputStream(file);

        out.write(0);
        out.close();
    }

}
