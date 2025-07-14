package br.com.alura.literalura_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexResponse(
    @JsonProperty("results") List<BookDto> results
) {
}