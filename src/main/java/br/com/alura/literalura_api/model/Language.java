package br.com.alura.literalura_api.model;

import java.util.Arrays;
import java.util.List;

public enum Language {
    SPANISH("es", "espanhol"),
    ENGLISH("en", "inglês"),
    FRENCH("fr", "francês"),
    PORTUGUESE("pt", "português");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<Language> getAllLanguages() {
        return Arrays.asList(Language.values());
    }

    public static Language fromCode(String code) {
        return Arrays.stream(Language.values())
                .filter(lang -> lang.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
}