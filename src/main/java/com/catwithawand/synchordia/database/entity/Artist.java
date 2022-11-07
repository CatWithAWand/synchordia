package com.catwithawand.synchordia.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "artist", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Artist {

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "artist", cascade = CascadeType.ALL)
  Set<Track> tracks = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "artists", cascade = CascadeType.ALL)
  Set<Album> albums = new LinkedHashSet<>();

  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(generator = "snowflake_generator")
  @GenericGenerator(
      name = "snowflake_generator",
      parameters = @Parameter(name = "epoch", value = "1420070400000"),
      strategy = "com.catwithawand.synchordia.database.generator.SnowflakeGenerator"
  )
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "image_url", nullable = true)
  private String imageUrl;

  @Column(name = "name", nullable = false)
  private String name;

}
