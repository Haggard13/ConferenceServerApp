package com.example.conference;

import com.example.conference.db.DataBaseInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@SpringBootApplication
public class ConferenceApplication {
    public static Connection con;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        SpringApplication.run(ConferenceApplication.class, args);

        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        con = DriverManager.getConnection(
                DataBaseInfo.url,
                DataBaseInfo.user,
                DataBaseInfo.password
        );
    }
}
