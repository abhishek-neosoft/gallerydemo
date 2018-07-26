package com.example.webworks.galleryapp.home;

import com.example.webworks.galleryapp.util.FileUtil;

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

    public void copyAndDeleteFiles(String picturePath, String lastName) {
        if (fileUtil.copyFile(picturePath, lastName) && fileUtil.deletFiles()) {
            mView.addedFiles();
        } else {
            mView.failedToaddedFiles();
        }
    }
}
