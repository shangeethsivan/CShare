package com.shrappz.contactsharer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MyContactWidget extends AppWidgetProvider {
    public static String MY_WIDGET_UPDATE = "MY_OWN_WIDGET_UPDATE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_contact);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Bitmap bitmap = BitmapFactory.decodeFile("data/data/com.shrappz.contactsharer/app_images/qr_profile.png");
        if (bitmap != null) {
            views.setImageViewBitmap(R.id.qr_image, bitmap);
        } else {
            views.setTextViewText(R.id.appwidget_caution, "Please configure in the app to get your QRCode");
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
 @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

