package com.example.conference.Controllers;

import com.example.conference.JsonEntity.*;
import com.example.conference.JsonEntity.Input.*;
import com.example.conference.JsonEntity.Output.UserInfo;
import com.example.conference.db.*;
import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@RestController
public class ConferenceController {
    @GetMapping("/conference/{id}/members")
    public String getConferenceMembers(@PathVariable int id) {
        return new Gson().toJson(ConferenceTable.getConferenceMembers(id));
    }

    @GetMapping("/conference/{id}/deleteUser/{user_id}/as/{deleter_id}")
    public int deleteUser(@PathVariable int id, @PathVariable int user_id, @PathVariable int deleter_id) {
        return ConferenceTable.deleteUser(id, user_id, deleter_id);
    }

    @GetMapping("/conference/{id}/addUser/{user_id}/as/{adder_id}")
    public int addUser(@PathVariable int adder_id, @PathVariable int id, @PathVariable int user_id) {
        return ConferenceTable.addUser(id, user_id, adder_id);
    }
}
