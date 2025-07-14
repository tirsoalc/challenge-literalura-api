package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.model.Author;
import br.com.alura.literalura_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    public Author findOrCreateAuthor(Author author) {
        if (author == null || author.getName() == null) {
            return null;
        }
        
        Optional<Author> existingAuthor = authorRepository.findByNameIgnoreCase(author.getName());
        
        if (existingAuthor.isPresent()) {
            return updateAuthorIfNeeded(existingAuthor.get(), author);
        }
        
        return authorRepository.save(author);
    }
    
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
    
    public List<Author> getAuthorsAliveInYear(int year) {
        return authorRepository.findAuthorsAliveInYear(year);
    }
    
    private Author updateAuthorIfNeeded(Author existingAuthor, Author newAuthor) {
        boolean needsUpdate = false;
        
        if (existingAuthor.getBirthYear() == null && newAuthor.getBirthYear() != null) {
            existingAuthor.setBirthYear(newAuthor.getBirthYear());
            needsUpdate = true;
        }
        
        if (existingAuthor.getDeathYear() == null && newAuthor.getDeathYear() != null) {
            existingAuthor.setDeathYear(newAuthor.getDeathYear());
            needsUpdate = true;
        }
        
        if (needsUpdate) {
            return authorRepository.save(existingAuthor);
        }
        
        return existingAuthor;
    }
}