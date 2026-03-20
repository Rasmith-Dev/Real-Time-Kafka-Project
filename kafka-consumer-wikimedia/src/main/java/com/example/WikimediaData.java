package com.example;

import jakarta.persistence.*;

@Entity
@Table(name = "wikimedia_recentchange")
public class WikimediaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String wikiEventData;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWikiEventData() { return wikiEventData; }
    public void setWikiEventData(String wikiEventData) { this.wikiEventData = wikiEventData; }
}