package com.catwithawand.synchordia.database.service;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class ArtistService {

  private ArtistRepository artistRepository;

  @Autowired
  public ArtistService(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  public Optional<Artist> getArtistById(long id) {
    return artistRepository.findById(id);
  }

  public Optional<Artist> getArtistByName(String name) {
    return Optional.ofNullable(artistRepository.findByName(name));
  }

  /**
   * Ignores case sensitivity.
   */
  public Optional<Artist> getArtistByNameContains(String name) {
    return Optional.ofNullable(artistRepository.findByNameContainsIgnoreCase(name));
  }

  public List<Artist> getAllArtists() {
    return artistRepository.findAll();
  }

  public List<Artist> getArtistsByNames(List<String> names) {
    return artistRepository.findByNameIn(names);
  }

  public List<Artist> getArtistsByNamesIgnoreCase(List<String> names) {
    return artistRepository.findByNameIgnoreCaseIn(names);
  }


  public List<Artist> getArtistsByAlbums(Collection<Set<Album>> albums) {
    return artistRepository.findByAlbumsIn(albums);
  }

  public List<Artist> getArtistsByTracks(Collection<Set<Track>> tracks) {
    return artistRepository.findByTracksIn(tracks);
  }

  public boolean existsById(long id) {
    return artistRepository.existsById(id);
  }

  public boolean existsByName(String name) {
    return artistRepository.existsByName(name);
  }

  public long getArtistCount() {
    return artistRepository.count();
  }

  public void saveArtist(Artist artist) {
    artistRepository.save(artist);
  }

  public void saveArtistAndFlush(Artist artist) {
    artistRepository.saveAndFlush(artist);
  }

  public void saveAllArtists(List<Artist> artists) {
    artistRepository.saveAll(artists);
  }

  public void saveAllArtistsAndFlush(List<Artist> artists) {
    artistRepository.saveAllAndFlush(artists);
  }

  public void deleteArtistById(long id) {
    artistRepository.deleteById(id);
  }

  public void deleteArtist(Artist artist) {
    artistRepository.delete(artist);
  }

  public void deleteAllArtists() {
    artistRepository.deleteAllInBatch();
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllArtistsByIds(List<Long> ids) {
    artistRepository.deleteAllByIdInBatch(ids);
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllArtists(List<Artist> artists) {
    artistRepository.deleteAllInBatch(artists);
  }

}
