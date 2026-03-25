package com.project.reflash.backend.service;

import com.project.reflash.backend.dto.FlashcardsCollectionDto;
import com.project.reflash.backend.dto.FlashcardDto;
import com.project.reflash.backend.entity.Deck;
import com.project.reflash.backend.entity.Flashcard;
import com.project.reflash.backend.entity.Note;
import com.project.reflash.backend.entity.Student;
import com.project.reflash.backend.repository.DeckRepository;
import com.project.reflash.backend.repository.FlashcardRepository;
import com.project.reflash.backend.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final StudentRepository studentRepository;

    public FlashcardService(FlashcardRepository flashcardRepository, DeckRepository deckRepository, StudentRepository studentRepository) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.studentRepository = studentRepository;
    }


    @PreAuthorize("hasRole('STUDENT')")
    @Transactional
    public void updateFlashcards(List<FlashcardDto> dtos, Integer deckId, Integer userId) {
        System.out.println(System.currentTimeMillis()/1000);
        //check if the deck is accessible to the user
        Deck deck = deckRepository.getDeckByIdIfAccessibleByStudent(deckId, userId)
                .orElseThrow(() -> new RuntimeException("Deck not found: " + deckId));


        List<Integer> cardIds = dtos.stream().map(FlashcardDto::getId).toList();

        List<Flashcard> flashcards = flashcardRepository.getCardsByIdsOfADeck(cardIds, deck.getId(), userId);

        if (dtos.size() != flashcards.size()) {
            //TODO: make the error more specific, like which card
            throw new RuntimeException("All flashcards submitted aren't accessible");
        }



        updateFlashcardData(flashcards, dtos);
    }

    private void updateFlashcardData(List<Flashcard> flashcards, List<FlashcardDto> dtos) {
        Map<Integer, FlashcardDto> dtoMap = dtos.stream().collect(Collectors.toMap(FlashcardDto::getId, dto -> dto));

        for (Flashcard flashcard : flashcards) {
            FlashcardDto dto = dtoMap.get(flashcard.getId());
            flashcard.setType(dto.getType());
            flashcard.setQueue(dto.getQueue());
            flashcard.setIvl(dto.getIvl());
            flashcard.setFactor(dto.getFactor());
            flashcard.setReps(dto.getReps());
            flashcard.setLapses(dto.getLapses());
            flashcard.setLeft(dto.getLeft());
            flashcard.setDue(dto.getDue());
        }
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

        //1. RETRIEVE NEW NOTES
        List<Note> newNotes = flashcardRepository.getOrphanNotes(deckId, userId,  PageRequest.of(0, 20));

        //2. create empty flashcards for the New Notes and convert to DTO
        List<Flashcard> newUnsavedCards = newNotes.stream().map(Flashcard::new).toList();


        //NOTE: only getting reference, avoid hitting the database
        Student student = studentRepository.getReferenceById(userId);
        newUnsavedCards.forEach(newCard -> newCard.setStudent(student));

        //3. Save flashcard for notes which don't have flashcards yet
        flashcardRepository.saveAll(newUnsavedCards);


        //4. load the 'NEW' flashcards
        List<Flashcard> newCards = flashcardRepository.getNewCardsForStudent(deck.getId(), userId);
        List<FlashcardDto> allCards = new ArrayList<>(newCards.stream().map(FlashcardDto::new).toList());


        //5. RETRIEVE LEARNING AND RELEARNING CARDS
        long currentTimeSeconds = Instant.now().getEpochSecond(); // UTC epoch seconds

        List<Flashcard> learningRelearningCards = flashcardRepository.getLearningRelearingCardsForStudent(deckId,
                currentTimeSeconds, userId);
        allCards.addAll(learningRelearningCards.stream().map(FlashcardDto::new).toList());


        //6. RETRIEVE REVIEW CARDS
        long deckCrtSeconds = deck.getCrt();

        //TODO: shouldn't today be calculated based on NPT or timezone of the user?
        long todaySeconds = LocalDate.now()
                .atStartOfDay(ZoneId.of("UTC"))
                .toEpochSecond();
        long today = (todaySeconds - deckCrtSeconds) / 86400L;

        List<Flashcard> reviewCards = flashcardRepository.getReviewCardsForStudent(deckId,
                today, userId);
        allCards.addAll(reviewCards.stream().map(FlashcardDto::new).toList());

        return new FlashcardsCollectionDto(deck, allCards);
    }
}
