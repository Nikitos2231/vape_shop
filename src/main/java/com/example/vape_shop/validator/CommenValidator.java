package com.example.vape_shop.validator;

import com.example.vape_shop.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.lang.annotation.Annotation;

@Component
public class CommenValidator implements Valid {

    private final CommentService commentService;

    @Autowired
    public CommenValidator(CommentService commentService) {
        this.commentService = commentService;
    }


    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
