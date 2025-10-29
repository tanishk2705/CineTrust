package com.CineTrust.controller;

import com.CineTrust.dto.MovieDiscoveryResponse;
import com.CineTrust.entity.AvailabilityType;
import com.CineTrust.service.MovieDiscoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MovieDiscoveryController {
    private final MovieDiscoveryService movieDiscoveryService;

    // http://localhost:8080/api/v1/movies/available?region=IN&platform=Netflix
    @GetMapping("/movies/available")
    public ResponseEntity<List<MovieDiscoveryResponse>> getAvailableMovies(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Long platformId,
            @RequestParam(required = false) AvailabilityType type,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                movieDiscoveryService.getAvailableMovies(region, platformId, type, pageable).getContent()
        );
    }

    //  /api/platforms/{platformId}/movies?page=1&size=5&sort=title,asc
    @GetMapping("/platforms/{platformId}/movies")
    public ResponseEntity<List<MovieDiscoveryResponse>> getMoviesByPlatform(
            @PathVariable Long platformId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                movieDiscoveryService.getMoviesByPlatform(platformId, pageable).getContent()
        );
    }

    // /api/regions/{code}/movies?page=0&size=10&sort=releaseYear,desc
    @GetMapping("/regions/{code}/movies")
    public ResponseEntity<List<MovieDiscoveryResponse>> getMoviesByRegion(
            @PathVariable String code,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                movieDiscoveryService.getMoviesByRegion(code, pageable).getContent()
        );
    }
}
