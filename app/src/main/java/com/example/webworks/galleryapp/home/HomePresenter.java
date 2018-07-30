package com.example.webworks.galleryapp.home;

import com.example.webworks.galleryapp.util.FileUtil;
import java.util.ArrayList;

public class HomePresenter {

    private FileUtil fileUtil;
    private HomeView mView;

    HomePresenter(HomeView mView) {
        fileUtil = new FileUtil();
        this.mView = mView;
    }

    public boolean createFolder(String folderName) {
        if (fileUtil.makeDir(folderName))
        {
            mView.createFolder();
        }
        return true;
    }

    public void getFileFromFolder(String folderName) {
        mView.setImagesToAdapert(fileUtil.getFilesFromDir(folderName));
    }

    public void copyAndDeleteFiles(ArrayList picturePath, ArrayList lastName) {
        if (fileUtil.copyFile(picturePath, lastName) && fileUtil.deletFiles()) {
            mView.addedFiles();
        } else {
            mView.failedToaddedFiles();
        }
    }
}
