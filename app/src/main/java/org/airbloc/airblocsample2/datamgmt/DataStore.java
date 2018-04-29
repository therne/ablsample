package org.airbloc.airblocsample2.datamgmt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Logger;

import org.airbloc.airblocsample2.App;
import org.airbloc.airblocsample2.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataStore {
    private static final String DB_NAME = "data";
    private static final int DB_VERSION = 1;

    private static SQLiteOpenHelper sqLiteHelper;

    public static void init(Context context) {
        sqLiteHelper =  new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                createDatabase(sqLiteDatabase);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
                Logger.d("Upgrading Databases... (from v%d to v%d)", oldVer, newVer);

                // 기존 (legacy) 데이터를 로드한다.

                // 기존 테이블을 전부 날린다.
                db.execSQL("DROP TABLE IF EXISTS data");

                // 새로운 테이블을 생성한다.
                this.onCreate(db);

                // 다시 데이터를 넣는다.
            }
        };

        sqLiteHelper.getWritableDatabase();
        sqLiteHelper.close();
    }

    static boolean isInitialized() {
        return sqLiteHelper != null;
    }

    /**
     * 데이터베이스 스키마 구조를 생성한다.
     * @param db
     */
    private static void createDatabase(SQLiteDatabase db) {

        Logger.d("Creating Databases...");
        String sql = "CREATE TABLE data ("
                + "id TEXT PRIMARY KEY,"
                + "className TEXT NOT NULL,"
                + "entityId TEXT NOT NULL,"
                + "data TEXT,"
                + "tag TEXT,"
                + "createdAt BIGINT NOT NULL,"
                + "UNIQUE(className, entityId));";
        db.execSQL(sql);
    }

    /**
     * 데이터를 저장한다.
     * @param object 저장할 데이터
     * @param tag 태그
     */
    public synchronized static void save(Storable object, String tag) {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("entityId", object.getId());
            values.put("className", object.getClass().getName());
            values.put("createdAt", object.getCreatedAt());
            values.put("data", App.getGson().toJson(object));
            if (tag != null) values.put("tag", tag);

            // UPSERT!
            sqLiteDatabase.insertWithOnConflict("data", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        } catch (Exception e) {
            // critical.
            Logger.e(e);
            throw e;

        } finally {
            sqLiteDatabase.close();
        }
    }

    /**
     * 데이터를 저장한다.
     * @param object 저장할 데이터
     */
    public static void save(Storable object) {
        save(object, null);
    }

    /**
     * 데이터를 삭제한다.
     * @param object 삭제할 데이터
     */
    public static void delete(Storable object) {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        sqLiteDatabase.delete("data", "entityId=? AND className=?",
                new String[]{ object.getId(), object.getClass().getName() });
        sqLiteDatabase.close();
    }

    /**
     * 데이터를 삭제한다.
     * @param id 삭제할 데이터 id
     * @param clazz 클래스명
     */
    public static void delete(String id, Class clazz) {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        sqLiteDatabase.delete("data", "entityId=? AND className=?",
                new String[]{ id, clazz.getName() });
        sqLiteDatabase.close();
    }

    public synchronized static <T extends Storable> T get(Class<T> clazz, String entityId) {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM data WHERE className=? AND entityId=?",
                new String[]{ clazz.getName(), entityId });

        if (cursor == null) {
            throw new RuntimeException("Failed to query to datastore.");
        }

        // Convert JSON Data to Entity object
        try {
            cursor.moveToNext();
            T result = App.getGson().fromJson(cursor.getString(cursor.getColumnIndex("data")), clazz);
            cursor.close();
            return result;

        } catch (CursorIndexOutOfBoundsException e) {
            return null;

        } catch (Exception error) {
            Logger.e(error);
            return null;

        } finally {
            sqLiteDatabase.close();
        }
    }

    /**
     * 특정 태그가 붙은, 해당 클래스의 데이터를 전부 가져온다.
     */
    private synchronized static <T extends Storable> List<T> retrieveAll(Class<T> clazz, String tag, int limit) {
        // build query
        String query = "SELECT * FROM data WHERE className=? ", args[];
        if (tag != null) {
            query += "AND tag=? ";
            args = new String[] { clazz.getName(), tag };
        }
        else args = new String[] { clazz.getName() };
        query += "ORDER BY createdAt desc";
        if (limit != -1) query += " LIMIT " + limit;

        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, args);
        if (cursor == null) throw new RuntimeException("Failed to query to datastore.");

        // Convert JSON Data to Entity object
        try {
            List<T> results = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                results.add(App.getGson().fromJson(cursor.getString(cursor.getColumnIndex("data")), clazz));
            }
            cursor.close();
            return results;

        } catch (CursorIndexOutOfBoundsException e) {
            return new ArrayList<>();

        } catch (Exception error) {
            Logger.e(error);
            return new ArrayList<>();

        } finally {
            sqLiteDatabase.close();
        }
    }

    /**
     * 특정 태그가 붙은, 해당 클래스의 데이터를 전부 가져온다.
     */
    public static <T extends Storable> List<T> getAllWithTag(Class<T> clazz, String tag) {
        return retrieveAll(clazz, tag, -1);
    }

    /**
     * 특정 태그가 붙은, 해당 클래스의 데이터를 하나 가져온다.
     */
    public static <T extends Storable> T getOneWithTag(Class<T> clazz, String tag) {
        return retrieveAll(clazz, tag, 1).get(0);
    }

    /**
     * 해당 클래스의 데이터를 전부 가져온다.
     */
    public static <T extends Storable> List<T> getAll(Class<T> clazz) {
        return retrieveAll(clazz, null, -1);
    }

    /**
     * 해당 클래스의 데이터를 하나 가져온다.
     */
    public static <T extends Storable> T getOne(Class<T> clazz) {
        return retrieveAll(clazz, null, 1).get(0);
    }

    /**
     * 해당 태그가 붙은 데이터의 개수를 센다.
     * @param clazz 클래스
     * @param tag 태그
     * @return 개수
     */
    public static int countWithTag(Class clazz, String tag) {
        String query = "SELECT id FROM data WHERE className=?", args[];
        if (tag != null) {
            query += " AND tag=?";
            args = new String[] { clazz.getName(), tag };
        }
        else args = new String[] { clazz.getName() };

        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, args);
        int count = cursor.getCount();
        cursor.close();
        sqLiteDatabase.close();

        return count;
    }

    /**
     * 해당 태그가 붙은 데이터의 개수를 센다.
     * @param clazz 클래스
     * @return 개수
     */
    public static int count(Class clazz) {
        return countWithTag(clazz, null);
    }

    /**
     * 해당 클래스의 데이터를 삭제한다.
     * @param clazz 클래스명
     */
    public static void deleteAll(Class clazz) {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        sqLiteDatabase.delete("data", "className=?",
                new String[]{ clazz.getName() });
        sqLiteDatabase.close();
    }


    /**
     * 데이터스토어를 리셋한다.
     */
    public static void deleteAll() {
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        sqLiteDatabase.delete("data", null, null);
        sqLiteDatabase.close();
    }
}
