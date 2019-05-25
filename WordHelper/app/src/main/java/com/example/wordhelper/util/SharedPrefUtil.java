package com.example.wordhelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class SharedPrefUtil {
    private static final String FILE_NAME = "history";
    private static final String FILE_NAME_GROUP = "group";
    private static Gson gson = new Gson();


    public static void saveHistory(Context context,String key ,Object data){
        String type =data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case "Integer":
                editor.putInt(key, (Integer) data);
                break;
            case "Boolean":
                editor.putBoolean(key, (Boolean) data);
                break;
            case "String":
                editor.putString(key, (String) data);
                break;
            case "Float":
                editor.putFloat(key, (Float) data);
                break;
            case "Long":
                editor.putLong(key, (Long) data);
                break;
            default:
                String object = gson.toJson(data);
                editor.putString(key, object);
                break;
        }
        editor.apply();

    }


    public static List<String> getList(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);
        String listStr = sharedPreferences.getString(key, null);
        return gson.fromJson(listStr, List.class);
    }


    public static void clearHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Toast.makeText(context, "清除成功", Toast.LENGTH_SHORT).show();

    }


    public static void saveGroup(Context context, String key, List<String> data) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME_GROUP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String group = gson.toJson(data);
        editor.putString(key, group);
        editor.apply();
    }


    public static List<String> getGroup(Context context, String key) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME_GROUP, Context.MODE_PRIVATE);
        String group = sharedPreferences.getString(key, "");
        return gson.fromJson(group, List.class);
    }
}
