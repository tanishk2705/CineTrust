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

    @GetMapping("/platforms/{platformId}/movies")
    public ResponseEntity<List<MovieDiscoveryResponse>> getMoviesByPlatform(
            @PathVariable Long platformId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                movieDiscoveryService.getMoviesByPlatform(platformId, pageable).getContent()
        );
    }

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
