package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class MenuService {

    @Autowired
    private BookService bookService;

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

    private void displayBook(Book book) {
        System.out.println("------Livro------");
        System.out.println("Título: " + book.getTitle());
        System.out.println("Autor: " + (book.getAuthor() != null ? book.getAuthor().getName() : "Desconhecido"));
        System.out.println("Idioma: " + book.getLanguage());
        System.out.println("Número de downloads: " + book.getDownloadCount());
        System.out.println("-----------------");
    }
}