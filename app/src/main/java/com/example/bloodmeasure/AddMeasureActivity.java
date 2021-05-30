package com.example.bloodmeasure;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AddMeasureActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddMeasureActivity.class.getName();
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private NotificationHelper mNotificationHandler;
    private ArrayList<MeasureItem> mItemList;
    private MeasureItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measure);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        mNotificationHandler = new NotificationHelper(this);
        mItemList = new ArrayList<>();
        mAdapter = new MeasureItemAdapter(this, mItemList);
        mAdapter.notifyDataSetChanged();
    }

    public void onCancel(View view) {
        finish();
    }

    public void onAddMeasure(View view) {

        EditText Mmeasured = findViewById(R.id.addmeasured);
        EditText Mdevice = findViewById(R.id.adddevice);
        EditText MdoctorsName = findViewById(R.id.adddoctor);
        EditText MsincLastMeasure = findViewById(R.id.addsince);
        EditText Mcondition = findViewById(R.id.addcondition);
        EditText Mdate = findViewById(R.id.adddate);
        EditText Mcomment = findViewById(R.id.addcomment);
        EditText MpatienteName = findViewById(R.id.patienteName);

        String measured = Mmeasured.getText().toString();
        String device = Mdevice.getText().toString();
        String doctor = MdoctorsName.getText().toString();
        String since = MsincLastMeasure.getText().toString();
        String condition = Mcondition.getText().toString();
        String date = Mdate.getText().toString();
        String comment = Mcomment.getText().toString();
        String patient = MpatienteName.getText().toString();

        MeasureItem measure = new MeasureItem(2131230887,measured,device,doctor,since,condition,date,comment,patient);
        mItems.add(measure);
        mNotificationHandler.send("Added patient: " + measure.getPatienteName());
        mAdapter.notifyDataSetChanged();
        finish();
    }
}