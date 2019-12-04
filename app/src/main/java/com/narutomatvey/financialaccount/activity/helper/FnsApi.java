package com.narutomatvey.financialaccount.activity.helper;

import android.os.AsyncTask;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.narutomatvey.financialaccount.activity.enums.FnsAction;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class FnsApi extends AsyncTask<FnsAction, Void, JSONObject> {

    private String email;
    private String name;
    private String phone;
    private String password;

    public FnsApi(@NonNull String phone, @Nullable String email, @Nullable String name, @Nullable String password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    @Override
    protected JSONObject doInBackground(FnsAction... actions) {
        JSONObject response = null;

        FnsAction action = actions[0];
        switch (action) {
            case LOGIN:
                response = this.login();
                break;
            case REGISTRATION:
                response = this.registration();
                break;
            case PASSWORD_RECOVERY:
                response = this.passwordRecovery();
                break;
        }
        return response;
    }

    private JSONObject registration() {
        String REGISTRATION_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/signup";
        JSONObject response = new JSONObject();

        try {
            String data = new JSONObject()
                    .put("email", this.email)
                    .put("name", this.name)
                    .put("phone", this.phone)
                    .toString();

            URL url = new URL(REGISTRATION_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(data);
            writer.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
                response.put("code", responseCode);
                if (responseCode == HttpsURLConnection.HTTP_CONFLICT) {
                    response.put("message", "Пользователь с этими данными уже существует в базе ФНС");
                } else response.put("message", "Ошибка взаимодействия с сервером ФНС");
            } else {
                response.put("code", HttpsURLConnection.HTTP_OK);
                response.put("message", "Регистрация прошла успешно");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private JSONObject login() {
        String LOGIN_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/login";
        JSONObject response = new JSONObject();

        try {
            URL url = new URL(LOGIN_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Device-OS", "Adnroid 6.0");
            urlConnection.setRequestProperty("Device-Id", "androidID");

            String authString = this.phone + ":" + this.password;
            String basicAuth = "Basic " + Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty("Authorization", basicAuth);
            
            int responseCode = urlConnection.getResponseCode();
            response.put("code", responseCode);

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    response.put("message", "Некоректный номер телефона или пароль!");
                } else response.put("message", "Ошибка взаимодействия с сервером ФНС");
            } else {
                response.put("message", "Вы вошли в систему");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private JSONObject passwordRecovery() {
        String PASSWORD_RECOVERY_URL = "https://proverkacheka.nalog.ru:9999/v1/mobile/users/restore";
        JSONObject response = new JSONObject();;

        try {
            String data = new JSONObject()
                    .put("phone", this.phone)
                    .toString();

            URL url = new URL(PASSWORD_RECOVERY_URL);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(data);
            writer.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_NO_CONTENT) {
                response.put("code", responseCode);
                if (responseCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                    response.put("message", "Пользователя с телефоном " + this.phone + " не существует");
                } else {
                    response.put("message", "Ошибка взаимодействия с сервером ФНС");
                }
            } else {
                response.put("code", HttpsURLConnection.HTTP_OK);
                response.put("message", "На ваш номер телефона выслан пароль");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}

