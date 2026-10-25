package com.example.premiumdownloader.direct;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

import java.util.Locale;

public final class DirectDownloadManager {
    private DirectDownloadManager() {}

    public static boolean isValidHttpUrl(String value) {
        if (value == null || !URLUtil.isValidUrl(value)) return false;
        String lower = value.toLowerCase(Locale.ROOT);
        return lower.startsWith("https://");
    }

    public static boolean hasAllowedMediaExtension(String value) {
        if (value == null) return false;
        String path = Uri.parse(value).getPath();
        if (path == null) return false;
        String p = path.toLowerCase(Locale.ROOT);
        return p.endsWith(".mp4") || p.endsWith(".m4v") ||
                p.endsWith(".webm") || p.endsWith(".mp3") ||
                p.endsWith(".m4a") || p.endsWith(".aac") ||
                p.endsWith(".wav") || p.endsWith(".ogg");
    }

    public static long enqueue(Context context, String url) {
        DownloadManager manager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(URLUtil.guessFileName(url, null, null));
        request.setDescription("Downloading permitted media");
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(false);
        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, null, null));
        return manager.enqueue(request);
    }
}
