package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.model.Author;
import br.com.alura.literalura_api.model.Book;
import br.com.alura.literalura_api.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    public void searchAndDisplayBook(Scanner scanner) {
        System.out.print("Insira o nome do livro que você deseja procurar: ");
        String bookTitle = scanner.nextLine();
        
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            System.out.println("Título não pode ser vazio!");
            return;
        }
        
        try {
            Optional<Book> book = bookService.searchAndSaveBook(bookTitle.trim());
            
            if (book.isPresent()) {
                displayBook(book.get());
            } else {
                System.out.println("Livro não encontrado ou erro na busca.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar o livro: " + e.getMessage());
        }
    }

    public void listAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            
            if (books.isEmpty()) {
                System.out.println("Nenhum livro registrado no banco de dados.");
                return;
            }
            
            System.out.println("\n=== Livros Registrados ===");
            for (Book book : books) {
                displayBook(book);
            }
            System.out.println("Total de livros: " + books.size());
            
        } catch (Exception e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
        }
    }

    public void listAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();
            
            if (authors.isEmpty()) {
                System.out.println("Nenhum autor registrado no banco de dados.");
                return;
            }
            
            System.out.println("\n=== Autores Registrados ===");
            for (Author author : authors) {
                displayAuthor(author);
            }
            System.out.println("Total de autores: " + authors.size());
            
        } catch (Exception e) {
            System.out.println("Erro ao listar autores: " + e.getMessage());
        }
    }

    private void displayBook(Book book) {
        System.out.println("------Livro------");
        System.out.println("Título: " + book.getTitle());
        System.out.println("Autor: " + (book.getAuthor() != null ? book.getAuthor().getName() : "Desconhecido"));
        System.out.println("Idioma: " + book.getLanguage());
        System.out.println("Número de downloads: " + book.getDownloadCount());
        System.out.println("-----------------");
    }

    private void displayAuthor(Author author) {
        System.out.println("Autor: " + author.getName());
        System.out.println("Ano de nascimento: " + (author.getBirthYear() != null ? author.getBirthYear() : "Desconhecido"));
        System.out.println("Ano de falecimento: " + (author.getDeathYear() != null ? author.getDeathYear() : "Ainda vivo"));
        
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            List<String> bookTitles = author.getBooks().stream()
                    .map(Book::getTitle)
                    .toList();
            System.out.println("Livros: " + bookTitles);
        } else {
            System.out.println("Livros: Nenhum livro registrado");
        }
        System.out.println();
    }

    public void listAuthorsAliveInYear(Scanner scanner) {
        System.out.print("Insira o ano que deseja pesquisar: ");
        String yearInput = scanner.nextLine();
        
        if (yearInput == null || yearInput.trim().isEmpty()) {
            System.out.println("Ano não pode ser vazio!");
            return;
        }
        
        try {
            int year = Integer.parseInt(yearInput.trim());
            
            List<Author> authorsAlive = authorService.getAuthorsAliveInYear(year);
            
            if (authorsAlive.isEmpty()) {
                System.out.println("Nenhum autor encontrado vivo no ano " + year + ".");
                return;
            }
            
            System.out.println("\n=== Autores vivos em " + year + " ===");
            for (Author author : authorsAlive) {
                displayAuthor(author);
            }
            System.out.println("Total de autores vivos em " + year + ": " + authorsAlive.size());
            
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite um ano válido (apenas números).");
        } catch (Exception e) {
            System.out.println("Erro ao buscar autores: " + e.getMessage());
        }
    }

    public void listBooksByLanguage(String language) {
        if (!isValidLanguage(language)) {
            String availableCodes = Language.getAllLanguages().stream()
                    .map(Language::getCode)
                    .collect(Collectors.joining(", "));
            System.out.println("Idioma inválido! Use: " + availableCodes);
            return;
        }
        
        try {
            List<Book> booksByLanguage = bookService.getBooksByLanguage(language);
            
            if (booksByLanguage.isEmpty()) {
                System.out.println("Nenhum livro encontrado no idioma: " + getLanguageName(language));
                return;
            }
            
            displayBooksInLanguage(booksByLanguage, language);
            displayLanguageStatistics(booksByLanguage, language);
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar livros por idioma: " + e.getMessage());
        }
    }

    private boolean isValidLanguage(String language) {
        return Language.isValidCode(language);
    }

    private void displayBooksInLanguage(List<Book> books, String language) {
        System.out.println("\n=== Livros em " + getLanguageName(language) + " ===");
        for (Book book : books) {
            displayBook(book);
        }
    }

    private void displayLanguageStatistics(List<Book> books, String language) {
        int totalBooks = books.size();
        double totalDownloads = calculateTotalDownloads(books);
        double averageDownloads = calculateAverageDownloads(books);
        long uniqueAuthors = countUniqueAuthors(books);
        
        System.out.println("\n=== Estatísticas ===");
        System.out.println("Total de livros em " + getLanguageName(language) + ": " + totalBooks);
        System.out.println("Total de downloads: " + String.format("%.0f", totalDownloads));
        System.out.println("Média de downloads: " + String.format("%.1f", averageDownloads));
        System.out.println("Autores únicos: " + uniqueAuthors);
        
        displayMostDownloadedBook(books);
    }

    private double calculateTotalDownloads(List<Book> books) {
        return books.stream()
                .mapToDouble(Book::getDownloadCount)
                .sum();
    }

    private double calculateAverageDownloads(List<Book> books) {
        return books.stream()
                .mapToDouble(Book::getDownloadCount)
                .average()
                .orElse(0.0);
    }

    private long countUniqueAuthors(List<Book> books) {
        return books.stream()
                .filter(book -> book.getAuthor() != null)
                .map(book -> book.getAuthor().getName())
                .distinct()
                .count();
    }

    private void displayMostDownloadedBook(List<Book> books) {
        books.stream()
                .max((b1, b2) -> Double.compare(b1.getDownloadCount(), b2.getDownloadCount()))
                .ifPresent(book -> System.out.println("Livro mais baixado: " + book.getTitle() + 
                                                   " (" + book.getDownloadCount() + " downloads)"));
    }

    private String getLanguageName(String code) {
        Language language = Language.fromCode(code);
        return language != null ? language.getDisplayName() : code;
    }

    public List<Language> getAvailableLanguages() {
        return Language.getAllLanguages();
    }
}