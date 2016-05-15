package cz.thehumr.sportactivitydiary.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFragment extends Fragment {
    TextView textViewGoalType;
    TextView textViewGoalUnits;

    ImageView imageViewCycling;
    ImageView imageViewRunning;
    ImageView imageViewWalking;

    TextView textViewNumberOfCyclingWorkouts;
    TextView textViewNumberOfRunningWorkouts;
    TextView textViewNumberOfWalkingWorkouts;
    TextView textViewNumberOfWorkouts;

    ProgressBar progressBar;

    String units;

    SharedPreferences sharedPref;

    private WorkoutsDatabaseHelper databaseHelper;

    int numberOfWorkoutsGoalCycle, numberOfWorkoutsGoalRun, numberOfWorkoutsGoalWalk, numberOfWorkoutsGoal;
    int numberOfWorkoutsDoneCycle, numberOfWorkoutsDoneRun, numberOfWorkoutsDoneWalk, numberOfWorkoutsDoneGoal;

    int numberOfDistanceGoalCycle, numberOfDistanceGoalRun, numberOfDistanceGoalWalk, numberOfDistanceGoal;
    int numberOfDistanceDoneCycle, numberOfDistanceDoneRun, numberOfDistanceDoneWalk, numberOfDistanceDoneGoal;

    public GoalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_goals, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        databaseHelper = WorkoutsDatabaseHelper.getInstance(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseHelper.close();
    }

    private void initViews() {
        textViewGoalType = (TextView) getView().findViewById(R.id.txt_goal_type);
        textViewGoalUnits = (TextView) getView().findViewById(R.id.txt_goal_units);
        imageViewCycling = (ImageView) getView().findViewById(R.id.img_icon_cycling);
        imageViewRunning = (ImageView) getView().findViewById(R.id.img_icon_running);
        imageViewWalking = (ImageView) getView().findViewById(R.id.img_icon_walking);
        textViewNumberOfCyclingWorkouts = (TextView) getView().findViewById(R.id.txt_num_of_cycling_workouts);
        textViewNumberOfRunningWorkouts = (TextView) getView().findViewById(R.id.txt_num_of_running_workouts);
        textViewNumberOfWalkingWorkouts = (TextView) getView().findViewById(R.id.txt_num_of_walking_workouts);
        textViewNumberOfWorkouts = (TextView) getView().findViewById(R.id.txt_num_of_workouts);

        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();

        initViews();

        imageViewCycling.setImageResource(R.drawable.cycling);
        imageViewWalking.setImageResource(R.drawable.walking);
        imageViewRunning.setImageResource(R.drawable.running);

        numberOfWorkoutsDoneCycle = databaseHelper.getCountOfWorkoutsByType("Cycling", "2016-05-01");
        numberOfWorkoutsDoneRun = databaseHelper.getCountOfWorkoutsByType("Running", "2016-05-01");
        numberOfWorkoutsDoneWalk = databaseHelper.getCountOfWorkoutsByType("Walking", "2016-05-01");
        numberOfWorkoutsDoneGoal = numberOfWorkoutsDoneCycle + numberOfWorkoutsDoneRun + numberOfWorkoutsDoneWalk;

        numberOfDistanceDoneCycle = (int) databaseHelper.getSumOfDistanceByType("Cycling", "2016-05-01");
        numberOfDistanceDoneRun = (int) databaseHelper.getSumOfDistanceByType("Running", "2016-05-01");
        numberOfDistanceDoneWalk = (int) databaseHelper.getSumOfDistanceByType("Walking", "2016-05-01");
        numberOfDistanceDoneGoal = numberOfDistanceDoneCycle + numberOfDistanceDoneRun + numberOfDistanceDoneWalk;

        updateViews();

        setupListeners();

    }

    private void showNumberPickerDialog(final int type){
        NumberPicker myNumberPicker = new NumberPicker(getActivity());
        int currentValue = 0;
        int minValue = 0;
        int maxValue = 500;
        final int step = 5;

        int length = maxValue/step;
        String[] numberValues = new String[length + 1];

        for (int i = 0; i < numberValues.length; i++)
        {
            numberValues[i] = String.valueOf(i*step);
        }

        myNumberPicker.setMinValue(0);
        myNumberPicker.setMaxValue(length);
        myNumberPicker.setDisplayedValues(numberValues);
        switch (type){
            case 0:
                if (units.equals("Workouts"))
                    currentValue = numberOfWorkoutsGoalCycle;
                else
                    currentValue = numberOfDistanceGoalCycle / step;
                break;
            case 1:
                if (units.equals("Workouts"))
                    currentValue = numberOfWorkoutsGoalRun;
                else
                    currentValue = numberOfDistanceGoalRun / step;
                break;
            case 2:
                if (units.equals("Workouts"))
                    currentValue = numberOfWorkoutsGoalWalk;
                else
                    currentValue = numberOfDistanceGoalWalk / step;
                break;
        }

        myNumberPicker.setValue(currentValue);
        NumberPicker.OnValueChangeListener myOnValueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                SharedPreferences.Editor editor = sharedPref.edit();
                switch (type) {
                    case 0:
                        if (units.equals("Workouts"))
                            editor.putInt("NUMBER_OF_GOAL_CYCLE", newVal);
                        else
                            editor.putInt("DISTANCE_OF_GOAL_CYCLE", newVal * step);
                        break;
                    case 1:
                        if (units.equals("Workouts"))
                            editor.putInt("NUMBER_OF_GOAL_RUN", newVal);
                        else
                            editor.putInt("DISTANCE_OF_GOAL_RUN", newVal * step);
                        break;
                    case 2:
                        if (units.equals("Workouts"))
                            editor.putInt("NUMBER_OF_GOAL_WALK", newVal);
                        else
                            editor.putInt("DISTANCE_OF_GOAL_WALK", newVal * step);
                        break;
                }
                editor.commit();
                updateViews();
            }
        };
        myNumberPicker.setOnValueChangedListener(myOnValueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(myNumberPicker);
        builder.setTitle("Choose number..");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.show();
    }

    private void setupListeners() {
        textViewGoalType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Weekly", "Monthly"};
                SingleChoiceDialogFragment goalTypeChoiceDialog = SingleChoiceDialogFragment.newInstance(items, 0);
                goalTypeChoiceDialog.setTargetFragment(GoalsFragment.this, 101);
                goalTypeChoiceDialog.show(getActivity().getSupportFragmentManager(), "goalTypeChoiceDialog");
            }
        });
        textViewGoalUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] items = {"Workouts", "Distance"};
                SingleChoiceDialogFragment goalUnitsChoiceDialog = SingleChoiceDialogFragment.newInstance(items, 1);
                goalUnitsChoiceDialog.setTargetFragment(GoalsFragment.this, 101);
                goalUnitsChoiceDialog.show(getActivity().getSupportFragmentManager(), "goalUnitsChoiceDialog");
            }
        });

        textViewNumberOfCyclingWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(0);
            }
        });

        textViewNumberOfRunningWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(1);
            }
        });

        textViewNumberOfWalkingWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(2);
            }
        });


    }

    private void updateViews(){
        sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String sharedType = sharedPref.getString("GOAL_TYPE", "Monthly");
        textViewGoalType.setText(sharedType);
        units = sharedPref.getString("GOAL_UNITS", "Workouts");
        textViewGoalUnits.setText(units);

        numberOfWorkoutsGoalCycle = sharedPref.getInt("NUMBER_OF_GOAL_CYCLE", 0);
        numberOfWorkoutsGoalRun = sharedPref.getInt("NUMBER_OF_GOAL_RUN", 0);
        numberOfWorkoutsGoalWalk = sharedPref.getInt("NUMBER_OF_GOAL_WALK", 0);
        numberOfWorkoutsGoal = numberOfWorkoutsGoalCycle + numberOfWorkoutsGoalWalk + numberOfWorkoutsGoalRun;

        numberOfDistanceGoalCycle = sharedPref.getInt("DISTANCE_OF_GOAL_CYCLE", 0);
        numberOfDistanceGoalRun = sharedPref.getInt("DISTANCE_OF_GOAL_RUN", 0);
        numberOfDistanceGoalWalk = sharedPref.getInt("DISTANCE_OF_GOAL_WALK", 0);
        numberOfDistanceGoal = numberOfDistanceGoalCycle + numberOfDistanceGoalRun + numberOfDistanceGoalWalk;


        if (units.equals("Workouts")){
            textViewNumberOfCyclingWorkouts.setText(numberOfWorkoutsDoneCycle + "/" + numberOfWorkoutsGoalCycle);
            textViewNumberOfRunningWorkouts.setText(numberOfWorkoutsDoneRun + "/" + numberOfWorkoutsGoalRun);
            textViewNumberOfWalkingWorkouts.setText(numberOfWorkoutsDoneWalk + "/" + numberOfWorkoutsGoalWalk);

            textViewNumberOfWorkouts.setText(numberOfWorkoutsDoneGoal + "/" + numberOfWorkoutsGoal + " workouts");
            progressBar.setMax(numberOfWorkoutsGoal);
            progressBar.setProgress(numberOfWorkoutsDoneGoal);
        } else {
            textViewNumberOfCyclingWorkouts.setText(numberOfDistanceDoneCycle + "/" + numberOfDistanceGoalCycle);
            textViewNumberOfRunningWorkouts.setText(numberOfDistanceDoneRun + "/" + numberOfDistanceGoalRun);
            textViewNumberOfWalkingWorkouts.setText(numberOfDistanceDoneWalk + "/" + numberOfDistanceGoalWalk);

            textViewNumberOfWorkouts.setText(numberOfDistanceDoneGoal + "/" + numberOfDistanceGoal + " km");
            progressBar.setMax(numberOfDistanceGoal);
            progressBar.setProgress(numberOfDistanceDoneGoal);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 101:
                updateViews();
                break;
            default:
                break;
        }
    }
}
