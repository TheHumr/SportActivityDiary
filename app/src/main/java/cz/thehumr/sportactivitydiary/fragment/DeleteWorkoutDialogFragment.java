package cz.thehumr.sportactivitydiary.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteWorkoutDialogFragment extends DialogFragment {
    ListOfWorkoutsListener listener;

    public static DeleteWorkoutDialogFragment newInstance(int id) {
        DeleteWorkoutDialogFragment fragment = new DeleteWorkoutDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ListOfWorkoutsListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this workout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = getArguments().getInt("id");
                        WorkoutsDatabaseHelper databaseHelper = WorkoutsDatabaseHelper.getInstance(getActivity());
                        if (databaseHelper.deleteWorkout(id)){
                            Toast.makeText(getActivity(), "1 workout was deleted", Toast.LENGTH_LONG).show();
                            listener.loadListOfWorkouts();

                        } else {
                            Toast.makeText(getActivity(), "Some issue occured!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }

    public interface ListOfWorkoutsListener{
        void loadListOfWorkouts();
    }

}
