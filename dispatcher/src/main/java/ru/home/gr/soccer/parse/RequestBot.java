package ru.home.gr.soccer.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class RequestBot {

    public FootballEvents getFromWebBody() {
        OkHttpClient client = new OkHttpClient();

//        String year = String.valueOf(LocalDate.now().getYear());
//        String month = String.valueOf(LocalDate.now().getMonth().getValue());
//        String day = String.valueOf(LocalDate.now().getDayOfMonth());

        String year = "2023";
        String month = "09";
        String day = "19";

        Request request = new Request.Builder()
                .url("https://footapi7.p.rapidapi.com/api/matches/" + day + "/" + month + "/" + year)
                .get()
                .addHeader("X-RapidAPI-Key", "83172d3ac7msh65a39f2bf95cbccp162169jsn3e38337626fc")
                .addHeader("X-RapidAPI-Host", "footapi7.p.rapidapi.com")
                .build();

        FootballEvents footballEvents = null;

        try {
            var response = client.newCall(request).execute();
            var jsonStr = Objects.requireNonNull(response.body()).string();
            ObjectMapper mapper = new ObjectMapper();

            footballEvents = mapper.readValue(jsonStr, FootballEvents.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return footballEvents;
    }

    public FootballEvents getFromJsonBody() throws IOException {
        FootballEvents footballEvents = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            FileInputStream fis = new FileInputStream("/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/json.txt");
            String stringTooLong = IOUtils.toString(fis, "UTF-8");
            footballEvents = mapper.readValue(stringTooLong, FootballEvents.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return footballEvents;
    }

}
