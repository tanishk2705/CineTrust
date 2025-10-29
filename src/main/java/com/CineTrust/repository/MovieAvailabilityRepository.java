package com.CineTrust.repository;


import com.CineTrust.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieAvailabilityRepository extends JpaRepository<MovieAvailability, Long> {
    boolean existsByMovieAndPlatformAndRegion(Movie movie, Platform platform, Region region);

    @Query("""
        SELECT ma FROM MovieAvailability ma
        WHERE (:regionCode IS NULL OR LOWER(ma.region.code) = LOWER(:regionCode))
          AND (:platformId IS NULL OR ma.platform.id = :platformId)
          AND (:availabilityType IS NULL OR ma.availabilityType = :availabilityType)
          AND ma.movie.approved = true
    """)
    Page<MovieAvailability> findAvailableMovies(
            @Param("regionCode") String regionCode,
            @Param("platformId") Long platformId,
            @Param("availabilityType") AvailabilityType availabilityType,
            Pageable pageable
    );

    Page<MovieAvailability> findByPlatform_IdAndMovie_ApprovedTrue(Long platformId, Pageable pageable);

    Page<MovieAvailability> findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue(String regionCode, Pageable pageable);
}
