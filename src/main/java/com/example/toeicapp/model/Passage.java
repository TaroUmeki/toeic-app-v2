package com.example.toeicapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passage")
public class Passage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "part_type")
    private String partType;

    @Column(name = "skill_type")
    private String skillType;

    @Lob
    private String body;

    @Lob
    @Column(name = "audio_script")
    private String audioScript;

    @OneToMany(mappedBy = "passage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Passage() {}

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPartType() { return partType; }
    public void setPartType(String partType) { this.partType = partType; }
    public String getSkillType() { return skillType; }
    public void setSkillType(String skillType) { this.skillType = skillType; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getAudioScript() { return audioScript; }
    public void setAudioScript(String audioScript) { this.audioScript = audioScript; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public void addQuestion(Question q) {
        questions.add(q);
        q.setPassage(this);
    }

    public void removeQuestion(Question q) {
        questions.remove(q);
        q.setPassage(null);
    }
}
