#!/bin/bash

STD="2026_10_A_1"
TCHR="username"
ADMN="username"
PASS="password"
BASE_URL="http://localhost:8080"

# Login endpoint
login_student() {
  echo "Calling student login..."
  curl -X GET "$BASE_URL/login" -u $STD:$PASS -H "role:STUDENT" | jq .
  echo

}


all_courses() {
  echo "Getting all courses"
  curl -X GET "$BASE_URL/api/admin/all-course" -u $ADMN:$PASS -H "role:ADMINISTRATOR" | jq .
  echo

}


global_search_student() {
  echo "Searching for A"
  curl -X GET "$BASE_URL/api/student/global-search?input=what+is" -u $STD:$PASS -H "role:STUDENT" | jq .
  echo
}


global_search_teacher() {
  echo "Searching for what+is+tangent"
  curl -X GET "$BASE_URL/api/teacher/global-search?input=what+is+a+tangent" -u $TCHR:$PASS -H "role:TEACHER" | jq .
  echo
}

global_search_admin() {
  echo "Searching for John"
  curl -X GET "$BASE_URL/api/admin/global-search?input=math" -u $ADMN:$PASS -H "role:ADMINISTRATOR" | jq .
  echo
}



get_questions() {
  echo "Generating questions from decks 1,2,3,4 with count 5..."

  curl -X GET "$BASE_URL/api/teacher/questions?deckIds=1,2,3,4&count=5" \
    -u "$TCHR:$PASS" \
    -H "role: TEACHER" | jq .

  echo
}



get_course_for_edit() {
  echo "Fetching deck for edit..."

  curl -X GET "$BASE_URL/api/admin/course-full?courseId=1" \
    -u "$ADMN:$PASS" \
    -H "role: ADMINISTRATOR" | jq .

  echo
}

edit_course() {
  echo "Editing course with new data..."

  curl -X PUT "$BASE_URL/api/admin/edit-course" \
    -u "$ADMN:$PASS" \
    -H "Content-Type: application/json" \
    -H "role: ADMINISTRATOR" \
    -d '{
      "courseId": 1, 
      "courseName": "UPDATED", 
      "courseDescription": "UPDATED", 
      "grade": "8",
      "academicYear": "2020",
      "teachers": [
        {
          "id": 2 
        }
      ],
      "students": [
        {
          "id": 2
        }
      ]
    }' | jq .

  echo
}

#addition context and tags removed from note id = 2;

edit_deck() {
  echo "Editing deck with new data..."

  curl -X PUT "$BASE_URL/api/teacher/edit-deck" \
    -u "$TCHR:$PASS" \
    -H "Content-Type: application/json" \
    -H "role: TEACHER" \
    -d '{
      "deckId": 1,
      "deckName": "UPDATED",
      "deckDescription": "UPDATED",
      "notes": [
        {
          "noteId": 1,
          "front": "UPDATED",
          "back": "UPDATED",
          "additionalContext": "Solve ax²+bx+c=0, examples included",
          "tags": ["algebra","UPDATED","formulas"]
        },
        {
          "noteId": 2,
          "front": "Slope-Intercept Form",
          "back": "y = mx + b"
        },
        {
          "noteId": 4,
          "front": "UPDATED",
          "back": "First Outer Inner Last",
          "additionalContext": "UPDATED",
          "tags": ["UPDATED","multiplication"]
        },
        {
          "front": "NEW CARD",
          "back": "NEW CARD",
          "additionalContext": "NEW CARD",
          "tags": ["algebra","matrices","linear algebra"]
        },
        {
          "front": "NEW CARD 2",
          "back": "NEW CARD 2",
          "additionalContext": "NEW CARD 2",
          "tags": ["biology","plants","science"]
        }
      ]
    }' | jq .

  echo
}

get_deck_for_edit() {
  echo "Fetching deck for edit..."

  curl -X GET "$BASE_URL/api/teacher/deck-full?deckId=1" \
    -u "$TCHR:$PASS" \
    -H "role: TEACHER" | jq .

  echo
}


login_administrator() {
  echo "Calling student login..."
  curl -X GET "$BASE_URL/login" -u $ADMN:$PASS -H "role:ADMINISTRATOR" | jq .
  echo
}

