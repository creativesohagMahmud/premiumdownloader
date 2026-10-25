package com.example.premiumdownloader.player;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.premiumdownloader.R;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerView playerView;
    private TextView audioTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 16, 16, 16);
        root.setBackgroundColor(0xFF0B0B12);

        TextView title = new TextView(this);
        title.setText("Premium Media Player");
        title.setTextColor(0xFFF5F3FF);
        title.setTextSize(24);
        title.setPadding(0, 8, 0, 16);
        root.addView(title);

        playerView = new PlayerView(this);
        playerView.setUseController(true);
        root.addView(playerView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(230)));

        audioTitle = new TextView(this);
        audioTitle.setText("Video / Audio playback");
        audioTitle.setTextColor(0xFFF5F3FF);
        audioTitle.setTextSize(16);
        audioTitle.setPadding(0, 16, 0, 12);
        root.addView(audioTitle);

        Button open = new Button(this);
        open.setText("Play media URL");
        root.addView(open);
        open.setOnClickListener(v -> {
            String url = getIntent().getStringExtra("media_url");
            if (url == null || !(url.startsWith("https://") || url.startsWith("content://") || url.startsWith("file://"))) {
                Toast.makeText(this, "Open player with a supported media URL or local media URI", Toast.LENGTH_LONG).show();
                return;
            }
            play(url);
        });

        setContentView(root);
    }

    private void play(String url) {
        releasePlayer();
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
        player.prepare();
        player.play();
        audioTitle.setText("Playing: " + Uri.parse(url).getLastPathSegment());
        player.addListener(new Player.Listener() {
            @Override public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_ENDED) audioTitle.setText("Playback finished");
            }
        });
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void releasePlayer() {
        if (player != null) {
            playerView.setPlayer(null);
            player.release();
            player = null;
        }
    }

    @Override protected void onStop() {
        super.onStop();
        releasePlayer();
    }
}
