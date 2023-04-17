package com.example.revuproject.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "place")
public class PlaceEntity {
    @PrimaryKey @ColumnInfo(name = "place_id")
    @NonNull public String placeId;

    @ColumnInfo(name = "place_name")
    @NonNull public String placeName = "";

    @ColumnInfo(name = "place_alt_name")
    public String placeAltName = "";

    @ColumnInfo(name = "place_address")
    @NonNull public String placeAddress = "";

    @ColumnInfo(name = "place_coords")
    @NonNull public String placeCoords = "1";

    @ColumnInfo(name = "place_bio")
    public String placeBio = "";

    @ColumnInfo(name = "place_avatar")
    public String placeAvatar;
}
