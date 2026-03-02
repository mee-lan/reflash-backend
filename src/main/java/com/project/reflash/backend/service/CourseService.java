package com.project.reflash.backend.service;

import com.project.reflash.backend.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
}
