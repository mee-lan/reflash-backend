package com.project.reflash.backend.controller;

import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.dto.DeckDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.FlashcardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class FlashcardController {
    private final FlashcardService flashcardService;

    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/flashcards")
    public ResponseEntity<ApiResponse> getDeckForStudent(
            @AuthenticationPrincipal StudentUserDetails student,
            @RequestParam Integer deckId) {
        DeckDto deck = flashcardService.getDeckStudent(deckId, student.getId(), "STUDENT");
        return new ResponseEntity<>(new ApiResponse(deck), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('TEACHER')")
//    @GetMapping("/teacher/flashcards")
//    public ResponseEntity<ApiResponse> getDeckForTeacher(
//            @AuthenticationPrincipal TeacherUserDetails teacher,
//            @RequestParam Integer deckId) {
//        DeckDto deck = flashcardService.getDeck(deckId, teacher.getId(), "TEACHER");
//        return new ResponseEntity<>(new ApiResponse(deck), HttpStatus.OK);
//    }
}
