package com.example.conceptsusedsalesapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("error", errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        String geofenceTransitionDetails2 = getGeofenceTransitionDetails(geofencingEvent);
        Log.d("transition: ", "onReceive: "+geofenceTransitionDetails2);
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofencingEvent);

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails);
            Log.i("trn details", geofenceTransitionDetails);
        } else {
            // Log the error.
            Resources res = mContext.getResources();

            String invalidText = res.getString(R.string.geofence_transition_invalid_type);

            Log.e("error", invalidText+ geofenceTransition);
        }


    }

    private void sendNotification(String notificationDetails) {

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setColor(Notification.COLOR_DEFAULT)
                .setContentTitle(notificationDetails)
                .setContentText("You have entered trak n tell zone.")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(generateRandom(), builder.build());
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString;
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("IN"));
        SimpleDateFormat df = new SimpleDateFormat("hh-mm-ss");

        String formattedDate = df.format(c.getTime());

        int geofenceTransition = event.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            transitionString = "IN-" + formattedDate;
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            transitionString = "OUT-" + formattedDate;
        } else {
            transitionString = "OTHER-" + formattedDate;
        }
        List<String> triggeringIDs;
        triggeringIDs = new ArrayList<>();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
        }
        return String.format("%s: %s", transitionString, TextUtils.join(", ", triggeringIDs));
    }

    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(10000 - 100) + 100;
    }


}
