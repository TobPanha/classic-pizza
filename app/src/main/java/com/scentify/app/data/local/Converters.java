package com.scentify.app.data.local;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {
    @TypeConverter
    public static List<String> stringToList(String stored) {
        if (stored == null || stored.isEmpty()) {
            return new ArrayList<>();
        }
        String[] parts = stored.split(",");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    @TypeConverter
    public static String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
                .filter(value -> value != null && !value.isEmpty())
                .collect(Collectors.joining(","));
    }
}
