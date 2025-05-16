package ru.mirea.mironovsp.mireaproject;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.mironovsp.mireaproject.databinding.ActivityMainBinding;

import android.app.AlertDialog;
import android.widget.Toast;

import ru.mirea.mironovsp.mireaproject.ui.FilesFragment;
import androidx.navigation.NavController;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);

                if (navController.getCurrentDestination().getId() == R.id.nav_files) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                    if (fragment != null && fragment.getChildFragmentManager().getPrimaryNavigationFragment() instanceof FilesFragment) {
                        FilesFragment filesFragment = (FilesFragment) fragment.getChildFragmentManager().getPrimaryNavigationFragment();
                        showCreateFileDialog(filesFragment);
                    }
                } else {
                    Snackbar.make(view, "Действие доступно только в разделе 'Работа с файлами'", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .setAnchorView(R.id.fab).show();
                }
            }
        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_data,
                R.id.nav_api,
                R.id.nav_profile,
                R.id.nav_files,
                R.id.nav_altitude,
                R.id.nav_microphone,
                R.id.nav_browser,
                R.id.nav_worker)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void showCreateFileDialog(FilesFragment filesFragment) {
        new AlertDialog.Builder(this)
                .setTitle("Новая запись")
                .setMessage("Выберите действие:")
                .setPositiveButton("Шифрование", (dialog, which) -> {
                    filesFragment.clearFields();
                    Toast.makeText(this, "Готово к шифрованию", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

}