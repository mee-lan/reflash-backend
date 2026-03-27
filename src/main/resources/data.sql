-- =============================================
-- Students
-- =============================================
INSERT INTO students (id, firstname, lastname, password, grade, section, roll, academic_year)
VALUES (1, 'John', 'Doe', '{noop}password', '10', 'A', '1', '2026'),
       (2, 'Jane', 'Smith', '{noop}password', '10', 'A', '2', '2026'),
       (3, 'Aarav', 'Sharma', '{noop}password', '10', 'B', '1', '2026'),
       (4, 'Maya', 'Thapa', '{noop}password', '9', 'A', '6', '2026');

-- =============================================
-- Teachers
-- =============================================
INSERT INTO teachers (firstname, lastname, username, password, email)
VALUES ('First', 'Teacher', 'username', '{noop}password', 'username@pcampus.edu.np'),
       ('Second', 'Teacher', 'new_username_1', '{noop}password', 'username@pcampus.edu.np');



-- =============================================
-- Administrators
-- =============================================
INSERT INTO administrators (firstname, lastname, username, password, email)
VALUES ('Arjun', 'Karki', 'username', '{noop}password', 'username@pcampus.edu.np');


-- =============================================
-- Courses
-- =============================================
INSERT INTO courses (id, name, grade, academic_year, description)
VALUES (1, 'Mathematics', '10', '2026', 'Mathematics course'),
       (2, 'Science', '10', '2026', 'Science course');

-- =============================================
-- Course ↔ Teacher
-- =============================================
INSERT INTO course_teacher (course_id, teacher_id)
VALUES (1, 1),
       (2, 1);

-- =============================================
-- Course ↔ Student
-- =============================================
INSERT INTO course_student (course_id, student_id)
VALUES (1, 1),
       (2, 2),
       (1, 2),
       (2, 3);

