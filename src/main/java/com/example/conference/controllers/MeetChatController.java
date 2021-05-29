package com.example.conference.controllers;

import com.example.conference.db.MeetChatTable;
import com.example.conference.json.ConferenceMessageWithoutID;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/meet_chat")
public class MeetChatController {
    Logger log = LoggerFactory.getLogger(MeetChatController.class);

    @PostMapping("/send_message")
    public void sendMessage(@RequestBody ConferenceMessageWithoutID message,
                        HttpServletResponse response) {
        int messageID = MeetChatTable.sendMessage(message);
        response.addIntHeader("message_id", messageID);
    }

    @PostMapping("/send_photo")
    public void sendPhotography(@RequestParam("file") MultipartFile file,
                                HttpServletResponse response,
                                @RequestBody ConferenceMessageWithoutID message) throws IOException {
        int message_id = MeetChatTable.sendMessage(message);
        response.addIntHeader("message_id", message_id);
        Path path = Paths.get("/home/eh13/server/users-files/conferences/meet/photo", message_id + ".png");
        file.transferTo(path);
    }

    @GetMapping("/get_messages")
    public String getNewCMessage(@RequestParam("conference_id") int conference_id,
                                 @RequestParam("last_message_id") int l_m_id,
                                 @RequestParam("user_id") int user_id) {
        return new Gson().toJson(MeetChatTable.getNewMessages(user_id, conference_id, l_m_id));
    }

    @GetMapping("/check_new_message")
    public boolean checkNewCMessage(@RequestParam("conference_id") int conferenceID,
                                    @RequestParam("last_message_id") int lastMessageID) {
        return MeetChatTable.checkNewMessage(conferenceID, lastMessageID);
    }
}
