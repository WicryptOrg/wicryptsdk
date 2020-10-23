package com.ugarsoft.wicryptsdk_android.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ugarsoft.wicryptsdk_android.Models.APIResponse;
import com.ugarsoft.wicryptsdk_android.Models.Tuple;
import com.ugarsoft.wicryptsdk_android.Models.User;
import com.ugarsoft.wicryptsdk_android.Models.UserExist;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthService {

    private final String baseUrl = "http://216.117.149.42:9010";
    private OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build();


    public void userExist(String email, final onUserExistCallback callback){
        Request request = new Request.Builder()
                .url(String.format(baseUrl + "/api/v2/OTP/SendOtpIfUserExist?emailAddress=%s", email))
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailed(new Error(e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    String data = object.toString();

                    Gson gson = new Gson();
                    Type type = new TypeToken<APIResponse<UserExist>>(){}.getType();
                    APIResponse<UserExist> r = gson.fromJson(data, type);
                    callback.onSuccess(r.getData());

                }catch (Exception ex){
                    callback.onFailed(new Error(ex.getLocalizedMessage()));
                }
            }
        });
    }

    public void loginUser(String email, String otp, String businessId, final onLoginUserCallback callback){

        HashMap<String, String> bodyAsMap = new HashMap<>();
        bodyAsMap.put("emailAddress", email);
        bodyAsMap.put("otp", otp);
        bodyAsMap.put("businessId", businessId);

        Tuple<OkHttpClient, Request> tuple = createPostClient(bodyAsMap, baseUrl + "/api/v2/OTP/EmailOTPValidator");

        tuple.x.newCall(tuple.y).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailed(new Error(e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    String data = object.toString();

                    Gson gson = new Gson();
                    Type type = new TypeToken<APIResponse<User>>(){}.getType();
                    APIResponse<User> r = gson.fromJson(data, type);
                    callback.onSuccess(r.getData());

                }catch (Exception ex){
                    callback.onFailed(new Error(ex.getLocalizedMessage()));
                }
            }
        });
    }

    public void registerUser(String name, String email, String referrer,
                               String password, String macAddress, String businessId,
                             final onRegisterUserCallback callback){

        HashMap<String, String> bodyAsMap = new HashMap<>();
        bodyAsMap.put("name", name);
        bodyAsMap.put("email", email);
        bodyAsMap.put("referrer", referrer);
        bodyAsMap.put("password", password);
        bodyAsMap.put("macAddress", macAddress);
        bodyAsMap.put("businessId", businessId);

        Tuple<OkHttpClient, Request> tuple = createPostClient(bodyAsMap, baseUrl + "/api/v2/auth/SDKsignup");

        tuple.x.newCall(tuple.y).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFailed(new Error(e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    String result = response.body().string();
                    JSONObject object = new JSONObject(result);
                    String data = object.toString();

                    Gson gson = new Gson();
                    Type type = new TypeToken<APIResponse<User>>(){}.getType();
                    APIResponse<User> r = gson.fromJson(data, type);
                    callback.onSuccess(r.getData());

                }catch (Exception ex){
                    callback.onFailed(new Error(ex.getLocalizedMessage()));
                }
            }
        });

    }

    private Tuple<OkHttpClient, Request> createPostClient(HashMap<String, String> bodyAsMap, String url){
        String v = serialize(bodyAsMap);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, serialize(bodyAsMap));
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        return new Tuple<>(client, request);
    }

    private String serialize(HashMap<String, String> map) {
        String s = "{";
        int index = 1;
        for (Map.Entry<String, String> valueSet : map.entrySet()){
            s += ("\n \"" + valueSet.getKey() + "\": \"" + valueSet.getValue() + "\"");
            if (index == map.size()){
                s += "\n}";
            }else{
                s += ",";
            }
            index++;
        }
        return s;
    }

    public interface onUserExistCallback{
        void onSuccess(UserExist userExist);
        void onFailed(Error error);
    };

    public interface onLoginUserCallback{
        void onSuccess(User user);
        void onFailed(Error error);
    }

    public interface onRegisterUserCallback{
        void onSuccess(User user);
        void onFailed(Error error);
    }
}
