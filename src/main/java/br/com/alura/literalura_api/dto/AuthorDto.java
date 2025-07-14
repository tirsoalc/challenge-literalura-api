package br.com.alura.literalura_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDto(
    @JsonProperty("name") String name,
    @JsonAlias("birth_year") @JsonProperty("birth_year") Integer birthYear,
    @JsonAlias("death_year") @JsonProperty("death_year") Integer deathYear
) {
}