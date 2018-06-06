package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String exception(final ResourceNotFoundException ex, final Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/404";
    }

}
