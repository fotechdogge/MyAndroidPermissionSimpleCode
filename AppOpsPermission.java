package com.example.dog.demo.permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Process;
import android.provider.Settings;

public class AppOpsPermission
{
    public static final int OPS_PERMISSION_REQUEST = 999;

    public interface SettingsActivityCallback
    {
        void onSettingActivityResult(int requestCode, int resultCode, Intent data);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public static boolean hasOpsPermission(Context context, String... perms) {

        if (!Utils.isSdkVersionHigherKitkat()) {
            return true;
        }

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        for (String perm : perms) {
            String ops_perm = checkPermissionString(perm);
            if (ops_perm == null) return false;
            int mode = appOps.checkOpNoThrow(ops_perm, Process.myUid(), context.getPackageName());
            if (mode == AppOpsManager.MODE_DEFAULT) {
                return context.checkCallingOrSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
            } else {
                return mode == AppOpsManager.MODE_ALLOWED;
            }
        }

        return true;
    }

    public static void StartSetOpsPermissionActivity(Object object) {
        Utils.checkCallingObjectSuitability(object);
        final Activity activity = Utils.getActivity(object);
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, OPS_PERMISSION_REQUEST);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data, SettingsActivityCallback... callbacks) {

        if (requestCode == OPS_PERMISSION_REQUEST) {
            for (SettingsActivityCallback cb : callbacks)
                cb.onSettingActivityResult(requestCode, resultCode, data);
        } else {
            for (SettingsActivityCallback cb : callbacks)
                cb.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static String checkPermissionString(String perm) {
        if (perm.equals(Manifest.permission.PACKAGE_USAGE_STATS)) {
            return AppOpsManager.OPSTR_GET_USAGE_STATS;
        }
        return null;
    }

    public static void ShowRationaleAlertDialog(final Activity activity, String rationale) {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setMessage(rationale)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartSetOpsPermissionActivity(activity);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .create()
                .show();
    }
}