create_student_profile() {
  echo "Creating student profile..."

  curl -X POST "$BASE_URL/api/admin/student-profile" \
    -u "$ADMN:$PASS" \
    -H "Content-Type: application/json" \
    -H "role: ADMINISTRATOR" \
    -d '{
      "firstName": "New",
      "lastName": "Student",
      "roll": "12",
      "password": "password",
      "grade": "10",
      "section": "A",
      "academicYear": "2025"
    }' | jq .

  echo
}

create_teacher_profile() {
  echo "Creating teacher profile..."

  curl -X POST "$BASE_URL/api/admin/teacher-profile" \
    -u "$ADMN:$PASS" \
    -H "Content-Type: application/json" \
    -H "role: ADMINISTRATOR" \
    -d '{
      "firstName": "New",
      "lastName": "Teacher",
      "password": "password",
      "email": "alice.smith@example.com",
      "username": "new_username"
    }' | jq .

  echo
}



create_course() {
  echo "Creating course..."

  curl -X POST "$BASE_URL/api/admin/course" \
    -u "$ADMN:$PASS" \
    -H "Content-Type: application/json" \
    -H "role: ADMINISTRATOR" \
    -d '{
      "courseName": "New Course",
      "courseDescription": "New Course Updated",
      "grade": "10",
      "academicYear": "2025",
      "teachers": [1, 2],
      "students": [1, 2, 3]
    }' | jq .

  echo
}

create_empty_deck() {
  echo "Creating deck..."

  curl -X POST "$BASE_URL/api/teacher/empty-deck" \
    -u "$TCHR:$PASS" \
    -H "Content-Type: application/json" \
    -H "role:TEACHER" \
    -d '{
      "deckName": "New Deck",
      "deckDescription": "New Deck Description",
      "courseId": 1
    }' | jq .

  echo
}

create_note() {
  echo "Creating note..."

  curl -X POST "$BASE_URL/api/teacher/note" \
    -u "$TCHR:$PASS" \
    -H "Content-Type: application/json" \
    -H "role:TEACHER" \
    -d '{
      "deckId": 1,
      "front": "What is this new card?",
      "back": "Proaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacess by which plants make food using sunligaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaht",
      "additionalContext": "Occurs in chloroplasts",
      "tags": ["biology", "plants"]
    }' | jq .

  echo
}

create_notes() {
  echo "Creating multiple notes..."

  curl -X POST "$BASE_URL/api/teacher/notes" \
    -u "$TCHR:$PASS" \
    -H "Content-Type: application/json" \
    -H "role:TEACHER" \
    -d '[
      {
        "deckId": 1,
        "front": "What is new card 1?",
        "back": "Process by which plants make food using sunlight",
        "additionalContext": "Occurs in chloroplasts"
      },
      {
        "deckId": 1,
        "front": "What is ATP?",
        "back": "Energy currency of the new card 2",
        "additionalContext": "Produced in mitochondria",
        "tags": ["biology", "cell"]
      }
    ]' | jq .

  echo
}

get_all_teachers() {
    echo "Getting all teachers..."
    curl -X GET "$BASE_URL/api/admin/teachers" -u $ADMN:$PASS -H "role:ADMINISTRATOR" | jq .
    echo
}

get_students_by_grade() {
    echo "Getting students by grade..."
    curl -X GET "$BASE_URL/api/admin/students-by-grade?grade=10" -u $ADMN:$PASS -H "role:ADMINISTRATOR" | jq .
    echo
}

login_student_failure() {
  echo "Calling student login..."
  curl -X GET "$BASE_URL/login" -u $STD:"random" -H "role:STUDENT" | jq .
  echo
}

login_teacher() {
  echo "Calling teacher login..."
  curl -X GET "$BASE_URL/login" -u $TCHR:$PASS -H "role: TEACHER" | jq .
  echo

}

get_courses_student() {
  echo "Getting student courses..."
  curl -X GET "$BASE_URL/api/student/courses" -u $STD:$PASS -H "role:STUDENT" | jq .
  echo
}



get_courses_teacher() {
  echo "Getting teacher courses..."
  curl -X GET "$BASE_URL/api/teacher/courses" -u $TCHR:$PASS -H "role:TEACHER" | jq .
  echo
}


get_decks_student() {
  echo "Getting student decks..."
  curl -X GET "$BASE_URL/api/student/decks?courseId=2" -u $STD:$PASS -H "role:STUDENT" | jq .
  echo
}


