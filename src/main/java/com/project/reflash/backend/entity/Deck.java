package com.project.reflash.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "decks")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    /**
     * Creation date expressed as an epoch-second timestamp,
     * truncated to the **start of the day** (midnight).
     *
     * so that "day X" calculations are always relative to the start of
     * a full day.
     */
    @Column(name="crt")
    private long crt;

    @ManyToOne
    @JoinColumn(name="course_id", referencedColumnName="id")
    Course course;

    @OneToMany(mappedBy="deck")
    List<Note> notes;

    public Deck(String name) {
        this.name  = name;
        this.notes = new ArrayList<>();

        this.crt = LocalDate.now()
                .atStartOfDay(ZoneId.of("UTC"))
                .toEpochSecond();
    }
}
