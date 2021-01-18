package com.example.conference.Controllers;

import com.example.conference.db.OpinionTable;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/opinions")
public class OpinionController {
    @GetMapping("/addOpinion")
    public int addOpinion(@RequestParam("user_id") int user_id,
                          @RequestParam("text") String text,
                          @RequestParam("type") int type,
                          @RequestParam("result_id") int result_id,
                          @RequestParam("user_name") String user_name,
                          @RequestParam("user_surname") String user_surname) {
        return OpinionTable.addOpinion(user_id, text, type, result_id, user_name, user_surname);
    }

    @GetMapping("/getOpinions")
    public String getOpinions(@RequestParam("result_id") int result_id) {
        return new Gson().toJson(OpinionTable.getOpinions(result_id));
    }

    @GetMapping("/getOpinion")
    public String getOpinion(@RequestParam("user_id") int user_id,
                             @RequestParam("result_id") int result_id) {
        return new Gson().toJson(OpinionTable.getOpinion(user_id, result_id));
    }
}
