package com.catwithawand.synchordia.database.repository;

import com.catwithawand.synchordia.database.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

  Preference findByKey(String key);

  List<Preference> findAllByKeyIn(List<String> keys);

}
