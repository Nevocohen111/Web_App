package Demo_app_Rest.Demo_app_Rest.model;

import com.google.gson.JsonObject;
import lombok.*;

@Data
public class CurrencyResponse {
    private double result;

    public CurrencyResponse(double result) {
        this.result = result;
    }




}
