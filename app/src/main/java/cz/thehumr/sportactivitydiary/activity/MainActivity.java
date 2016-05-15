package cz.thehumr.sportactivitydiary.activity;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.adapter.WorkoutAdapter;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;
import cz.thehumr.sportactivitydiary.fragment.DeleteWorkoutDialogFragment;
import cz.thehumr.sportactivitydiary.fragment.GoalsFragment;
import cz.thehumr.sportactivitydiary.fragment.ListOfLocationsFragment;
import cz.thehumr.sportactivitydiary.fragment.ListOfWorkoutsFragment;
import cz.thehumr.sportactivitydiary.fragment.SettingsFragment;
import cz.thehumr.sportactivitydiary.model.Workout;

public class MainActivity extends AppCompatActivity implements DeleteWorkoutDialogFragment.ListOfWorkoutsListener {
    private DrawerLayout mDrawer;

    private Toolbar toolbar;
    Class fragmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Fragment fragment = null;

        try {
            fragment = ListOfWorkoutsFragment.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.nav_main:
                fragmentClass = ListOfWorkoutsFragment.class;
                break;
            case R.id.nav_second:
                fragmentClass = GoalsFragment.class;
                break;
            case R.id.nav_third:
                fragmentClass = ListOfLocationsFragment.class;
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        if (fragmentClass != null){
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();

            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            mDrawer.closeDrawers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigate_new_activity:
                startActivity(new Intent(this, NewWorkoutActivity.class));
                return true;
            case R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public void loadListOfWorkouts() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ListOfWorkoutsFragment listOfWorkoutsFragment = (ListOfWorkoutsFragment) fragmentManager.findFragmentById(R.id.mainContent);
        listOfWorkoutsFragment.loadListOfWorkouts();
    }
}
