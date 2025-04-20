package ru.mirea.mironovsp.employeedb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText superpowerEditText;
    private Button addButton;
    private Button showAllButton;
    private TextView resultTextView;

    private DatabaseHero db;
    private HeroDao superHeroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = DatabaseHero.getDatabase(this);
        superHeroDao = db.HeroDao();

        nameEditText = findViewById(R.id.nameEditText);
        superpowerEditText = findViewById(R.id.superpowerEditText);

        addButton = findViewById(R.id.addButton);
        showAllButton = findViewById(R.id.showAllButton);
        resultTextView = findViewById(R.id.resultTextView);

        addButton.setOnClickListener(v -> addSuperHero());
        showAllButton.setOnClickListener(v -> showAllSuperHeroes());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (superHeroDao.getAll().isEmpty()) {
            addInitialHeroes();
        }
    }

    private void addSuperHero() {
        try {
            String name = nameEditText.getText().toString();
            String superpower = superpowerEditText.getText().toString();


            Hero hero = new Hero(name, superpower);
            superHeroDao.insert(hero);

            Toast.makeText(this, "Супер-герой добавлен!", Toast.LENGTH_SHORT).show();
            clearInputs();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректное имя супер-героя", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAllSuperHeroes() {
        List<Hero> heroes = superHeroDao.getAll();
        StringBuilder sb = new StringBuilder();

        for (Hero hero : heroes) {
            sb.append("ID: ").append(hero.id)
                    .append("\nИмя: ").append(hero.name)
                    .append("\nСуперсила: ").append(hero.superpower)
                    .append("\n\n");
        }

        resultTextView.setText(sb.toString());
    }

    private void addInitialHeroes() {
        Hero[] heroes = {
                new Hero("Человек-морковь", "Сладкий привкус, карнитин"),
                new Hero("Увлажнитель KITFORT 2810", "Свободное дыхание, аромамасло"),
                new Hero("Sony CH720N", "Басы, -деньги из кошелька, музыкальный экстаз")
        };

        for (Hero hero : heroes) {
            superHeroDao.insert(hero);
        }
    }

    private void clearInputs() {
        nameEditText.setText("");
        superpowerEditText.setText("");
    }
}