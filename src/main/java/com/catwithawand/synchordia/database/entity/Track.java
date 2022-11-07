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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "track", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Track {

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "track_artist",
      joinColumns = {@JoinColumn(name = "track_id")},
      inverseJoinColumns = {@JoinColumn(name = "artist_id")}
  )
  Artist artist;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "track_featured_artist",
      joinColumns = {@JoinColumn(name = "track_id")},
      inverseJoinColumns = {@JoinColumn(name = "artist_id")}
  )
  Set<Artist> featuredArtists = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "track_album",
      joinColumns = {@JoinColumn(name = "track_id")},
      inverseJoinColumns = {@JoinColumn(name = "album_id")}
  )
  Set<Album> albums = new LinkedHashSet<>();

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "track_playlist", joinColumns = {
      @JoinColumn(name = "track_id")
  }, inverseJoinColumns = {@JoinColumn(name = "playlist_id")}
  )
  Set<Playlist> playlists = new LinkedHashSet<>();

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

  @Column(name = "imported_path_hash", nullable = false)
  private String importedPathHash;

  @Column(name = "file_path_hash", nullable = false)
  private String filePathHash;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  @Column(name = "file_format", nullable = false)
  private String fileFormat;

  @Column(name = "thumbnail_url", nullable = true)
  private String thumbnailUrl;

  @Column(name = "title", nullable = true)
  private String title;

  @Column(name = "track_number", nullable = true)
  private Integer trackNumber;

  @Column(name = "disc_number", nullable = true)
  private Integer discNumber;

  @Column(name = "genre", nullable = true)
  private String genre;

  @Column(name = "explicit", nullable = true)
  private Boolean explicit;

  @Column(name = "year", nullable = true)
  private Integer year;

  @Column(name = "bitrate", nullable = false)
  private Long bitrate;

  @Column(name = "duration", nullable = false)
  private Integer duration;

  @Column(name = "liked", nullable = false)
  private Boolean liked;

  @Column(name = "play_count", nullable = false)
  private Integer playCount;

  public final Set<Artist> getAllArtists() {
    Set<Artist> allArtists = new LinkedHashSet<>();
    allArtists.add(artist);
    allArtists.addAll(featuredArtists);

    return allArtists;
  }

}
