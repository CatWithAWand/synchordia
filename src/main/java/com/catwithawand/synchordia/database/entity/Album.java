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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "album", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Album {

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "album_artist",
      joinColumns = {@JoinColumn(name = "album_id")},
      inverseJoinColumns = {@JoinColumn(name = "artist_id")}
  )
  Set<Artist> artists = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "albums", cascade = CascadeType.ALL)
  Set<Track> tracks = new LinkedHashSet<>();

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

  @Column(name = "type", nullable = false)
  private String type;

  @Column(name = "total_tracks", nullable = false)
  private Integer totalTracks;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "year", nullable = true)
  private Integer year;

}
