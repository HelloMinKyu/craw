package com.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rahodongnotice")

public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String link;
    private String writedate;
    private String writer;
    private String content;
}
