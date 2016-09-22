package com.amatkivskiy.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.amatkivskiy.navigation.intent.Referrers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class ContentDetailsActivity extends AppCompatActivity {

    public static final String CONTENT_ID = "content_id";

    public static Intent createIntent(Context context, int contentId, Class<?> referrer) {
        Intent intent = new Intent(context, ContentDetailsActivity.class);
        intent.putExtra(CONTENT_ID, contentId);

        Referrers.putReferrer(intent, referrer);

        return intent;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_item_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Item Details");
        this.setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int contentId = getIntent().getIntExtra(CONTENT_ID, -1);

        TextView details = (TextView) findViewById(R.id.text_item_number);
        details.setText("Item #: " + contentId);

        this.findViewById(R.id.button_item_details).setOnClickListener(view -> {
            // Start new item details screen.
            int newValue = contentId + 1;
            startActivity(createIntent(view.getContext(), newValue, Referrers.getReferrer(getIntent())));
        });

        this.findViewById(R.id.button_item_collection)
            .setOnClickListener(view -> this.startActivity(ContentCollectionActivity.createIntent(ContentDetailsActivity.this,
                                                                                                  ContentDetailsActivity.class)));
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.startParentActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startParentActivity() {
        // Check whether back stack exists.
        if (Referrers.hasReferrer(getIntent())) {
            // If the current back stack exists just navigate to parent activity.
            Class parentActivityClass = Referrers.getReferrer(getIntent());
            Intent intent =
                    new Intent(this, parentActivityClass).setFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP);

            this.startActivity(intent);
        } else {
            // If ItemDetailsActivity was open for example from the notification then create back stack manually.
            Intent collectionsIntent = ContentCollectionActivity.createIntent(this, null);
            Intent mainActivityIntent = new Intent(this, MainActivity.class);

            TaskStackBuilder.create(this)
                            .addNextIntent(mainActivityIntent)
                            .addNextIntent(collectionsIntent)
                            .startActivities();
        }
    }
}
