package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.DeckDto;
import com.project.reflash.backend.dto.FlashcardDto;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Flashcard;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.FlashcardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;

    public FlashcardService(FlashcardRepository flashcardRepository, DeckRepository deckRepository) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
    }


    //to return a flashcard, following information are needed
    /*

        the user id

        join two tables, notes and the flaschard

        retrieve the new cards if: the deckId matches the deck_id in notes and that notes does not have any flashcard associated with it.

        create the flashcards for these notes


        then retrieve all the learning and review cards rom the flashcard repository for this particular user and this particular deck(may have to join with note to get the deck informatoin)

        return all of these
     */




    public DeckDto getDeck(Integer deckId, Integer userId, String role) {
        //check if the deck is accessible to the user
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found: " + deckId));




        //retrieve all the new notes from the deck
        List<Note> newNotes = flashcardRepository.getNewCardsForStudent(deckId, userId, PageRequest.of(0, 20));
        List<Flashcard> newCards = newNotes.stream().map(Flashcard::new).toList();
        List<FlashcardDto> allCards = newCards.stream().map(FlashcardDto::new).toList();


        return new DeckDto(deck, allCards);

    }
//        Deck deck = deckRepository.findById(deckId)
//                .orElseThrow(() -> new RuntimeException("Deck not found: " + deckId));
//
//        // "today" for review cards: number of days elapsed since the deck was created.
//        long deckCrtSeconds = deck.getCrt();
//
//
//        //TODO: shouldn't today be calculated based on NPT or timezone of the user?
//        long todaySeconds = LocalDate.now()
//                .atStartOfDay(ZoneId.of("UTC"))
//                .toEpochSecond();
//        long today = (todaySeconds - deckCrtSeconds) / 86400L;
//
//        //TODO: here we are using UTC seconds, because all data in the backend is stored in UTC
//       long currentTimeSeconds = Instant.now().getEpochSecond(); // UTC epoch seconds
//
//        List<Flashcard> allCards = new ArrayList<>();
//
//        if ("STUDENT".equalsIgnoreCase(role)) {
//            allCards.addAll(flashcardRepository.getNewCardsForStudent(deckId, userId, PageRequest.of(0, 20)));
//            allCards.addAll(flashcardRepository.getLearningRelearningCardsForStudent(deckId, currentTimeSeconds, userId));
//            allCards.addAll(flashcardRepository.getReviewCardsForStudent(deckId, today, userId));
//        } else {
//            allCards.addAll(flashcardRepository.getNewCardsForTeacher(deckId, userId, PageRequest.of(0, 20)));
//            allCards.addAll(flashcardRepository.getLearningRelearningCardsForTeacher(deckId, currentTimeSeconds, userId));
//            allCards.addAll(flashcardRepository.getReviewCardsForTeacher(deckId, today, userId));
//        }
//
//        List<FlashcardDto> flashcardDtos = allCards.stream()
//                .map(FlashcardDto::new)
//                .toList();
//
//        return new DeckDto(deck, flashcardDtos);
//    }
}
