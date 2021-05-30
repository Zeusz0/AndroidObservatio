package com.example.bloodmeasure;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class EditMeasureActivity extends AppCompatActivity {

    private static final String LOG_TAG = EditMeasureActivity.class.getName();
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private NotificationHelper mNotificationHandler;
    private ArrayList<MeasureItem> mItemList;
    private MeasureItemAdapter mAdapter;
    private MeasureItem e;
    private String eId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_measure);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");
        mNotificationHandler = new NotificationHelper(this);
        mItemList = new ArrayList<>();
        mAdapter = new MeasureItemAdapter(this, mItemList);
        mAdapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String measured2 = intent.getStringExtra("MeasuredMeasured");
        String device2 = intent.getStringExtra("MeasuredDevice");
        String doctor2 = intent.getStringExtra("MeasuredDoctor");
        String since2 = intent.getStringExtra("MeasuredSince");
        String condition2 = intent.getStringExtra("MeasuredCondition");
        String date2 = intent.getStringExtra("MeasuredDate");
        String comment2 = intent.getStringExtra("MeasuredComment");
        String patient2 = intent.getStringExtra("MeasuredPatient");
        String id2 = intent.getStringExtra("MeasuredId");

        MeasureItem got = new MeasureItem(2131230887, measured2, device2, doctor2, since2, condition2, date2, comment2, patient2);
        this.e = got;
        this.eId = id2;
        EditText Mmeasured = findViewById(R.id.addmeasuredE);
        EditText Mdevice = findViewById(R.id.adddeviceE);
        EditText MdoctorsName = findViewById(R.id.adddoctorE);
        EditText MsincLastMeasure = findViewById(R.id.addsinceE);
        EditText Mcondition = findViewById(R.id.addconditionE);
        EditText Mdate = findViewById(R.id.adddateE);
        EditText Mcomment = findViewById(R.id.addcommentE);
        EditText MpatienteName = findViewById(R.id.patienteNameE);

        Mmeasured.setText(measured2);
        Mdevice.setText(device2);
        MdoctorsName.setText(doctor2);
        MsincLastMeasure.setText(since2);
        Mcondition.setText(condition2);
        Mdate.setText(date2);
        Mcomment.setText(comment2);
        MpatienteName.setText(patient2);
    }

    public void onEditMeasure(View view) {
        EditText Mmeasured = findViewById(R.id.addmeasuredE);
        EditText Mdevice = findViewById(R.id.adddeviceE);
        EditText MdoctorsName = findViewById(R.id.adddoctorE);
        EditText MsincLastMeasure = findViewById(R.id.addsinceE);
        EditText Mcondition = findViewById(R.id.addconditionE);
        EditText Mdate = findViewById(R.id.adddateE);
        EditText Mcomment = findViewById(R.id.addcommentE);
        EditText MpatienteName = findViewById(R.id.patienteNameE);

        String measured = Mmeasured.getText().toString();
        String device = Mdevice.getText().toString();
        String doctor = MdoctorsName.getText().toString();
        String since = MsincLastMeasure.getText().toString();
        String condition = Mcondition.getText().toString();
        String date = Mdate.getText().toString();
        String comment = Mcomment.getText().toString();
        String patient = MpatienteName.getText().toString();

        MeasureItem asd = new MeasureItem(2131230887,measured,device,doctor,since,condition,date,comment,patient);
//        mItems.document(eId).delete();
//        mItems.add(asd);

        Log.d(LOG_TAG,"ID: "+eId);
        mItems.document(eId).update("measured", measured);
        mItems.document(eId).update("device", device);
        mItems.document(eId).update("doctor", doctor);
        mItems.document(eId).update("since", since);
        mItems.document(eId).update("condition", condition);
        mItems.document(eId).update("date", date);
        mItems.document(eId).update("comment", comment);
        mItems.document(eId).update("patient", patient);

        mNotificationHandler.send("Updated evidence: " + e.getPatienteName());
        mAdapter.notifyDataSetChanged();
        finish();
    }

    public void onCancel(View view) {
        finish();
    }
}