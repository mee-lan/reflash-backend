package com.project.reflash.backend.entity;

import com.project.reflash.backend.algorithm.SchedulingAlgoUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notes")
@AllArgsConstructor
@Builder
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(
    mappedBy = "note",
    cascade = CascadeType.ALL,
    orphanRemoval = true)       // This ensures removed items are actually deleted from DB
    private List<Flashcard> flashCards;

    @ManyToOne
    @JoinColumn(name="deck_id", referencedColumnName="id")
    private Deck deck ;

    @Column(name="front")
    private String front;

    @Column(name="back")
    private String back;

    @Column(name="additional_context")
    private String additionalContext;

    @Column(name="crt")
    private Long crt;

    @ElementCollection
    @CollectionTable(
        name = "note_tags",
        joinColumns = @JoinColumn(name = "note_id")
    )
    @Column(name = "tag")
    private List<String> tags;

    public Note() {
        this.tags = new ArrayList<>();
        this.crt    = SchedulingAlgoUtils.intTime(1);   // current epoch seconds
    }

    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }
}
