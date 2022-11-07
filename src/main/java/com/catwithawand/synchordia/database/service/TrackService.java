package com.catwithawand.synchordia.database.service;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Playlist;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.repository.TrackRepository;
import com.catwithawand.synchordia.util.RandomUtils;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.apache.logging.log4j.util.Strings;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Transactional
@Service
public class TrackService {

  private TrackRepository trackRepository;
  private ArtistService artistService;

  @Autowired
  public TrackService(TrackRepository trackRepository, ArtistService artistService) {
    this.trackRepository = trackRepository;
    this.artistService = artistService;
  }

  private static Integer parseYear(String year) {
    Pattern pattern = Pattern.compile("\\d{4}");
    Matcher matcher = pattern.matcher(year);

    if (matcher.find()) {
      return Integer.parseInt(matcher.group(0));
    }

    return null;
  }

  public Optional<Track> getById(long id) {
    return trackRepository.findById(id);
  }

  public Optional<Track> getByFilePathHash(String path) {
    return Optional.ofNullable(trackRepository.findByFilePathHash(path));
  }

  public List<Track> getAll() {
    return trackRepository.findAll();
  }

  public List<Track> getByPathHash(String hash) {
    return trackRepository.findByImportedPathHash(hash);
  }

  public List<Track> getByTitle(String title) {
    return trackRepository.findByTitle(title);
  }

  public List<Track> getByTitleIgnoreCase(String title) {
    return trackRepository.findByTitleIgnoreCase(title);
  }

  /**
   * Ignores case sensitivity.
   */
  public List<Track> getByTitleContains(String title) {
    return trackRepository.findByTitleContainsIgnoreCase(title);
  }

  public List<Track> getByGenre(String genre) {
    return trackRepository.findByGenre(genre);
  }

  public List<Track> getByYear(int year) {
    return trackRepository.findByYear(year);
  }

  public List<Track> getByLiked(boolean liked) {
    return trackRepository.findByLiked(liked);
  }

  public List<Track> getByArtist(Artist artist) {
    return trackRepository.findByArtist(artist);
  }

  public List<Track> getByFeaturedArtists(Collection<Set<Artist>> artists) {
    return trackRepository.findByFeaturedArtistsIn(artists);
  }

  public List<Track> getByAlbums(Collection<Set<Album>> albums) {
    return trackRepository.findByAlbumsIn(albums);
  }

  public List<Track> getByAlbumsOrdered(Collection<Set<Album>> albums) {
    return trackRepository.findByAlbumsInOrderByTrackNumber(albums);
  }

  public List<Track> getByPlaylist(Collection<Set<Playlist>> playlists) {
    return trackRepository.findByPlaylistsIn(playlists);
  }

  public List<Track> getByPlayCountGreaterThanEqual(int playCount) {
    return trackRepository.findByPlayCountGreaterThanEqual(playCount);
  }

  public List<Track> getByPlayCountLessThanEqual(int playCount) {
    return trackRepository.findByPlayCountLessThanEqual(playCount);
  }

  public List<Track> getOrderedByPlayCount() {
    return trackRepository.findAllByOrderByPlayCount();
  }

  public void save(Track track) {
    trackRepository.save(track);
  }

  public void saveAndFlush(Track track) {
    trackRepository.saveAndFlush(track);
  }

  public void saveAll(List<Track> tracks) {
    trackRepository.saveAll(tracks);
  }

  public void saveAllAndFlush(List<Track> tracks) {
    trackRepository.saveAllAndFlush(tracks);
  }

  public void deleteById(long id) {
    trackRepository.deleteById(id);
  }

  public void delete(Track track) {
    trackRepository.delete(track);
  }

