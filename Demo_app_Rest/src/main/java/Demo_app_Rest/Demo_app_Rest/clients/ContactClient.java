package Demo_app_Rest.Demo_app_Rest.clients;


import Demo_app_Rest.Demo_app_Rest.config.FeignConfiguration;
import Demo_app_Rest.Demo_app_Rest.model.Contact;
import Demo_app_Rest.Demo_app_Rest.model.CustomHttpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "contact", url = "https://my-web-portfolio-app-1.herokuapp.com/api/inquiries", configuration = FeignConfiguration.class)
public interface ContactClient {
   @GetMapping("/getInquiriesByState")
   List<Contact> getInquiriesByState(@RequestParam String state);
   @PostMapping(value = "/saveContact", produces = {"application/json"})
   Contact saveContact(@Valid @RequestBody Contact contact, @RequestHeader(value = "user-agent") String userAgent);
   @DeleteMapping("/deleteContact")
   ResponseEntity<CustomHttpResponse> deleteContact(@RequestBody Contact contact);

   @PutMapping(value = "/updateContact")
   ResponseEntity<Contact> updateContact(@Valid @RequestBody Contact contact);
}
