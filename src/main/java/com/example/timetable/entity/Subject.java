package com.example.timetable.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok annotation to generate getters, setters, toString, etc.
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @Column(name = "subject_id")
    @JsonProperty("subject_id") // Maps JSON field "subject_id" to this field
    private Long subjectId;

    @Column(name = "subject_name")
    @JsonProperty("subject_name") // Maps JSON field "subject_name" to this field
    private String subjectName;
}
