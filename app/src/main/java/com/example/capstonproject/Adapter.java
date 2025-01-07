package com.example.capstonproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {
    private ArrayList<JobListData> jobArrayList;
    private OnItemClickListener itemClickListener;
    private Context context;
    private int position;

    public ArrayList<JobListData> setFilteredList(ArrayList<JobListData> filteredList) {
        this.jobArrayList = filteredList;
        this.notifyDataSetChanged();
        return this.jobArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public Adapter(ArrayList<JobListData> jobArrayList) {
        this.jobArrayList = jobArrayList;
    }

    @NonNull
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_list2, viewGroup, false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int i) {
        customViewHolder.job.setText(((JobListData)this.jobArrayList.get(i)).getStar_img());
        customViewHolder.job.setText(((JobListData)this.jobArrayList.get(i)).getJob_name());
        customViewHolder.hour_money.setText(((JobListData)this.jobArrayList.get(i)).getHour_money());
        customViewHolder.area.setText(((JobListData)this.jobArrayList.get(i)).getArea());
        customViewHolder.hireType.setText(((JobListData)this.jobArrayList.get(i)).getHireType());
    }

    public int getItemCount() {
        return this.jobArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageButton star;
        TextView job;
        TextView hour_money;
        TextView area;
        TextView hireType;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.star = (ImageButton)itemView.findViewById(R.id.starBtn);
            this.job = (TextView)itemView.findViewById(R.id.jobName);
            this.hour_money = (TextView)itemView.findViewById(R.id.hourlyMoney);
            this.area = (TextView)itemView.findViewById(R.id.area);
            this.hireType = (TextView)itemView.findViewById(R.id.job);
            this.star.setTag(R.drawable.img_1);
            itemView.getRootView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int pos = CustomViewHolder.this.getAdapterPosition();
                    if (pos != -1 && Adapter.this.itemClickListener != null) {
                        Adapter.this.itemClickListener.onItemClick(v, pos);
                    }

                }
            });
            this.star.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.e("recyclerview 11111", "!!!!!!!!!!!!!!!!!");
                    int pos = CustomViewHolder.this.getAdapterPosition();
                    if (pos != -1) {
                        Log.e("recyclerview 22222", "!!!!!!!!!!!!!!!!!");
                        if (Adapter.this.itemClickListener != null) {
                            Adapter.this.itemClickListener.onButtonClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View var1, int var2);

        void onButtonClick(View var1, int var2);
    }
}