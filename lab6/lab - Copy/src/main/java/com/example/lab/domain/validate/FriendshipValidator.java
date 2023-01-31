package com.example.lab.domain.validate;

import com.example.lab.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId().getRight() == null || entity.getId().getLeft() == null)
            throw new ValidationException("IDs cannot be null");
    }
}
