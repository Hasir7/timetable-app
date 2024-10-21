package com.example.timetable;

import com.example.timetable.dto.TimetableDTO;
import com.example.timetable.entity.Subject;
import com.example.timetable.entity.Timetable;
import com.example.timetable.repository.SubjectRepository;
import com.example.timetable.repository.TimetableRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(TimetableRepository timetableRepository, SubjectRepository subjectRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            // Load subjects
            List<Subject> subjects = objectMapper.readValue(
                    new File("src/main/resources/subjects.json"),
                    new TypeReference<List<Subject>>() {}
            );
            subjectRepository.saveAll(subjects);
            logger.info("Subjects loaded successfully.");

            // Load timetables using DTO
            List<TimetableDTO> timetableDTOs = objectMapper.readValue(
                    new File("src/main/resources/timetable.json"),
                    new TypeReference<List<TimetableDTO>>() {}
            );
            logger.info("Timetables loaded successfully.");

            // Map subject ID to subject object
            Map<Long, Subject> subjectMap = subjects.stream()
                    .collect(Collectors.toMap(Subject::getSubjectId, subject -> subject));

            // Convert DTOs to Timetable entities
            List<Timetable> timetables = timetableDTOs.stream().map(dto -> {
                Timetable timetable = new Timetable();
                timetable.setClassDay(dto.getClassDay());
                timetable.setStartTime(dto.getStartTime());
                timetable.setEndTime(dto.getEndTime());
                Subject subject = subjectMap.get(dto.getSubjectId());
                if (subject != null) {
                    timetable.setSubject(subject);
                } else {
                    logger.warn("Subject ID {} not found for timetable entry on {}",
                            dto.getSubjectId(), dto.getClassDay());
                }
                return timetable;
            }).collect(Collectors.toList());

            timetableRepository.saveAll(timetables);
            logger.info("Timetables saved successfully.");
        } catch (IOException e) {
            logger.error("Error reading JSON files: ", e);
        }
    }
}
