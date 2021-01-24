package com.example.lucidmain;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class settings extends AppCompatActivity {

    readAndWriteSettings rw;

    Button save;

    TextView langDisplay, speedDisplay;

    File settingsData;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rw = new readAndWriteSettings();

        save = findViewById(R.id.saveButton);

        langDisplay = findViewById(R.id.langDisplay);
        speedDisplay = findViewById(R.id.speedDisplay);

        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        settingsData = new File(dir, "settings.txt");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = langDisplay.getText() + "," + speedDisplay.getText();
                rw.writeFile(settingsData, data);
            }
        });


    }
}