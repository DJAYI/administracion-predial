package com.agmdesarrollos.geopix.security.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeResponseDTO {
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
}
