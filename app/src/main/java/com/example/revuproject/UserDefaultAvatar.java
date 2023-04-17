package com.example.revuproject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UserDefaultAvatar {
    public static String getRandom(){
        final List<String> variants = Arrays.asList(
                "example_photo_avatar_1",
                "example_photo_avatar_2",
                "example_photo_avatar_3",
                "example_photo_avatar_4",
                "example_photo_avatar_5",
                "example_photo_avatar_6",
                "example_photo_avatar_7",
                "example_photo_avatar_8",
                "example_photo_avatar_9",
                "example_photo_avatar_10",
                "example_photo_avatar_11",
                "example_photo_avatar_12",
                "example_photo_avatar_13",
                "example_photo_avatar_14",
                "example_photo_avatar_15",
                "example_photo_avatar_16",
                "example_photo_avatar_17",
                "example_photo_avatar_18",
                "example_photo_avatar_19",
                "example_photo_avatar_20",
                "example_photo_avatar_21",
                "example_photo_avatar_22",
                "example_photo_avatar_23",
                "example_photo_avatar_24",
                "example_photo_avatar_25",
                "example_photo_avatar_26",
                "example_photo_avatar_27",
                "example_photo_avatar_28",
                "example_photo_avatar_29",
                "example_photo_avatar_30",
                "example_photo_avatar_31"
        );
        int i = new Random().nextInt(31);
        return variants.get(i);
    }
}
