package com.catwithawand.synchordia.database.repository;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Playlist;
import com.catwithawand.synchordia.database.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TrackRepository extends JpaRepository<Track, Long> {

  List<Track> findByImportedPathHash(String hash);

  Track findByFilePathHash(String hash);

  List<Track> findByTitle(String title);

  List<Track> findByTitleIgnoreCase(String title);

  List<Track> findByTitleContainsIgnoreCase(String title);

  List<Track> findByGenre(String genre);

  List<Track> findByYear(int year);

  List<Track> findByLiked(boolean liked);

  List<Track> findByArtist(Artist artist);

  List<Track> findByFeaturedArtistsIn(Collection<Set<Artist>> artists);

  List<Track> findByAlbumsIn(Collection<Set<Album>> albums);

  List<Track> findByAlbumsInOrderByTrackNumber(Collection<Set<Album>> albums);

  List<Track> findByPlaylistsIn(Collection<Set<Playlist>> playlists);

  List<Track> findAllByOrderByPlayCount();

  List<Track> findByPlayCountGreaterThanEqual(int playCount);

  List<Track> findByPlayCountLessThanEqual(int playCount);

  @Modifying
  @Query("update Track t set t.playCount = ?1 where t.id = ?2")
  void setPlayCount(int count, long id);

}
