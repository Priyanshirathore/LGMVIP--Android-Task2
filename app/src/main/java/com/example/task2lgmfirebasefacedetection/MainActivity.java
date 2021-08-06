package com.example.task2lgmfirebasefacedetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;

public class MainActivity extends AppCompatActivity {
Button captureButton;
private final  static int REQ_IMG_CAPTURE=124;
FirebaseVisionImage image;
FirebaseVisionFaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        captureButton=findViewById(R.id.camera_button);
        captureButton.setOnClickListener(
              new View.OnClickListener(){

                  @Override
                  public void onClick(View view) {
                      Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(intent.resolveActivity(getPackageManager()) !=null)
                      {
                          startActivityForResult(intent, REQ_IMG_CAPTURE);
                      }
                            else
                            {
                                Toast.makeText(MainActivity.this,"Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                  }
              }
        );
    }
    @Override
    protected void onActivityResult(int reqcode, int resultcode, @Nullable Intent data) {

        super.onActivityResult(reqcode, resultcode, data);
        if(reqcode==REQ_IMG_CAPTURE && resultcode==RESULT_OK)
        {
              Bundle extra= data.getExtras();
            Bitmap bitmap= (Bitmap)extra.get("data");
            detectface(bitmap);
        }
        
    }

    private void detectface(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions options= new FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();
        try{
            image=FirebaseVisionImage.fromBitmap(bitmap);
            detector= FirebaseVision.getInstance().getVisionFaceDetector(options);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                        String resultText="";
                        int i=1;
                        for(FirebaseVisionFace firebaseVisionFace: firebaseVisionFaces)
                        {
                            resultText=resultText.concat("\nFACE NUMBER "+i+": ")
                                    .concat("\nSmile:" + firebaseVisionFace.getSmilingProbability()*100 + "%")
                                    .concat("\nLeft eye open: "+ firebaseVisionFace.getLeftEyeOpenProbability()*100 + "%")
                                    .concat("\nRight eye open: "+ firebaseVisionFace.getRightEyeOpenProbability()*100+"%")
//                                    .concat("\n "+ firebaseVisionFace.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK))
//                                    .concat("\n "+ firebaseVisionFace.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK))
//                                    .concat("\n "+ firebaseVisionFace.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR))
//                                    .concat("\n "+ firebaseVisionFace.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR));
                            ;
                            i++;
                        }
                        if(firebaseVisionFaces.size()==0)
                        {
                            Toast.makeText(MainActivity.this,"NO FACE DETECTED",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Bundle bundle=new Bundle();
                            bundle.putString(FaceDetection.resultTV,resultText);
                            DialogFragment dialogFragment=new ResultBox();
                            dialogFragment.setArguments(bundle);
                            dialogFragment.setCancelable(true);
                            dialogFragment.show(getSupportFragmentManager(),FaceDetection.resultbox);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Oops,Can't detect this",Toast.LENGTH_SHORT).show();
            }
        });
    }


}