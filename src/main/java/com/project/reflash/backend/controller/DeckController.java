package com.project.reflash.backend.controller;


import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.CourseCreationDto;
import com.project.reflash.backend.dto.DeckCreationDto;
import com.project.reflash.backend.dto.DeckStudentDto;
import com.project.reflash.backend.dto.DeckTeacherDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.DeckService;
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
public class DeckController {

    private DeckService deckService;

    DeckController(DeckService deckService) {
        this.deckService = deckService;
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/decks")
    public ResponseEntity<ApiResponse> getDecksOfStudent(@AuthenticationPrincipal StudentUserDetails student, @RequestParam Integer courseId) {
        List<DeckStudentDto> decks = deckService.getDecksofStudent(student.getId(), courseId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(decks), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/decks")
    public ResponseEntity<ApiResponse> getTeacherCourses(@AuthenticationPrincipal TeacherUserDetails teacher, @RequestParam Integer courseId) {
        List<DeckTeacherDto> decks = deckService.getDecksOfTeacher(teacher.getId(), courseId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(decks), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/teacher/empty-deck")
    public ResponseEntity<ApiResponse> createDeck(@RequestBody DeckCreationDto deckCreationDto) {
        deckService.createDeck(deckCreationDto);
        return new ResponseEntity<ApiResponse>(new ApiResponse("Deck Created Successfully"), HttpStatus.OK);
    }

}
