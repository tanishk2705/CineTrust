package com.CineTrust.service;

import com.CineTrust.dto.MovieDiscoveryResponse;
import com.CineTrust.entity.*;
import com.CineTrust.repository.MovieAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieDiscoveryServiceTest {

    @Mock
    private MovieAvailabilityRepository movieAvailabilityRepository;

    @InjectMocks
    private MovieDiscoveryService movieDiscoveryService;

    private Movie movie;
    private Platform platform;
    private Region region;
    private MovieAvailability availability;
    private Pageable pageable;
    private Page<MovieAvailability> page;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setTitle("Inception");
        movie.setGenre("Sci-Fi");
        movie.setLanguage("English");
        movie.setRating(9.0f);
        movie.setApproved(true);

        platform = new Platform();
        platform.setName("Netflix");

        region = new Region();
        region.setName("North America");
        region.setCode("NA");

        availability = MovieAvailability.builder()
                .movie(movie)
                .platform(platform)
                .region(region)
                .availabilityType(AvailabilityType.STREAMING)
                .url("https://www.netflix.com/inception")
                .build();

        pageable = PageRequest.of(0, 10, Sort.by("movie.title").ascending());
        page = new PageImpl<>(List.of(availability));
    }


    @Test
    void testGetAvailableMovies_Success() {
        when(movieAvailabilityRepository.findAvailableMovies("NA", 1L, AvailabilityType.STREAMING, pageable))
                .thenReturn(page);

        Page<MovieDiscoveryResponse> response = movieDiscoveryService.getAvailableMovies("NA", 1L, AvailabilityType.STREAMING, pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        MovieDiscoveryResponse dto = response.getContent().get(0);
        assertEquals("Inception", dto.getTitle());
        assertEquals("Netflix", dto.getPlatformName());
        assertEquals("NA", dto.getRegionCode());
        verify(movieAvailabilityRepository).findAvailableMovies("NA", 1L, AvailabilityType.STREAMING, pageable);
    }


    @Test
    void testGetMoviesByPlatform_Success() {
        when(movieAvailabilityRepository.findByPlatform_IdAndMovie_ApprovedTrue(1L, pageable))
                .thenReturn(page);

        Page<MovieDiscoveryResponse> response = movieDiscoveryService.getMoviesByPlatform(1L, pageable);

        assertEquals(1, response.getContent().size());
        assertEquals("Inception", response.getContent().get(0).getTitle());
        verify(movieAvailabilityRepository).findByPlatform_IdAndMovie_ApprovedTrue(1L, pageable);
    }


    @Test
    void testGetMoviesByRegion_Success() {
        when(movieAvailabilityRepository.findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue("NA", pageable))
                .thenReturn(page);

        Page<MovieDiscoveryResponse> response = movieDiscoveryService.getMoviesByRegion("NA", pageable);

        assertEquals(1, response.getContent().size());
        MovieDiscoveryResponse dto = response.getContent().get(0);
        assertEquals("Inception", dto.getTitle());
        assertEquals("Netflix", dto.getPlatformName());
        verify(movieAvailabilityRepository).findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue("NA", pageable);
    }


    @Test
    void testEmptyResults_ReturnsEmptyPage() {
        Page<MovieAvailability> emptyPage = new PageImpl<>(List.of());

        when(movieAvailabilityRepository.findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue("ASIA", pageable))
                .thenReturn(emptyPage);

        Page<MovieDiscoveryResponse> response = movieDiscoveryService.getMoviesByRegion("ASIA", pageable);

        assertTrue(response.isEmpty());
        verify(movieAvailabilityRepository).findByRegion_CodeIgnoreCaseAndMovie_ApprovedTrue("ASIA", pageable);
    }
}
