package Demo_app_Rest.Demo_app_Rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Converter {
    String from;
    String to;
    double amount;
}
