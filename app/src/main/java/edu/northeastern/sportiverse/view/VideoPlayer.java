package edu.northeastern.sportiverse.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import edu.northeastern.sportiverse.R;

public class VideoPlayer extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);

        // Get the video URL from the intent
        Intent intent = getIntent();
        String videoUri = intent.getStringExtra("videoUri");

        // Initialize the VideoView
        initializePlayer(videoUri);
    }

    private void initializePlayer(String videoUri) {
        try {
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(videoUri));
            videoView.requestFocus();
            videoView.setOnPreparedListener(mp -> videoView.start());
            videoView.setOnErrorListener((mp, what, extra) -> {
                // Log the error or show an error message to the user.
                Log.d("VideoPlayer", "Error occurred while playing video: what=" + what + ", extra=" + extra);

                return true; // Indicates we handled the error.
            });
        } catch (Exception e) {
            // Log the exception or show an error message.
            Log.e("VideoPlayer", "Error occurred while initializing video player", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
