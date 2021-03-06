package com.example.conference.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/user/avatar")
public class UserAvatarController {

    @PostMapping("/upload")
    public void setAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        Path path = Paths.get("/home/eh13/server/users-files/users-avatars/", file.getOriginalFilename());
        file.transferTo(path);
    }

    @GetMapping("/download")
    @ResponseBody
    public StreamingResponseBody downloadAvatar(@RequestParam("id") String id,
                                                HttpServletResponse response){
        try {
            response.addIntHeader("user_avatar", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("/home/eh13/server/users-files/users-avatars/" + id + ".png");

            return outputStream -> {
                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }
                inputStream.close();
            };
        } catch (FileNotFoundException ignored){
        }
        return null;
    }
}
