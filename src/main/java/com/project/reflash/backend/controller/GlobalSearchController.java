package com.project.reflash.backend.controller;

import com.project.reflash.backend.auth.user_details.StudentUserDetails;
import com.project.reflash.backend.auth.user_details.TeacherUserDetails;
import com.project.reflash.backend.dto.GlobalSearchResultDto;
import com.project.reflash.backend.response.ApiResponse;
import com.project.reflash.backend.service.GlobalSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GlobalSearchController {

    GlobalSearchService globalSearchService;
    GlobalSearchController(GlobalSearchService globalSearchService) {
        this.globalSearchService = globalSearchService;
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/global-search")
    public ResponseEntity<ApiResponse> globalSearchStudent(@RequestParam String input, @AuthenticationPrincipal StudentUserDetails student) {
        GlobalSearchResultDto globalSearchResultDto = globalSearchService.globalSearchStudent(input, student.getId());
        return ResponseEntity.ok().body(new ApiResponse(globalSearchResultDto));
    }


    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/global-search")
    public ResponseEntity<ApiResponse> globalSearchTeacher(@RequestParam String input, @AuthenticationPrincipal TeacherUserDetails teacher) {
        GlobalSearchResultDto globalSearchResultDto = globalSearchService.globalSearchTeacher(input, teacher.getId());
        return ResponseEntity.ok().body(new ApiResponse(globalSearchResultDto));
    }
}
