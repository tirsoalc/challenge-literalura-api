package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.dto.BookDto;
import br.com.alura.literalura_api.dto.GutendexResponse;
import br.com.alura.literalura_api.model.Author;
import br.com.alura.literalura_api.model.Book;
import br.com.alura.literalura_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    
    @Autowired
    private GutendexService gutendexService;
    
    @Autowired
    private BookMappingService bookMappingService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private BookRepository bookRepository;
    
    public Optional<Book> searchAndSaveBook(String title) {
        Optional<GutendexResponse> response = gutendexService.searchBooks(title);
        
        if (response.isEmpty() || response.get().results().isEmpty()) {
            return Optional.empty();
        }
        
        BookDto firstBookDto = response.get().results().getFirst();
        
        if (!bookMappingService.isValidBook(firstBookDto)) {
            return Optional.empty();
        }
        
        Optional<Book> existingBook = findExistingBook(firstBookDto);
        if (existingBook.isPresent()) {
            return Optional.of(updateExistingBookIfNeeded(existingBook.get(), firstBookDto));
        }
        
        Optional<Book> mappedBook = bookMappingService.mapToBook(firstBookDto);
        
        if (mappedBook.isEmpty()) {
            return Optional.empty();
        }
        
        Book book = mappedBook.get();
        
        if (book.getAuthor() != null) {
            Author managedAuthor = authorService.findOrCreateAuthor(book.getAuthor());
            book.setAuthor(managedAuthor);
        }
        
        return Optional.of(bookRepository.save(book));
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }
    
    private Optional<Book> findExistingBook(BookDto bookDto) {
        String title = bookDto.title();
        String authorName = bookDto.authors() != null && !bookDto.authors().isEmpty() 
            ? bookDto.authors().get(0).name() 
            : null;
            
        if (authorName == null) {
            return bookRepository.findByTitleIgnoreCase(title);
        }
        
        return bookRepository.findByTitleIgnoreCaseAndAuthor_NameIgnoreCase(title, authorName);
    }
    
    private Book updateExistingBookIfNeeded(Book existingBook, BookDto newBookDto) {
        boolean bookUpdated = false;
        
        if (existingBook.getAuthor() != null && newBookDto.authors() != null && !newBookDto.authors().isEmpty()) {
            Author newAuthorData = new Author();
            newAuthorData.setName(newBookDto.authors().get(0).name());
            newAuthorData.setBirthYear(newBookDto.authors().get(0).birthYear());
            newAuthorData.setDeathYear(newBookDto.authors().get(0).deathYear());
            
            Author updatedAuthor = authorService.findOrCreateAuthor(newAuthorData);
            
            if (!updatedAuthor.equals(existingBook.getAuthor())) {
                existingBook.setAuthor(updatedAuthor);
                bookUpdated = true;
            }
        }
        
        Double newDownloadCount = newBookDto.downloadCount();
        if (newDownloadCount != null && !newDownloadCount.equals(existingBook.getDownloadCount())) {
            existingBook.setDownloadCount(newDownloadCount);
            bookUpdated = true;
        }
        
        if (bookUpdated) {
            return bookRepository.save(existingBook);
        }
        
        return existingBook;
    }
}