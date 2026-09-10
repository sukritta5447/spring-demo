package com.techup.spring_demo.controller;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.service.NoteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techup.spring_demo.service.SupabaseStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final SupabaseStorageService supabaseStorageService;

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes() {
        return ResponseEntity.ok(noteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable Long id) {
        return noteService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(@RequestBody NoteRequest req) {
        NoteResponse created = noteService.create(req);
        URI location = URI.create("/api/notes/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> update(
            @PathVariable Long id,
            @RequestBody NoteRequest req) {
        return noteService.update(id, req)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return noteService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/upload")
  public ResponseEntity<NoteResponse> uploadForNote(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file) {
    String url = supabaseStorageService.uploadFile(file);
    NoteResponse updated = noteService.attachFileUrl(id, url);
    return ResponseEntity.ok(updated);
  }
}
