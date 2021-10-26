package com.i13n.AHP.resource;

import javax.persistence.*;

@Entity
@Table(name = "results")
public class ResultsResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String winner;

//    private
}
