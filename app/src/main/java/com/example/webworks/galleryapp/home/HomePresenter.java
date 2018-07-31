package com.example.webworks.galleryapp.home;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.webworks.galleryapp.util.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;

public class HomePresenter{

    private FileUtil fileUtil;
    private HomeView mView;
    ArrayList arrUri;
    ArrayList lastName;
    Context context;
    ArrayList imagesEncodedList= new ArrayList();;
    ArrayList picturePath;
    ArrayList restoreImagespaths=new ArrayList();;

    HomePresenter(HomeView mView, Context context) {
        fileUtil = new FileUtil();
        this.mView = mView;
        this.context = context;
        lastName=new ArrayList();

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

    private void copyAndDeleteFiles(ArrayList picturePath, ArrayList lastName) {
        this.picturePath=picturePath;
        if (fileUtil.copyFileToApp(picturePath, lastName) && fileUtil.deletFiles()) {
            mView.addedFiles();
        } else {
            mView.failedToaddedFiles();
        }
    }
    public void onResultDataAndRecieveData(Intent data) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (data.getClipData() != null) {
            ClipData mClipData = data.getClipData();
            arrUri = new ArrayList<Uri>();
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item item = mClipData.getItemAt(i);
                Uri uri = item.getUri();
                arrUri.add(uri);
                Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    File file = new File(picturePath);
                    lastName.add(file.getName());
                    imagesEncodedList.add(picturePath);
                    cursor.close();
                }
            }
            copyAndDeleteFiles(imagesEncodedList,lastName);
        }
        Log.i("arraysize", String.valueOf(imagesEncodedList.size()));
    }
    public void restoreData(ArrayList adapterItemPosition){
        restoreImagespaths.clear();

        if (adapterItemPosition!=null && imagesEncodedList!=null)
        {
            for (int i=0;i<adapterItemPosition.size();i++)
            {
                restoreImagespaths.add(imagesEncodedList.get(i));
            }
            if (restoreImagespaths!=null)
            {
                if (fileUtil.restoreBackToPlace(restoreImagespaths))
                {

                }
            }

        }
        else
        {
            mView.restoreUnSuccessfully();
        }
    }

}
