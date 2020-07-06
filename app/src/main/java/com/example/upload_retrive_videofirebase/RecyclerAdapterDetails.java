package com.example.upload_retrive_videofirebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerAdapterDetails extends FirebaseRecyclerAdapter<Member,RecyclerAdapterDetails.ViewHolder> {

    public RecyclerAdapterDetails(@NonNull FirebaseRecyclerOptions<Member> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_list, parent, false);
        return new RecyclerAdapterDetails.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Member model) {
        holder.name.setText("Name: "+model.getName());
        holder.link.setText("Name: "+model.getVideoUri());

        final String url = model.getVideoUri();

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PlayVideo.class);
                intent.putExtra("url",url);
                view.getContext().startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,link;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraintView);
            name = itemView.findViewById(R.id.name);
            link = itemView.findViewById(R.id.link);

        }
    }
}

