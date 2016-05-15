package cz.thehumr.sportactivitydiary.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import cz.thehumr.sportactivitydiary.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    int request_code;


    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(int request_code) {
        Bundle args = new Bundle();
        args.putInt("request_code", request_code);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        request_code = getArguments().getInt("request_code");

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear += 1;
        String month = String.valueOf(monthOfYear);
        String day = String.valueOf(dayOfMonth);
        if (monthOfYear < 10) month = "0" + monthOfYear;
        if (dayOfMonth < 10) day = "0" + dayOfMonth;
        String result = year + "-" + month + "-" + day;
        if (request_code == 101){
            EditText editTextDate = (EditText) getActivity().findViewById(R.id.editText_datepicker);
            editTextDate.setText(result);
        } else if (request_code == 102){
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("GOALS_DATE_FROM", result);
            editor.commit();
            getTargetFragment().onActivityResult(101, Activity.RESULT_OK, null);
        }
    }
}
