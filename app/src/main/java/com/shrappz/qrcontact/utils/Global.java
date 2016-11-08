package com.shrappz.qrcontact.utils;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.shrappz.qrcontact.MyContactWidget;
import com.shrappz.qrcontact.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created on 09-10-2016.
 */

public class Global {
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;
    public static String permissions = "PERMISSIONS";
    public final static int WIDTH = 500;
    public final static int HEIGHT = 500;
// TODO: if not needed delete the class

    public static void writeBooleanPreference(Activity activity, String key, Boolean data) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static Boolean readBooleanPreference(Activity activity, String key) {
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public static AlertDialog.Builder alerter(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        return builder;
    }

    public static Boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_images
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "qr_profile.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static void updateWidget(Context context) {
        Intent intent = new Intent(context, MyContactWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = {R.id.qr_image};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

   public static Bitmap encodeAsBitmap(String str, Context context) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method

}
