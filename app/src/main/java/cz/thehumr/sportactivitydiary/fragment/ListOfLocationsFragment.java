package cz.thehumr.sportactivitydiary.fragment;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;

import cz.thehumr.sportactivitydiary.R;
import cz.thehumr.sportactivitydiary.database.WorkoutsDatabaseHelper;
import cz.thehumr.sportactivitydiary.model.Location;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ListOfLocationsFragment extends Fragment {
    private ListView listViewLocations;
    private WorkoutsDatabaseHelper databaseHelper;
    private List<Location> locations;

    private MapAdapter mapAdapter;


    public ListOfLocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_list_of_locations, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseHelper = WorkoutsDatabaseHelper.getInstance(getActivity());
        locations = databaseHelper.getAllLocations();

        mapAdapter = new MapAdapter(getActivity(), locations);
        listViewLocations = (ListView) getView().findViewById(R.id.list_of_locations);
        listViewLocations.setAdapter(mapAdapter);

        mapAdapter.notifyDataSetChanged();

        AbsListView lv = listViewLocations;
        lv.setRecyclerListener(mRecycleListener);

    }

    private class MapAdapter extends ArrayAdapter<Location>{

        private final HashSet<MapView> mMaps = new HashSet<MapView>();

        public MapAdapter(Context context, List<Location> objects) {
            super(context, R.layout.list_item_location, R.id.list_item_location_name, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null){
                row = getActivity().getLayoutInflater().inflate(R.layout.list_item_location, null);

                holder = new ViewHolder();
                holder.mapView = (MapView) row.findViewById(R.id.list_item_location_map);
                holder.name = (TextView) row.findViewById(R.id.list_item_location_name);
                holder.countOfWorkouts = (TextView) row.findViewById(R.id.txt_num_of_location_workouts);
                row.setTag(holder);
                holder.initializeMapView();

                mMaps.add(holder.mapView);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Location item = getItem(position);
            holder.mapView.setTag(item);

            if (holder.map != null){
                setMapLocation(holder.map, item);
            }

            holder.name.setText(item.getName());
            holder.countOfWorkouts.setText("" + item.getNumOfWorkouts());
            return row;
        }

        public HashSet<MapView> getMaps() {
            return mMaps;
        }
    }

    private static void setMapLocation(GoogleMap map, Location data) {
        // Add a marker for this item and set the camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getLatitude(), data.getLongitude()), 9));
        map.addMarker(new MarkerOptions().position(new LatLng(data.getLatitude(), data.getLongitude())));

        // Set the map type back to normal.
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    class ViewHolder implements OnMapReadyCallback{

        MapView mapView;

        TextView name;

        TextView countOfWorkouts;

        GoogleMap map;

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(getActivity().getApplicationContext());
            map = googleMap;

            Location data = (Location) mapView.getTag();
            if (data != null){
                setMapLocation(map, data);
            }
        }

        public void initializeMapView() {
            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }
        }
    }

    private AbsListView.RecyclerListener mRecycleListener = new AbsListView.RecyclerListener() {

        @Override
        public void onMovedToScrapHeap(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null && holder.map != null) {
                // Clear the map and free up resources by changing the map type to none
                holder.map.clear();
                holder.map.setMapType(GoogleMap.MAP_TYPE_NONE);
            }

        }
    };

}
