package com.example.dog.demo;

import android.Manifest;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.dog.demo.permission.AppOpsPermission;
import com.example.dog.demo.permission.Utils;
import com.example.dog.demo.permission.mCustomEasyPermission;

import java.util.List;

/**
 * Created by Dog on 2018/3/28.
 */

public class HomeActivity extends AppCompatActivity
{

    private final String TAG                               = "HomeActivity";
    private final int    PERMISSIONS_REQUEST_READ_CONTACTS = 666;
    private final String perms                             = Manifest.permission.READ_PHONE_STATE;
    private final String rationale                         = "dudududududuu";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

    }


    private void checkPermission() {
        if (Utils.isSdkVersionHigherM()) {
            Log.e("hasPermissions", "isSdkVersionHigherM");
            if (mCustomEasyPermission.hasPermissions(this, perms)) {
                Log.e(TAG, "hasPermissions");
                checkOpsPermission();
            } else {
                Log.e(TAG, "no Permissions");
                mCustomEasyPermission.requestPermissions(this,
                        rationale, PERMISSIONS_REQUEST_READ_CONTACTS, perms);
            }
        } else if (Utils.isSdkVersionHigherKitkat()) {
            ;
        } else {
            ;
        }
    }

    private void checkOpsPermission() {
        if (Utils.isSdkVersionHigherM()) {
            String[] ops_perms = new String[]{Manifest.permission.PACKAGE_USAGE_STATS};
            if (AppOpsPermission.hasOpsPermission(this, ops_perms)) {
                Log.e(TAG, "hasOpsPermission");
                goToMainActivity();
            } else {
                Log.e(TAG, "no OpsPermissions");
                AppOpsPermission.ShowRationaleAlertDialog(this, "同意設定，否則關閉");
            }
        } else if (Utils.isSdkVersionHigherKitkat()) {
            ;
        } else {
            ;
        }
    }


    private void goToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mCustomEasyPermission.onRequestPermissionResult(requestCode, permissions, grantResults, new mCustomEasyPermission.PermissionCallbacks()
        {
            @Override
            public void onPermissionsGranted(int i, List<String> list) {
                for (String s : list)
                    Log.e("Granted", s);
            }

            @Override
            public void onPermissionsDenied(int i, List<String> list) {
                for (String s : list)
                    Log.e("Denied", s);
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                Log.e(TAG, "requestCode: " + requestCode);
                if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
                    checkPermission();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppOpsPermission.onActivityResult(requestCode, resultCode, data, new AppOpsPermission.SettingsActivityCallback()
        {
            @Override
            public void onSettingActivityResult(int requestCode, int resultCode, Intent data) {
                Log.e(TAG, "on setting activity result" + "\trequestCode : " + requestCode + "\tresultCode : " + resultCode);
                if (requestCode == AppOpsPermission.OPS_PERMISSION_REQUEST) {
                    checkPermission();
                }
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                Log.e(TAG, "on activity result" + "\trequestCode : " + requestCode + "\tresultCode : " + resultCode);
            }
        });
    }


}
