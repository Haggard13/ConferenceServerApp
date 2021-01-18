package com.example.conference.Controllers;

import com.example.conference.JsonEntity.Output.UserInfo;
import com.example.conference.db.UserTable;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    //region User Registration
    @GetMapping("/registration")
    public int addUser(@RequestParam("id") int id,
                           @RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("surname") String surname) {
        return UserTable.addUser(new UserInfo(id, email, name, surname));
    }
    //endregion

    //region User Info
    @GetMapping("/info")
    public String getUserInfo(@RequestParam("id") int id) {
        Gson gson = new Gson();
        return gson.toJson(UserTable.getUserInfo(id));
    }
    //endregion
}
