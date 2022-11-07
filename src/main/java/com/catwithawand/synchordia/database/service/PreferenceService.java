package com.catwithawand.synchordia.database.service;

import com.catwithawand.synchordia.database.entity.Preference;
import com.catwithawand.synchordia.database.repository.PreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PreferenceService {

  private PreferenceRepository preferenceRepository;

  @Autowired
  public PreferenceService(PreferenceRepository preferenceRepository) {
    this.preferenceRepository = preferenceRepository;
  }

  public Optional<Preference> getPreference(String key) {
    return Optional.ofNullable(preferenceRepository.findByKey(key));
  }

  public List<Preference> getPreferences(List<String> keys) {
    return preferenceRepository.findAllByKeyIn(keys);
  }

  public List<Preference> getAllPreferences() {
    return preferenceRepository.findAll();
  }

  public boolean exists(String key) {
    return preferenceRepository.findByKey(key) != null;
  }

  public void savePreference(Preference preference) {
    preferenceRepository.save(preference);
  }

  public void savePreferenceAndFlush(Preference preference) {
    preferenceRepository.saveAndFlush(preference);
  }

  public void saveAllPreferences(List<Preference> preferences) {
    preferenceRepository.saveAll(preferences);
  }

  public void saveAllPreferencesAndFlush(List<Preference> preferences) {
    preferenceRepository.saveAllAndFlush(preferences);
  }

  public void deletePreference(Preference preference) {
    preferenceRepository.delete(preference);
  }

  public void deleteAllPreferences() {
    preferenceRepository.deleteAllInBatch();
  }

  /**
   * Deletes the entities identified by the given ids using a single query. This kind of
   * operation leaves JPAs first level cache and the database out of sync. Consider
   * flushing the EntityManager before calling this method
   */
  public void deleteAllPreferences(List<Preference> preferences) {
    preferenceRepository.deleteAllInBatch(preferences);
  }

}
