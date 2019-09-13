package com.example.assingment.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<Official> officialList;
    private MainActivity mainActivity;

    public Adapter(List<Official> officialList, MainActivity mainActivity) {
        this.officialList = officialList;
        this.mainActivity = mainActivity;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_list_row, parent, false);
        myItemView.setOnClickListener(mainActivity);
        return new RecyclerViewHolder(myItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Official newOfficial = officialList.get(position);
        holder.getOfficialRole().setText(newOfficial.getOfficeTitle());
        String partyName=newOfficial.getPartyName();
        if(partyName.equals("Democratic")|| partyName.equals("Republican")) {
            holder.getOfficialName().setText(newOfficial.getName() + " (" + newOfficial.getPartyName() + ")");
        }
        else
        {
            holder.getOfficialName().setText(newOfficial.getName() + " (" + "Other" + ")");
        }
    }

    @Override
    public int getItemCount() {
        return officialList.size();
    }
}
