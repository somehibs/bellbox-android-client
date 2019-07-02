package de.circuitco.pushnotifications.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by alex on 1/20/2018.
 */

@Database(entities = {Push.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PushDao pushDao();

    public static AppDatabase database = null;

    public static AppDatabase getDatabase(Context ctx) {
        if (database == null) {
            database = Room.databaseBuilder(ctx.getApplicationContext(),
                                            AppDatabase.class,
                                            "pushdb")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
