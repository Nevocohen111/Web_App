package Demo_app_Rest.Demo_app_Rest.controller;


import Demo_app_Rest.Demo_app_Rest.model.Contact;
import Demo_app_Rest.Demo_app_Rest.model.Converter;
import Demo_app_Rest.Demo_app_Rest.model.CurrencyResponse;
import Demo_app_Rest.Demo_app_Rest.model.CustomHttpResponse;
import Demo_app_Rest.Demo_app_Rest.service.ConsumerRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/myConsumer")
public class ConsumerRestController {
    @Autowired
    private ConsumerRestService service;

    @GetMapping("/getInquiriesByState")
    public List<Contact> getInquiriesByState(@RequestParam String state) {
        return service.getInquiriesByState(state);
    }

    @PostMapping("/rates")
    public CurrencyResponse displayIlsCurrency(@RequestBody Converter converter) {
        return service.displayIlsCurrency(converter);
    }

    @PostMapping("/saveMyContact")
    public Contact saveContact(@RequestBody Contact contact) {
        return service.saveContact(contact);
    }

    @DeleteMapping("/deleteMyContact")
    public ResponseEntity<CustomHttpResponse> deleteContact(@RequestBody Contact contact) {
        return service.deleteContact(contact);
    }

    @PutMapping(value = "/updateMyContact")
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact) {
        return service.updateContact(contact);
    }
}
