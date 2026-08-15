package com.example.toeicapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passage_id")
    private Passage passage;

    @Lob
    @Column(name = "question_text")
    private String questionText;

    @Column(name = "blank_number")
    private Integer blankNumber;

    @Lob
    private String explanation;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

    public Question() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Passage getPassage() { return passage; }
    public void setPassage(Passage passage) { this.passage = passage; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public Integer getBlankNumber() { return blankNumber; }
    public void setBlankNumber(Integer blankNumber) { this.blankNumber = blankNumber; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }

    public void addChoice(Choice c) {
        choices.add(c);
        c.setQuestion(this);
    }

    public void removeChoice(Choice c) {
        choices.remove(c);
        c.setQuestion(null);
    }
}
