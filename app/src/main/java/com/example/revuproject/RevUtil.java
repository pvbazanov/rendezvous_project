package com.example.revuproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RevUtil {
    public SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());
    public boolean correctDateFormat(String string) {
        Date date = null;
        try {
            date = sdf.parse(string);
        }catch (ParseException e){}
        return (date != null);
    }

    public boolean noEmptyFields(String name, String place, String start, String end, List<String> participants){
        return (!name.trim().isEmpty()) && (!place.trim().isEmpty()) && (!start.trim().isEmpty()) && (!end.trim().isEmpty()) && !participants.isEmpty();
    }
}
