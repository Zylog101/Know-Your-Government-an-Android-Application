package com.example.assingment.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView officialRole;
    private TextView officialName;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        officialRole = ((TextView)itemView.findViewById(R.id.official_role));
        officialName = ((TextView)itemView.findViewById(R.id.official_name));
    }

    public TextView getOfficialRole() {
        return officialRole;
    }

    public TextView getOfficialName() {
        return officialName;
    }
}
