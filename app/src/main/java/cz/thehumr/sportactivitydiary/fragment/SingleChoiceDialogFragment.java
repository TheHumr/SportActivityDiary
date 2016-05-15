package cz.thehumr.sportactivitydiary.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cz.thehumr.sportactivitydiary.R;

/**
 * Created by Ondřej on 29.04.2016.
 */
public class SingleChoiceDialogFragment extends DialogFragment {
    CharSequence[] items;
    String selection;
    int id;


    public static SingleChoiceDialogFragment newInstance(CharSequence[] items, int id) {
        Bundle args = new Bundle();
        args.putCharSequenceArray("items", items);
        args.putInt("id", id);
        SingleChoiceDialogFragment fragment = new SingleChoiceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SingleChoiceDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        items = getArguments().getCharSequenceArray("items");
        id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Goal type..").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        selection = (String) items[which];
                        break;
                    case 1:
                        selection = (String) items[which];
                        break;

                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                TextView textView;
                if (id == 0){
                    editor.putString("GOAL_TYPE", selection);
                    editor.commit();
                    textView = (TextView) getActivity().findViewById(R.id.txt_goal_type);
                } else {
                    editor.putString("GOAL_UNITS", selection);
                    editor.commit();
                    textView = (TextView) getActivity().findViewById(R.id.txt_goal_units);

                }
                textView.setText(selection);
                getTargetFragment().onActivityResult(101, Activity.RESULT_OK, null);
                Toast.makeText(getActivity(), "Your choice is: " + selection, Toast.LENGTH_LONG).show();

            }
        });

        return builder.create();
    }
}
