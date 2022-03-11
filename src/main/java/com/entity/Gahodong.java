package com.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "gahodong")

public class Gahodong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String link;
    private String writedate;
    private String imagelink;
    private String writer;
    private String content;
    private String type;
}
