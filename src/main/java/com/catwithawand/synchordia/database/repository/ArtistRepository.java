package com.catwithawand.synchordia.database.repository;

import com.catwithawand.synchordia.database.entity.Album;
import com.catwithawand.synchordia.database.entity.Artist;
import com.catwithawand.synchordia.database.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

  Artist findByName(String name);

  Artist findByNameContainsIgnoreCase(String name);

  List<Artist> findByNameIgnoreCaseIn(List<String> names);

  List<Artist> findByNameIn(List<String> names);

  List<Artist> findByAlbumsIn(Collection<Set<Album>> albums);

  List<Artist> findByTracksIn(Collection<Set<Track>> tracks);

  boolean existsByName(String name);

}
