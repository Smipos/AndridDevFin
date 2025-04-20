package ru.mirea.mironovsp.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HeroDao {
    @Query("SELECT * FROM superheroes")
    List<Hero> getAll();

    @Query("SELECT * FROM superheroes WHERE id = :id")
    Hero getById(long id);


    @Insert
    void insert(Hero hero);

    @Update
    void update(Hero hero);

    @Delete
    void delete(Hero hero);
}
