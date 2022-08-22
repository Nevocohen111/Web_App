package com.example.demo.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ConversionApiService {

    @Autowired
    private WebClient webClient;

    public String convert(String from, String to, String amount) {
        String response = webClient.method(HttpMethod.GET)
                .uri("https://api.exchangerate.host/convert" +  '?' + "from=" + from + '&' + "to=" + to + '&' + "amount=" + amount)
                .retrieve()
                .bodyToMono(String.class).block();
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            JsonElement result = jsonObject.get("result");
        if(result == null || !from.equalsIgnoreCase("usd") && !from.equalsIgnoreCase("ils") || !to.equalsIgnoreCase("usd") && !to.equalsIgnoreCase("ils") || !amount.matches("[0-9]+")) {
            return "Invalid currency";
        }else if(from.equalsIgnoreCase("ils") && to.equalsIgnoreCase("ils") && amount.matches("[0-9]+") || from.equalsIgnoreCase("usd") && to.equalsIgnoreCase("usd") &&amount.matches("[0-9]+"))
            return amount;
        else {
            if(from.equalsIgnoreCase("usd"))
                return amount + "$" + " = " + result.getAsString() + "₪" ;
            else
                return amount + "₪" + " = " + result.getAsString() + "$" ;
         }
    }
}
