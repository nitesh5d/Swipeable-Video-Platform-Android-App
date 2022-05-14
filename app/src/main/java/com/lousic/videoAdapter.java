package com.lousic;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

class videoAdapter extends FirebaseRecyclerAdapter <videomodel, videoAdapter.myViewHolder>{

    public videoAdapter(@NonNull FirebaseRecyclerOptions<videomodel> options) {
        super(options);

    }
    private Context context;
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull videomodel model) {
        holder.setdata(model);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{



        DatabaseReference UserRef;
        StorageReference VideoRef;

        VideoView videoView;
        TextView title,desc;
        ProgressBar progressBar;
        float xDown = 0, yDown = 0;

        LinearLayout videoControls, plainBg;
        Button upvoteBtn, downvoteBtn, downloadBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView= itemView.findViewById(R.id.video_view);
            title= itemView.findViewById(R.id.video_info_title);
            desc= itemView.findViewById(R.id.video_info_desc);
            progressBar = itemView.findViewById(R.id.progressBar);
            videoControls = itemView.findViewById(R.id.video_controls);
            plainBg = itemView.findViewById(R.id.plain_bg);
            upvoteBtn = itemView.findViewById(R.id.upvote_btn);
            downvoteBtn = itemView.findViewById(R.id.downvote_btn);
            downloadBtn = itemView.findViewById(R.id.download_btn);


        }


        void setdata(videomodel obj){
            videoView.setVideoPath(obj.getUrl());
            title.setText(obj.getTitle());
            desc.setText(obj.getDescription());



            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer stopPosition = videoView.getCurrentPosition()+800;
                    if (videoView.isPlaying()){
                        videoView.pause();

                    }
                    else {
                        videoView.seekTo(stopPosition);
                        videoView.start();
                    }

                }
            });

            videoControls.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getActionMasked()){

                        case MotionEvent.ACTION_DOWN:
                            xDown = motionEvent.getX();
                            yDown = motionEvent.getY();


                            break;
                        case MotionEvent.ACTION_MOVE:
                            float movedX, movedY;
                            movedX = motionEvent.getX();
                            movedY = motionEvent.getY();


                            float distanceX = movedX-xDown;
                            float distanceY = movedY-yDown;

                            videoControls.setX(videoControls.getX()+distanceX);
                            videoControls.setY(videoControls.getY()+distanceY);



                            break;
                    }
                    return true;
                }
            });

            upvoteBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:
                            xDown = motionEvent.getX();
                            yDown = motionEvent.getY();


                            break;
                        case MotionEvent.ACTION_MOVE:
                            float movedX, movedY;
                            movedX = motionEvent.getX();
                            movedY = motionEvent.getY();


                            float distanceX = movedX-xDown;
                            float distanceY = movedY-yDown;

                            videoControls.setX(videoControls.getX()+distanceX);
                            videoControls.setY(videoControls.getY()+distanceY);



                            break;
                    }
                    return true;
                }
            });

            downvoteBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_DOWN:
                            xDown = motionEvent.getX();
                            yDown = motionEvent.getY();


                            break;
                        case MotionEvent.ACTION_MOVE:
                            float movedX, movedY;
                            movedX = motionEvent.getX();
                            movedY = motionEvent.getY();


                            float distanceX = movedX-xDown;
                            float distanceY = movedY-yDown;

                            videoControls.setX(videoControls.getX()+distanceX);
                            videoControls.setY(videoControls.getY()+distanceY);


                            break;
                    }
                    return true;
                }
            });



        }

    }

}
