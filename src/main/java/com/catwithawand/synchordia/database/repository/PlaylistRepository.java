package com.catwithawand.synchordia.database.repository;

import com.catwithawand.synchordia.database.entity.Playlist;
import com.catwithawand.synchordia.database.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

  List<Playlist> findByName(String name);

  List<Playlist> findByNameIgnoreCase(String name);

  List<Playlist> findByNameContainsIgnoreCase(String name);

  List<Playlist> findByDescriptionContainsIgnoreCase(String description);

  List<Playlist> findByTracksIn(Collection<Set<Track>> tracks);

}
