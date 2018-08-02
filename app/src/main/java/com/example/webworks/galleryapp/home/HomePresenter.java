package com.example.webworks.galleryapp.home;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.webworks.galleryapp.database.SaveGalleryPath;
import com.example.webworks.galleryapp.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

public class HomePresenter {

    private FileUtil fileUtil;
    private HomeView mView;
    ArrayList lastName;
    Context context;
    ArrayList imagesEncodedList = new ArrayList();
    ArrayList restoreImagespaths = new ArrayList();
    ArrayList oldPath;
    SaveGalleryPath saveGalleryPath;

    HomePresenter(HomeView mView, Context context) {
        fileUtil = new FileUtil();
        this.mView = mView;
        this.context = context;
        lastName = new ArrayList();
        oldPath = new ArrayList();
        saveGalleryPath=new SaveGalleryPath(context);

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
            for (int i=0;i<oldPath.size();i++)
            {
                saveGalleryPath.insertData(oldPath.get(i).toString());
            }

            copyAndDelete(oldPath, newPath);
        }
    }

    public void restoreData(ArrayList adapterItemPosition) {   //5,6,7

        ArrayList restoreImagesNewpaths = new ArrayList();
        for (int i=0;i<adapterItemPosition.size();i++) {
            Cursor result = saveGalleryPath.fetchData((Integer) adapterItemPosition.get(i));
            if (result.getCount() == 0) {
                return;
            } else {
                StringBuffer buffer = new StringBuffer();
                do {
                    buffer.append(result.getString(1));
                    Log.i("datafecth", buffer.toString());
                } while (result.moveToNext());
                restoreImagesNewpaths.add(buffer);
                Log.i("restoreImagesPath", String.valueOf(restoreImagesNewpaths));

                //  Log.i("parrentFile",file.getParent());
            }
        }

        if (fileUtil.restoreBackToPlace(restoreImagesNewpaths)&& fileUtil.deleteFileAfetrResoter(restoreImagesNewpaths))
        {
            for (int i=0;i<adapterItemPosition.size();i++)
            {
                int deleteRow=saveGalleryPath.deletePathFromDB((Integer) adapterItemPosition.get(i));
                if (deleteRow > 0)
                {
                    //delete record
                }
                else
                {
                    //not deleted
                }
            }

            mView.restoreSuccessfully();
        }
        else
        {
            mView.restoreUnSuccessfully();
        }


    }
}
