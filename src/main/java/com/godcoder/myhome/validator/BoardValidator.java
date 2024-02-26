package com.godcoder.myhome.validator;

import com.godcoder.myhome.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BoardValidator implements Validator {
    public boolean supports(Class clazz) {
        return Board.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        Board b = (Board) obj;
        if (StringUtils.isEmpty(b.getContent())) {
            e.rejectValue("content", "key", "내용을 입력하세요");
        }
    }
}
