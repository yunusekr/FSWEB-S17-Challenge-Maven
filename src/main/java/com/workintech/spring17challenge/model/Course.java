package com.workintech.spring17challenge.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Course {
    private int id;
    private String name;
    private int credit;
    private Grade grade;

    public Course(Integer id, String name, Integer credit, Grade grade) {
        if(id != null && credit != null){
            this.id = id;
            this.name = name;
            this.credit = credit;
            this.grade = grade;
        }
    }
}