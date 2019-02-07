package com.robotemplates.webviewapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private ArrayList<User> mListOfUser = new ArrayList<>();

    public LeaderBoardAdapter(ArrayList<User> users){
        mListOfUser = users;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        LeaderBoardAdapter.ViewHolder vh = new LeaderBoardAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mListOfUser.get(position).getName());
        holder.mPosition.setText(String.valueOf(position+1));
        holder.mMoney.setText("₹"+(mListOfUser.get(position).getmRefferals()*5));
    }

    @Override
    public int getItemCount() {
        return mListOfUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mPosition,mName,mMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosition = itemView.findViewById(R.id.mPositionTV);
            mMoney = itemView.findViewById(R.id.mMoneyTV);
            mName = itemView.findViewById(R.id.mNameTV);
        }
    }
}
