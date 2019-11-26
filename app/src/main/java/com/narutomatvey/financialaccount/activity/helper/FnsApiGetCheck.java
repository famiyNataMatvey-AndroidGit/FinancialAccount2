package com.narutomatvey.financialaccount.activity.helper;

import android.os.AsyncTask;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class FnsApiGetCheck extends AsyncTask<String, Void, String> {

    private String phone;
    private String password;

    public FnsApiGetCheck(@NonNull String phone, @NonNull String password) {
        this.phone = phone;
        this.password = password;
    }

    @Override
    protected String doInBackground(String... informations) {
        String info = informations[0];
        Pattern regexp = Pattern.compile("t=(\\w+)|s=([\\w.]+)|fn=(\\w+)|i=(\\w+)|fp=(\\w+)");
        return null;
    }


    private JSONObject isCheck(String FN, String FD, String FPD, String date, double sum) {
        String IS_CHECK_URL = "https://proverkacheka.nalog.ru:9999/v1/ofds/*/inns/*/fss/";
        String baseUrl = IS_CHECK_URL + FN + "/operations/1/tickets/" + FD;
        String baseUrlParameters = baseUrl + "?fiscalSign=" + FPD + "&date=" + date + "&sum=" + sum;

        JSONObject response = new JSONObject();

        try {

            URL url = new URL(baseUrlParameters);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 8.0");
            urlConnection.setRequestProperty("Device-Id", "");

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
                response.put("code", responseCode);
                if (responseCode == HttpsURLConnection.HTTP_NOT_ACCEPTABLE) {
                    response.put("message", "Чек не найден.");
                } else if (responseCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
                    response.put("message", "Не указана сумма или дата");
                } else response.put("message", "Ошибка взаимодействия с сервером ФНС");
            } else {
                response.put("code", HttpsURLConnection.HTTP_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private JSONObject getCheck(String FN, String FD, String FPD) {
        String GET_CHECK_URL = "https://proverkacheka.nalog.ru:9999/v1/inns/*/kkts/*/fss/";
        String baseUrl = GET_CHECK_URL + FN + "/tickets/" + FD;
        String baseUrlParameters = baseUrl + "?fiscalSign=" + FPD + "&sendToEmail=no";

        JSONObject response = new JSONObject();
        try {

            URL url = new URL(baseUrlParameters);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 8.0");
            urlConnection.setRequestProperty("Device-Id", "");

            String authString = this.phone + ":" + this.password;
            String basicAuth = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            int responseCode = urlConnection.getResponseCode();
            response.put("code", responseCode);

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    response.put("massage", "Указаны некоректные данные пользователя.");
                } else if (responseCode == HttpsURLConnection.HTTP_NOT_ACCEPTABLE) {
                    response.put("massage", "Чек не найден.2");
                } else {
                    response.put("massage", "Неизвестный код пришел от ФНС");
                }
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

                String s;
                StringBuilder sb = new StringBuilder();
                while ((s = reader.readLine()) != null) {
                    sb.append(s);
                }
                reader.close();
                response.put("massage", new JSONObject(sb.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
