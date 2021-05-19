package com.example.conference.controllers;

import com.example.conference.db.ConferenceTable;
import com.example.conference.json.ConferenceEntity;
import com.example.conference.json.Input.ConferenceMember;
import com.example.conference.json.Output.ContactEntityWithStatus;
import com.google.common.reflect.TypeToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ConferenceController {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceController.class);

    @GetMapping("/test")
    public String test() {
        return "good";
    }

    @GetMapping("/conference/get_conference_members")
    public List<ContactEntityWithStatus> getConferenceMembers(@RequestParam("conference_id") int id) {
        return ConferenceTable.getConferenceMembers(id);
    }

    @PostMapping("/conference/create_new_conference")
    public boolean createNewConference(@RequestParam("conference") String conference,
                                       @RequestParam("members") String members) {

        ConferenceEntity conferenceEntity = new Gson().fromJson(conference, ConferenceEntity.class);
        List<ConferenceMember> conferenceMembers =
                new Gson().fromJson(members, new TypeToken<List<ConferenceMember>>(){}.getType());
        logger.info("New conference created: {}", conferenceEntity.getName());
        int conferenceID = ConferenceTable.createNewConference(conferenceEntity, conferenceMembers);
        if (conferenceID != -1) {
            conferenceEntity.setId(conferenceID);
            conferenceMembers.forEach(
                    it -> {
                        try {
                            sendNewConferenceNotification(conferenceEntity, it.id);
                        } catch (FirebaseMessagingException e) {
                            logger.error(e.getMessage());
                        }
                    });
            return true;
        }
        return false;
    }

    @GetMapping("/conference/get_all_conferences")
    public List<ConferenceEntity> getAllConference(@RequestParam("user_id") int userID) {
        return ConferenceTable.getConferences(userID);
    }

    @PostMapping("/conference/{id}/delete_user/{user_id}/{deleter_id}")
    public boolean deleteUser(
            @PathVariable("id") int id,
            @PathVariable("user_id") int user_id,
            @PathVariable("deleter_id") int deleter_id
    ) {
        return ConferenceTable.deleteUser(id, user_id, deleter_id);
    }

    @PostMapping("/conference/{id}/addUser/{user_id}/{adder_id}")
    public boolean addUser(
            @PathVariable("id") int id,
            @PathVariable("user_id") int user_id,
            @PathVariable("adder_id") int adder_id
    ) {
        return ConferenceTable.addUser(id, user_id, adder_id);
    }

    @PostMapping("/conference/rename/{id}/{new_name}")
    public boolean renameConference(@PathVariable("id") int id,
                                 @PathVariable("new_name") String name,
                                 HttpServletResponse response) {
        return ConferenceTable.renameConference(id, name);
    }

    @GetMapping("/conference/getConferenceName")
    public String getConferenceName(@RequestParam("id") int id) {
        return ConferenceTable.getConferenceName(id);
    }

    private void sendNewConferenceNotification(ConferenceEntity conference, int userID) throws FirebaseMessagingException {
        Message notification = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(conference.getName())
                                .setBody("Вас добавили в конференцию")
                                .build()
                ).setCondition("'u" + userID + "' in topics")
                .putData("notification_type", "conference")
                .putData("id", String.valueOf(conference.getId()))
                .putData("name", conference.getName())
                .putData("message", conference.getLast_message())
                .putData("count", String.valueOf(conference.getCount()))
                .build();

        FirebaseMessaging.getInstance().send(notification);
    }
}
