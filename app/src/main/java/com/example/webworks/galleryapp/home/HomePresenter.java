package com.example.webworks.galleryapp.home;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.webworks.galleryapp.util.FileUtil;
import java.util.ArrayList;

public class HomePresenter {

    private FileUtil fileUtil;
    private HomeView mView;
    ArrayList lastName;
    Context context;
    ArrayList imagesEncodedList = new ArrayList();
    ArrayList restoreImagespaths = new ArrayList();
    ArrayList oldPath;

    HomePresenter(HomeView mView, Context context) {
        fileUtil = new FileUtil();
        this.mView = mView;
        this.context = context;
        lastName = new ArrayList();
        oldPath = new ArrayList();

    }

    public boolean createFolder(String folderName) {
        if (fileUtil.makeDir(folderName)) {
            mView.createFolder();
        }
        return true;
    }

    public void getFileFromFolder(String folderName) {
        mView.setImagesToAdapert(fileUtil.getFilesFromDir(folderName));
    }

    private void copyAndDelete(ArrayList oldPath, String newPath) {

        if (fileUtil.copyFile(oldPath, newPath) && fileUtil.deleteImages()) {
            mView.addedFiles();
        } else {
            mView.failedToaddedFiles();
        }
    }

    public void onResultDataAndRecieveDataFromGallery(Intent data, String newPath) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (data.getClipData() != null) {
            ClipData mClipData = data.getClipData();
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    oldPath.add(picturePath);
                    cursor.close();
                }
            }
            copyAndDelete(oldPath, newPath);
        }
    }

    public void restoreData(ArrayList adapterItemPosition) {
        restoreImagespaths.clear();

        if (adapterItemPosition != null && imagesEncodedList != null) {
            for (int i = 0; i < adapterItemPosition.size(); i++) {
                restoreImagespaths.add(imagesEncodedList.get(i));
            }
            if (restoreImagespaths != null) {
                if (fileUtil.restoreBackToPlace(restoreImagespaths)) {

                }
            }

        } else {
            mView.restoreUnSuccessfully();
        }
    }
}
