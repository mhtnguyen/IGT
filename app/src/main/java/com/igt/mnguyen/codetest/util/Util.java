package com.igt.mnguyen.codetest.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.igt.mnguyen.codetest.R;

public class Util {
    private final static String TAG = Util.class.getSimpleName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showDialog(Context context, String title, String message)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getUserAgent(Context context){
        String packageName = "com.igt.mnguyen.codetest";
        String appVersionString = null;
        int appVersion=0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            if (info != null) {
                appVersionString = info.versionName;
                appVersion = info.versionCode;
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG,"Problem reading package info", e);
        }
        catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
        String userAgent = "InvestableGames/" + appVersionString + "[" + appVersion + "] (" + Build.PRODUCT + " "
                + Build.VERSION.RELEASE + "; " + Build.DEVICE + "/" + Build.BRAND + "/" + Build.MODEL + "; "
                + System.getProperty("user.language") + "-" + System.getProperty("user.region") + ") "
                + System.getProperty("java.runtime.name").replace(' ', '-') + "/"
                + System.getProperty("java.runtime.version");
        Log.i(TAG,"userAgent: [" + userAgent + "]");
        return userAgent;
    }
}
