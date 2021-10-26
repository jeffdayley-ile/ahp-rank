package com.i13n.AHP.Controller;

import com.i13n.AHP.resource.InputResource;
import com.i13n.AHP.resource.ResultsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/analyze")
public class Analyze {
//    @PostMapping
//    public ResponseEntity<ResultsResource> analyzeInput(@RequestBody InputResource) {
//
//        UriComponents location = UriComponentsBuilder.fromPath("analyze/{uuid}")
//                .buildAndExpand(ResultsResource)
//        return ResponseEntity.created(location.toUri()).body(new ResultsResource());
//    }


}
