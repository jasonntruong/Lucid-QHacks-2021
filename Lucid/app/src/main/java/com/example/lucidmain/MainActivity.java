package com.example.lucidmain;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    File dir, settingsData;
    Animation bounceLogo, bounceSettings, blink;

    ImageView logo, picture;
    ImageButton stopButton;
    Button settings;

    String operation;
    Uri photoURI;

    TextToSpeech tts;

    visionAPI vis;

    readAndWriteSettings rw;

    static final int REQUEST_IMAGE_CAPTURE = 2002;

    public File createIMG() throws IOException {    //creates, saves, and returns an image file
        deleteIMG(); //deletes all files before creating new image file so there's always only 1 image file saved on the device (saves space). No need to keep prev scanned images
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = "JPG_" + time + "_";

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(imgName, ".jpg", dir);   //saves image file to Pictures directory

        return img;
    }

    public void deleteIMG() {   //deletes all image files in the Pictures directory
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] dirList = dir.listFiles();
        for (File files : dirList) {
            files.delete();
        }
    }

    public void takePic () throws IOException { //takesPicture and saves them using createIMG() method above
        Intent takePicIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = null;
        try{
            photo = createIMG();    //calls createIMG() method that will create and return an image file
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photo != null){ //if the photo exists, then we setup the Intent
            photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photo);
            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);  //put in the photoURI data in the takePicIntent
            startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);   //start activity with the takePicIntent to actually take the picture
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //when activity started with the takePicIntent, it takes a picture
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK) {

            picture.setImageURI(photoURI);  //set the URI to photoURI we got in takePic() method

            //operation was set in onKeyDown method
            if (operation.equals("text")){  //if volume up was pressed, we call processText method from visionAPI class
                vis.processText(photoURI);
            }

            else if (operation.equals("object")){   //if volume down was pressed, we call processImage method from visionAPI class
                vis.processImage(photoURI);
            }

            picture.setAlpha(255);  //after the processText or processImage method is finished, we must show the picture, hide the logo and settings, and stop the blinking animations
            logo.setAlpha(0);
            settings.setAlpha(0);
            logo.clearAnimation();
            settings.clearAnimation();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //detects key presses

        //Lucid uses volume buttons over on-screen buttons so users have a physical/tactile feedback of when they click buttons since accidental screen taps
        //will leave blind users lost

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {    //Volume up button is for text-reading (using Vision API's text recognition)
            operation = "text";
            tts.speak("Text mode", TextToSpeech.QUEUE_FLUSH, null); //TTS "Text mode" so the user knows exactly what mode they have just entered
        }

        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {   //Volume down button is for image-labelling (using Vision API's image-labelling)
            operation = "object";
            tts.speak("Object mode", TextToSpeech.QUEUE_FLUSH, null); //TTS "Object mode" so the user knows that they have entered this mode
        }

        try {
            takePic();  //takePic method called so user can take image of what they want
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent
        Intent settingsIntent = new Intent(getApplicationContext(), settings.class);

        //Animations
        bounceLogo = AnimationUtils.loadAnimation(this, R.anim.bounce);
        bounceSettings = AnimationUtils.loadAnimation(this, R.anim.bounce);
        blink = AnimationUtils.loadAnimation(this, R.anim.blink_anim);

        //ImageView
        logo = findViewById(R.id.logo);
        picture = findViewById(R.id.picture);
        picture.setAlpha(0);    //hide picture ImageView at beginning

        //Buttons
        stopButton = findViewById(R.id.stopButton);
        settings = findViewById(R.id.settings);

        //String
        operation = "";

        //Text-To-Speech
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    String data = rw.readFile();    //read the settings.txt file
                    String[] dataList = data.split(",");
                    float speed = Float.valueOf(dataList[1])/10;    //speed is on 1-10 scale so get speed value (dataList[1]) and divide by 10
                    tts.setSpeechRate(speed); //lowered speech rate and language set to Canadian English

                    tts.setLanguage(Locale.CANADA);
                }
            }
        });

        //Files
        dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        settingsData = new File(dir, "settings.txt");

        //Objects
        vis = new visionAPI(getApplicationContext(), tts);
        rw = new readAndWriteSettings(settingsData);

        settings.startAnimation(bounceSettings);

        logo.startAnimation(bounceLogo);
        bounceLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(blink); //after bounce animation ends, the logo will blink until picture is taken
                tts.speak("Lucid is now ready to be your eyes", TextToSpeech.QUEUE_FLUSH, null); //Plays on app open so user knows that app is open
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {  //User can stop TTS by simply clicking the screen
            @Override
            public void onClick(View v) {
                tts.stop();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {    //Settings is designed for a care-giver to help make Lucid perfect for the blind user
            @Override
            public void onClick(View v) {
                startActivity(settingsIntent);
                tts.speak("Settings", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }


}