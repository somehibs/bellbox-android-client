package de.circuitco.pushnotifications.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by alex on 1/20/2018.
 */

@Dao
public interface PushDao {
    @Query("SELECT * FROM push")
    List<Push> getAll();


    @Query("SELECT * FROM push WHERE uid IN (:userIds)")
    List<Push> findByIds(int[] userIds);

    @Insert
    void insert(Push... pushes);

    @Delete
    void delete(Push push);


}
