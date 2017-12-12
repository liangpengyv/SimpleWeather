package online.laoliang.simpleweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import online.laoliang.simpleweather.model.City;
import online.laoliang.simpleweather.model.County;
import online.laoliang.simpleweather.model.Province;
import online.laoliang.simpleweather.model.SimpleWeatherDB;

/**
 * 由于服务器返回的省市县数据都是“代号|城市”这种格式， 所以创建此工具类来解析和处理这种数据
 *
 * @author liang
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param simpleWeatherDB
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResponse(SimpleWeatherDB simpleWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    // 将解析出来的数据存储到Province表
                    simpleWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param simpleWeatherDB
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCitiesResponse(SimpleWeatherDB simpleWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    // 将解析出来的数据存储到City表
                    simpleWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param simpleWeatherDB
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountiesResponse(SimpleWeatherDB simpleWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    // 将解析出来的数据存储到County表
                    simpleWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析服务器返回的天气JSON数据， 并将解析出的数据存储到本地
     *
     * @param context
     * @param response
     * @return
     */
    public static String handleWeatherResponse(Context context, String response) {
        String city = null;
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONObject data = jsonObject.getJSONObject("data");
            city = data.getString("city");
            String wendu = data.getString("wendu");

            // 昨天（yesterday）
            JSONObject yesterday = data.getJSONObject("yesterday");
            String high_00 = yesterday.getString("high");
            int high_00_length = high_00.length();
            high_00 = high_00.substring(3, high_00_length - 1) + "°";
            String low_00 = yesterday.getString("low");
            int low_00_length = low_00.length();
            low_00 = low_00.substring(3, low_00_length - 1) + "°";
            String date_00 = "昨天";
            String type_00 = yesterday.getString("type");
            String fengli_00 = yesterday.getString("fl");

            JSONArray forecast = data.getJSONArray("forecast");
            // 今天（day_0）
            JSONObject day_0 = forecast.getJSONObject(0);
            String high_0 = day_0.getString("high");
            int high_0_length = high_0.length();
            high_0 = high_0.substring(3, high_0_length - 1) + "°";
            String low_0 = day_0.getString("low");
            int low_0_length = low_0.length();
            low_0 = low_0.substring(3, low_0_length - 1) + "°";
            String date_0 = "今天";
            String type_0 = day_0.getString("type");
            String fengli_0 = day_0.getString("fengli");

            // 明天（day_1）
            JSONObject day_1 = forecast.getJSONObject(1);
            String high_1 = day_1.getString("high");
            int high_1_length = high_1.length();
            high_1 = high_1.substring(3, high_1_length - 1) + "°";
            String low_1 = day_1.getString("low");
            int low_1_length = low_1.length();
            low_1 = low_1.substring(3, low_1_length - 1) + "°";
            String date_1 = "明天";
            String type_1 = day_1.getString("type");
            String fengli_1 = day_1.getString("fengli");

            // 后天（day_2）
            JSONObject day_2 = forecast.getJSONObject(2);
            String high_2 = day_2.getString("high");
            int high_2_length = high_2.length();
            high_2 = high_2.substring(3, high_2_length - 1) + "°";
            String low_2 = day_2.getString("low");
            int low_2_length = low_2.length();
            low_2 = low_2.substring(3, low_2_length - 1) + "°";
            String date_2 = day_2.getString("date").substring(day_2.getString("date").length() - 3);
            String type_2 = day_2.getString("type");
            String fengli_2 = day_2.getString("fengli");

            // 大后天（day_3）
            JSONObject day_3 = forecast.getJSONObject(3);
            String high_3 = day_3.getString("high");
            int high_3_length = high_3.length();
            high_3 = high_3.substring(3, high_3_length - 1) + "°";
            String low_3 = day_3.getString("low");
            int low_3_length = low_3.length();
            low_3 = low_3.substring(3, low_3_length - 1) + "°";
            String date_3 = day_3.getString("date").substring(day_3.getString("date").length() - 3);
            String type_3 = day_3.getString("type");
            String fengli_3 = day_3.getString("fengli");

            // 大大后天（day_4）
            JSONObject day_4 = forecast.getJSONObject(4);
            String high_4 = day_4.getString("high");
            int high_4_length = high_4.length();
            high_4 = high_4.substring(3, high_4_length - 1) + "°";
            String low_4 = day_4.getString("low");
            int low_4_length = low_4.length();
            low_4 = low_4.substring(3, low_4_length - 1) + "°";
            String date_4 = day_4.getString("date").substring(day_4.getString("date").length() - 3);
            String type_4 = day_4.getString("type");
            String fengli_4 = day_4.getString("fengli");

            // 处理json数据中“风力”字段<![CDATA[]]>问题（问题样例：<![CDATA[<3级]]>）
            // 待服务器返回数据良好时，这里的操作可以删除
            fengli_00 = fengli_00.substring(9, fengli_00.length() - 3);
            fengli_0 = fengli_0.substring(9, fengli_0.length() - 3);
            fengli_1 = fengli_1.substring(9, fengli_1.length() - 3);
            fengli_2 = fengli_2.substring(9, fengli_2.length() - 3);
            fengli_3 = fengli_3.substring(9, fengli_3.length() - 3);
            fengli_4 = fengli_4.substring(9, fengli_4.length() - 3);

            //将服务器返回的所有天气信息存储到SharedPreferences文件中
            saveWeather(context, city, wendu, high_00, low_00, date_00, type_00, fengli_00, high_0, low_0, date_0, type_0, fengli_0, high_1, low_1, date_1, type_1, fengli_1, high_2, low_2, date_2, type_2, fengli_2, high_3, low_3, date_3, type_3, fengli_3, high_4, low_4, date_4, type_4, fengli_4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中
     *
     * @param context
     * @param city
     * @param wendu
     * @param high_00
     * @param low_00
     * @param date_00
     * @param type_00
     * @param fengli_00
     * @param high_0
     * @param low_0
     * @param date_0
     * @param type_0
     * @param fengli_0
     * @param high_1
     * @param low_1
     * @param date_1
     * @param type_1
     * @param fengli_1
     * @param high_2
     * @param low_2
     * @param date_2
     * @param type_2
     * @param fengli_2
     * @param high_3
     * @param low_3
     * @param date_3
     * @param type_3
     * @param fengli_3
     * @param high_4
     * @param low_4
     * @param date_4
     * @param type_4
     * @param fengli_4
     */
    private static void saveWeather(Context context, String city, String wendu, String high_00, String low_00, String date_00, String type_00, String fengli_00, String high_0, String low_0, String date_0, String type_0, String fengli_0, String high_1, String low_1, String date_1, String type_1, String fengli_1, String high_2, String low_2, String date_2, String type_2, String fengli_2, String high_3, String low_3, String date_3, String type_3, String fengli_3, String high_4, String low_4, String date_4, String type_4, String fengli_4) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm刷新", Locale.CHINA);
        SharedPreferences.Editor editor = context.getSharedPreferences(city, 0).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("current_date", sdf.format(new Date()));
        editor.putString("city", city);
        editor.putString("wendu", wendu);

        editor.putString("high_00", high_00);
        editor.putString("low_00", low_00);
        editor.putString("date_00", date_00);
        editor.putString("type_00", type_00);
        //得到的“昨日风力”如果是微风，就总是少一个“级”字——强迫症优化
        if (fengli_00.equals("微风")) {
            fengli_00 = "微风级";
        }
        editor.putString("fengli_00", fengli_00);

        editor.putString("high_0", high_0);
        editor.putString("low_0", low_0);
        editor.putString("date_0", date_0);
        editor.putString("type_0", type_0);
        editor.putString("fengli_0", fengli_0);

        editor.putString("high_1", high_1);
        editor.putString("low_1", low_1);
        editor.putString("date_1", date_1);
        editor.putString("type_1", type_1);
        editor.putString("fengli_1", fengli_1);

        editor.putString("high_2", high_2);
        editor.putString("low_2", low_2);
        editor.putString("date_2", date_2);
        editor.putString("type_2", type_2);
        editor.putString("fengli_2", fengli_2);

        editor.putString("high_3", high_3);
        editor.putString("low_3", low_3);
        editor.putString("date_3", date_3);
        editor.putString("type_3", type_3);
        editor.putString("fengli_3", fengli_3);

        editor.putString("high_4", high_4);
        editor.putString("low_4", low_4);
        editor.putString("date_4", date_4);
        editor.putString("type_4", type_4);
        editor.putString("fengli_4", fengli_4);

        editor.commit();
    }

}
