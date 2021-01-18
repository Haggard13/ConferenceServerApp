package com.example.conference.Controllers;


import com.example.conference.JsonEntity.Dialogue;
import com.example.conference.JsonEntity.Input.ConferenceMessage;
import com.example.conference.JsonEntity.Input.DialogueIDList;
import com.example.conference.JsonEntity.Input.DialogueMessage;
import com.example.conference.db.CMessageTable;
import com.example.conference.db.DMessageTable;
import com.example.conference.db.DialogueTable;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/dialogue")
public class DialogueMessageController {
    @GetMapping("/checkNewMessage")
    public String checkNewDMessage(@RequestParam("dialogue_list") String dialogue_list,
                                   @RequestParam("user_id") int user_id) {
        DialogueIDList ms = new Gson().fromJson(dialogue_list, DialogueIDList.class);
        return new Gson().toJson(DMessageTable.checkNewMessage(ms, user_id));
    }

    @GetMapping("/sendMessage")
    public int sendDMessage(@RequestParam("text") String text,
                            @RequestParam("dialogue_id") int dialogue_id,
                            @RequestParam("date_time") long date_time,
                            @RequestParam("sender_id") int sender_id,
                            @RequestParam("sender_name") String sender_name,
                            @RequestParam("sender_surname") String sender_surname) {
        DialogueMessage dm = new DialogueMessage(text,
                dialogue_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        return DMessageTable.addMessage(dm, 1);
    }

    @GetMapping("/getNewMessages")
    public String getNewDMessage(@RequestParam("dialogue_id") int dialogue_id,
                                 @RequestParam("last_message_id") int l_m_id,
                                 @RequestParam("user_id") int user_id) {
        return new Gson().toJson(DMessageTable.getNewMessage(user_id, dialogue_id, l_m_id));
    }

    @GetMapping("/checkNewDialogue")
    public String checkNewDialogue(@RequestParam("user_id") int user_id) {
        return new Gson().toJson(DialogueTable.checkNewDialogue(user_id));
    }

    @GetMapping("/createNewDialogue")
    public int createNewDialogue(@RequestParam("dialogue_info") String dialogue_info) {
        Gson gson = new Gson();
        Dialogue d = gson.fromJson(dialogue_info, Dialogue.class);
        return DialogueTable.createNewDialogue(d);
    }

    @GetMapping("/getNewDialogue")
    public String getNewDialogue(@RequestParam("user_id") int user_id,
                                 @RequestParam("last_dialogue_id") int l_d_id) {
        Gson gson = new Gson();
        return gson.toJson(DialogueTable.getNewDialogue(user_id, l_d_id));
    }

    @PostMapping("/sendPhotography/{text}/{dialogue_id}/{date_time}/{sender_id}/{sender_name}/{sender_surname}")
    public void sendPhotography(@RequestParam("file") MultipartFile file,
                                HttpServletResponse response,
                                @PathVariable int dialogue_id,
                                @PathVariable long date_time,
                                @PathVariable int sender_id,
                                @PathVariable String sender_name,
                                @PathVariable String sender_surname,
                                @PathVariable String text) throws IOException {
        DialogueMessage dm = new DialogueMessage(text,
                dialogue_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        int message_id = DMessageTable.addMessage(dm, 2);
        response.addIntHeader("message_id", message_id);
        Path path = Paths.get("D:/dialogue_messages/", message_id + ".png");
        file.transferTo(path);
    }

    @GetMapping("/getPhotography")
    public StreamingResponseBody getPhotography(@RequestParam("id") String id,
                                                HttpServletResponse response) {
        try {
            response.addIntHeader("dialogue_messages", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("D:\\dialogue_messages\\" + id + ".png");
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

    @PostMapping("/sendAudioMessage/{text}/{dialogue_id}/{date_time}/{sender_id}/{sender_name}/{sender_surname}")
    public void senAudioMessage(@RequestParam("file") MultipartFile file,
                                HttpServletResponse response,
                                @PathVariable int dialogue_id,
                                @PathVariable long date_time,
                                @PathVariable int sender_id,
                                @PathVariable String sender_name,
                                @PathVariable String sender_surname,
                                @PathVariable String text) throws IOException {
        DialogueMessage dm = new DialogueMessage(text,
                dialogue_id,
                new SimpleDateFormat("yyyy-MM-dd").format(date_time) +
                        "T" +
                        new SimpleDateFormat("HH:mm:ss.SSS").format(date_time),
                sender_id,
                sender_name,
                sender_surname);
        int message_id = DMessageTable.addMessage(dm, 3);
        response.addIntHeader("message_id", message_id);
        Path path = Paths.get("D:/dialogue_messages/", message_id + ".3gp");
        file.transferTo(path);
    }

    @GetMapping("/getAudioMessage")
    public StreamingResponseBody getAudioMessage(@RequestParam("id") String id,
                                                 HttpServletResponse response) {
        try {
            response.addIntHeader("dialogue_messages", Integer.parseInt(id));
            FileInputStream inputStream = new FileInputStream("D:\\dialogue_messages\\" + id + ".3gp");
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
