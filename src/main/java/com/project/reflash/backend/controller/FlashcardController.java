package com.project.reflash.backend.controller;

import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.dto.FlashcardDto;
import com.project.reflash.backend.dto.FlashcardsCollectionDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.FlashcardService;
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
        FlashcardsCollectionDto deck = flashcardService.getDeckStudent(deckId, student.getId());
        return new ResponseEntity<>(new ApiResponse(deck), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/student/flashcards")
    public ResponseEntity<ApiResponse> updateFlashcards(@RequestBody List<FlashcardDto> flashcardDtos,
                                                        @AuthenticationPrincipal StudentUserDetails student,
                                                        @RequestParam Integer deckId) {
        flashcardService.updateFlashcards(flashcardDtos, deckId, student.getId());
        return new ResponseEntity<>(new ApiResponse("Updation Successful"), HttpStatus.OK);
    }
}
