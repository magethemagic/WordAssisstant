package com.example.wordhelper.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wordhelper.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class AppPermissionActivity extends AppCompatActivity {

    private static final int NOT_NOTICE =2;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_permission);
        myRequestPermission();
    }

    private void myRequestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1);
        }else {
            //Toast.makeText(this,"您已经申请了权限",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==1){
            for(int i=0;i<permissions.length;i++){
                if(grantResults[i] == PERMISSION_GRANTED){
                    Toast.makeText(this,"权限"+permissions[i]+"申请成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                }else {
                    if(!ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[i])){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("权限申请")
                                .setMessage("此软件需要"+CAMERA_SERVICE+"权限")
                                .setPositiveButton("确定", (dialog, which) -> {
                                   if(mDialog != null && mDialog.isShowing()){
                                       mDialog.dismiss();
                                   }
                                    Intent intent =new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                                    intent.setData(uri);
                                    startActivityForResult(intent,NOT_NOTICE);
                                });

                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AppPermissionActivity.this);
                        builder.setTitle("权限申请")
                                .setMessage("点击允许才可以使用APP哦")
                                .setPositiveButton("去允许", (dialog, id) -> {
                                    if (alertDialog != null && alertDialog.isShowing()) {
                                        alertDialog.dismiss();
                                    }
                                    ActivityCompat.requestPermissions(AppPermissionActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            myRequestPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }
}
