package com.example.webworks.galleryapp.home;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompatSideChannelService;
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
    ArrayList galleryPath;
    ArrayList getGalleryPathLastName;
    SaveGalleryPath saveGalleryPath;
    String galleryFolderPath;

    HomePresenter(HomeView mView, Context context) {
        fileUtil = new FileUtil();
        this.mView = mView;
        this.context = context;
        lastName = new ArrayList();
        galleryPath = new ArrayList();
        getGalleryPathLastName = new ArrayList();
        saveGalleryPath = new SaveGalleryPath(context);
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

    private void copyAndDelete(ArrayList inputGalleryPath, String outputNewPath) {

        if (fileUtil.copyFile(inputGalleryPath, outputNewPath) && fileUtil.deleteImages()) {
            mView.addedFiles();
        } else {
            mView.failedToaddedFiles();
        }
    }

    public void onResultDataAndRecieveDataFromGallery(Intent data, String newPath) {
        this.galleryFolderPath = newPath;
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
                    galleryPath.add(picturePath);
                    File file = new File(picturePath);
                    getGalleryPathLastName.add(file.getName());
                    cursor.close();
                }
            }
            for (int i = 0; i < galleryPath.size(); i++) {
                saveGalleryPath.insertData(galleryPath.get(i).toString(), getGalleryPathLastName.get(i).toString());
            }
            copyAndDelete(galleryPath, newPath);
            galleryPath.clear();
        }
    }

    public void fetchImagesWithPosition(ArrayList adapterItemPosition, String folderName) {

        File file = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        File[] fileArr = file.listFiles();
        ArrayList gettingPath = new ArrayList();
        ArrayList lastNameToMatchDB = new ArrayList();
        ArrayList DBPreviousPath = new ArrayList();
        for (int j = 0; j < adapterItemPosition.size(); j++) {
            gettingPath.add(String.valueOf(fileArr[(int) adapterItemPosition.get(j)]));
            File fetchLastName = new File(gettingPath.get(j).toString());
            lastNameToMatchDB.add(fetchLastName.getName());
        }


        for (int i=0;i<lastNameToMatchDB.size();i++)
        {
            Cursor cursor = saveGalleryPath.fetchPathWithLastName(lastNameToMatchDB.get(i).toString());
            if (cursor.moveToFirst())
            {
                StringBuffer buffer = new StringBuffer();
                do {
                    buffer.append(cursor.getString(cursor.getColumnIndex("GALLERY_PATH_LOCATION")));
                }while(cursor.moveToNext());
                DBPreviousPath.add(buffer);
            }
        }
        if (fileUtil.restoreBackToPlace(gettingPath,DBPreviousPath) && fileUtil.deleteFileAfetrResoter(gettingPath))
        {
            DBPreviousPath.clear();
            gettingPath.clear();
            for (int i=0;i<lastNameToMatchDB.size();i++)
            {
                saveGalleryPath.deleteData(lastNameToMatchDB.get(i).toString());
                mView.restoreSuccessfully();
            }
            lastNameToMatchDB.clear();

        }
        else {
            mView.restoreUnSuccessfully();
        }



        /*if (cursor.getCount() == 0) {
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            do{
                if (cursor.moveToNext())
                buffer.append(cursor.getString(cursor.getColumnIndex("GALLERY_PATH_LOCATION")));
            }while(cursor.moveToNext());

            Log.i("bufferr",buffer.toString());

        }*/
    }

}









































    /*public void restoreData(ArrayList adapterItemPosition) {   //5,6,7

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


    }*/

