package com.example.lucidmain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class readAndWriteSettings {
    FileOutputStream outStream;
    FileInputStream inStream;

    protected void writeFile(File file, String message){
        try {
            outStream = new FileOutputStream(file, false);
            outStream.write(message.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String readFile(File file){
        byte[] bytes = new byte[(int) file.length()];

        try {
            inStream = new FileInputStream(file);
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
