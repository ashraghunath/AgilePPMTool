package io.ashwin.ppmtool.validator;

import io.ashwin.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass); //saying we want it fot User class
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User)object;

        if(user.getPassword().length()<6)
        {
            errors.rejectValue("password","length","Password must be at least 6 characters");
        }

        if(!user.getConfirmPassword().equals(user.getPassword()))
        {
            errors.rejectValue("confirmPassword","Match","Passwords must match");
        }
    }
}
