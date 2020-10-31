package org.example.tracker.datamodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
public class ImageResponse {

    private final Map<String, String> urls = new HashMap<>();

    public ImageResponse() {
    }

    public ImageResponse(String defaultUrl) {
        urls.put("default", defaultUrl);
    }

    public ImageResponse add(String key, String url) {
        urls.put(key, url);
        return this;
    }
}
