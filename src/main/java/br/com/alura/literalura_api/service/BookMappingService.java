package br.com.alura.literalura_api.service;

import br.com.alura.literalura_api.dto.AuthorDto;
import br.com.alura.literalura_api.dto.BookDto;
import br.com.alura.literalura_api.model.Author;
import br.com.alura.literalura_api.model.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookMappingService {
    
    public Optional<Book> mapToBook(BookDto bookDto) {
        if (!isValidBook(bookDto)) {
            return Optional.empty();
        }
        
        Author author = createAuthorFromDto(bookDto);
        Book book = createBookFromDto(bookDto, author);
        
        return Optional.of(book);
    }
    
    public boolean isValidBook(BookDto bookDto) {
        return bookDto != null 
            && hasValidTitle(bookDto.title())
            && hasValidAuthors(bookDto.authors());
    }
    
    private boolean hasValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
    
    private boolean hasValidAuthors(java.util.List<AuthorDto> authors) {
        return authors != null 
            && !authors.isEmpty()
            && authors.get(0).name() != null
            && !authors.get(0).name().trim().isEmpty();
    }
    
    private Author createAuthorFromDto(BookDto bookDto) {
        AuthorDto firstAuthor = getFirstAuthor(bookDto);
        if (firstAuthor == null) {
            return null;
        }
        
        Author author = new Author();
        author.setName(firstAuthor.name());
        author.setBirthYear(firstAuthor.birthYear());
        author.setDeathYear(firstAuthor.deathYear());
        
        return author;
    }
    
    private Book createBookFromDto(BookDto bookDto, Author author) {
        Book book = new Book();
        book.setTitle(bookDto.title());
        book.setLanguage(getFirstLanguage(bookDto));
        book.setDownloadCount(getDownloadCount(bookDto));
        
        if (author != null) {
            book.setAuthor(author);
        }
        
        return book;
    }
    
    private AuthorDto getFirstAuthor(BookDto bookDto) {
        return bookDto.authors() != null && !bookDto.authors().isEmpty() 
            ? bookDto.authors().get(0) 
            : null;
    }
    
    private String getFirstLanguage(BookDto bookDto) {
        return bookDto.languages() != null && !bookDto.languages().isEmpty() 
            ? bookDto.languages().get(0) 
            : "unknown";
    }
    
    private Double getDownloadCount(BookDto bookDto) {
        return bookDto.downloadCount() != null ? bookDto.downloadCount() : 0.0;
    }
}