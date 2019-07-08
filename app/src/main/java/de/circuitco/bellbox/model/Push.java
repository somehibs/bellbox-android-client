package de.circuitco.bellbox.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alex on 1/20/2018.
 */

@Entity
public class Push {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "original_data")
    public String originalData;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "sender")
    public String sender;
}
