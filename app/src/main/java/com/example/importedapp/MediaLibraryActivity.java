package com.example.premiumdownloader.library;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.premiumdownloader.player.PlayerActivity;

import java.util.ArrayList;

public class MediaLibraryActivity extends AppCompatActivity {
    private final ArrayList<String> labels = new ArrayList<>();
    private final ArrayList<Uri> uris = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView status;
    private String currentType = "audio";

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> loadMedia());

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));
        root.setBackgroundColor(0xFF0B0B12);

        TextView title = new TextView(this);
        title.setText("My Media Library");
        title.setTextColor(0xFFF7F2FF);
        title.setTextSize(24);
        title.setPadding(0, 0, 0, dp(12));
        root.addView(title);

        LinearLayout buttons = new LinearLayout(this);
        Button audio = new Button(this); audio.setText("Audio");
        Button video = new Button(this); video.setText("Video");
        buttons.addView(audio, new LinearLayout.LayoutParams(0, -2, 1));
        buttons.addView(video, new LinearLayout.LayoutParams(0, -2, 1));
        root.addView(buttons);

        status = new TextView(this);
        status.setTextColor(0xFFB9B0CE);
        root.addView(status);

        ListView list = new ListView(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, labels);
        list.setAdapter(adapter);
        root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));

        audio.setOnClickListener(v -> { currentType = "audio"; loadMedia(); });
        video.setOnClickListener(v -> { currentType = "video"; loadMedia(); });
        list.setOnItemClickListener((parent, view, position, id) -> {
            if (position < uris.size()) {
                Intent intent = new Intent(this, PlayerActivity.class);
                intent.putExtra("media_url", uris.get(position).toString());
                startActivity(intent);
            }
        });
        setContentView(root);
        loadMedia();
    }

    private void loadMedia() {
        if (!hasPermission()) {
            if (Build.VERSION.SDK_INT >= 33) {
                permissionLauncher.launch(new String[]{Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO});
            } else {
                permissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            }
            status.setText("Allow media access to scan this device.");
            return;
        }
        labels.clear(); uris.clear();
        Uri collection = "audio".equals(currentType)
                ? MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                : MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DISPLAY_NAME};
        String selection = "audio".equals(currentType) ? MediaStore.Audio.Media.IS_MUSIC + " != 0" : null;
        try (Cursor cursor = getContentResolver().query(collection, projection, selection, null,
                MediaStore.MediaColumns.DISPLAY_NAME + " COLLATE NOCASE ASC")) {
            if (cursor != null) {
                int idCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
                int nameCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idCol);
                    String name = cursor.getString(nameCol);
                    labels.add(name == null ? "Unknown media" : name);
                    uris.add(ContentUris.withAppendedId(collection, id));
                }
            }
            adapter.notifyDataSetChanged();
            status.setText(labels.size() + " " + currentType + " file(s) found • Tap one to play");
        } catch (Exception e) {
            status.setText("Could not read media: " + e.getMessage());
            Toast.makeText(this, "Media scan failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            String p = "audio".equals(currentType) ? Manifest.permission.READ_MEDIA_AUDIO : Manifest.permission.READ_MEDIA_VIDEO;
            return ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private int dp(int n) { return (int)(n * getResources().getDisplayMetrics().density); }
}
