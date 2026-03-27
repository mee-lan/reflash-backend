package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.GlobalSearchResultDto;
import com.project.reflash.backend.entity.*;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.NoteRepository;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalSearchService {

    private final NoteRepository noteRepository;
    private final CourseRepository courseRepository;
    private final DeckRepository deckRepository;

    GlobalSearchService(CourseRepository courseRepository, DeckRepository deckRepository, NoteRepository noteRepository) {
        this.courseRepository = courseRepository;
        this.deckRepository = deckRepository;
        this.noteRepository = noteRepository;
    }

    private static Predicate buildNameLikePredicate(CriteriaBuilder cb, Path<String> field, String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String pattern = "%" + input.trim().toLowerCase() + "%";
        return cb.like(cb.lower(field), pattern);
    }

    private static Predicate buildNoteLikePredicate(CriteriaBuilder cb, Root<Note> root, String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String pattern = "%" + input.trim().toLowerCase() + "%";
        Predicate frontLike = cb.like(cb.lower(root.get("front")), pattern);
        Predicate backLike = cb.like(cb.lower(root.get("back")), pattern);
        return cb.or(frontLike, backLike);
    }

    private static Predicate combinePredicates(CriteriaBuilder cb, Predicate userPredicate, Predicate keywordPredicate) {
        if (keywordPredicate != null) {
            return cb.and(userPredicate, keywordPredicate);
        }
        return userPredicate;
    }

    // --- Student Specifications ---

    private static Specification<Course> courseSpecForStudent(String input, Integer studentId) {
        return (root, query, cb) -> {
            Join<Course, Student> studentJoin = root.join("students");
            Predicate studentPredicate = cb.equal(studentJoin.get("id"), studentId);
            Predicate keywordPredicate = buildNameLikePredicate(cb, root.get("name"), input);
            query.distinct(true);
            return combinePredicates(cb, studentPredicate, keywordPredicate);
        };
    }

    private static Specification<Deck> deckSpecForStudent(String input, Integer studentId) {
        return (root, query, cb) -> {
            Join<Deck, Course> courseJoin = root.join("course");
            Join<Course, Student> studentJoin = courseJoin.join("students");
            Predicate studentPredicate = cb.equal(studentJoin.get("id"), studentId);
            Predicate keywordPredicate = buildNameLikePredicate(cb, root.get("name"), input);
            query.distinct(true);
            return combinePredicates(cb, studentPredicate, keywordPredicate);
        };
    }

    private static Specification<Note> noteSpecForStudent(String input, Integer studentId) {
        return (root, query, cb) -> {
            Join<Note, Deck> deckJoin = root.join("deck");
            Join<Deck, Course> courseJoin = deckJoin.join("course");
            Join<Course, Student> studentJoin = courseJoin.join("students");
            Predicate studentPredicate = cb.equal(studentJoin.get("id"), studentId);
            Predicate keywordPredicate = buildNoteLikePredicate(cb, root, input);
            query.distinct(true);
            return combinePredicates(cb, studentPredicate, keywordPredicate);
        };
    }

    // --- Teacher Specifications ---

    private static Specification<Course> courseSpecForTeacher(String input, Integer teacherId) {
        return (root, query, cb) -> {
            Join<Course, Teacher> teacherJoin = root.join("teachers");
            Predicate teacherPredicate = cb.equal(teacherJoin.get("id"), teacherId);
            Predicate keywordPredicate = buildNameLikePredicate(cb, root.get("name"), input);
            query.distinct(true);
            return combinePredicates(cb, teacherPredicate, keywordPredicate);
        };
    }

    private static Specification<Deck> deckSpecForTeacher(String input, Integer teacherId) {
        return (root, query, cb) -> {
            Join<Deck, Course> courseJoin = root.join("course");
            Join<Course, Teacher> teacherJoin = courseJoin.join("teachers");
            Predicate teacherPredicate = cb.equal(teacherJoin.get("id"), teacherId);
            Predicate keywordPredicate = buildNameLikePredicate(cb, root.get("name"), input);
            query.distinct(true);
            return combinePredicates(cb, teacherPredicate, keywordPredicate);
        };
    }

    private static Specification<Note> noteSpecForTeacher(String input, Integer teacherId) {
        return (root, query, cb) -> {
            Join<Note, Deck> deckJoin = root.join("deck");
            Join<Deck, Course> courseJoin = deckJoin.join("course");
            Join<Course, Teacher> teacherJoin = courseJoin.join("teachers");
            Predicate teacherPredicate = cb.equal(teacherJoin.get("id"), teacherId);
            Predicate keywordPredicate = buildNoteLikePredicate(cb, root, input);
            query.distinct(true);
            return combinePredicates(cb, teacherPredicate, keywordPredicate);
        };
    }

    @PreAuthorize("hasRole('STUDENT')")
    public GlobalSearchResultDto globalSearchStudent(String input, Integer studentId) {
        List<Course> courses = courseRepository.findAll(courseSpecForStudent(input, studentId));
        List<Deck> decks = deckRepository.findAll(deckSpecForStudent(input, studentId));
        List<Note> notes = noteRepository.findAll(noteSpecForStudent(input, studentId));
        return new GlobalSearchResultDto(courses, decks, notes);
    }

    @PreAuthorize("hasRole('TEACHER')")
    public GlobalSearchResultDto globalSearchTeacher(String input, Integer teacherId) {
        List<Course> courses = courseRepository.findAll(courseSpecForTeacher(input, teacherId));
        List<Deck> decks = deckRepository.findAll(deckSpecForTeacher(input, teacherId));
        List<Note> notes = noteRepository.findAll(noteSpecForTeacher(input, teacherId));
        return new GlobalSearchResultDto(courses, decks, notes);
    }
}
