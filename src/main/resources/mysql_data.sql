INSERT INTO decks (id, name, course_id, crt, description)
VALUES
    (1, 'Algebra Basics', 1, UNIX_TIMESTAMP('2026-03-01 00:00:00'), 'Basic algebra cards'),

    (2, 'Chemistry Foundations', 2,
     UNIX_TIMESTAMP('2026-03-05 00:00:00'), 'Chemistry cards for first chapter'),

    (3, 'Geometry Essentials', 1,
     UNIX_TIMESTAMP('2026-03-10 00:00:00'), 'Geometry essentials for unit 5'),

    (4, 'Physics Fundamentals', 2,
     UNIX_TIMESTAMP('2026-03-11 00:00:00'), 'Physics chapter 1 notes');


INSERT INTO notes (id, front, back, additional_context, deck_id, crt)
VALUES
    -- Deck 1 – Algebra Basics
    (1, 'What is the quadratic formula?', 'x = (-b ± √(b²-4ac)) / 2a', 'Used to solve ax²+bx+c=0', 1,
     UNIX_TIMESTAMP(NOW())),
    (2, 'What is the slope-intercept form?', 'y = mx + b', 'm is slope, b is y-intercept', 1,
     UNIX_TIMESTAMP(NOW())),
    (3, 'What is the difference of squares identity?', 'a² - b² = (a+b)(a-b)', 'Useful for fast factoring', 1,
     UNIX_TIMESTAMP(NOW())),
    (4, 'What is the FOIL method?', 'First Outer Inner Last', 'Used when multiplying two binomials', 1,
     UNIX_TIMESTAMP(NOW())),

    -- Deck 2 – Chemistry Foundations
    (5, 'What is an exothermic reaction?', 'A reaction that releases heat.', 'Example: combustion', 2,
     UNIX_TIMESTAMP(NOW())),
    (6, 'What is the chemical formula for water?', 'H₂O', 'Two hydrogen, one oxygen', 2,
     UNIX_TIMESTAMP(NOW())),
    (7, 'What is Avogadro''s number?', '6.022 × 10²³', 'Number of particles in one mole', 2,
     UNIX_TIMESTAMP(NOW())),
    (8, 'What is the pH of a neutral solution?', '7', 'At 25 °C; below 7 = acidic, above 7 = basic', 2,
     UNIX_TIMESTAMP(NOW())),

    -- Deck 3 – Geometry Essentials
    (9, 'What is the Pythagorean theorem?', 'a² + b² = c²', 'Applies to right-angled triangles', 3,
     UNIX_TIMESTAMP(NOW())),
    (10, 'What is the area of a circle?', 'A = πr²', 'r is the radius', 3,
     UNIX_TIMESTAMP(NOW())),
    (11, 'What is the sum of interior angles of a triangle?', '180°', 'True for any triangle in Euclidean geometry', 3,
     UNIX_TIMESTAMP(NOW())),
    (12, 'What is a tangent to a circle?', 'A line that touches the circle at exactly one point.', NULL, 3,
     UNIX_TIMESTAMP(NOW())),

    -- Deck 4 – Physics Fundamentals
    (13, 'What is Newton''s second law?', 'F = ma', 'Force equals mass times acceleration', 4,
     UNIX_TIMESTAMP(NOW())),
    (14, 'What is the speed of light in vacuum?', '3 × 10⁸ m/s', 'Approximately 299,792,458 m/s', 4,
     UNIX_TIMESTAMP(NOW())),
    (15, 'What is Ohm''s law?', 'V = IR', 'Voltage = Current × Resistance', 4,
     UNIX_TIMESTAMP(NOW())),
    (16, 'What is kinetic energy?', 'KE = ½mv²', 'm = mass, v = velocity', 4,
     UNIX_TIMESTAMP(NOW()));

INSERT INTO flashcards (id, note_id, type, queue, ivl, factor, reps, lapses, left_count, due, student_id)
VALUES
    -- Deck 1 – Algebra Basics (course 1)
    (1, 1, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 1),
    (2, 2, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 1),

    -- LEARNING: due 10 minutes ago (+ 1 day ago as in original)
    (3, 3, 'LEARNING', 0, 2500, 1, 0, 1, 0,
     UNIX_TIMESTAMP(NOW()) - 600 - 86400, 1),

    -- REVIEW: due 1 day before today relative to deck 1 creation
    (4, 4, 'REVIEW', 10, 2500, 3, 0, 0, 0,
     DATEDIFF(CURRENT_DATE(), '2026-03-01') - 1, 1),

    -- Deck 2 – Chemistry Foundations (course 2)
    (5, 5, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 2),
    (6, 6, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 2),

    -- LEARNING (as per your data)
    (7, 7, 'LEARNING', 'LEARNING', 0, 2500, 1, 0, 1,
     UNIX_TIMESTAMP(NOW()), 2),

    -- REVIEW: due 1 day before today relative to deck 2 creation
    (8, 8, 'REVIEW', 'REVIEW', 6, 2500, 4, 1, 0,
     DATEDIFF(CURRENT_DATE(), '2026-03-05') - 1, 2),

    -- Deck 3 – Geometry Essentials (course 1)
    (9, 9, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 3),
    (10, 10, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 3),
    (11, 11, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 3),

    -- RELEARNING: due 2 minutes ago
    (12, 12, 'RELEARNING', 'LEARNING', 1, 1800, 5, 2, 1,
     UNIX_TIMESTAMP(NOW()) - 120, 3),

    -- Deck 4 – Physics Fundamentals (course 2)
    (13, 13, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 4),
    (14, 14, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 4),
    (15, 15, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 4),
    (16, 16, 'LEARNING', 'LEARNING', 0, 0, 0, 0, 0, UNIX_TIMESTAMP(NOW()), 4);



-- to clone a database in mysql: mysqldump -u root -p reflash | mysql -u root -p reflash_original
