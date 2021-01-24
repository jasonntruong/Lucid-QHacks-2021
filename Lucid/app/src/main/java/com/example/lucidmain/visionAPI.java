package com.example.lucidmain;

import android.content.Context;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

public class visionAPI {    //Class that holds methods that uses Google cloud's Vision API

    TextRecognizer tRec;
    ImageLabeler labeler;
    TextToSpeech mtts;

    Context mContext;

    protected visionAPI(Context context, TextToSpeech tts){ //object init takes context and tts as we need those 2 variables for the processText and processImage methods
        mContext = context;
        tRec = TextRecognition.getClient();
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        mtts = tts;
    }

    protected InputImage getImage(Uri photoURI){
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(mContext, photoURI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    protected void processText(Uri URI){
        InputImage image = getImage(URI);

        tRec.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                if (text.getText().length() > 0){
                    mtts.speak(text.getText(), TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    mtts.speak("No text found", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mtts.speak("No text found", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    protected void processImage(Uri URI) {
        InputImage image = getImage(URI);

        labeler.process(image).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                String allLabels = "";
                for (ImageLabel labels : imageLabels) {
                    Log.d("Label", String.valueOf(labels.getText() + " " + String.valueOf(labels.getConfidence())));
                    if (labels.getConfidence() > 0.7) {
                        allLabels += labels.getText() + "\n";
                    }
                }

                if (allLabels.length() > 0){
                    mtts.speak(allLabels, TextToSpeech.QUEUE_FLUSH, null);
                }

                else {
                    mtts.speak("No objects found", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mtts.speak("No text found", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}