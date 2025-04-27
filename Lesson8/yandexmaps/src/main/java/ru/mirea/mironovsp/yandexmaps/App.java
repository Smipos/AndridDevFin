package ru.mirea.mironovsp.yandexmaps;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "3234bae5-2fdf-43c6-a16b-a4735c2c12df";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}
