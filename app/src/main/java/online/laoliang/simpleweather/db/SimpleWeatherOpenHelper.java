package online.laoliang.simpleweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 将三条建表语句定义成常量，然后再onCreate()方法中去执行创建
 *
 * @author liang
 */
public class SimpleWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * Province表建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province (" + "id integer primary key autoincrement, " + "province_name text, " + "province_code text)";

    /**
     * City表建表语句
     */
    public static final String CREATE_CITY = "create table City (" + "id integer primary key autoincrement, " + "city_name text, " + "city_code text, " + "province_id integer)";

    /**
     * County表建表语句
     */
    public static final String CREATE_COUNTY = "create table County (" + "id integer primary key autoincrement, " + "county_name text, " + "county_code text, " + "city_id integer)";

    public SimpleWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE); // 创建Province表
        db.execSQL(CREATE_CITY); // 创建City表
        db.execSQL(CREATE_COUNTY); // 创建County表
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

}
