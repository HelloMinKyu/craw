package com.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rahodongfree")
public class Free {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String link;
    private String imagelink;
    private String writedate;
    private String writer;
    private String content;

}
