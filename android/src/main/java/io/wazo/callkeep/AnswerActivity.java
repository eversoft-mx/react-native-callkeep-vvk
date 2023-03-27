package io.wazo.callkeep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static io.wazo.callkeep.Constants.ACTION_ANSWER_CALL;
import static io.wazo.callkeep.Constants.EXTRA_CALL_UUID;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Connection;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.List;

public class AnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancel(14);

        Intent intentExtras = getIntent();
        String callUuid = intentExtras.getStringExtra("call_uuid");

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
        finish();
    }
}