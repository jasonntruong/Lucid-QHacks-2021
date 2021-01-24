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
    Bundle slBundle;
    Intent mainActivityIntent;

    readAndWriteSettings rw;

    Button save, langNext, langPrev, speedUp, speedDown;

    TextView langDisplay, speedDisplay;

    File settingsData;

    int currSpeed;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        slBundle = new Bundle();
        mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

        langNext = findViewById(R.id.langNext);
        langPrev = findViewById(R.id.langPrev);

        speedUp = findViewById(R.id.speedUp);
        speedDown = findViewById(R.id.speedDown);



        save = findViewById(R.id.saveButton);

        langDisplay = findViewById(R.id.langDisplay);
        speedDisplay = findViewById(R.id.speedDisplay);

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        settingsData = new File(dir, "settings.txt");
        rw = new readAndWriteSettings(settingsData);
        String oldData = rw.readFile();

        String[] oldDataList = oldData.split(",");

        langDisplay.setText(oldDataList[0]);
        speedDisplay.setText(oldDataList[1]);

        currSpeed = Integer.parseInt(speedDisplay.getText().toString());

        speedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currSpeed != 10) {
                    currSpeed = currSpeed + 1;
                }

                speedDisplay.setText(String.valueOf(currSpeed));

            }
        });

        speedDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currSpeed != 1) {
                    currSpeed = currSpeed - 1;
                }

                speedDisplay.setText(String.valueOf(currSpeed));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = langDisplay.getText() + "," + speedDisplay.getText();
                rw.writeFile(data);

                startActivity(mainActivityIntent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}