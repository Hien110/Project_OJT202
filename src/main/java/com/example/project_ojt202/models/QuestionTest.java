package com.example.project_ojt202.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "QUESTIONTEST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class QuestionTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questionTestID")
    private Long questionTestID;

    @Column(name = "questionTestContent", columnDefinition = "TEXT")
    private String questionTestContent;

    @Column(name = "questionChapter")
    private int questionChapter;

    @Column(name = "questionLevel")
    private String questionLevel;

    @ManyToOne
    @JoinColumn(name = "lectureID")
    private LectureProfile lectureProfile;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    @OneToMany(mappedBy = "questionTest")
    private List<AnswerTest> answers;
}
