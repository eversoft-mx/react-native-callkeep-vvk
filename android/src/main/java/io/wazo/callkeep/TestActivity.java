package io.wazo.callkeep;

import static io.wazo.callkeep.Constants.ACTION_ANSWER_CALL;
import static io.wazo.callkeep.Constants.ACTION_END_CALL;
import static io.wazo.callkeep.Constants.EXTRA_CALL_UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.NotificationManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    public static boolean isMainActivityRunning = false;
    private static TestActivity activityInstance;
    private Ringtone ringtone;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        isMainActivityRunning = true;
        activityInstance = this;
        Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate((VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)));
        }else{
            vibrator.vibrate(1000);
        }
        ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        ringtone.play();
        /*
        AudioManager audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        boolean isVibrateOn = audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
        if(isVibrateOn){
            Vibrator vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            long[] pattern = { 500, 500 };
            vibrator.vibrate(pattern, -1);
            //vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));
        }else{
            ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            ringtone.play();
        }

         */

    }

    public void AnswerCall(View view){
        Intent intentExtras = getIntent();
        String callUuid = intentExtras.getStringExtra("call_uuid");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancel(14);

        Intent focusIntent = getPackageManager().getLaunchIntentForPackage("com.vivook").cloneFilter();
        focusIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(focusIntent);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(EXTRA_CALL_UUID,callUuid);
        Intent intent = new Intent(ACTION_ANSWER_CALL);
        Bundle extras = new Bundle();
        extras.putSerializable("attributeMap", hashMap);
        intent.putExtras(extras);
        try {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            Log.d("MyApp", "Transmisión enviada correctamente");
        } catch (Exception e) {
            Log.e("MyApp", "Error al enviar la transmisión: " + e.getMessage());
        }
        //finish();
    }

    public void RejectCall(View view){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancel(14);

        Intent intentExtras = getIntent();
        String callUuid = intentExtras.getStringExtra("call_uuid");

        Intent focusIntent = getPackageManager().getLaunchIntentForPackage("com.vivook").cloneFilter();
        focusIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(focusIntent);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(EXTRA_CALL_UUID,callUuid);
        Intent intent = new Intent(ACTION_END_CALL);
        Bundle extras = new Bundle();
        extras.putSerializable("attributeMap", hashMap);
        intent.putExtras(extras);
        try {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            Log.d("MyApp", "Transmisión enviada correctamente");
        } catch (Exception e) {
            Log.e("MyApp", "Error al enviar la transmisión: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isMainActivityRunning = false;
        if(ringtone != null){
            ringtone.stop();
        }
        if(vibrator != null){
            vibrator.cancel();
        }

    }

    public static void closeActivity() {
        if (activityInstance != null) {
            activityInstance.finish();
        }
    }
}