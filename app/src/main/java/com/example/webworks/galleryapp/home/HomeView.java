package com.example.webworks.galleryapp.home;

import java.util.ArrayList;

public interface HomeView {

    void setImagesToAdapert(ArrayList<String> data);
    void addedFiles();
    void failedToaddedFiles();
    void createFolder();

    void restoreSuccessfully();

    void restoreUnSuccessfully();
}
