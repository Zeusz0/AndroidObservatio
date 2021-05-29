package com.example.bloodmeasure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MeasureItemAdapter extends RecyclerView.Adapter<MeasureItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<MeasureItem> mMeasureItemsData;
    private ArrayList<MeasureItem> mMeasureItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    MeasureItemAdapter(Context context, ArrayList<MeasureItem> itemsData){
        this.mMeasureItemsData = itemsData;
        this.mMeasureItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public MeasureItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( MeasureItemAdapter.ViewHolder holder, int position) {
        MeasureItem currentItem = mMeasureItemsData.get(position);

        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public Filter getFilter() {
        return measureFilter;
    }

    private Filter measureFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MeasureItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.count = mMeasureItemsDataAll.size();
                results.values = mMeasureItemsDataAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(MeasureItem item : mMeasureItemsDataAll) {
                    if(item.getPatienteName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                    else if(item.getDoctorsName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mMeasureItemsData = (ArrayList)results.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return mMeasureItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mItemImage;
        private TextView mMeasured;
        private TextView mDevicedoctorsName;
        private TextView mDoctor;
        private TextView mSincLastMeasure;
        private TextView mCondition;
        private TextView mDate;
        private TextView mComment;
        private TextView mPatienteName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mItemImage = itemView.findViewById(R.id.patienteImage);
            mMeasured =itemView.findViewById(R.id.measured);
            mDevicedoctorsName =itemView.findViewById(R.id.device);
            mDoctor =itemView.findViewById(R.id.doctorsName);
            mSincLastMeasure =itemView.findViewById(R.id.sincLastMeasure);
            mCondition =itemView.findViewById(R.id.condition);
            mDate =itemView.findViewById(R.id.date);
            mComment =itemView.findViewById(R.id.comment);
            mPatienteName = itemView.findViewById(R.id.patienteName);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //((MeasureListActivity)mContext).updateAlertIcon();
                }
            });
        }

        public void bindTo(MeasureItem currentItem) {

            Glide.with(mContext).load(currentItem.getPatienteImageResource()).into(mItemImage);

            mMeasured.setText(currentItem.getMeasured());
            mDevicedoctorsName.setText(currentItem.getDevice());
            mDoctor.setText(currentItem.getDoctorsName());
            mSincLastMeasure.setText(currentItem.getSincLastMeasure());
            mCondition.setText(currentItem.getCondition());
            mDate.setText(currentItem.getDate());
            mComment.setText(currentItem.getComment());
            mPatienteName.setText(currentItem.getPatienteName());

        }
    }

}
