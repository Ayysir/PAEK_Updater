package com.ayysir.paek.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ayysir.paek.R;

public class SettingsActivity extends Activity {

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);

        // xml properties
        ListView mList = (ListView) findViewById(R.id.settings);

        mList.setOnItemClickListener(new LVItemClickListener());


        String[] strings = getResources().getStringArray(R.array.settings);

        // Prepare the header view for our list
        mList.setAdapter(new ArrayAdapter<String>(this, R.layout.settings_list_item,
                strings));


    }

    private String selectItem(int position) {
        switch (position) {

            case 0:
                Intent reboot = new Intent(this, RebootActivity.class);
                reboot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(reboot);
                break;

            case 1:
                Intent scripts = new Intent(this, ExtrasActivity.class);
                scripts.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(scripts);
                break;
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LVItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

            view.isHorizontalFadingEdgeEnabled();
        }
    }
}