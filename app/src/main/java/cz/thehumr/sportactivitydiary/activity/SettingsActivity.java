package cz.thehumr.sportactivitydiary.activity;

import android.app.Activity;
import android.os.Bundle;

import cz.thehumr.sportactivitydiary.fragment.SettingsFragment;

/**
 * Created by Ondřej on 29.04.2016.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
