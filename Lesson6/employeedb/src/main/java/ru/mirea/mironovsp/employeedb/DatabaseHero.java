package ru.mirea.mironovsp.employeedb;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Hero.class}, version = 1, exportSchema = false)
public abstract class DatabaseHero extends RoomDatabase {
    public abstract HeroDao HeroDao();

    private static volatile DatabaseHero INSTANCE;

    public static DatabaseHero getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHero.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DatabaseHero.class, "superhero_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
