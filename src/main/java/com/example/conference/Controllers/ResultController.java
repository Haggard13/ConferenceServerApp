package com.example.conference.Controllers;

import com.example.conference.db.ResultTable;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/results")
public class ResultController {
    @GetMapping("/addResult")
    public int addResult(@RequestParam("conference_id") int conference_id,
                          @RequestParam("name") String name,
                          @RequestParam("description") String description) {
        return ResultTable.addResult(conference_id, name, description);
    }

    @GetMapping("/getResults")
    public String getResults(@RequestParam("conference_id") int conference_id) {
        return new Gson().toJson(ResultTable.getResults(conference_id));
    }
}
