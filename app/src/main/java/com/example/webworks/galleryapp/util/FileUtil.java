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
                    Log.i("arrhjhdfFD", String.valueOf(arrImage));//[/storage/emulated/0/.GalleryApp/ten.jpg]
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

    public boolean restoreBackToPlace(ArrayList restorePath){

        ArrayList inputpaths =new ArrayList();
        boolean result = false;
        File restoreFile =new File(Environment.getExternalStorageDirectory() +"/"+folderName);
        File[] listOfPath = restoreFile.listFiles();
        for (int i=0;i<listOfPath.length;i++) {
            inputpaths.add(listOfPath[i]);
        }

                for (int j=0;j<restorePath.size();j++)
                    try {
                {
                    in = new FileInputStream(String.valueOf(inputpaths.get(j)));
                    out =new FileOutputStream(String.valueOf(restorePath.get(j)));
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    result=true;
                    out.flush();
                    out.close();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputpaths.clear();
    return result;
    }

    public boolean deleteFileAfetrResoter(ArrayList restorePath){
        boolean result=false;
        ArrayList deletePath = new ArrayList();
        File file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        File[] listFiles= file.listFiles();
        for (File listFile : listFiles) {
            deletePath.add(listFile.toString());
        }
        for (int i=0;i<restorePath.size();i++)
        {
            File files =  new File(String.valueOf(deletePath.get(i)));
            if (files.exists()) {
                result = files.delete();
                if (result)
                {
                    arrImage.remove(deletePath.get(i));
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    // oldPath = to read as input -- newPath - to copy with oldPath lastname
    //find lastname of oldPath
    //newPath == your folder name path

    public boolean copyFile(ArrayList oldPath , String newPath){ //input     output
        this.oldPath=oldPath;
        boolean result = false;
        ArrayList lastName =new ArrayList();
        for (int i=0;i<oldPath.size();i++)
        {
            File file = new File(String.valueOf(oldPath.get(i)));
            Log.i("parrentFile",file.getParent());
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
        lastName.clear();
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
