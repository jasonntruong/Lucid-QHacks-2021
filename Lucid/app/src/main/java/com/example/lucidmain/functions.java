package com.example.lucidmain;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class functions {

    public String createIMG() throws IOException {

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = "JPEG_" + time + "_";

        return imgName;
    }
}
