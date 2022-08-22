package Demo_app_Rest.Demo_app_Rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private String userName;
    private String mobile_num;
    private String email;
    private String subject;
    private String message;
    private String state;
}
