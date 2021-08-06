package com.example.task2lgmfirebasefacedetection;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class FaceDetection extends Application {
    public final static String resultTV= "Result_Text";
    public final static String resultbox= "Result_Dailog";

    public void onCreate() {

        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
