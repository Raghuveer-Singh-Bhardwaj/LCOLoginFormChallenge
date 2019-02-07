package com.robotemplates.webviewapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final ArrayList<Team> mListOfTeam;
    private ArrayList<Posts> mListOfPosts = new ArrayList<>();
    int sportId;
    OnClickListener onClickListener;
    public RecyclerViewAdapter(ArrayList<Posts> mListOfArray, ArrayList<Team> mListOfTeams, OnClickListener mOnClickL,Context c) {
        this.mListOfPosts = mListOfArray;
        this.mListOfTeam = mListOfTeams;
        onClickListener = mOnClickL;
        context =c;
    }



    interface OnClickListener{
        public void onClick(int pos);
    }


    public void setSport(int id){
        this.sportId = id;
    }

    
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    private Context context;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        try{
            Team team1 = null,team2=null;

       /* for(Posts posts : mListOfPosts){
            Posts mPost = new Posts();
            String date = "NULL";
            if(posts.getSport() == sportId){
                mPost = posts;
                date = mPost.getDate();

                holder.mDate.setText(mListOfPosts.get(position).getDate());
                team1 = new Team();
                team2 = new Team();


            }*/
            Posts post=mListOfPosts.get(position);


            for(Team teams:mListOfTeam){
                Team team = teams;
                if(post.getTeam1() == team.getId()){
                    team1 = team;

                    holder.mTeam1Name.setText(team1.getName());
                    GlideApp.with(context)
                            .load(team1.getImageUrl())
                            .fitCenter()
                            .placeholder(R.drawable.ic_icon_round)
                            .placeholder(R.mipmap.ic_icon_round)
                           .into(holder.mTeam2Pic);
                }

                if(post.getTeam2() == team.getId()){
                    team2 = team;

                    holder.mTeam2Name.setText(team2.getName());
                    GlideApp.with(context)
                            .load(team2.getImageUrl())
                            .placeholder(R.drawable.ic_icon_round)
                            .placeholder(R.mipmap.ic_icon_round)
                            .fitCenter()
                            .into(holder.mTeam1Pic);
                }


            }


           /* SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yy");

            try {

                String reformattedStr = myFormat.format(fromUser.parse(mListOfPosts.get(position).getDate()));
                holder.mDate.setText(reformattedStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            //   Picasso.get().load(team1.getImageUrl()).placeholder(context.getResources().getDrawable(R.drawable.noimageicon4)).fit().centerInside().into(holder.mTeam2Pic);
            //    Picasso.get().load(team2.getImageUrl()).placeholder(context.getResources().getDrawable(R.drawable.noimageicon4)).fit().centerInside().into(holder.mTeam1Pic);


            holder.mType.setText(post.getTitle());
            holder.mDate.setText(post.getDate());
        }catch (Exception e){
            e.printStackTrace();
        }

        }


      /*  */


    @Override
    public int getItemCount() {

            return mListOfPosts.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTeam1Name,mTeam2Name,mDate,mType;
        ImageView mTeam1Pic,mTeam2Pic;

        public ViewHolder(View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);
                mTeam1Name = itemView.findViewById(R.id.mTeam1Name);
                mTeam2Name = itemView.findViewById(R.id.mTeam2Name);
                mTeam1Pic = itemView.findViewById(R.id.mTeam1Image);
                mTeam2Pic = itemView.findViewById(R.id.mTeam2Image);
                mType = itemView.findViewById(R.id.mType);
                mDate= itemView.findViewById(R.id.mDate);

        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            onClickListener.onClick(itemPosition);
        }
    }
}
