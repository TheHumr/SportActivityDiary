package cz.thehumr.sportactivitydiary.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.model.Workout;

/**
 * Created by Ondřej on 07.03.2016.
 */
public class WorkoutAdapter extends ArrayAdapter<Workout> {


    public WorkoutAdapter(Context context, int resource, List<Workout> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_workout, null);
        }

        Workout workout = getItem(position);

        TextView textViewWorkoutDate = (TextView) convertView.findViewById(R.id.textView_date);
        textViewWorkoutDate.setText(workout.getDate());

        TextView textViewWorkoutDuration = (TextView) convertView.findViewById(R.id.textView_duration);
        textViewWorkoutDuration.setText(workout.getDuration());

        TextView textViewWorkoutDistance = (TextView) convertView.findViewById(R.id.textView_distance);
        textViewWorkoutDistance.setText(String.valueOf(workout.getDistance()));

        TextView textViewWorkoutSpeed = (TextView) convertView.findViewById(R.id.textView_speed);
        textViewWorkoutSpeed.setText("AVG Speed");

        TextView textViewWorkoutDescription = (TextView) convertView.findViewById(R.id.textView_description);
        textViewWorkoutDescription.setText(workout.getDescription());

        ImageView imageViewActivityTypeIcon = (ImageView) convertView.findViewById(R.id.imageView_activity_type);
        switch (workout.getType()){
            case "Cycling":
                imageViewActivityTypeIcon.setImageResource(R.drawable.cycling);
                break;
            case "Running":
                imageViewActivityTypeIcon.setImageResource(R.drawable.running);
                break;
            case "Walking":
                imageViewActivityTypeIcon.setImageResource(R.drawable.walking);
                break;
            default:
                imageViewActivityTypeIcon.setImageResource(R.drawable.running);
                break;
        }

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_feeling);
        ratingBar.setRating((float) workout.getFeeling());


        return convertView;
    }
}
