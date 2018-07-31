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

    private ArrayList arrImage = new ArrayList();
    private File file;
    private ArrayList picturePath;
    String folderName;
    ArrayList restoreImagespaths;

    public FileUtil(){
        restoreImagespaths=new ArrayList();
    }


    public ArrayList<String> getFilesFromDir(String folderName) {
        this.folderName = folderName;
        file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);//.GalleryApp
        Log.i("filename", String.valueOf(file));
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                File[] dirFile = file.listFiles();
                for (int i = 0; i < dirFile.length; i++) {
                    arrImage.add(dirFile[i].toString());
                }
            }
        }
        return arrImage;
    }
    public boolean deletFiles() {
        boolean result = false;

        for (int i = 0; i < picturePath.size(); i++) {
            File files = new File(String.valueOf(picturePath.get(i)));
            if (files.exists()) {
                result = files.delete();
            } else {
                result = false;
            }
        }
        return result;
    }

    public boolean copyFileToApp(ArrayList picturePath, ArrayList lastName) {//sourcefile=moveFile
        File filePathName = new File(Environment.getExternalStorageDirectory() + "/" + ".GalleryApp");
        this.picturePath = picturePath;
        InputStream in = null;
        OutputStream out = null;

        boolean result = false;
        for (int i = 0; i < picturePath.size(); i++) {

            try {
                in = new FileInputStream(String.valueOf(picturePath.get(i)));
                out = new FileOutputStream(filePathName + "/" + lastName.get(i));
                String newPath = this.file + "/" + lastName.get(i);
                arrImage.add(newPath);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean makeDir(String folderName) {
        File createFolder = new File(Environment.getExternalStorageDirectory(), folderName);
        if (createFolder.exists()) {
            return false;
        } else {
            return createFolder.mkdir();
        }
    }

    public boolean restoreDone(ArrayList adapterItemPosition,ArrayList lastUsedImagePaths){
        restoreImagespaths.clear();
        if (adapterItemPosition!=null && lastUsedImagePaths!=null)
        {
            for (int i=0;i<adapterItemPosition.size();i++)
            {
            restoreImagespaths.add(lastUsedImagePaths.get(i));
            }
        }
        else
        {
            return false;
        }
    return true;
    }
}
