package com.stroke.repository;

import com.stroke.model.ImagingVolume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagingVolumeRepository extends JpaRepository<ImagingVolume, String> {
}