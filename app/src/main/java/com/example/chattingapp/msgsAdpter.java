package com.example.chattingapp;

import static com.example.chattingapp.chatWin.reciverIImg;
import static com.example.chattingapp.chatWin.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class msgsAdpter extends RecyclerView.Adapter {
    Context context;
    ArrayList<msgModel> msgsAdpterArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public msgsAdpter(Context context, ArrayList<msgModel> msgsAdpterArrayList) {
        this.context = context;
        this.msgsAdpterArrayList = msgsAdpterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == ITEM_SEND){
           View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
           return new senderViewHolder(view);
       }
       else{
           View view = LayoutInflater.from(context).inflate(R.layout.reciver,parent,false);
           return new reciverHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       msgModel messages= msgsAdpterArrayList.get(position);
       if(holder.getClass()== senderViewHolder.class){
           senderViewHolder viewHolder = (senderViewHolder) holder;
           viewHolder.msgtxt.setText(messages.getMsg());
           Picasso.get().load(senderImg).into(viewHolder.circleImageView);

       }
       else{
           reciverHolder viewHolder = (reciverHolder) holder;
           viewHolder.msgtxt.setText(messages.getMsg());
           Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
       }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        msgModel messages = msgsAdpterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class reciverHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;
        public reciverHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }

}
