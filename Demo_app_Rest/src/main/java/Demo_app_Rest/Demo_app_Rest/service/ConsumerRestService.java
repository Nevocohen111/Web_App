package Demo_app_Rest.Demo_app_Rest.service;

import Demo_app_Rest.Demo_app_Rest.clients.ContactClient;
import Demo_app_Rest.Demo_app_Rest.config.RestConfiguration;
import Demo_app_Rest.Demo_app_Rest.model.Contact;
import Demo_app_Rest.Demo_app_Rest.model.Converter;
import Demo_app_Rest.Demo_app_Rest.model.CurrencyResponse;
import Demo_app_Rest.Demo_app_Rest.model.CustomHttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ConsumerRestService {

    @Autowired
    private ContactClient contactClient;
    @Autowired
    private RestConfiguration restConfiguration;

    @Autowired
    private WebClient webClient;


    public List<Contact> getInquiriesByState(String state) {
        return contactClient.getInquiriesByState(state);
    }

    public Contact saveContact(Contact contact) {
        return contactClient.saveContact(contact, "user-agent");
    }

    public ResponseEntity<CustomHttpResponse> deleteContact(Contact contact) {
        return contactClient.deleteContact(contact);
    }

    public ResponseEntity<Contact> updateContact(Contact contact) {
        return contactClient.updateContact(contact);
    }

    public CurrencyResponse displayIlsCurrency(Converter converter) {
        String response = webClient.method(HttpMethod.POST)
                .uri(restConfiguration.getBaseUrl() +  '?' + "from=" + converter.getFrom() + '&' + "to=" + converter.getTo() + '&' + "amount=" + converter.getAmount())
                .retrieve()
                .bodyToMono(String.class).block();
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        JsonElement result = jsonObject.get("rate");
        return new CurrencyResponse(result.getAsDouble());
    }
}
