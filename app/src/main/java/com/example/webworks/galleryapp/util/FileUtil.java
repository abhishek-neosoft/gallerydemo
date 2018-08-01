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
    String folderName;
    ArrayList restoreImagespaths;
    InputStream in = null;
    OutputStream out = null;
    ArrayList previousPath;
    ArrayList oldPath;

    public FileUtil(){
        restoreImagespaths=new ArrayList();
        previousPath=new ArrayList();
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
    public boolean makeDir(String folderName) {
        File createFolder = new File(Environment.getExternalStorageDirectory(), folderName);
        if (createFolder.exists()) {
            return false;
        } else {
            return createFolder.mkdir();
        }
    }

    public boolean restoreBackToPlace(ArrayList restoreImagespaths){

        File restoreFile =new File(Environment.getExternalStorageDirectory() +"/"+folderName);
        File[] restoreListFiles = restoreFile.listFiles();
        for (int i=0; i<restoreListFiles.length;i++)
        {
            previousPath.add(restoreListFiles[i].toString());
        }
        for (int j=0;j<restoreImagespaths.size();j++)
        {
            try {
                in = new FileInputStream(String.valueOf(previousPath.get(j)));
                out = new FileOutputStream(String.valueOf(restoreImagespaths.get(j)));
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    return true;
    }

    // oldPath = to read as input -- newPath - to copy with oldPath lastname
    //find lastname of oldPath
    //newPath == your folder name path

    public boolean copyFile(ArrayList oldPath , String newPath){
        this.oldPath=oldPath;
        boolean result = false;
        ArrayList lastName =new ArrayList();
        for (int i=0;i<oldPath.size();i++)
        {
            File file = new File(String.valueOf(oldPath.get(i)));
            lastName.add(file.getName());

            try {
                in=new FileInputStream(String.valueOf(oldPath.get(i)));
                out=new FileOutputStream(newPath +"/"+lastName.get(i));
                String pathToAdd = this.file + "/" + lastName.get(i);
                arrImage.add(pathToAdd);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean deleteImages()
    {
        boolean result = false;
        for (int i = 0; i < oldPath.size(); i++) {
            File files = new File(String.valueOf(oldPath.get(i)));
            if (files.exists()) {
                result = files.delete();
            } else {
                result = false;
            }
        }
        return result;
    }
}
