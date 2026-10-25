package com.example.premiumdownloader.youtube;

import android.net.Uri;

import java.util.Locale;

/**
 * YouTube link validation/opening only.
 * This class intentionally does not extract streams or bypass YouTube download restrictions.
 */
public final class YouTubeLinkHandler {
    private YouTubeLinkHandler() {}

    public static boolean isYouTubeUrl(String value) {
        if (value == null) return false;
        try {
            Uri uri = Uri.parse(value.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (scheme == null || host == null ||
                    !(scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("http"))) {
                return false;
            }
            String h = host.toLowerCase(Locale.ROOT);
            return h.equals("youtube.com") || h.endsWith(".youtube.com") ||
                    h.equals("youtu.be") || h.endsWith(".youtu.be");
        } catch (Exception ignored) {
            return false;
        }
    }
}
