package com.example.yumi.utils;
import android.net.Uri;
import android.util.Log;
import java.util.Objects;

public abstract class YouTubeHelper {
    public static String getYoutubeVideoId(String url) {
        if (url == null || url.isEmpty()) return null;

        try {
            Uri uri = Uri.parse(url);

            String v = uri.getQueryParameter("v");
            if (v != null && v.length() == 11) {
                return v;
            }

            if ("youtu.be".equals(uri.getHost())) {
                String id = uri.getLastPathSegment();
                if (id != null && id.length() == 11) {
                    return id;
                }
            }

            for (String segment : uri.getPathSegments()) {
                if (segment.length() == 11) {
                    return segment;
                }
            }

        } catch (Exception e) {
            Log.e("Youtube Handler", Objects.requireNonNull(e.getLocalizedMessage()));
        }

        return null;
    }
}
