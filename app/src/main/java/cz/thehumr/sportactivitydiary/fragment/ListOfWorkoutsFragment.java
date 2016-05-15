package cz.thehumr.sportactivitydiary.fragment;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.activity.MainActivity;
import cz.thehumr.sportactivitydiary.activity.WorkoutDetailActivity;
import cz.thehumr.sportactivitydiary.adapter.WorkoutAdapter;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;
import cz.thehumr.sportactivitydiary.model.Workout;

public class ListOfWorkoutsFragment extends Fragment implements DeleteWorkoutDialogFragment.ListOfWorkoutsListener {

    private ListView listViewWorkouts;
    List<Workout> workouts;
    WorkoutsDatabaseHelper databaseHelper;
    WorkoutAdapter workoutAdapter;


    public ListOfWorkoutsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onStart() {
        super.onStart();
        listViewWorkouts = (ListView) getView().findViewById(R.id.listView);

        databaseHelper = WorkoutsDatabaseHelper.getInstance(getActivity());
        workouts = databaseHelper.getAllWorkouts();

        workoutAdapter = new WorkoutAdapter(getActivity(), R.layout.list_item_workout, workouts);
        listViewWorkouts.setAdapter(workoutAdapter);

        workoutAdapter.notifyDataSetChanged();

        listViewWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
                intent.putExtra("workout_id", workouts.get(position).getId());
                startActivity(intent);
            }
        });

        listViewWorkouts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment dialogFragment = DeleteWorkoutDialogFragment.newInstance(workouts.get(position).getId());
                dialogFragment.show(getActivity().getFragmentManager(), "dialog");
                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_workouts, container, false);
    }

    @Override
    public void loadListOfWorkouts() {
        workouts = databaseHelper.getAllWorkouts();
        workoutAdapter = new WorkoutAdapter(getActivity(), R.layout.list_item_workout, workouts);
        listViewWorkouts.setAdapter(workoutAdapter);
        workoutAdapter.notifyDataSetChanged();
    }

}