get_decks_teacher() {
  echo "Getting teacher decks..."
  curl -X GET "$BASE_URL/api/teacher/decks?courseId=1" -u $TCHR:$PASS -H "role:TEACHER" | jq .
  echo
}


get_flashcard_students() {
  echo "Getting flashcards courses..."
  curl -X GET "$BASE_URL/api/student/flashcards?deckId=1" -u $STD:$PASS -H "role:STUDENT" | jq .
  echo
}


get_notes_by_deck() {
  echo "Getting flashcards courses..."
  curl -X GET "$BASE_URL/api/teacher/notes-by-deck?deckId=1" -u $TCHR:$PASS -H "role:TEACHER" | jq .
  echo
}




generate_flashcards() {
  echo "Fetching cards..."

  TEXT=$(cat << 'EOF'
The human brain is an extraordinarily complex organ responsible for thought and memory.
It operates through billions of neurons.
Each neuron communicates using electrical signals.
Neural connections form vast networks.
These networks enable learning.
The brain consumes large amounts of energy.
Despite its size, it uses about twenty percent of body energy.
Neuroplasticity allows adaptation.
Learning strengthens neural pathways.
Unused connections weaken.
Memory involves multiple regions.
Sleep supports memory consolidation.
Emotions are processed in the limbic system.
The amygdala regulates fear.
The prefrontal cortex handles decisions.
Damage affects cognition.
Brain development is rapid in childhood.
Environment shapes growth.
Language is often lateralized.
Left hemisphere dominates speech.
Right hemisphere supports spatial skills.
Imaging reveals brain activity.
MRI shows structure.
EEG measures signals.
Neuroscience studies consciousness.
Many disorders remain unexplained.
Research continues worldwide.
Better understanding improves medicine.
Education benefits from brain science.
Ethics arise from cognition research.
The brain remains a scientific mystery.
EOF
)

  jq -n \
    --arg text "$TEXT" \
    --argjson count 50 \
    '{ text: $text, count: $count }' |
  curl -X POST "$BASE_URL/api/ai/generate-flashcards" \
    -u "$TCHR:$PASS" \
    -H "Content-Type: application/json" \
    -d @- | jq

  echo
}


# generate_flashcards() {
#   echo "Fetching cards..."
#   curl -X POST "$BASE_URL/api/ai/generate-flashcards" -u $USER:$PASS | jq
#   echo
# }


# # Get incomplete todos
# incomplete_todos() {
#   echo "Fetching incomplete todos..."
#   curl -X GET "$BASE_URL/api/incomplete-todos" -u $USER:$PASS | jq
#   echo
# }
#
# expired_todos() {
#   echo "Fetching expired todos..."
#   curl -X GET "$BASE_URL/api/expired-todos" -u $USER:$PASS | jq
#   echo
# }
#
#
# start_todo() {
#   echo "Starting todo..."
#   curl -X PUT "$BASE_URL/api/start-todo?id=1" -u $USER:$PASS | jq
#   echo
# }
#
# pause_todo() {
#   echo "Pausing todo..."
#   curl -X PUT "$BASE_URL/api/pause-todo?id=1" -u $USER:$PASS | jq
#   echo
# }
#
#
# weekly_leaderboards() {
#   echo "Loading weekly leaderboards..."
#   curl -X GET "$BASE_URL/api/weekly-leaderboards" -u $USER:$PASS | jq
#   echo
# }
#
# activities() {
#   echo "Loading activities..."
#   curl -X GET "$BASE_URL/api/activities" -u $USER:$PASS | jq
#   echo
#
# }
#
# community_summary() {
#   echo "Loading community summary..."
#   curl -X GET "$BASE_URL/api/community-summary" -u $USER:$PASS | jq
#   echo
# }
#
# recent_completions() {
#   echo "Loading recent completions..."
#   curl -X GET "$BASE_URL/api/recent-completions?pageNumber=0" -u $USER:$PASS | jq
#   echo
# }
#
# my_todos() {
#   echo "Loading recent completions..."
#   curl -X GET "$BASE_URL/api/my-todos" -u $USER:$PASS | jq
#   echo
# }
#


## Another endpoint example
#get_all_todos() {
#  echo "Fetching all todos..."
#  curl -X GET "$BASE_URL/todos" -u $USER:$PASS
#  echo
#}

# call functions based on CLI args
for cmd in "$@"; do
  $cmd
done
