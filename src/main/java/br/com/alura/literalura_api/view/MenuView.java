package br.com.alura.literalura_api.view;

import br.com.alura.literalura_api.model.Language;
import br.com.alura.literalura_api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MenuView {

    @Autowired
    private MenuService menuService;

    private Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        int option = -1;

        while (option != 0) {
            String menu = """
                    
                    Escolha o número da sua opção
                    1- buscar livro pelo título
                    2- listar livros registrados
                    3- listar autores registrados
                    4- listar autores vivos em um determinado ano
                    5- listar livros em um determinado idioma
                    0- sair
                    """;

            System.out.println(menu);
            
            try {
                option = scanner.nextInt();
                scanner.nextLine();
                
                switch (option) {
                    case 1:
                        menuService.searchAndDisplayBook(scanner);
                        break;
                    case 2:
                        menuService.listAllBooks();
                        break;
                    case 3:
                        menuService.listAllAuthors();
                        break;
                    case 4:
                        menuService.listAuthorsAliveInYear(scanner);
                        break;
                    case 5:
                        showLanguageMenu();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: Digite um número válido!");
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    private void showLanguageMenu() {
        System.out.println("Insira o idioma para realizar a busca:");
        
        for (Language language : menuService.getAvailableLanguages()) {
            System.out.println(language.getCode() + " - " + language.getDisplayName());
        }
        
        System.out.print("Digite o código do idioma: ");
        String languageInput = scanner.nextLine();
        
        if (languageInput == null || languageInput.trim().isEmpty()) {
            System.out.println("Idioma não pode ser vazio!");
            return;
        }
        
        menuService.listBooksByLanguage(languageInput.trim().toLowerCase());
    }
}