package com.example.webworks.galleryapp.home;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import com.example.webworks.galleryapp.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomePresenter {

   private FileUtil fileUtil;
   private HomeView mView;

   HomePresenter(HomeView mView){
       fileUtil= new FileUtil();
       this.mView=mView;
   }

   public void getFileFromFolder(String folderName){
       mView.setImagesToAdapert(fileUtil.getFilesFromDir(folderName));
   }

    public void add(String picturePath) {
       fileUtil.arrImage.add(picturePath);
    }

    public void copyFile(String picturePath,String folderName) throws IOException {
       fileUtil.copyFile(new File(picturePath),new File(folderName));
    }
}
