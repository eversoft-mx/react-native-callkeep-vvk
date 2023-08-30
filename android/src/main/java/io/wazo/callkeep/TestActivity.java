package io.wazo.callkeep;

import static io.wazo.callkeep.Constants.ACTION_ANSWER_CALL;
import static io.wazo.callkeep.Constants.ACTION_END_CALL;
import static io.wazo.callkeep.Constants.EXTRA_CALL_UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    public static boolean isMainActivityRunning = false;
    private static TestActivity activityInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intentExtras = getIntent();
        String name = intentExtras.getStringExtra("caller_name");
        TextView myTextView = findViewById(R.id.caller_name_textview);
        myTextView.setText(name);
        isMainActivityRunning = true;
        activityInstance = this;

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
    }

    public static void closeActivity() {
        if (activityInstance != null) {
            activityInstance.finish();
        }
    }
}