-- =============================================
-- Decks
-- crt = epoch seconds at midnight UTC of the deck creation date.
-- H2 2.x: DATEDIFF with unquoted unit, TIMESTAMP literal for specific dates.
-- =============================================
INSERT INTO decks (id, name, course_id, crt, description)
VALUES (1, 'Algebra Basics', 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', TIMESTAMP '2026-03-01 00:00:00'),
        'Basic algebra cards'),
       (2, 'Chemistry Foundations', 2,
        DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', TIMESTAMP '2026-03-05 00:00:00'),
        'Chemistry cards for first chapter'),
       (3, 'Geometry Essentials', 1,
        DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', TIMESTAMP '2026-03-10 00:00:00'),
        'Geometry essentials for unit 5'),
       (4, 'Physics Fundamentals', 2,
        DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', TIMESTAMP '2026-03-11 00:00:00'), 'Physics chapter 1 notes');

-- -- =============================================
-- -- Notes
-- -- =============================================
-- CONTAINS the notes associated with decks 1, 2, 3, 4, and crt is current time in seconds from Jan 1, 1970
INSERT INTO notes (id, front, back, additional_context, deck_id, crt)
VALUES
    -- Deck 1 – Algebra Basics
    (1, 'What is the quadratic formula?', 'x = (-b ± √(b²-4ac)) / 2a', 'Used to solve ax²+bx+c=0', 1,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (2, 'What is the slope-intercept form?', 'y = mx + b', 'm is slope, b is y-intercept', 1,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (3, 'What is the difference of squares identity?', 'a² - b² = (a+b)(a-b)', 'Useful for fast factoring', 1,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (4, 'What is the FOIL method?', 'First Outer Inner Last', 'Used when multiplying two binomials', 1,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),


    -- Deck 2 – Chemistry Foundations
    (5, 'What is an exothermic reaction?', 'A reaction that releases heat.', 'Example: combustion', 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (6, 'What is the chemical formula for water?', 'H₂O', 'Two hydrogen, one oxygen', 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (7, 'What is Avogadro''s number?', '6.022 × 10²³', 'Number of particles in one mole', 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (8, 'What is the pH of a neutral solution?', '7', 'At 25 °C; below 7 = acidic, above 7 = basic', 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),

    -- Deck 3 – Geometry Essentials
    (9, 'What is the Pythagorean theorem?', 'a² + b² = c²', 'Applies to right-angled triangles', 3,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (10, 'What is the area of a circle?', 'A = πr²', 'r is the radius', 3,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (11, 'What is the sum of interior angles of a triangle?', '180°', 'True for any triangle in Euclidean geometry', 3,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (12, 'What is a tangent to a circle?', 'A line that touches the circle at exactly one point.', NULL, 3,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),


    -- Deck 4 – Physics Fundamentals
    (13, 'What is Newton''s second law?', 'F = ma', 'Force equals mass times acceleration', 4,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (14, 'What is the speed of light in vacuum?', '3 × 10⁸ m/s', 'Approximately 299,792,458 m/s', 4,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (15, 'What is Ohm''s law?', 'V = IR', 'Voltage = Current × Resistance', 4,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())),
    (16, 'What is kinetic energy?', 'KE = ½mv²', 'm = mass, v = velocity', 4,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()));
--
-- =============================================
-- Note tags
-- =============================================
INSERT INTO note_tags (note_id, tag)
VALUES (1, 'algebra'),
       (1, 'formula'),
       (2, 'algebra'),
       (2, 'linear'),
       (3, 'algebra'),
       (3, 'factoring'),
       (4, 'algebra'),
       (4, 'binomials'),
       (5, 'thermochemistry'),
       (6, 'basic'),
       (6, 'compounds'),
       (7, 'moles'),
       (7, 'constants'),
       (8, 'acids'),
       (8, 'bases'),
       (9, 'geometry'),
       (9, 'triangles'),
       (10, 'geometry'),
       (10, 'circles'),
       (11, 'geometry'),
       (11, 'triangles'),
       (12, 'geometry'),
       (12, 'circles'),
       (13, 'mechanics'),
       (13, 'newton'),
       (14, 'constants'),
       (14, 'light'),
       (15, 'electricity'),
       (15, 'circuits'),
       (16, 'mechanics'),
       (16, 'energy');

-- -- =============================================
-- -- Flashcards
-- -- =============================================

-- flaschard for user 1 for deck 1, but the user does not have any flashcards for deck 2,3,4
-- this means all cards in deck 2, 3, 4 are new cards to the user


INSERT INTO flashcards (id, note_id, type, queue, ivl, factor, reps, lapses, left_count, due, student_id)
VALUES
    -- Deck 1 – Algebra Basics (course 1) for student id 1
    (3, 3, 'LEARNING', 'LEARNING', 1, 2500, 0, 1, 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 600 - 86400, 1),
    -- REVIEW: due 1 day before today relative to deck 1 creation
    (4, 4, 'REVIEW', 'REVIEW', 3, 2500, 0, 0, 2,
     DATEDIFF(DAY, TIMESTAMP '2026-03-01 00:00:00', CURRENT_DATE()) - 1, 1),


     -- Deck 1 - for student id 2
    (17, 3, 'LEARNING', 'LEARNING', 1, 2500, 0, 1, 2,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 600 - 86400, 2),
    -- REVIEW: due 1 day before today relative to deck 1 creation
    (18, 4, 'REVIEW', 'REVIEW', 3, 2500, 0, 0, 2,
     DATEDIFF(DAY, TIMESTAMP '2026-03-01 00:00:00', CURRENT_DATE()) - 1, 2),
--
    -- Deck 2 – Chemistry Foundations (course 2) for student id 2
    (5, 5, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) -86400, 2),
    (6, 6, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 2, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 2),
    -- LEARNING: due 5 minutes ago
    (7, 7, 'LEARNING', 'LEARNING', 0, 2500, 1, 0, 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW())- 86400, 2),
    -- REVIEW: due 1 day before today relative to deck 2 creation
    (8, 8, 'REVIEW', 'REVIEW', 6, 2500, 4, 1, 1,
     DATEDIFF(DAY, TIMESTAMP '2026-03-05 00:00:00', CURRENT_DATE()) - 1, 2),
--
    -- Deck 3 – Geometry Essentials (course 1) for student id 3
    (9, 9, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 3),
    (10, 10, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 2, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 3),
    (11, 11, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 2, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 3),
    -- RELEARNING: due 2 minutes ago
    (12, 12, 'RELEARNING', 'LEARNING', 1, 1800, 5, 2, 1,
     DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 3),

    -- Deck 4 – Physics Fundamentals (course 2)
    (13, 13, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) -86400, 4),
    (14, 14, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 1, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 4),
    (15, 15, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 2, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 4),
    (16, 16, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 2, DATEDIFF(SECOND, TIMESTAMP '1970-01-01 00:00:00', NOW()) - 86400, 4);
