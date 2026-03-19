package com.project.reflash.backend.controller;


import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.NoteCreationDto;
import com.project.reflash.backend.dto.NoteDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/teacher/note")
    public ResponseEntity<ApiResponse> createNote(@RequestBody NoteCreationDto noteCreationDto) {
        noteService.createNote(noteCreationDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Note Created Successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/teacher/notes")
    public ResponseEntity<ApiResponse> createNotes(@RequestBody List<NoteCreationDto> noteCreationDtos) {
        noteService.createNotes(noteCreationDtos);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Notes Created Successfully"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/notes-by-deck")
    public ResponseEntity<ApiResponse> getNotesByDeck(@RequestParam Integer deckId, @AuthenticationPrincipal TeacherUserDetails teacher) {
        List<NoteDto> notes = noteService.getNotesForADeckTeacher(deckId, teacher.getId());
        return new ResponseEntity<ApiResponse>(new ApiResponse(notes), HttpStatus.OK);


    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/questions")
    public ResponseEntity<ApiResponse> generateQuestions(@RequestParam List<Integer> deckIds, @RequestParam Integer count, @AuthenticationPrincipal TeacherUserDetails teacherUserDetails) {
        List<String> questions = noteService.getQuestionsFromDeck(deckIds, count, teacherUserDetails.getId());
        return new ResponseEntity<ApiResponse>(new ApiResponse(questions), HttpStatus.OK);
    }
}
