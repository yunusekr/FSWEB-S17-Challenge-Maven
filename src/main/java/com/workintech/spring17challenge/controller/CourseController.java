package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private Map<Integer, Course> courses;

    private CourseGpa lowCourseGpa;
    private CourseGpa mediumCourseGpa;
    private CourseGpa highCourseGpa;

    @PostConstruct
    public void init(){
        courses = new HashMap();
    }

    @Autowired
    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa, @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa, @Qualifier("highCourseGpa") CourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @GetMapping
    public List<Course> getCourses(){
        return courses.values().stream().toList();
    }

    @GetMapping("/{name}")
    public Course getCourse(@PathVariable String name){
        if(courses.values().stream().noneMatch(o->o.getName().equals(name))){
            throw new ApiException("Geçersiz giriş: Sayı, özel karakter veya boş değer kabul edilmez. Lütfen bir metin (String) giriniz."
                    , HttpStatus.NOT_FOUND);
        }
        return courses.values().stream().filter(o->o.getName().equals(name)).findFirst().get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<Course, Integer> postCourse(@RequestBody Course course){
        int totalGpa = 0;
        Map<Course,Integer> course1 = new HashMap<>();
        if(courses.values().stream().anyMatch(o->o.getName().equals(course.getName())) && (course.getCredit() < 0 && course.getCredit() > 4)){
            throw new ApiException("Girilen kurs ismi mevcut veya kredi değeri 0 ile 4 aralığında değil!", HttpStatus.NOT_FOUND);
        }
        if (course.getCredit() <= 2){
            totalGpa = course.getGrade().getCoefficient()*course.getCredit()* lowCourseGpa.getGpa();
            course1.put(course,totalGpa);
        } else if (course.getCredit() == 3){
            totalGpa = course.getGrade().getCoefficient()*course.getCredit()* mediumCourseGpa.getGpa();
            course1.put(course,totalGpa);
        } else {
            totalGpa = course.getGrade().getCoefficient()*course.getCredit()* highCourseGpa.getGpa();
            course1.put(course,totalGpa);
        }
        courses.put(course.getId(), course);
        return course1;
    }

    @PutMapping("/{id}")
    public Course putCourse(@PathVariable int id, @RequestBody Course course){
        return courses.replace(course.getId(), course);
    }

    @DeleteMapping("/{id}")
    public Course deleteCourse(@PathVariable int id){
        return courses.remove(id);
    }
}