package com.example.conference;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;


@SpringBootTest
class ConferenceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void whenUploadFile_thenCorrect() throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "file.txt",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("src/test/resources/test.txt")))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.0.106:8082/user/avatar")
                .post(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder().build();

        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response.code());
    }
}
