package com.example.conference.signaling;

import com.example.conference.signaling.entity.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CallHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);
    private static final Gson gson = new Gson();

    private final List<VideoConference> conferences = new ArrayList<>();
    private final List<VideoConferenceMember> members = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
            switch (jsonMessage.get("id").getAsString()) {
                case "join":
                    log.info("join");
                    join(session, jsonMessage);
                    break;
                case "sendOffer":
                    log.info("sendOffer");
                    sendOffer(session, jsonMessage);
                    break;
                case "sendAnswer":
                    log.info("sendAnswer");
                    sendAnswer(session, jsonMessage);
                    break;
                case "sendIceCandidate":
                    log.info("sendIceCandidate");
                    sendIceCandidate(session, jsonMessage);
                    break;
                default:
                    break;
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    private void join(WebSocketSession session, JsonObject message) {
        final int conferenceId = message.get("conferenceId").getAsInt();
        final int userId = message.get("userId").getAsInt();

        VideoConference vc = new VideoConference(conferenceId);
        VideoConferenceMember vcm = new VideoConferenceMember(userId, session, conferenceId);

        if (!conferences.contains(vc)) {
            conferences.add(vc);
        } else {
            vc = conferences.stream().filter(x -> x.getId() == conferenceId).findFirst().get();
            sendMessage(session, new Members("youJoin", vc.getMembers().stream().map(VideoConferenceMember::getId).collect(Collectors.toList())));
            log.info("{}", vc.getMembers().size());
        }

        vc.addMember(vcm);
        members.add(vcm);
    }

    private void sendOffer(WebSocketSession session, JsonObject message) {
        Offer offer = gson.fromJson(message.toString(), Offer.class);

        members.stream()
                .filter(x -> x.getId() == offer.getUserId())
                .forEach(x ->
                        sendMessage(
                                x.getSession(),
                                new Offer2("offer", offer.getSenderId(), offer.getOffer())
                        )
        );
    }

    private void sendAnswer(WebSocketSession session, JsonObject message) {
        Answer answer = gson.fromJson(message.toString(), Answer.class);

        VideoConferenceMember member =
                conferences.stream()
                        .filter(x -> x.getId() == answer.getConferenceId())
                        .findFirst()
                        .get()
                        .getMembers()
                        .stream()
                        .filter(x -> x.getId() == answer.getUserId())
                        .findFirst()
                        .get();

        sendMessage(member.getSession(), new Answer2("answer", answer.getAnswererId(), answer.getAnswer()));
    }

    private void sendIceCandidate(WebSocketSession session, JsonObject message) {
        IceCandidate iceCandidate = gson.fromJson(message.toString(), IceCandidate.class);

        conferences.stream()
                .filter(x -> x.getId() == iceCandidate.getConferenceId())
                .findFirst()
                .get()
                .getMembers()
                .stream()
                .filter(x -> x.getId() == iceCandidate.getUserId())
                .forEach(
                        x -> {
                            sendMessage(
                                    x.getSession(),
                                    new IceCandidate2(
                                            "iceCandidate",
                                            iceCandidate.getSenderId(),
                                            iceCandidate.getIceCandidate()
                                    )
                            );
                        }
                );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("closed");
        VideoConferenceMember vcm =
                members.stream()
                        .filter(x -> x.getSession().getId().equals(session.getId()))
                        .findFirst()
                        .get();

        members.remove(vcm);

        VideoConference vc = conferences.stream()
                .filter(x -> x.getId() == vcm.getConferenceId())
                .findFirst()
                .get();

        vc.getMembers().remove(vcm);

        vc.getMembers()
                .forEach(x -> sendMessage(x.getSession(), new LeavingUser("userLeft", vcm.getId())));
    }

    private void sendMessage(WebSocketSession session, Object message) {
        try {
            session.sendMessage(new TextMessage(gson.toJson(message)));
            log.info("message sent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
