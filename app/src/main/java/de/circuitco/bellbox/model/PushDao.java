package de.circuitco.bellbox.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
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

    @Query("SELECT * FROM push WHERE sender = :sender")
    List<Push> findBySender(String sender);

    @Query("SELECT COUNT(*) FROM push WHERE sender = :sender")
    Long countBySender(String sender);

    @Query("SELECT DISTINCT(sender) FROM push")
    List<String> findSenders();

    @Insert
    void insert(Push... pushes);

    @Delete
    void delete(Push push);


}
