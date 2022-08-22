package com.example.demo.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//ControllerAdvice Monitors all the controllers and their methods and if any exception is thrown, it will be handled by this class.
@ControllerAdvice
public class ExceptionController {
    // @ExceptionHandler is used to handle the exception.
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler() {
        ModelAndView exceptionPage = new ModelAndView();
        exceptionPage.setViewName("exception");
        exceptionPage.addObject("errorMsg","Oops! Something went wrong...Please try again later.");
        return exceptionPage;
    }
}
