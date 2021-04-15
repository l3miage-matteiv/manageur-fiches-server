package com.example;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/chamis")
public class ChamiCRUD {
    @Autowired
    ChamiRepository chamis;

    @GetMapping("/")
    public ResponseEntity<List<Chami>> index() {
        return ResponseEntity.ok( chamis.findAll() );
    }

    @GetMapping("/{chamiId}")
    ResponseEntity<Chami> read(@PathVariable(value="chamiId") String id) {
        Optional<Chami> c = chamis.findById(id);
        if (c.isPresent()){
            return ResponseEntity.ok(c.get());
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{chamiId}")
    public ResponseEntity<Chami> create(@PathVariable(value="chamiId") String id, @RequestBody Chami c) {
        return ResponseEntity.ok( chamis.save(c) );
    }
}
