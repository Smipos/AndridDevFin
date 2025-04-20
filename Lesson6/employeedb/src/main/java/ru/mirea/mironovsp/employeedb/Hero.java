package ru.mirea.mironovsp.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "superheroes")
public class Hero {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String superpower;

    public Hero(String name, String superpower) {
        this.name = name;
        this.superpower = superpower;
    }
}
