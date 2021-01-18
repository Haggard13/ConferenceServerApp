package com.example.conference.Controllers;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/conference/avatar")
public class ConferenceAvatarController {
    @PostMapping("/upload")
    public void setAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        Path path = Paths.get("D:/conference_avatars/", file.getOriginalFilename());
        file.transferTo(path);
    }

    @GetMapping("/download")
    public StreamingResponseBody downloadAvatar(@RequestParam("id") String id,
                                                HttpServletResponse response){
        try {
            response.addIntHeader("conference_avatar", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("D:\\conference_avatars\\" + id + ".png");
            return outputStream -> {
                int b;
                while((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }
                inputStream.close();
            };
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }
}
