package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.FlashcardsCollectionDto;
import com.project.reflash.backend.dto.FlashcardDto;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Flashcard;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.FlashcardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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


    public FlashcardsCollectionDto getDeckStudent(Integer deckId, Integer userId) {
        //check if the deck is accessible to the user
        Deck deck = deckRepository.getDeckByIdIfAccessibleByStudent(deckId, userId)
                .orElseThrow(() -> new RuntimeException("Deck not found: " + deckId));

        //RETRIEVE NEW CARDS
        List<Note> newNotes = flashcardRepository.getNewCardsForStudent(deckId, PageRequest.of(0, 20));

        //create empty flashcards for the New Notes and convert to DTO
        List<Flashcard> newCards = newNotes.stream().map(Flashcard::new).toList();
        List<FlashcardDto> allCards = new ArrayList<>(newCards.stream().map(FlashcardDto::new).toList());



        //RETRIEVE LEARNING AND RELEARNING CARDS

        long currentTimeSeconds = Instant.now().getEpochSecond(); // UTC epoch seconds

        List<Flashcard> learningRelearningCards = flashcardRepository.getLearningRelearingCardsForStudent(deckId,
                currentTimeSeconds);
        allCards.addAll(learningRelearningCards.stream().map(FlashcardDto::new).toList());



        //RETRIEVE REVIEW CARDS
        long deckCrtSeconds = deck.getCrt();

        //TODO: shouldn't today be calculated based on NPT or timezone of the user?
        long todaySeconds = LocalDate.now()
                .atStartOfDay(ZoneId.of("UTC"))
                .toEpochSecond();
        long today = (todaySeconds - deckCrtSeconds) / 86400L;

        List<Flashcard> reviewCards = flashcardRepository.getReviewCardsForStudent(deckId,
                today);
        allCards.addAll(reviewCards.stream().map(FlashcardDto::new).toList());

        return new FlashcardsCollectionDto(deck, allCards);
    }
}
