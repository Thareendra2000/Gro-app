package com.example.groapp.Utils;

import com.example.groapp.Models.ProductModel;
import com.example.groapp.Repositories.User;

public class CastToObject {
    public static User toUser(User user, String key, String value){
        switch (key.toLowerCase()){
            case "id":
                user.setId(value);
                break;
            case "name":
                user.setName(value);
                break;
            case "password":
                user.setPassword(value);
                break;
            case "email":
                user.setEmail(value);
                break;
        }
        return user;
    }
}
