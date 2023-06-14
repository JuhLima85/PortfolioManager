package br.com.portfolioManager.Controller.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Exceptions {

    @ExceptionHandler(Exception.class)
    public String tratarErro(Model model, Exception exception) {
        model.addAttribute("mensagem", exception.getMessage());
        return "erro";
    }
}

