package com.example.toeicapp.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

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
    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
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

    private static final Pattern SECTION_HEADER = Pattern.compile("^\\[(.+)\\]$");

    /**
     * Splits body text into sections on lines like "[Advertisement]" so a passage
     * made of multiple documents (ad + e-mail, two e-mails, etc.) can be rendered
     * as separate boxes instead of one run-on block.
     */
    public List<BodySection> getBodySections() {
        List<BodySection> sections = new ArrayList<>();
        if (body == null || body.isEmpty()) {
            return sections;
        }
        String label = null;
        StringBuilder current = new StringBuilder();
        for (String line : body.split("\n", -1)) {
            var matcher = SECTION_HEADER.matcher(line.trim());
            if (matcher.matches()) {
                addSection(sections, label, current);
                label = matcher.group(1);
            } else {
                current.append(line).append("\n");
            }
        }
        addSection(sections, label, current);
        return sections;
    }

    private static void addSection(List<BodySection> sections, String label, StringBuilder text) {
        String trimmed = text.toString().trim();
        if (!trimmed.isEmpty()) {
            sections.add(new BodySection(label, trimmed));
        }
        text.setLength(0);
    }

    public static class BodySection {
        private final String label;
        private final String text;

        public BodySection(String label, String text) {
            this.label = label;
            this.text = text;
        }

        public String getLabel() { return label; }
        public String getText() { return text; }
    }
}
