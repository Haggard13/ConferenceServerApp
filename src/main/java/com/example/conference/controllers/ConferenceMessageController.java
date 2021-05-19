package com.example.conference.controllers;

import com.example.conference.db.CMessageTable;
import com.example.conference.db.ConferenceTable;
import com.example.conference.json.CMessageEntity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/conference")
public class ConferenceMessageController {

    Logger logger = LoggerFactory.getLogger(ConferenceMessageController.class);

    @GetMapping("/check_new_messages")
    public boolean checkNewMessages(@RequestParam("conference_id") int conferenceID,
                                    @RequestParam("last_message_id") int lastMessageID) {
        return CMessageTable.checkNewMessages(conferenceID, lastMessageID);
    }

    @GetMapping("/get_new_messages")
    public String getNewCMessage(@RequestParam("conference_id") int conferenceID,
                                 @RequestParam("last_message_id") int lastMessageID,
                                 @RequestParam("user_id") int userID) {
        return new Gson().toJson(CMessageTable.getNewMessage(userID, conferenceID, lastMessageID));
    }

    @PostMapping("/send_message")
    public int sendMessage(@RequestBody CMessageEntity message) throws FirebaseMessagingException {
        int messageID =  CMessageTable.addMessage(message);
        if (messageID != -1) {
            sendNotification(message);
        }
        ConferenceTable.setLastMessageAndTime(
                message.getConference_id(), message.getText(), getTime(message.getDate_time()));
        return messageID;
    }

    @PostMapping("/send_message_with_photo")
    public int sendMessageWithPhoto(@RequestParam("photo") MultipartFile file,
                                    @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        CMessageEntity messageEntity = new Gson().fromJson(message, CMessageEntity.class);
        int message_id = CMessageTable.addMessage(messageEntity);
        Path path = Paths.get("/home/eh13/server/users-files/conferences/conferences-add/", message_id + ".png");
        file.transferTo(path);
        if (messageEntity.getText().equals("")) {
            messageEntity.setText("Фотография");
        }
        if (message_id != -1) {
            sendNotification(messageEntity);
        }
        ConferenceTable.setLastMessageAndTime(
                messageEntity.getConference_id(), messageEntity.getText(), getTime(messageEntity.getDate_time()));
        return message_id;
    }

    @PostMapping("/send_audio_message")
    public int senAudioMessage(@RequestParam("audio") MultipartFile file,
                               @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        CMessageEntity messageEntity = new Gson().fromJson(message, CMessageEntity.class);
        int message_id = CMessageTable.addMessage(messageEntity);
        Path path = Paths.get("/home/eh13/users-files/conferences/conferences-add/", message_id + ".3gp");
        file.transferTo(path);
        if (message_id != -1) {
            sendNotification(messageEntity);
        }
        ConferenceTable.setLastMessageAndTime(
                messageEntity.getConference_id(), messageEntity.getText(), getTime(messageEntity.getDate_time()));
        return message_id;
    }

    @PostMapping("/send_message_with_file/")
    public int sendMessageWithFile(@RequestParam("file") MultipartFile file,
                        @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        CMessageEntity messageEntity = new Gson().fromJson(message, CMessageEntity.class);
        int message_id = CMessageTable.addMessage(messageEntity);
        new File("/home/eh13/server/users-files/conferences/conferences-add/" + message_id).mkdir();
        Path path = Paths.get("/home/eh13/server/users-files/conferences/conferences-add/" +
                message_id + "/" + messageEntity.getText());
        file.transferTo(path);
        if (message_id != -1) {
            sendNotification(messageEntity);
        }
        ConferenceTable.setLastMessageAndTime(
                messageEntity.getConference_id(), messageEntity.getText(), getTime(messageEntity.getDate_time()));
        return message_id;
    }

    @GetMapping("/getPhotography")
    public StreamingResponseBody getPhotography(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("conference_messages", Integer.parseInt(id));
            FileInputStream inputStream =
                    new FileInputStream("/home/eh13/server/users-files/conferences/conferences-add/" +
                            id + ".png");
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

    @GetMapping("/getAudioMessage")
    public StreamingResponseBody getAudioMessage(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("conference_messages", Integer.parseInt(id));
            FileInputStream inputStream =
                    new FileInputStream("/home/eh13/Документы/users-files/conferences/conferences-add/" +
                            id + ".3gp");
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

    @GetMapping("getFile")
    public ResponseEntity<Resource> getFile(@RequestParam("id") String id,
                                            HttpServletResponse response) {
        try {
            response.addIntHeader("conference_messages", Integer.parseInt(id));
            File dir = new File("/home/eh13/server/users-files/conferences/conferences-add/" + id + "\\");
            File[] arrFiles = dir.listFiles();
            FileInputStream inputStream =
                    new FileInputStream(
                            "/home/eh13/server/users-files/conferences/conferences-add/" +
                                    id + "/" + arrFiles[0].getName());
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .contentLength(arrFiles[0].length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException | NullPointerException ignored) {
        }
        return null;
    }

    private void sendNotification(CMessageEntity message) throws FirebaseMessagingException {
        Message notification = Message.builder().setNotification(
                Notification.builder()
                        .setTitle(message.getSender_name() + " " + message.getSender_surname())
                        .setBody(message.getText())
                        .build())
                .setCondition("'c" + message.getConference_id() + "' in topics")
                .putData("notification_type", "cmessage")
                .putData("id", String.valueOf(message.getConference_id()))
                .putData("senderID", String.valueOf(message.getSender_id()))
                .putData("time", String.valueOf(message.getDate_time()))
                .build();

        FirebaseMessaging.getInstance().send(notification);
    }

    private static String getTime(long ms) {
        return new SimpleDateFormat("yyyy-MM-dd").format(ms) +
                "T" +
                new SimpleDateFormat("HH:mm:ss.SSS").format(ms);
    }
}
