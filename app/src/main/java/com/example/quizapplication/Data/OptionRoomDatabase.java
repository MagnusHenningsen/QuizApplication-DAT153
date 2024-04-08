package com.example.quizapplication.Data;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Option.class}, version = 1, exportSchema = false)
public abstract class OptionRoomDatabase extends RoomDatabase {

    public abstract OptionDAO optionDAO();

    private static volatile OptionRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static OptionRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OptionRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    OptionRoomDatabase.class, "options_database") // Changed database name to be more representative
                            .addCallback(sRoomDatabaseCallback) // Attach your callback here
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                OptionDAO dao = INSTANCE.optionDAO();
                // Delete all content here.
                dao.deleteAll();

                // Add sample options
                dao.insert(new Option("android.resource://com.example.quizapplication/drawable/dog1", "Rico"));
                dao.insert(new Option("android.resource://com.example.quizapplication/drawable/dog2", "Kiki"));
                dao.insert(new Option("android.resource://com.example.quizapplication/drawable/dog3", "Baxter"));
            });
        }
    };
}