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
public interface KnownSenderDao {
    @Query("SELECT * FROM knownsender")
    List<KnownSender> getAll();

    @Query("SELECT * FROM knownsender WHERE uid IN (:ids)")
    List<KnownSender> findByIds(int[] ids);

    @Query("SELECT * FROM knownsender WHERE sender = :sender")
    List<KnownSender> findBySender(String sender);

    @Query("SELECT DISTINCT(sender) FROM knownsender")
    List<String> findSenders();

    @Insert
    void insert(KnownSender... senders);

    @Delete
    void delete(KnownSender sender);


}
