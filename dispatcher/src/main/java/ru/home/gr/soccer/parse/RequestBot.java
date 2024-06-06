package ru.home.gr.soccer.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.log4j.Log4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j
@Builder
public class RequestBot {

    public static final String FOOTAPI_MATCHES_SLOW = "https://footapi7.p.rapidapi.com/api/matches/";
    public static final String FOOTAPI_MATCHES_FAST = "https://footapi7.p.rapidapi.com/api/matches/top/";
    public static final String X_RAPID_API_KEY = "X-RapidAPI-Key";
    public static final String X_RAPID_API_HOST = "X-RapidAPI-Host";
    public static final String FOOTAPI_7_P_RAPIDAPI_COM = "footapi7.p.rapidapi.com";
    public static final String KEY = "83172d3ac7msh65a39f2bf95cbccp162169jsn3e38337626fc";
    public static final String JSON_FILE = "/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/json.txt";
    public static final String FRIENDLY_JSON_FILE = "/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/jsonFriendlyUEFA.txt";
    public static final String LEAGUES_JSON_FILE = "/home/dev/Projects/soccer-bot/dispatcher/src/main/resources/jsonLeagues.txt";

    public FootballEvents tryToGetFromWebBody() {
        try {
            log.info("Send request. Waiting for response..");
            return getFootballEventsFromJson(
                    Objects.requireNonNull(buildOkHttpClient().newCall(buildRequest()).execute().body()).string());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private FootballEvents getFootballEventsFromJson(String jsonStr) throws IOException {
        if (jsonStr == null) {
            return null;
        }
        log.info("Success request! Process to response..");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, FootballEvents.class);
    }

    public FootballEvents getFootballEventsFromJson() throws IOException {
        String jsonStr = IOUtils.toString(new FileInputStream(FRIENDLY_JSON_FILE), StandardCharsets.UTF_8);
        return getFootballEventsFromJson(jsonStr);
    }

    @NotNull
    private OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                                         .writeTimeout(30, TimeUnit.SECONDS)
                                         .readTimeout(30, TimeUnit.SECONDS)
                                         .build();
    }

    @NotNull
    private Request buildRequest() {
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.valueOf(LocalDate.now().getMonth().getValue());
        String day = String.valueOf(LocalDate.now().getDayOfMonth());
        return new Request.Builder().url(FOOTAPI_MATCHES_FAST + day + "/" + month + "/" + year)
                                    .get()
                                    .addHeader(X_RAPID_API_KEY, KEY)
                                    .addHeader(X_RAPID_API_HOST, FOOTAPI_7_P_RAPIDAPI_COM)
                                    .build();
    }

}
