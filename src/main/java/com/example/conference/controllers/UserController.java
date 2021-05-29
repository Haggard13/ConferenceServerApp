package com.example.conference.controllers;

import com.example.conference.json.ContactEntity;
import com.example.conference.db.UserTable;
import com.example.conference.json.Output.UserInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/registration")
    public int addUser(@RequestParam("id") int id,
                           @RequestParam("email") String email,
                           @RequestParam("name") String name,
                           @RequestParam("surname") String surname) {
        return UserTable.addUser(new UserInfo(id, email, name, surname));
    }

    @GetMapping("/info")
    public String getUserInfoDeprecated(@RequestParam("id") int id) {
        Gson gson = new Gson();
        return gson.toJson(UserTable.getUserInfoDeprecated(id));
    }

    @PostMapping("send_firebase_messaging_token")
    public void sendFirebaseMessagingToken(@RequestBody String tokenWithID) {
        String[] userIDAndToken = tokenWithID.split(" ");
        int userID = Integer.parseInt(userIDAndToken[0].substring(1));
        String token = userIDAndToken[1].substring(0, userIDAndToken[1].length() - 1);
        log.info("User: {} Token: {}", userID, token);
        UserTable.addToken(userID, token);
    }

    @GetMapping("/get_user_info")
    public ContactEntity getUserInfo(@RequestParam("email") String email,
                                     HttpServletResponse response) {
        ContactEntity user = UserTable.getUserInfo(email);
        if (user == null) {
            response.setStatus(400);
        }
        return user;
    }
}
