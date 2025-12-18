package com.scentify.app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.scentify.app.utils.SampleDataProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DatabaseProvider {
    private static final String DATABASE_NAME = "scentify.db";
    private static volatile AppDatabase instance;
    private static final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private DatabaseProvider() {
    }

    public static AppDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (DatabaseProvider.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseExecutor.execute(() -> {
                                        AppDatabase database = getInstance(context);
                                        database.productDao().insertProducts(SampleDataProvider.provideProducts());
                                    });
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
