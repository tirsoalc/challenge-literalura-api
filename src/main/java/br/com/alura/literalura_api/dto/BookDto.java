package br.com.alura.literalura_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDto(
    @JsonProperty("title") String title,
    @JsonProperty("authors") List<AuthorDto> authors,
    @JsonProperty("languages") List<String> languages,
    @JsonAlias("download_count") @JsonProperty("download_count") Double downloadCount
) {
}