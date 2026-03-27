package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.GlobalSearchResultAdminDto;
import com.project.reflash.backend.dto.GlobalSearchResultDto;
import com.project.reflash.backend.entity.*;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.NoteRepository;
import com.project.reflash.backend.repository.StudentRepository;
import com.project.reflash.backend.repository.TeacherRepository;
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
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    GlobalSearchService(
            CourseRepository courseRepository,
            DeckRepository deckRepository,
            NoteRepository noteRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository
    ) {
        this.courseRepository = courseRepository;
        this.deckRepository = deckRepository;
        this.noteRepository = noteRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
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

    // --- Admin Specifications ---

    private static Specification<Course> courseSpecForAdmin(String input) {
        return (root, query, cb) -> {
            Predicate keywordPredicate = buildNameLikePredicate(cb, root.get("name"), input);
            if (keywordPredicate == null) {
                return cb.conjunction();
            }
            return keywordPredicate;
        };
    }

    private static Predicate buildTeacherLikePredicate(CriteriaBuilder cb, Root<Teacher> root, String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String pattern = "%" + input.trim().toLowerCase() + "%";
        Predicate firstNameLike = cb.like(cb.lower(root.get("firstName")), pattern);
        Predicate lastNameLike = cb.like(cb.lower(root.get("lastName")), pattern);
        Predicate usernameLike = cb.like(cb.lower(root.get("username")), pattern);
        Predicate emailLike = cb.like(cb.lower(root.get("email")), pattern);
        return cb.or(firstNameLike, lastNameLike, usernameLike, emailLike);
    }

    private static Specification<Teacher> teacherSpecForAdmin(String input) {
        return (root, query, cb) -> {
            Predicate keywordPredicate = buildTeacherLikePredicate(cb, root, input);
            if (keywordPredicate == null) {
                return cb.conjunction();
            }
            return keywordPredicate;
        };
    }

    private static Predicate buildStudentLikePredicate(CriteriaBuilder cb, Root<Student> root, String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String pattern = "%" + input.trim().toLowerCase() + "%";
        Predicate firstNameLike = cb.like(cb.lower(root.get("firstName")), pattern);
        Predicate lastNameLike = cb.like(cb.lower(root.get("lastName")), pattern);
        Predicate rollLike = cb.like(cb.lower(root.get("roll")), pattern);
        Predicate gradeLike = cb.like(cb.lower(root.get("grade")), pattern);
        Predicate sectionLike = cb.like(cb.lower(root.get("section")), pattern);
        return cb.or(firstNameLike, lastNameLike, rollLike, gradeLike, sectionLike);
    }

    private static Specification<Student> studentSpecForAdmin(String input) {
        return (root, query, cb) -> {
            Predicate keywordPredicate = buildStudentLikePredicate(cb, root, input);
            if (keywordPredicate == null) {
                return cb.conjunction();
            }
            return keywordPredicate;
        };
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public GlobalSearchResultAdminDto globalSearchAdmin(String input) {
        List<Course> courses = courseRepository.findAll(courseSpecForAdmin(input));
        List<Teacher> teachers = teacherRepository.findAll(teacherSpecForAdmin(input));
        List<Student> students = studentRepository.findAll(studentSpecForAdmin(input));
        return new GlobalSearchResultAdminDto(courses, teachers, students);
    }
}
