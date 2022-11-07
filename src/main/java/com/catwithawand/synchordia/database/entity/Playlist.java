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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "playlist", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Playlist {

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "playlists", cascade = CascadeType.ALL)
  Set<Track> tracks = new LinkedHashSet<>();

  @Setter(AccessLevel.NONE)
  @Id
  @GeneratedValue(generator = "snowflake_generator")
  @GenericGenerator(
      name = "snowflake-generator",
      parameters = @Parameter(name = "epoch", value = "1420070400000"),
      strategy = "com.catwithawand.synchordia.database.generator.SnowflakeGenerator"
  )
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @Column(name = "image_url", nullable = true)
  private String imageUrl;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = true)
  private String description;

}
