package de.circuitco.bellbox.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by alex on 1/20/2018.
 */

@Entity
public class KnownSender {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "sender")
    public String sender;
}
