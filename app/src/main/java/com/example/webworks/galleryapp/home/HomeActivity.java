package com.example.webworks.galleryapp.home;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.webworks.galleryapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, HomeView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.floating_bar)

    FloatingActionButton floatingActionButton;
    private String folderName = ".GalleryApp";
    private HomeAdapter adapter;
    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private HomePresenter presenter;
    private RecyclerView.LayoutManager layoutManager;
    final static int GALLERY_REQUEST_CODE = 120;
    final static int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        presenter = new HomePresenter(this);
        ButterKnife.bind(this);

        if (permission()) {
            if (presenter.createFolder(folderName)) {
                Toast.makeText(this, "Folder Created", Toast.LENGTH_SHORT).show();
                presenter.getFileFromFolder(folderName);
            }
        }
    }

    @OnClick(R.id.floating_bar)
    public void onFabClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @AfterPermissionGranted(PERMISSION_REQUEST_CODE)
    public boolean permission() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "need permission", PERMISSION_REQUEST_CODE, perms);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        presenter.getFileFromFolder(folderName);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "neeed permission").build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(this, perms)) {
                presenter.getFileFromFolder(folderName);
            } else {
                EasyPermissions.requestPermissions(this, "need permission", 123, perms);

            }
        }
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            Uri imageURI = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imageURI, projection, null, null, null);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(projection[0]);
                String picturePath = cursor.getString(columnIndex); // returns null
                File file = new File(picturePath);
                String lastName = file.getName();
                cursor.close();
                Log.i("image", file.toString());
                presenter.copyAndDeleteFiles(picturePath, lastName);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setImagesToAdapert(ArrayList<String> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapter(this, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void addedFiles() {
        Toast.makeText(this, "Successfully added file", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedToaddedFiles() {
        Toast.makeText(this, "Failed added file", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void createFolder() {
        Toast.makeText(this, "Folder Created", Toast.LENGTH_SHORT).show();
    }
}
