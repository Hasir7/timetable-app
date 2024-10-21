package com.example.timetable.controller;

import com.example.timetable.entity.Subject;
import com.example.timetable.entity.Timetable;
import com.example.timetable.repository.SubjectRepository;
import com.example.timetable.repository.TimetableRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/timetable")
public class TimetableController {

    private final TimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;

    public TimetableController(TimetableRepository timetableRepository, SubjectRepository subjectRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/all")
    public String showAllTimetables(Model model) {
        List<Timetable> timetables = timetableRepository.findAll();
        model.addAttribute("timetables", timetables);
        return "timetables";
    }


    @GetMapping("/add")
    public String showAddTimetableForm(Model model) {
        model.addAttribute("timetable", new Timetable());
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "add-timetable";
    }

    @PostMapping("/add")
    public String addTimetable(@ModelAttribute Timetable timetable, Model model) {
        timetableRepository.save(timetable);
        return "redirect:/timetable/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditTimetableForm(@PathVariable("id") Long id, Model model) {
        Timetable timetable = timetableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid timetable ID:" + id));
        model.addAttribute("timetable", timetable);
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);
        return "edit-timetable";
    }

    @PostMapping("/edit/{id}")
    public String editTimetable(@PathVariable("id") Long id, @ModelAttribute Timetable timetable, Model model) {
        timetable.setNo(id);
        timetableRepository.save(timetable);
        return "redirect:/timetable/all";
    }


    @GetMapping("/delete/{id}")
    public String deleteTimetable(@PathVariable("id") Long id, Model model) {
        timetableRepository.deleteById(id);
        return "redirect:/timetable/all";
    }


    @GetMapping("/search-by-time")
    public String searchTimetableByTime(@RequestParam String startTime, @RequestParam String endTime, Model model) {
        List<Timetable> timetableList = timetableRepository.findByStartTimeAndEndTime(startTime, endTime);
        model.addAttribute("timetableList", timetableList);
        return "timetables";
    }

    @GetMapping("/search-by-subject")
    public String searchTimetableBySubject(@RequestParam Long subjectId, Model model) {
        List<Timetable> timetableList = timetableRepository.findBySubject_SubjectId(subjectId);
        model.addAttribute("timetableList", timetableList);
        return "timetables";
    }

    @GetMapping("/search-by-day")
    public String searchTimetableByDay(@RequestParam String classDay, Model model) {
        List<Timetable> timetableList = timetableRepository.findByClassDay(classDay);
        model.addAttribute("timetableList", timetableList);
        return "timetables";
    }
}
