package com.CineTrust.service;

import com.CineTrust.dto.MovieDiscoveryResponse;
import com.CineTrust.entity.AvailabilityType;
import com.CineTrust.entity.MovieAvailability;
import com.CineTrust.repository.MovieAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieDiscoveryService {
    private final MovieAvailabilityRepository movieAvailabilityRepository;

    public Page<MovieDiscoveryResponse> getAvailableMovies(String regionCode, Long platformId, AvailabilityType type, Pageable pageable) {
        return movieAvailabilityRepository.findAvailableMovies(regionCode, platformId, type, pageable)
                .map(this::mapToResponse);
    }


    public Page<MovieDiscoveryResponse> getMoviesByPlatform(Long platformId, Pageable pageable) {
        return movieAvailabilityRepository.findByPlatform_IdAndMovie_ApprovedTrue(platformId, pageable)
                .map(this::mapToResponse);
    }


    public Page<MovieDiscoveryResponse> getMoviesByRegion(String regionCode, Pageable pageable) {
        return movieAvailabilityRepository.findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue(regionCode, pageable)
                .map(this::mapToResponse);
    }

    private MovieDiscoveryResponse mapToResponse(MovieAvailability ma) {
        return MovieDiscoveryResponse.builder()
                .movieId(ma.getMovie().getId())
                .title(ma.getMovie().getTitle())
                .genre(ma.getMovie().getGenre())
                .language(ma.getMovie().getLanguage())
                .rating(ma.getMovie().getRating())
                .availabilityType(ma.getAvailabilityType())
                .platformName(ma.getPlatform().getName())
                .regionCode(ma.getRegion().getCode())
                .url(ma.getUrl())
                .build();
    }
}
