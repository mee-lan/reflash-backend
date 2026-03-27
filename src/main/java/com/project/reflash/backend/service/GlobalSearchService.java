package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.GlobalSearchResultStudentDto;
import com.project.reflash.backend.entity.Course;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.repository.CourseRepository;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.NoteRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalSearchService {

    private final NoteRepository noteRepository;
    private CourseRepository courseRepository;
    private DeckRepository deckRepository;

    GlobalSearchService(CourseRepository courseRepository, DeckRepository deckRepository, NoteRepository noteRepository) {
        this.courseRepository = courseRepository;
        this.deckRepository = deckRepository;
        this.noteRepository = noteRepository;
    }


    //retrieve the course in that a student belongs to and the keyword is in the name
    //only return the course that the contains the student as a student enrolled

    private static <T> Specification<T> containsKeywordAndStudentForCourse(
            String input,
            Integer studentId
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔗 join with students
            Join<Course, Student> studentJoin = root.join("students");

            // 🎯 filter: course must contain this student
            Predicate studentPredicate = criteriaBuilder.equal(studentJoin.get("id"), studentId);

            if (input != null && !input.trim().isEmpty()) {
                String[] keywords = input.trim().split("\\s+");

                List<Predicate> keywordPredicates = new ArrayList<>();

                for (String keyword : keywords) {
                    Predicate nameLike = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + keyword.toLowerCase() + "%"
                    );
                    keywordPredicates.add(nameLike);
                }

                // (name LIKE k1 OR name LIKE k2 ...)
                Predicate keywordOr = criteriaBuilder.or(keywordPredicates.toArray(new Predicate[0]));

                // (student matches AND keyword matches)
                predicates.add(criteriaBuilder.and(studentPredicate, keywordOr));
            } else {
                // if no keyword → just filter by student
                predicates.add(studentPredicate);
            }

            // avoid duplicates  ManyToMany join
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> Specification<T> containsKeywordAndStudentForDeck(
            String input,
            Integer studentId
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔗 Deck → Course
            Join<Deck, Course> courseJoin = root.join("course");

            // 🔗 Course → Students
            Join<Course, Student> studentJoin = courseJoin.join("students");

            // 🎯 student condition
            Predicate studentPredicate = criteriaBuilder.equal(
                    studentJoin.get("id"),
                    studentId
            );

            if (input != null && !input.trim().isEmpty()) {

                String[] keywords = input.trim().split("\\s+");
                List<Predicate> keywordPredicates = new ArrayList<>();

                for (String keyword : keywords) {
                    Predicate nameLike = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + keyword.toLowerCase() + "%"
                    );
                    keywordPredicates.add(nameLike);
                }

                // (name LIKE k1 OR name LIKE k2 ...)
                Predicate keywordOr = criteriaBuilder.or(
                        keywordPredicates.toArray(new Predicate[0])
                );

                // (student match AND keyword match)
                predicates.add(criteriaBuilder.and(studentPredicate, keywordOr));

            } else {
                // only student condition
                predicates.add(studentPredicate);
            }

            // 🚫 avoid duplicates joins
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<Note> containsKeywordAndStudentForNote(
            String input,
            Integer studentId
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔗 Note → Deck
            Join<Note, Deck> deckJoin = root.join("deck");

            // 🔗 Deck → Course
            Join<Deck, Course> courseJoin = deckJoin.join("course");

            // 🔗 Course → Students
            Join<Course, Student> studentJoin = courseJoin.join("students");

            // 🎯 student condition
            Predicate studentPredicate = criteriaBuilder.equal(
                    studentJoin.get("id"),
                    studentId
            );

            if (input != null && !input.trim().isEmpty()) {

                String[] keywords = input.trim().split("\\s+");
                List<Predicate> keywordPredicates = new ArrayList<>();

                for (String keyword : keywords) {

                    String pattern = "%" + keyword.toLowerCase() + "%";

                    // 🔍 match front
                    Predicate frontLike = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("front")),
                            pattern
                    );

                    // 🔍 match back
                    Predicate backLike = criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("back")),
                            pattern
                    );

                    // (front LIKE k OR back LIKE k)
                    Predicate frontOrBack = criteriaBuilder.or(frontLike, backLike);

                    keywordPredicates.add(frontOrBack);
                }

                // (keyword1 OR keyword2 OR ...)
                Predicate keywordOr = criteriaBuilder.or(
                        keywordPredicates.toArray(new Predicate[0])
                );

                // (student match AND keyword match)
                predicates.add(criteriaBuilder.and(studentPredicate, keywordOr));

            } else {
                // only student condition
                predicates.add(studentPredicate);
            }

            // 🚫 avoid duplicates بسبب joins
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @PreAuthorize("hasRole('STUDENT')")
    public GlobalSearchResultStudentDto globalSearchStudent(String input, Integer studentId) {
        List<Course> courses = courseRepository.findAll(containsKeywordAndStudentForCourse(input, studentId));

        List<Deck> decks = deckRepository.findAll(containsKeywordAndStudentForDeck(input, studentId));

        List<Note> notes = noteRepository.findAll(containsKeywordAndStudentForNote(input, studentId));

        GlobalSearchResultStudentDto searchResult = new GlobalSearchResultStudentDto(courses, decks, notes);

        return searchResult;
    }

}
