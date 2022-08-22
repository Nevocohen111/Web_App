package Demo_app_Rest.Demo_app_Rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomHttpResponse {
    int statusCode;
    String message;
}
