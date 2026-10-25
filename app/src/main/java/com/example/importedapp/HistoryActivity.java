package com.example.premiumdownloader.history;

import android.app.DownloadManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24,24,24,24);
        root.setBackgroundColor(0xFF0B0B12);
        TextView title = new TextView(this);
        title.setText("Download History");
        title.setTextSize(24);
        title.setTextColor(0xFFF7F2FF);
        root.addView(title);
        ListView list = new ListView(this);
        ArrayList<String> rows = new ArrayList<>();
        DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        try (Cursor c = dm.query(new DownloadManager.Query())) {
            if (c != null) {
                int titleCol = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
                int statusCol = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                while (c.moveToNext()) {
                    String name = titleCol >= 0 ? c.getString(titleCol) : "Download";
                    int s = statusCol >= 0 ? c.getInt(statusCol) : -1;
                    String state = s == DownloadManager.STATUS_SUCCESSFUL ? "Completed" :
                            s == DownloadManager.STATUS_FAILED ? "Failed" :
                            s == DownloadManager.STATUS_RUNNING ? "Downloading" :
                            s == DownloadManager.STATUS_PAUSED ? "Paused" : "Queued";
                    rows.add(name + "\n" + state);
                }
            }
        } catch (Exception e) { rows.add("Unable to load history: " + e.getMessage()); }
        if (rows.isEmpty()) rows.add("No downloads found yet.");
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
        root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }
}
