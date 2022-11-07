package com.catwithawand.synchordia.database.repository;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AlbumRepository extends JpaRepository<Album, Long> {

  List<Album> findByType(String type);

  List<Album> findByName(String name);

  List<Album> findByNameIgnoreCase(String name);

  List<Album> findByNameContainsIgnoreCase(String name);

  List<Album> findByYear(Integer year);

  List<Album> findByArtistsIn(Collection<Set<Artist>> artists);

  List<Album> findByTracksIn(Collection<Set<Track>> tracks);

}
