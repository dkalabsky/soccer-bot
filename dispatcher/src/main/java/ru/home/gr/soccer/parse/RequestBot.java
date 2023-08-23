package ru.home.gr.soccer.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class RequestBot {

    public JsonResult getBody() {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://footapi7.p.rapidapi.com/api/matches/24/8/2023")
//                .get()
//                .addHeader("X-RapidAPI-Key", "83172d3ac7msh65a39f2bf95cbccp162169jsn3e38337626fc")
//                .addHeader("X-RapidAPI-Host", "footapi7.p.rapidapi.com")
//                .build();
//
//        Response response;
        String jsonStr = null;
        JsonResult jsonResult = null;

        try {
//                response = client.newCall(request).execute();
//                jsonStr = Objects.requireNonNull(response.body()).string();
            ObjectMapper mapper = new ObjectMapper();
            FileInputStream fis = new FileInputStream("/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/json.txt");
            String stringTooLong = IOUtils.toString(fis, "UTF-8");
            jsonResult = mapper.readValue(stringTooLong, JsonResult.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResult;
    }

}
