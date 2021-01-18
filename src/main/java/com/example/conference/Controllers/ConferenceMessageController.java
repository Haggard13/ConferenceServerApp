package com.example.conference.Controllers;

import com.example.conference.JsonEntity.Input.Conference;
import com.example.conference.JsonEntity.Input.ConferenceIDList;
import com.example.conference.JsonEntity.Input.ConferenceMessage;
import com.example.conference.db.CMessageTable;
import com.example.conference.db.ConferenceTable;
import com.google.gson.Gson;
import okhttp3.MultipartBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/conference")
public class ConferenceMessageController {

    @GetMapping("/checkNewMessage")
    public String checkNewCMessage(@RequestParam("conference_list") String conference_list,
                                   @RequestParam("user_id") int user_id) {
        ConferenceIDList ms = new Gson().fromJson(conference_list, ConferenceIDList.class);
        return new Gson().toJson(CMessageTable.checkNewMessage(ms, user_id));
    }

    @GetMapping("/getNewMessages")
    public String getNewCMessage(@RequestParam("conference_id") int conference_id,
                                 @RequestParam("last_message_id") int l_m_id,
                                 @RequestParam("user_id") int user_id) {
        return new Gson().toJson(CMessageTable.getNewMessage(user_id, conference_id, l_m_id));
    }

    @GetMapping("/checkNewConference")
    public String checkNewConference(@RequestParam("user_id") int user_id) {
        return new Gson().toJson(ConferenceTable.checkNewConference(user_id));
    }

    @GetMapping("/createNewConference")
    public int createNewConference(@RequestParam("conference_info") String conference_info) {
        Gson gson = new Gson();
        Conference c = gson.fromJson(conference_info, Conference.class);
        return ConferenceTable.createNewConference(c);
    }

    @GetMapping("/getNewConference")
    public String getNewConference(@RequestParam("user_id") int user_id,
                                   @RequestParam("last_conference_id") int l_c_id) {
        Gson gson = new Gson();
        return gson.toJson(ConferenceTable.getNewConference(user_id, l_c_id));
    }

    @GetMapping("/rename")
    public void renameConference(@RequestParam("id") int id,
                                 @RequestParam("new_name") String name,
                                 HttpServletResponse response) {
        int code = ConferenceTable.renameConference(id, name);
        if (code == -1)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @GetMapping("/getConferenceName")
    public String getConference(@RequestParam("id") int id) {
        return ConferenceTable.getConferenceName(id);
    }

    @GetMapping("/sendMessage")
    public int sendCMessage(@RequestParam("text") String text,
                            @RequestParam("conference_id") int conference_id,
                            @RequestParam("date_time") long date_time,
                            @RequestParam("sender_id") int sender_id,
                            @RequestParam("sender_name") String sender_name,
                            @RequestParam("sender_surname") String sender_surname) {
        ConferenceMessage cm = new ConferenceMessage(text,
                conference_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        return CMessageTable.addMessage(cm, 1);
    }

    @PostMapping("/sendPhotography/{conference_id}/{date_time}/{sender_id}/{sender_name}/{sender_surname}")
    public void sendPhotography(@RequestParam("file") MultipartFile file,
                                @RequestParam("text") String text,
                                HttpServletResponse response,
                                @PathVariable int conference_id,
                                @PathVariable long date_time,
                                @PathVariable int sender_id,
                                @PathVariable String sender_name,
                                @PathVariable String sender_surname) throws IOException {
        text = URLDecoder.decode(text, "UTF-8");
        ConferenceMessage cm = new ConferenceMessage(text,
                conference_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        int message_id = CMessageTable.addMessage(cm, 2);
        response.addIntHeader("message_id", message_id);
        Path path = Paths.get("D:/conference_messages/", message_id + ".png");
        file.transferTo(path);
    }

    @GetMapping("/getPhotography")
    public StreamingResponseBody getPhotography(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("conference_messages", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("D:\\conference_messages\\" + id + ".png");
            return outputStream -> {
                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }
                inputStream.close();
            };
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }

    @PostMapping("/sendAudioMessage/{text}/{conference_id}/{date_time}/{sender_id}/{sender_name}/{sender_surname}")
    public void senAudioMessage(@RequestParam("file") MultipartFile file,
                                HttpServletResponse response,
                                @PathVariable int conference_id,
                                @PathVariable long date_time,
                                @PathVariable int sender_id,
                                @PathVariable String sender_name,
                                @PathVariable String sender_surname,
                                @PathVariable String text) throws IOException {
        ConferenceMessage cm = new ConferenceMessage(text,
                conference_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        int message_id = CMessageTable.addMessage(cm, 3);
        response.addIntHeader("message_id", message_id);
        Path path = Paths.get("D:/conference_messages/", message_id + ".3gp");
        file.transferTo(path);
    }

    @GetMapping("/getAudioMessage")
    public StreamingResponseBody getAudioMessage(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("conference_messages", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("D:\\conference_messages\\" + id + ".3gp");
            return outputStream -> {
                int b;
                while ((b = inputStream.read()) != -1) {
                    outputStream.write(b);
                }
                inputStream.close();
            };
        } catch (FileNotFoundException ignored) {
        }
        return null;
    }
}
