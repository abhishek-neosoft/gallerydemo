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
import java.util.Arrays;
import java.util.List;

public class FileUtil {

    private ArrayList addToAdapert;
    private File file;
    String folderName;
    ArrayList restoreImagespaths;
    InputStream in = null;
    OutputStream out = null;
    ArrayList previousPath;
    ArrayList inputGalleryPath;

    public FileUtil() {
        restoreImagespaths = new ArrayList();
        previousPath = new ArrayList();
        inputGalleryPath = new ArrayList();
        addToAdapert = new ArrayList();
    }


    public ArrayList<String> getFilesFromDir(String folderName) {
        this.folderName = folderName;
        file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);//.GalleryApp
        Log.i("filename", String.valueOf(file));
        if (file.isDirectory()) {
            if (file.listFiles() != null) {
                File[] dirFile = file.listFiles();
                for (int i = 0; i < dirFile.length; i++) {
                    addToAdapert.add(dirFile[i].toString());
                    Log.i("arrhjhdfFD", String.valueOf(addToAdapert));//[/storage/emulated/0/.GalleryApp/ten.jpg]
                }
            }
        }
        return addToAdapert;
    }

    public boolean makeDir(String folderName) {
        File createFolder = new File(Environment.getExternalStorageDirectory(), folderName);
        if (createFolder.exists()) {
            return false;
        } else {
            return createFolder.mkdir();
        }
    }

    public boolean restoreBackToPlace(ArrayList gettingPath,ArrayList restorePath) {

        ArrayList inputpaths = new ArrayList();


        boolean result = false;
        File restoreFile = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        File[] listOfPath = restoreFile.listFiles();
        for (int i = 0; i < listOfPath.length; i++) {
            inputpaths.add(listOfPath[i]);
        }

        for (int j = 0; j < restorePath.size(); j++) {

                try {


                    in = new FileInputStream(gettingPath.get(j).toString());
                    out = new FileOutputStream(String.valueOf(restorePath.get(j)));
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    result = true;
                    out.flush();
                    out.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputpaths.clear();
            }

        return result;
    }

    public boolean deleteFileAfetrResoter(ArrayList gettingPath) {

        boolean result = false;

        for (int i=0;i<gettingPath.size();i++)
        {
            File files =new File(gettingPath.get(i).toString());
            if (files.exists())
            {
                if (files.delete())
                {
                    result=true;

                }

            }
        }
        addToAdapert.removeAll(gettingPath);

        return result;
    }

    // oldPath = to read as input -- newPath - to copy with oldPath lastname
    //find lastname of oldPath
    //newPath == your folder name path

    public boolean copyFile(ArrayList inputGalleryPath, String outputNewPath) {//input     output
        this.inputGalleryPath = inputGalleryPath;
        boolean result = false;
        ArrayList lastName = new ArrayList();

        for (int i = 0; i < inputGalleryPath.size(); i++) {
            File file = new File(String.valueOf(inputGalleryPath.get(i)));
            Log.i("parrentFile", file.getParent());
            lastName.add(file.getName());

            try {
                in = new FileInputStream(inputGalleryPath.get(i).toString());
                out = new FileOutputStream(outputNewPath + "/" + lastName.get(i));
                String pathToAdd = this.file + "/" + lastName.get(i);
                addToAdapert.add(pathToAdd);
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

    public boolean deleteImages() {
        boolean result = false;
        for (int i = 0; i < inputGalleryPath.size(); i++) {
            File files = new File(inputGalleryPath.get(i).toString());
            if (files.exists()) {
                result = files.delete();
            } else {
                result = false;
            }
        }
        return result;
    }
}
