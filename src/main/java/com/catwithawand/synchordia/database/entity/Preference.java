package com.catwithawand.synchordia.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "preference")
public class Preference {

  @Id
  @Column(name = "key", nullable = false)
  private String key;

  @Column(name = "value", nullable = true)
  private String value;

}
