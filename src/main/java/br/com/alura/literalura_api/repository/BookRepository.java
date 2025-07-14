package br.com.alura.literalura_api.repository;

import br.com.alura.literalura_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByLanguage(String language);

    Optional<Book> findByTitle(String title);
    
    Optional<Book> findByTitleIgnoreCase(String title);
    
    Optional<Book> findByTitleIgnoreCaseAndAuthor_NameIgnoreCase(String title, String authorName);
}
