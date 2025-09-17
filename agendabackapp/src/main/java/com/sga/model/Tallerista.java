package com.sga.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TALLERISTA")
public class Tallerista extends Usuario { }
