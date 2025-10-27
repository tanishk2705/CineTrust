package com.CineTrust.dto;

import com.CineTrust.entity.AvailabilityType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDiscoveryResponse {
    private Long movieId;
    private String title;
    private String genre;
    private String language;
    private Float rating;
    private AvailabilityType availabilityType;
    private String platformName;
    private String regionCode;
    private String url;
}
