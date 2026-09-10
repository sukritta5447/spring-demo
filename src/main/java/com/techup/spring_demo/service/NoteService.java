package com.techup.spring_demo.service;

import com.techup.spring_demo.dto.NoteRequest;
import com.techup.spring_demo.dto.NoteResponse;
import com.techup.spring_demo.entity.Note;
import com.techup.spring_demo.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<NoteResponse> getAll() {
        return noteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<NoteResponse> getById(Long id) {
        return noteRepository.findById(id)
                .map(this::toResponse);
    }

    public NoteResponse create(NoteRequest req) {
        Note note = new Note();
        note.setTitle(req.getTitle());
        note.setContent(req.getContent());
        Note savedNote = noteRepository.save(note);
        return toResponse(savedNote);
    }

    public Optional<NoteResponse> update(Long id, NoteRequest req) {
        return noteRepository.findById(id)
                .map(note -> {
                    note.setTitle(req.getTitle());
                    note.setContent(req.getContent());
                    return toResponse(noteRepository.save(note));
                });
    }

    public boolean delete(Long id) {
        if (!noteRepository.existsById(id)) {
            return false;
        }
        noteRepository.deleteById(id);
        return true;
    }

    public NoteResponse attachFileUrl(Long id, String url) {
    Note note = noteRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

    note.setImageUrl(url);
    Note saved = noteRepository.save(note);
    return toResponse(saved);
  }

    private NoteResponse toResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }
}
