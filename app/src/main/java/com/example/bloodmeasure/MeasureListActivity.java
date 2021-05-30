package com.example.bloodmeasure;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MeasureListActivity extends AppCompatActivity {
    private static final String LOG_TAG = MeasureListActivity.class.getName();
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<MeasureItem> mItemsData;
    private MeasureItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private boolean viewRow = true;

    private int gridNumber = 1;

    private Integer itemLimit = 5;

    private int Done=0;



    private NotificationHelper mNotificationHelper;
    private AlarmManager mAlarmManager;
    private JobScheduler mJobScheduler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        // Get the data.
        queryData();
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_POWER_CONNECTED);
//        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//        this.registerReceiver(powerReceiver, filter);

        mNotificationHelper = new NotificationHelper(this);
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mJobScheduler =(JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        //setAlarmManager();
        setJobScheduler();
    }


    private void queryData(){
        mItemsData.clear();
        Log.d(LOG_TAG,"Query meghívva");

        mItems.orderBy("patienteName").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                MeasureItem item = document.toObject(MeasureItem.class);
                item.setId(document.getId());
                mItemsData.add(item);
            }

            if(mItemsData.size() == 0){
                queryData();
                initializeData();
            }
            mAdapter.notifyDataSetChanged();
        });

    }

    public void deleteItem(MeasureItem item) {
        DocumentReference ref = mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });

        mNotificationHelper.send("DELETED");
        queryData();
       // mNotificationHelper.cancel();
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

        for (int i = 0; i < itemsMeasured.length; i++) {
            mItems.add(new MeasureItem(
                    itemsImageResources.getResourceId(i, 0),
                    itemsMeasured[i],
                    itemsDevice[i],
                    itemsDoctor[i],
                    itemsSince[i],
                    itemsCondition[i],
                    itemsdate[i],
                    itemsComment[i],
                    itemsName[i] ));
        }

        itemsImageResources.recycle();
        //mAdapter.notifyDataSetChanged();
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

    private void setAlarmManager() {
        long repeatInterval = 60000; // AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);


        mAlarmManager.cancel(pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setJobScheduler() {
        // SeekBar, Switch, RadioButton
        int networkType = JobInfo.NETWORK_TYPE_UNMETERED;
        Boolean isDeviceCharging = true;
        int hardDeadline = 5000; // 5 * 1000 ms = 5 sec.

        ComponentName serviceName = new ComponentName(getPackageName(), NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceName)
                .setRequiredNetworkType(networkType)
                .setRequiresCharging(isDeviceCharging)
                .setOverrideDeadline(hardDeadline);

        JobInfo jobInfo = builder.build();
        mJobScheduler.schedule(jobInfo);

        // mJobScheduler.cancel(0);
        // mJobScheduler.cancelAll();

    }
    public void addMeasureForm() {
        Intent intent = new Intent(this, AddMeasureActivity.class);
        startActivity(intent);
    }

    public void editMeasure(MeasureItem item){
        Intent intent = new Intent(this, EditMeasureActivity.class);
        intent.putExtra("Picture", item.getPatienteImageResource());
        intent.putExtra("MeasuredMeasured", item.getMeasured());
        intent.putExtra("MeasuredDevice", item.getDevice());
        intent.putExtra("MeasuredDoctor", item.getDoctorsName());
        intent.putExtra("MeasuredSince", item.getSincLastMeasure());
        intent.putExtra("MeasuredCondition", item.getCondition());
        intent.putExtra("MeasuredDate", item.getDate());
        intent.putExtra("MeasuredComment", item.getComment());
        intent.putExtra("MeasuredPatient", item.getPatienteName());
        intent.putExtra("MeasuredId", item._getId());

        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
    }
}