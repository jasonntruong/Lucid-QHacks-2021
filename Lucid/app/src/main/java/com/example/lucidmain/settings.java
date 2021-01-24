package com.example.lucidmain;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class settings extends AppCompatActivity {

    Intent mainActivityIntent;

    File settingsData;
    readAndWriteSettings rw;

    Button save, speedUp, speedDown;
    TextView langDisplay, speedDisplay;

    int currSpeed;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

        //Button
        speedUp = findViewById(R.id.speedUp);
        speedDown = findViewById(R.id.speedDown);
        save = findViewById(R.id.saveButton);

        //TextView
        langDisplay = findViewById(R.id.langDisplay);
        speedDisplay = findViewById(R.id.speedDisplay);

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        settingsData = new File(dir, "settings.txt");   //Store settings.txt in Documents

        rw = new readAndWriteSettings(settingsData); //initialize rw object of readAndWriteSettings Class

        String oldData = rw.readFile();

        String[] oldDataList = oldData.split(","); //since information stored in "LANGUAGE,SPEED" syntax, we can separate the 2 parts of data by splitting at ","

        langDisplay.setText(oldDataList[0]);
        speedDisplay.setText(oldDataList[1]);

        currSpeed = Integer.parseInt(speedDisplay.getText().toString());    //current speed

        speedUp.setOnClickListener(new View.OnClickListener() { //increases speed (max at 10)
            @Override
            public void onClick(View v) {
                if (currSpeed != 10) {
                    currSpeed = currSpeed + 1;
                }
                speedDisplay.setText(String.valueOf(currSpeed));
            }
        });

        speedDown.setOnClickListener(new View.OnClickListener() {   //decreases speed (min at 1)
            @Override
            public void onClick(View v) {
                if (currSpeed != 1) {
                    currSpeed = currSpeed - 1;
                }
                speedDisplay.setText(String.valueOf(currSpeed));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {    //saves data to settings.txt
            @Override
            public void onClick(View v) {
                String data = langDisplay.getText() + "," + speedDisplay.getText(); //as seen above, data in syntax of "LANGUAGE,SPEED"
                rw.writeFile(data); //using writeFile, we write this data to settings.txt

                startActivity(mainActivityIntent);  //go back to the mainActivity
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}