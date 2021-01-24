package com.example.lucidmain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class readAndWriteSettings {
    FileOutputStream outStream;
    FileInputStream inStream;
    File mfile;

    protected readAndWriteSettings(File file){
        mfile = file;
    }

    protected void writeFile(String message){   //write to file
        try {
            outStream = new FileOutputStream(mfile);
            outStream.write(message.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String readFile(){    //read from file and returns contents as String
        byte[] bytes = new byte[(int) mfile.length()];

        try {
            inStream = new FileInputStream(mfile);
            inStream.read(bytes);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bytes);
    }
}