  public void deleteAll() {
    trackRepository.deleteAllInBatch();
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllByIds(List<Long> ids) {
    trackRepository.deleteAllByIdInBatch(ids);
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAll(List<Track> tracks) {
    trackRepository.deleteAllInBatch(tracks);
  }

  public void incrementPlayCount(Track track) {
    track.setPlayCount(track.getPlayCount() + 1);
    trackRepository.setPlayCount(track.getPlayCount(), track.getId());
  }

  @Async
  public CompletableFuture<Track> importTrack(File file, String importedPathHash) {
    log.debug("Importing file: " + file.getAbsolutePath());

    try {
      String filePathHash = Hashing.farmHashFingerprint64()
                                   .hashBytes(file.getAbsolutePath()
                                                  .getBytes())
                                   .toString();

      // If the track has already been imported, skip it
      // this can occur when importing from a parent directory
      // therefore we use the file path hash to check for uniqueness
      if (getByFilePathHash(filePathHash).isPresent()) {
        return CompletableFuture.completedFuture(null);
      }

      Track track = new Track();
      track.setImportedPathHash(importedPathHash);
      track.setFilePathHash(filePathHash);
      track.setFilePath(file.getAbsolutePath());
      track.setFileFormat(Files.getFileExtension(file.getAbsolutePath()));

      AudioFile audioFile = AudioFileIO.read(file);
      Tag tag = audioFile.getTag();
      AudioHeader audioHeader = audioFile.getAudioHeader();

      String fieldTitle = tag.getFirst(FieldKey.TITLE);
      if (Strings.isBlank(fieldTitle)) {
        track.setTitle(file.getName());
      } else {
        track.setTitle(fieldTitle);
      }

      String fieldArtist = tag.getFirst(FieldKey.ARTIST);
      if (Strings.isBlank(fieldArtist)) {
        fieldArtist = "Unknown Artist";
      }

      Artist originalArtist;
      Set<Artist> featuredArtists = new HashSet<>();
      List<String> artistNames = Arrays.stream(fieldArtist.split(","))
                                       .map(String::trim)
                                       .collect(Collectors.toList());

      List<Artist> existingArtists = artistService.getArtistsByNames(artistNames);
      Map<String, Artist> existingArtistsMap = existingArtists.stream()
                                                              .collect(Collectors.toMap(
                                                                  Artist::getName,
                                                                  artist -> artist
                                                              ));

      if (existingArtistsMap.containsKey(artistNames.get(0))) {
        originalArtist = existingArtistsMap.get(artistNames.get(0));
      } else {
        originalArtist = new Artist();
        originalArtist.setName(artistNames.get(0));
        artistNames.remove(0);
        existingArtists.remove(originalArtist);
        artistService.saveArtist(originalArtist);
      }

      for (String artistName : artistNames) {
        if (existingArtistsMap.containsKey(artistName)) {
          featuredArtists.add(existingArtistsMap.get(artistName));
        } else {
          Artist artist = new Artist();
          artist.setName(artistName);
          featuredArtists.add(artist);
          artistService.saveArtistAndFlush(artist);
        }
      }

      track.setArtist(originalArtist);
      track.setFeaturedArtists(featuredArtists);

      track.setGenre(tag.getFirst(FieldKey.GENRE));
      track.setTrackNumber(Ints.tryParse(tag.getFirst(FieldKey.TRACK)));
      track.setDiscNumber(Ints.tryParse(tag.getFirst(FieldKey.DISC_NO)));
      track.setExplicit(false);
      track.setYear(parseYear(tag.getFirst(FieldKey.YEAR)));
      track.setBitrate(audioHeader.getBitRateAsNumber());
      track.setDuration(audioHeader.getTrackLength());
      track.setLiked(false);
      track.setPlayCount(0);

      Artwork artwork = tag.getFirstArtwork();
      if (artwork != null) {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(artwork.getBinaryData()));

        String path = Paths.get("")
                           .toAbsolutePath() + "/thumbnails/"
            + RandomUtils.randomAlphanumeric(16) + ".jpg";

        Thumbnails.of(img)
                  .size(370, 370)
                  .keepAspectRatio(true)
                  .imageType(BufferedImage.TYPE_INT_ARGB)
                  .antialiasing(Antialiasing.ON)
                  .rendering(Rendering.QUALITY)
                  .alphaInterpolation(AlphaInterpolation.QUALITY)
                  .dithering(Dithering.DISABLE)
                  .scalingMode(ScalingMode.BICUBIC)
                  .outputQuality(1)
                  .outputFormat("jpg")
                  .toFile(path);

        track.setThumbnailUrl(path);
      }

      save(track);
      return CompletableFuture.completedFuture(track);
    } catch (Exception e) {
      log.error("Exception while importing " + file.getName() + " :", e);
      e.printStackTrace();
    }

    return CompletableFuture.completedFuture(null);
  }

}
