package com.example.lab.domain.validate;

import com.example.lab.domain.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";

        if(entity.getFirstName() == null || entity.getLastName() == null)
            errors += "Names cannot be null!";
        if(entity.getUsername() == null)
            errors += "Username cannot be null!";
        boolean firstName = entity.getFirstName().chars().allMatch(Character::isLetter);
        if(!firstName){
            errors += "First name should only contains letters!\n";
        }
        boolean lastName = entity.getLastName().chars().allMatch(Character::isLetter);
        if(!lastName){
            errors += "Last name should only contains letters!\n";
        }
        if(errors.length()>0)
            throw new ValidationException("\n" + errors);
    }
}
