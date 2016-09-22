package com.amatkivskiy.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.amatkivskiy.navigation.intent.Referrers;

public class ContentCollectionActivity extends TraceActivity {

    public static Intent createIntent(Context context, Class<?> referrer) {
        return Referrers.putReferrer(new Intent(context, ContentCollectionActivity.class), referrer);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_item_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Item Collection");
        this.setSupportActionBar(toolbar);

        this.getSupportActionBar()
            .setDisplayHomeAsUpEnabled(true);

        this.findViewById(R.id.button_item_details).setOnClickListener(view -> startActivity(ContentDetailsActivity.createIntent(
                view.getContext(),
                1,
                ContentCollectionActivity.class)));

        TextView text = (TextView) findViewById(R.id.text_collection);
        text.setText("Referrer: " + Referrers.getReferrer(getIntent()));
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
