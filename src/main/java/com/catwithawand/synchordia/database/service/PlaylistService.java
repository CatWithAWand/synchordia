package com.catwithawand.synchordia.database.service;

import com.catwithawand.synchordia.database.entity.Playlist;
import com.catwithawand.synchordia.database.entity.Track;
import com.catwithawand.synchordia.database.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class PlaylistService {

  private PlaylistRepository playlistRepository;

  @Autowired
  public PlaylistService(PlaylistRepository playlistRepository) {
    this.playlistRepository = playlistRepository;
  }

  public Optional<Playlist> getPlaylistById(long id) {
    return playlistRepository.findById(id);
  }

  public List<Playlist> getAllPlaylists() {
    return playlistRepository.findAll();
  }

  public List<Playlist> getPlaylistsByName(String name) {
    return playlistRepository.findByName(name);
  }

  public List<Playlist> getPlaylistsByNameIgnoreCase(String name) {
    return playlistRepository.findByNameIgnoreCase(name);
  }

  /**
   * Ignores case sensitivity.
   */
  public List<Playlist> getPlaylistsByNameContains(String name) {
    return playlistRepository.findByNameContainsIgnoreCase(name);
  }

  /**
   * Ignores case sensitivity.
   */
  public List<Playlist> getPlaylistsByDescriptionContains(String description) {
    return playlistRepository.findByDescriptionContainsIgnoreCase(description);
  }

  public List<Playlist> getPlaylistsByTracks(Collection<Set<Track>> tracks) {
    return playlistRepository.findByTracksIn(tracks);
  }

  public boolean existsById(long id) {
    return playlistRepository.existsById(id);
  }

  public long getPlaylistCount() {
    return playlistRepository.count();
  }

  public void savePlaylist(Playlist playlist) {
    playlistRepository.save(playlist);
  }

  public void savePlaylistAndFlush(Playlist playlist) {
    playlistRepository.saveAndFlush(playlist);
  }

  public void saveAllPlaylists(List<Playlist> playlists) {
    playlistRepository.saveAll(playlists);
  }

  public void saveAllPlaylistsAndFlush(List<Playlist> playlists) {
    playlistRepository.saveAllAndFlush(playlists);
  }

  public void deletePlaylistById(long id) {
    playlistRepository.deleteById(id);
  }

  public void deletePlaylist(Playlist playlist) {
    playlistRepository.delete(playlist);
  }

  public void deleteAllPlaylists() {
    playlistRepository.deleteAllInBatch();
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllPlaylistsByIds(List<Long> ids) {
    playlistRepository.deleteAllByIdInBatch(ids);
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllPlaylists(List<Playlist> playlists) {
    playlistRepository.deleteAllInBatch(playlists);
  }

}
