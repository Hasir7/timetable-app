package com.example.timetable.repository;

import com.example.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByStartTimeAndEndTime(String startTime, String endTime);
    List<Timetable> findBySubject_SubjectId(Long subjectId);
    List<Timetable> findByClassDay(String classDay);
    List<Timetable> findByClassDayAndSubject_SubjectId(String classDay, Long subjectId);
}

