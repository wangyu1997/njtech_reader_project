package com.reader.demo.reader.Controller;

import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.security.PermitAll;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@RestController
@ApiIgnore
@PermitAll
@RequestMapping(value = "/hao")
public class OtherController {

    @GetMapping("/query")
    public String addToCollect(String function, String outputsize, String symbol, String apikey) throws IOException {
        URL url = new URL("https://www.alphavantage.co/query"
                + "?function=" + function + "&outputsize=" + outputsize+"&symbol=" + symbol + "&apikey=" + apikey);
        System.out.println(url);
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null)
            builder.append(line).append("\n");
        in.close();
        return builder.toString();
    }


}
