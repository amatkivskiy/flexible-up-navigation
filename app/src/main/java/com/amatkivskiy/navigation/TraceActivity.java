package com.amatkivskiy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Simple activity that tracks all state method calls.
 */
public class TraceActivity extends AppCompatActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Investigator.log(this);
    }

    @Override protected void onResume() {
        super.onResume();
        Investigator.log(this);
    }

    @Override protected void onStart() {
        super.onStart();
        Investigator.log(this);
    }

    @Override protected void onStop() {
        super.onStop();
        Investigator.log(this);
    }

    @Override protected void onPause() {
        super.onPause();
        Investigator.log(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Investigator.log(this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Investigator.log(this);
    }

    @Override public void finish() {
        super.finish();
        Investigator.log(this);
    }
}
