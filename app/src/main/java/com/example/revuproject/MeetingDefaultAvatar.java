package com.example.revuproject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MeetingDefaultAvatar {
    public static String getRandom(){
        final List<String> variants = Arrays.asList(
                "example_photo_meeting_1",
                "example_photo_meeting_2",
                "example_photo_meeting_3",
                "example_photo_meeting_4",
                "example_photo_meeting_5",
                "example_photo_meeting_6",
                "example_photo_meeting_7",
                "example_photo_meeting_8",
                "example_photo_meeting_9",
                "example_photo_meeting_10",
                "example_photo_meeting_11",
                "example_photo_meeting_12",
                "example_photo_meeting_13",
                "example_photo_meeting_14",
                "example_photo_meeting_15",
                "example_photo_meeting_16",
                "example_photo_meeting_17",
                "example_photo_meeting_18",
                "example_photo_meeting_19",
                "example_photo_meeting_20",
                "example_photo_meeting_21",
                "example_photo_meeting_22",
                "example_photo_meeting_23",
                "example_photo_meeting_24",
                "example_photo_meeting_25",
                "example_photo_meeting_26",
                "example_photo_meeting_27",
                "example_photo_meeting_28",
                "example_photo_meeting_29",
                "example_photo_meeting_30",
                "example_photo_meeting_31"
        );

        int i = new Random().nextInt(31);
        return variants.get(i);
    }
}
