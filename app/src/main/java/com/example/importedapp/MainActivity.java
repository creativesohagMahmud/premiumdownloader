package com.example.premiumdownloader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.premiumdownloader.direct.DirectDownloadManager;
import com.example.premiumdownloader.player.PlayerActivity;
import com.example.premiumdownloader.library.MediaLibraryActivity;
import com.example.premiumdownloader.history.HistoryActivity;
import com.example.premiumdownloader.premium.PremiumActivity;
import com.example.premiumdownloader.youtube.YouTubeLinkHandler;

public class MainActivity extends AppCompatActivity {
    private EditText urlInput;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlInput);
        statusText = findViewById(R.id.statusText);
        Button downloadButton = findViewById(R.id.downloadButton);
        Button youtubeButton = findViewById(R.id.youtubeButton);
        Button playButton = findViewById(R.id.playButton);
        Button libraryButton = findViewById(R.id.libraryButton);
        Button historyButton = findViewById(R.id.historyButton);
        findViewById(R.id.premiumText).setOnClickListener(v ->
                startActivity(new Intent(this, PremiumActivity.class)));

        downloadButton.setOnClickListener(v -> startDirectDownload());
        youtubeButton.setOnClickListener(v -> openYouTube());
        playButton.setOnClickListener(v -> openPlayer());
        libraryButton.setOnClickListener(v -> startActivity(new Intent(this, MediaLibraryActivity.class)));
        historyButton.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void startDirectDownload() {
        String value = urlInput.getText().toString().trim();
        if (!DirectDownloadManager.isValidHttpUrl(value)) {
            urlInput.setError("Enter a valid HTTPS media URL");
            return;
        }
        if (!DirectDownloadManager.hasAllowedMediaExtension(value)) {
            Toast.makeText(this,
                    "Use a direct link to a permitted media file (mp4, mp3, m4a, webm, etc.)",
                    Toast.LENGTH_LONG).show();
            return;
        }
        long id = DirectDownloadManager.enqueue(this, value);
        statusText.setText("Download queued. ID: " + id);
        Toast.makeText(this, "Download queued", Toast.LENGTH_SHORT).show();
    }

    private void openYouTube() {
        String value = urlInput.getText().toString().trim();
        if (!YouTubeLinkHandler.isYouTubeUrl(value)) {
            urlInput.setError("Enter a valid YouTube URL");
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(value)));
    }

    private void openPlayer() {
        String value = urlInput.getText().toString().trim();
        if (!DirectDownloadManager.isValidHttpUrl(value)) {
            urlInput.setError("Paste a permitted direct HTTPS media URL first");
            return;
        }
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("media_url", value);
        startActivity(intent);
    }
}
