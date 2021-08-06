package com.example.task2lgmfirebasefacedetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



public class ResultBox extends DialogFragment {
    Button Okbtn;
    TextView resultText;
    public View
    onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup,@Nullable Bundle saveInstanceState)
    {
        View view= inflater.inflate(R.layout.resultbox,viewGroup,false);
        String dialogboxtext="";
        Okbtn= view.findViewById(R.id.ResultOkButton);
        resultText=view.findViewById(R.id.ResultText);
        Bundle bundle= getArguments();
        dialogboxtext=bundle.getString(FaceDetection.resultTV);
        resultText.setText(dialogboxtext);
        Okbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                }
        );
        return view;
    }
}
