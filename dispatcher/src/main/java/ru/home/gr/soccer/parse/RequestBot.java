package ru.home.gr.soccer.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Log4j
public class RequestBot {

    public FootballEvents tryToGetFromWebBody() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            Request request = prepareRequest();

            var response = client.newCall(request).execute();
            log.info("Send request. Waiting for response..");
            if ((response.code()) == 200) {
                String jsonStr = response.body().string();
                log.info("Success request! Process to response..");
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonStr, FootballEvents.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private Request prepareRequest() {
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.valueOf(LocalDate.now().getMonth().getValue());
        String day = String.valueOf(LocalDate.now().getDayOfMonth());

        return new Request.Builder()
                .url("https://footapi7.p.rapidapi.com/api/matches/" + day + "/" + month + "/" + year)
                .get()
                .addHeader("X-RapidAPI-Key", "83172d3ac7msh65a39f2bf95cbccp162169jsn3e38337626fc")
                .addHeader("X-RapidAPI-Host", "footapi7.p.rapidapi.com")
                .build();
    }

    public FootballEvents tryToGetFromJsonBody() throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            FileInputStream fis = new FileInputStream("/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/json.txt");
            String stringTooLong = IOUtils.toString(fis, "UTF-8");
            return mapper.readValue(stringTooLong, FootballEvents.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
