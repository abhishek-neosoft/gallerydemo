package com.example.webworks.galleryapp.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.webworks.galleryapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, HomeView, HomeAdapterDemo.OnRecyclerViewItemPosition {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.floating_bar)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.back_arrow)
    ImageView backButton;
    ArrayList adapterItemPosition;

    private String folderName = ".GalleryApp";//GalleryApp
    private HomeAdapter adapter;
    private HomeAdapterDemo adapterDemo;
    private String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private HomePresenter presenter;
    private RecyclerView.LayoutManager layoutManager;
    final static int PERMISSION_REQUEST_CODE = 123;
    final static int SELECT_PICK_REQUEST_CODE = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery);
        adapterItemPosition=new ArrayList();

        presenter = new HomePresenter(this, this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (permission()) {
            reciveData();
            if (presenter.createFolder(folderName)) {
                Toast.makeText(this, "Folder Created", Toast.LENGTH_SHORT).show();
                presenter.getFileFromFolder(folderName);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_items, menu);
        return true;
    }

    @OnClick(R.id.floating_bar)
    public void onFabClick() {
        Intent getImages = new Intent();
        getImages.setType("image/*");
        getImages.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImages.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(getImages, "Select Picture"), SELECT_PICK_REQUEST_CODE);
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
        if (requestCode == SELECT_PICK_REQUEST_CODE && resultCode == RESULT_OK
                && null != data) {
            presenter.onResultDataAndRecieveDataFromGallery(data,String.valueOf(new File(Environment.getExternalStorageDirectory() + "/"+folderName)));
            adapterDemo.notifyDataSetChanged();
        }
    }

    @Override
    public void setImagesToAdapert(ArrayList<String> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new HomeAdapter(this, data);
        adapterDemo=new HomeAdapterDemo(this,data);
        recyclerView.setAdapter(adapterDemo);
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

    @Override
    public void restoreSuccessfully() {
        Toast.makeText(this, "RestoreSuseccfull", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void restoreUnSuccessfully() {
        Toast.makeText(this, "RestoreNotDone", Toast.LENGTH_SHORT).show();

    }

    public void reciveData() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        } else {
            Toast.makeText(this, "NOdATA FOUND", Toast.LENGTH_SHORT).show();
        }
        if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        }
    }

    void handleSendMultipleImages(Intent intent) {
        presenter.onResultDataAndRecieveDataFromGallery(intent,String.valueOf(new File(Environment.getExternalStorageDirectory() + "/"+folderName)));
    }

    void handleSendImage(Intent intent) {
        presenter.onResultDataAndRecieveDataFromGallery(intent,String.valueOf(new File(Environment.getExternalStorageDirectory() + "/"+folderName)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = " ";
        switch (item.getItemId()) {
            case R.id.restore:

                if (adapterDemo.getImages().size() != 0) {
                    //presenter.fetchImagesWithPosition(adapterItemPosition,folderName);


                    presenter.fetchImagesWithPos(adapterDemo.getImages());
                    adapterDemo.clearSelectedList();
                    //adapter.notifyDataSetChanged();
                    adapterDemo.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Add images", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.edit:
                msg = "edit";
                break;
            case R.id.logout:
                msg = "logout";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedItemPosition(ArrayList itemPosition) {
        this.adapterItemPosition = itemPosition;

    }
}
