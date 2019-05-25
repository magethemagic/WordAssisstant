package com.example.wordhelper.util;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static SQLiteDatabase openDatabase(Context context,String path){
        try {
            File file =new File(path);
            if(!file.exists()){
                InputStream inputStream = context.getAssets().open("dictionary.db");
                FileOutputStream fileOutputStream =new FileOutputStream(path);
                byte[] buffer =new byte[1024];
                int count;
                while ((count = inputStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer,0,count);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }
            return SQLiteDatabase.openOrCreateDatabase(path,null);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void jumpToPanel(Context context){
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
