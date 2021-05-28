package com.example.bloodmeasure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MeasureListActivity extends AppCompatActivity {
    private static final String LOG_TAG = MeasureListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<MeasureItem> mItemsData;
    private MeasureItemAdapter mAdapter;

    private boolean viewRow = true;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_list);
        mAuth = FirebaseAuth.getInstance();
        // mAuth.signOut();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new GridLayoutManager(
                this, gridNumber));
        // Initialize the ArrayList that will contain the data.
        mItemsData = new ArrayList<>();
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new MeasureItemAdapter(this, mItemsData);
        mRecyclerView.setAdapter(mAdapter);
        // Get the data.
        initializeData();
    }

    private void initializeData() {
        String[] itemsMeasured = getResources().getStringArray(R.array.measure_item_measured);
        String[] itemsDevice = getResources().getStringArray(R.array.measure_item_davice);
        String[] itemsDoctor = getResources().getStringArray(R.array.measure_item_doctor);
        String[] itemsSince = getResources().getStringArray(R.array.measure_item_since);
        String[] itemsCondition = getResources().getStringArray(R.array.measure_item_condition);
        String[] itemsdate = getResources().getStringArray(R.array.measure_item_date);
        String[] itemsComment = getResources().getStringArray(R.array.measure_item_comment);
        String[] itemsName = getResources().getStringArray(R.array.measure_item_patientName);
        TypedArray itemsImageResources = getResources().obtainTypedArray(R.array.measure_item_picture);

        mItemsData.clear();

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsMeasured.length; i++) {
            mItemsData.add(new MeasureItem( itemsImageResources.getResourceId(i, 0),
                    itemsMeasured[i],
                    itemsDevice[i],
                    itemsDoctor[i],
                    itemsSince[i],
                    itemsCondition[i],
                    itemsdate[i],
                    itemsComment[i],
                    itemsName[i]
            ));
        }

        // Recycle the typed array.
        itemsImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.measure_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_button:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}