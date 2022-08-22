package com.example.demo.controllers;

import com.example.demo.model.Holiday;
import com.example.demo.model.User;
import com.example.demo.services.ProfileService;
import com.example.demo.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HolidaysController {
    private final List<Holiday> holidays;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private SecurityUserDetailsService userDetailsManager;


    public HolidaysController() {
        holidays = Arrays.asList(
                new Holiday(" Aug 11 ", "Tu B'Av", Holiday.Type.FESTIVAL,"https://www.myjewishlearning.com/article/tu-bav/"),
                new Holiday(" Dec 18 ", "Hanukkah", Holiday.Type.FESTIVAL,"https://www.history.com/topics/holidays/hanukkah"),
                new Holiday(" May 4 ", "Lag BaOmer", Holiday.Type.FESTIVAL,"https://www.chabad.org/library/article_cdo/aid/42944/jewish/Lag-BaOmer.htm"),
                new Holiday(" Dec 25 ", "Christmas", Holiday.Type.FEDERAL,"https://www.britannica.com/topic/Christmas"),
                new Holiday(" May 28 ", "Shavuot", Holiday.Type.FESTIVAL,"https://www.moishehouse.org/resources/shavuot-a-holiday-thats-much-more-than-cheesecake/?gclid=Cj0KCQjw2_OWBhDqARIsAAUNTTGx4yoiDHg97gZU3fkcfqvDbImpLVVuI7uQB0_bGa-6DxJVxsgrvtgaAn2KEALw_wcB"),
                new Holiday(" Oct 31 ", "Halloween", Holiday.Type.FEDERAL,"https://www.history.com/topics/halloween/history-of-halloween"),
                new Holiday(" Oct 16 ", "Simchat Torah", Holiday.Type.FESTIVAL,"https://toriavey.com/what-is-simchat-torah/"),
                new Holiday(" Apr 25 ", "Independence Day", Holiday.Type.FESTIVAL,"https://www.touristisrael.com/yom-haatzmaut-israeli-independence-day/6022/"),
                new Holiday(" July 4 ", "Independence Day", Holiday.Type.FEDERAL,"https://www.history.com/topics/holidays/july-4th"),
                new Holiday(" Oct 9 ", "Sukkot", Holiday.Type.FESTIVAL,"https://en.toraland.org.il/beit-midrash/articles/around-the-jewish-year/sukkut/"),
                new Holiday(" Apr 9 ", "Easter", Holiday.Type.FEDERAL,"https://nationaltoday.com/easter/"),
                new Holiday(" Feb 14  ", "Valentine's Day", Holiday.Type.FEDERAL,"https://www.history.com/topics/valentines-day/history-of-valentines-day-2"),
                new Holiday(" Mar 17  ", "St.Patrick's Day", Holiday.Type.FEDERAL,"https://www.history.com/topics/st-patricks-day"),
                new Holiday(" May 18  ", "Ascension Day", Holiday.Type.FEDERAL,"https://www.officeholidays.com/holidays/ascension-day"),
                new Holiday(" Nov 22  ", "Thanksgiving", Holiday.Type.FEDERAL,"https://www.almanac.com/thanksgiving-day"),
                new Holiday(" Mar 6 ", "Purim", Holiday.Type.FESTIVAL,"https://en.toraland.org.il/beit-midrash/articles/around-the-jewish-year/purim/"),
                new Holiday(" Apr 5 ", "Passover", Holiday.Type.FESTIVAL,"https://en.toraland.org.il/beit-midrash/articles/around-the-jewish-year/pesach/"));
    }

    @GetMapping("/holidays")
    public String displayHolidaysPage(Model model, Authentication authentication) {
        //checking if a user or admin is logged in
        if(authentication != null && authentication.getAuthorities() != null) {
            if(authentication.getName().equals("Nevo"))
                model.addAttribute("list", profileService.getNevoImgUrl());
            else
                model.addAttribute("list", profileService.getGuestImgUrl());

            List<User> users =  userDetailsManager.getAllUsers();
            for(User user : users) {
                if (user.getUsername().equals(authentication.getName())) {
                    //if the name of the user equals the authentication name,then fetch the specific user's id and set it to the model
                    model.addAttribute("myUser", userDetailsManager.getTheCurrentUserById(user.getId()));
                    //eventually grants the option to delete your user.
                }
            }
        }
        //for each holiday's enum type(FEDERAL,FESTIVAL,etc.),add it to the model for the sake of creating two lists of different types of holidays.
          for(Holiday.Type type : Holiday.Type.values()) {
              model.addAttribute(type.name(), holidays.stream().filter(h -> h.getType() == type).collect(Collectors.toList()));
          }
        return "holidays.html";

    }


}

