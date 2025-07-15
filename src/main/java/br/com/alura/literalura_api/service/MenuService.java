package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.model.Author;
import br.com.alura.literalura_api.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
}