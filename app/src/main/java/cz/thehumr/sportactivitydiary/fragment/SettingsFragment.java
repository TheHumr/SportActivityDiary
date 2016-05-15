package cz.thehumr.sportactivitydiary.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cz.thehumr.sportactivitydiary.R;
/**
 * Created by Ondřej on 29.04.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
