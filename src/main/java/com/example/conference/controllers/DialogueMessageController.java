package com.example.conference.controllers;


import com.example.conference.Account;
import com.example.conference.db.DMessageTable;
import com.example.conference.db.DialogueTable;
import com.example.conference.json.DMessageEntity;
import com.example.conference.json.DialogueEntity;
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
import java.util.List;

@RestController
@RequestMapping("/dialogue")
public class DialogueMessageController {
    Logger logger = LoggerFactory.getLogger(DialogueMessageController.class);

    @GetMapping("/get_all_dialogues")
    public List<DialogueEntity> getAllDialogues(@RequestParam("user_id") int userID) {
        return DialogueTable.getDialogues(userID);
    }

    @GetMapping("/get_new_messages")
    public String getNewDMessage(@RequestParam("dialogue_id") int dialogue_id,
                                 @RequestParam("last_message_id") int l_m_id,
                                 @RequestParam("user_id") int user_id) {
        return new Gson().toJson(DMessageTable.getNewMessage(user_id, dialogue_id, l_m_id));
    }

    @GetMapping("/check_new_messages")
    public boolean checkNewMessages(@RequestParam("dialogue_id") int dialogueID,
                                    @RequestParam("last_message_id") int lastMessageID) {
        return DMessageTable.checkNewMessages(dialogueID, lastMessageID);
    }

    @PostMapping("/create_new_dialogue")
    public boolean createNewDialogue(@RequestParam("dialogue") String dialogue,
                                       @RequestParam("account") String user) {

        DialogueEntity dialogueEntity = new Gson().fromJson(dialogue, DialogueEntity.class);
        Account account = new Gson().fromJson(user, Account.class);

        int dialogueID = DialogueTable.createNewDialogue(dialogueEntity, account);
        if (dialogueID != -1) {
            dialogueEntity.setId(dialogueID);
            try {
                sendNewDialogueNotification(dialogueEntity, account);
            } catch (FirebaseMessagingException e) {
                logger.error(e.getMessage());
            }
            logger.info("New dialogue created: {}", dialogueEntity.getSecond_user_id());
            return true;
        }
        return false;
    }

    @GetMapping("/getNewDialogue")
    public String getNewDialogue(@RequestParam("user_id") int user_id,
                                 @RequestParam("last_dialogue_id") int l_d_id) {
        Gson gson = new Gson();
        return gson.toJson(DialogueTable.getNewDialogue(user_id, l_d_id));
    }

    @PostMapping("/send_message")
    public int sendDMessage(@RequestBody DMessageEntity message) throws FirebaseMessagingException {
        int message_id = DMessageTable.addMessage(message);
        if (message_id != -1) {
            sendNotification(message);
        }
        DialogueTable.setLastMessageAndTime(
                message.getDialogue_id(), message.getText(), getTime(message.getDate_time()));
        return message_id;
    }

    @PostMapping("/send_message_with_photo")
    public int sendMessageWithPhoto(@RequestParam("file") MultipartFile file,
                                    @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        DMessageEntity messageEntity = new Gson().fromJson(message, DMessageEntity.class);
        int message_id = DMessageTable.addMessage(messageEntity);
        Path path = Paths.get("/home/eh13/server/users-files/dialogues/dialogues-add/", message_id + ".png");
        file.transferTo(path);
        if (messageEntity.getText().equals("")) {
            messageEntity.setText("Фотография");
        }
        if (message_id != -1) {
            sendNotification(messageEntity);
        }
        DialogueTable.setLastMessageAndTime(
                messageEntity.getDialogue_id(), messageEntity.getText(), getTime(messageEntity.getDate_time())
        );
        return message_id;
    }

    @PostMapping("/send_audio_message")
    public int sendAudioMessage(@RequestParam("file") MultipartFile file,
                                @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        DMessageEntity messageEntity = new Gson().fromJson(message, DMessageEntity.class);
        int messageId = DMessageTable.addMessage(messageEntity);
        Path path = Paths.get("/home/eh13/server/users-files/dialogues/dialogues-add/", messageId + ".3gp");
        file.transferTo(path);
        if (messageId != -1) {
            sendNotification(messageEntity);
        }
        DialogueTable.setLastMessageAndTime(messageEntity.getDialogue_id(), messageEntity.getText(), getTime(messageEntity.getDate_time()));
        return messageId;
    }

    @PostMapping("/send_message_with_file")
    public int sendFile(@RequestParam("file") MultipartFile file,
                        @RequestParam("message") String message) throws IOException, FirebaseMessagingException {
        DMessageEntity messageEntity = new Gson().fromJson(message, DMessageEntity.class);
        int message_id = DMessageTable.addMessage(messageEntity);
        new File("/home/eh13/server/users-files/dialogues/dialogues-add/" + message_id).mkdir();
        Path path = Paths.get("/home/eh13/server/users-files/dialogues/dialogues-add/" +
                message_id + "/" + messageEntity.getText());
        file.transferTo(path);
        if (message_id != -1) {
            sendNotification(messageEntity);
        }
        DialogueTable.setLastMessageAndTime(
                messageEntity.getDialogue_id(), messageEntity.getText(), getTime(messageEntity.getDate_time()));
        return message_id;
    }

    @GetMapping("/getPhotography")
    public StreamingResponseBody getPhotography(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("dialogue_messages", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("/home/eh13/server/users-files/dialogues/dialogues-add/" + id + ".png");
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
            response.addIntHeader("dialogue_messages", Integer.parseInt(id));
            FileInputStream inputStream =
                    new FileInputStream("/home/eh13/server/users-files/dialogues/dialogues-add/" +
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
            response.addIntHeader("dialogue_messages", Integer.parseInt(id));
            File dir = new File("/home/eh13/server/users-files/dialogues/dialogues-add/" + id + "\\");
            File[] arrFiles = dir.listFiles();
            FileInputStream inputStream =
                    new FileInputStream("/home/eh13/server/users-files/dialogues/dialogues-add/" +
                            id + "\\" + arrFiles[0].getName());
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .contentLength(arrFiles[0].length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (FileNotFoundException | NullPointerException ignored) {
        }
        return null;
    }

    private void sendNotification(DMessageEntity message) throws FirebaseMessagingException {
        Message notification = Message.builder().setNotification(
                Notification.builder()
                        .setTitle(message.getSender_name() + " " + message.getSender_surname())
                        .setBody(message.getText())
                        .build())
                .setCondition("'d" + message.getDialogue_id() + "' in topics")
                .putData("notification_type", "dmessage")
                .putData("id", String.valueOf(message.getDialogue_id()))
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

    private void sendNewDialogueNotification(DialogueEntity dialogue, Account account) throws FirebaseMessagingException {
        Message notification = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(account.getName() + " " + account.getSurname())
                                .setBody("Этот пользователь начал с вами переписку")
                                .build()
                ).setCondition("'u" + dialogue.getSecond_user_id() + "' in topics")
                .putData("notification_type", "dialogue")
                .putData("id", String.valueOf(account.getId()))
                .putData("dialogue_id", String.valueOf(dialogue.getId()))
                .putData("message", dialogue.getLast_message())
                .putData("email", account.getEmail())
                .putData("name", account.getName())
                .putData("surname", account.getSurname())
                .build();

        FirebaseMessaging.getInstance().send(notification);
    }
}
