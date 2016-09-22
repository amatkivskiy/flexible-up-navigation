package com.amatkivskiy.navigation;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends TraceActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Main");
        this.setSupportActionBar(toolbar);

        this.findViewById(R.id.button_item_collection)
            .setOnClickListener(view -> startActivity(ContentCollectionActivity.createIntent(MainActivity.this,
                                                                                             MainActivity.class)));

        this.findViewById(R.id.button_item_details)
            .setOnClickListener(view -> startActivity(ContentDetailsActivity.createIntent(MainActivity.this,
                                                                                          1,
                                                                                          MainActivity.class)));

        this.findViewById(R.id.button_start_notification)
            .setOnClickListener(view -> {

                // Simple use case for opening specific details screen without back stack.
                Intent detailsIntent = ContentDetailsActivity.createIntent(this, -5, null);

                PendingIntent pendingIntent =
                        PendingIntent.getActivity(this, 0, detailsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this).setContentIntent(pendingIntent)
                                                            .setSmallIcon(android.R.drawable.ic_menu_add)
                                                            .setContentTitle("ItemDetailsActivity")
                                                            .setContentText("Open content details screen");

                int notificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                notificationManager.notify(notificationId, builder.build());
                MainActivity.this.finish();
            });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater()
            .inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            String githubUrl = getString(R.string.github_project_url);

            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(githubUrl)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
