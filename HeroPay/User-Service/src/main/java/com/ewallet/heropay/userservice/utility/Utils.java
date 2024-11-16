package com.ewallet.heropay.userservice.utility;

import com.ewallet.heropay.userservice.dto.CreateUserRequest;
import com.ewallet.heropay.userservice.dto.UserResponse;
import com.ewallet.heropay.userservice.model.User;

public class Utils {
    public static User convertUserCreateRequest(CreateUserRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .age(request.getAge())
                .build();
    }

    public static UserResponse convertToUserResponse(User user){
        return UserResponse.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .age(user.getAge())
                .email(user.getEmail())
                .build();
    }
}
