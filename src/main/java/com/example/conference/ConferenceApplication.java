package com.example.conference;

import com.example.conference.db.DataBaseInfo;
import com.example.conference.signaling.CallHandler;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

@SpringBootApplication
@EnableWebSocket
public class ConferenceApplication implements WebSocketConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ConferenceApplication.class);
    public static Connection con;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        SpringApplication.run(ConferenceApplication.class, args);

        log.info("Time: {}", new Date().getTime());

        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        con = DriverManager.getConnection(
                DataBaseInfo.url,
                DataBaseInfo.user,
                DataBaseInfo.password
        );

        FileInputStream serviceAccount =
                new FileInputStream(
                        "/home/eh13/Документы/firebase" +
                                "/conferencealphaproject-firebase-adminsdk-fzmnm-1d438a63cd.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CallHandler(), "/groupcall").setAllowedOrigins("*");
    }
}
