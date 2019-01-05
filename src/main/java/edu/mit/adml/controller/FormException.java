package edu.mit.adml.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Form Error")  // 404
public class FormException extends RuntimeException {
}
