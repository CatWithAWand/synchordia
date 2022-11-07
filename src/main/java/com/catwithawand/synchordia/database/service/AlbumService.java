package com.catwithawand.synchordia.database.service;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Transactional
@Service
public class AlbumService {

  private AlbumRepository albumRepository;

  @Autowired
  public AlbumService(AlbumRepository albumRepository) {
    this.albumRepository = albumRepository;
  }

  public Optional<Album> getAlbumById(long id) {
    return albumRepository.findById(id);
  }

  public List<Album> getAllAlbums() {
    return albumRepository.findAll();
  }

  public List<Album> getAlbumsByType(String type) {
    return albumRepository.findByType(type);
  }

  public List<Album> getAlbumsByName(String name) {
    return albumRepository.findByName(name);
  }

  public List<Album> getAlbumsByNameIgnoreCase(String name) {
    return albumRepository.findByNameIgnoreCase(name);
  }

  /**
   * Ignores case sensitivity.
   */
  public List<Album> getAlbumsByNameContains(String name) {
    return albumRepository.findByNameContainsIgnoreCase(name);
  }

  public List<Album> getAlbumsByYear(int year) {
    return albumRepository.findByYear(year);
  }

  public List<Album> getAlbumsByArtist(Collection<Set<Artist>> artists) {
    return albumRepository.findByArtistsIn(artists);
  }

  public List<Album> getAlbumsByTrack(Collection<Set<Track>> tracks) {
    return albumRepository.findByTracksIn(tracks);
  }

  public long getAlbumCount() {
    return albumRepository.count();
  }

  public boolean existsById(long id) {
    return albumRepository.existsById(id);
  }

  public boolean existsByName(String name) {
    return albumRepository.findByName(name).size() > 0;
  }

  public void saveAlbum(Album album) {
    albumRepository.save(album);
  }

  public void saveAlbumAndFlush(Album album) {
    albumRepository.saveAndFlush(album);
  }

  public void saveAllAlbums(List<Album> albums) {
    albumRepository.saveAll(albums);
  }

  public void saveAllAlbumsAndFlush(List<Album> albums) {
    albumRepository.saveAllAndFlush(albums);
  }

  public void deleteAlbumById(long id) {
    albumRepository.deleteById(id);
  }

  public void deleteAlbum(Album album) {
    albumRepository.delete(album);
  }

  public void deleteAllAlbums() {
    albumRepository.deleteAllInBatch();
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllAlbumsByIds(List<Long> ids) {
    albumRepository.deleteAllByIdInBatch(ids);
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllAlbums(List<Album> albums) {
    albumRepository.deleteAllInBatch(albums);
  }

}
