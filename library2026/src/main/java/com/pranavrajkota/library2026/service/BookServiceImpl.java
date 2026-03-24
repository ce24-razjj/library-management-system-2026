package com.pranavrajkota.library2026.service;

import com.pranavrajkota.library2026.dto.BookDto;
import com.pranavrajkota.library2026.dto.PageResponse;
import com.pranavrajkota.library2026.entity.Author;
import com.pranavrajkota.library2026.entity.Category;
import com.pranavrajkota.library2026.exception.BookNotFoundException;
import com.pranavrajkota.library2026.entity.Book;
import com.pranavrajkota.library2026.repository.BookRepository;
import com.pranavrajkota.library2026.repository.CategoryRepository;
import com.pranavrajkota.library2026.specification.BookSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import com.pranavrajkota.library2026.repository.AuthorRepository;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repo;
    private final ModelMapper modelMapper;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository repo, ModelMapper modelMapper, AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<BookDto> getBookList() {
        return repo.findAll().stream().map(bk -> modelMapper.map(bk, BookDto.class)).toList();
    }

    public BookDto addBook(BookDto dto) {

        Author author = authorRepository
                .findByNameIgnoreCase(dto.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(dto.getAuthorName());
                    return authorRepository.save(newAuthor);
                });

        List<Category> categories = Optional.ofNullable(dto.getCategories())
                .orElse(List.of())
                .stream()
                .map(name -> categoryRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(name);
                            return categoryRepository.save(newCategory);
                        }))
                .toList();

        Book book = new Book();
        book.setBookName(dto.getBookName());
        book.setAuthor(author);
        book.setCategories(categories);

        Book saved = repo.save(book);

        log.info("Book added with id {}", saved.getId());

        return modelMapper.map(saved, BookDto.class);
    }

    public void deleteBook(int id) {
        Book book = repo.findById(id)
                .orElseThrow(() -> new BookNotFoundException("book with id = {" + id + "}, not found"));

        repo.delete(book);
        log.info("Book deleted with id {}", id);
    }


    public BookDto updateBook(int id, BookDto newBook) {

        Book book = repo.findById(id).orElseThrow(() -> new BookNotFoundException("book with id {" + id + "} not found"));

        book.setBookName(newBook.getBookName());

        Author author = authorRepository.findByNameIgnoreCase(newBook.getAuthorName()).orElseGet(() -> {
            Author newAuthor = new Author();
            newAuthor.setName(newBook.getAuthorName());
            return authorRepository.save(newAuthor);
        });
        book.setAuthor(author);

        List<Category> categories = newBook.getCategories()
                .stream()
                .map(name -> categoryRepository.findByNameIgnoreCase(name).orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    return categoryRepository.save(newCategory);
                }))
                .toList();
        book.setCategories(categories);

        Book saved = repo.save(book);
        return modelMapper.map(saved, BookDto.class);

        // this method is also good but looks a little lengthy
//        Optional<Book> existing = repo.findById(id);
//
//        if(existing.isPresent()){
//            log.info("Book found with id {} _______________________", id);
//            Book book = existing.get();
////
////            modelMapper.map(newBook, Book.class); // this is of no use here but is not creating error as well
////            book.setBookName(newBook.getBookName());
////            book.setAuthor(newBook.getAuthor());
//            // or --->
//            modelMapper.map(newBook, book);
//            //
//
//            log.info("Book updated with id {} ___________", id);
//            repo.save(book);
//            return modelMapper.map(book, BookDto.class);
//        }
//
//        log.warn("Book not found with id {} ______________", id);
//        throw new BookNotFoundException("Book with id " + id + " not found");
////        return null; // avoiding the use of             return null;
    }

    public List<BookDto> getBooksByAuthor(String author) {
        log.info("search for book, via name of author - {} has been started__________", author);
        return repo.findByAuthor_NameIgnoreCase(author)
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }

    public BookDto tinyUpdate(int id, Map<String, Object> changes) {
        Optional<Book> bk = repo.findById(id);
        if (bk.isEmpty()) {
            log.warn("No book with id {} exists______________", id);
//            System.out.println("book with id => " + id + " doesn't exist, SO CAN'T PARTIALLY UPDATE");
//            return null; // avoiding the use of       return null;
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        Book book = bk.get();
        log.info("book with id {} found", id);
        changes.forEach((str, obj) -> {
            switch (str) {
                case "bookName":
                    book.setBookName((String) obj);
                    log.info("book name for id {} updated ___________", id);
                    break;
                case "author":
                    Author author = authorRepository.findByNameIgnoreCase((String) obj).orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName((String) obj);
                        return authorRepository.save(newAuthor);
                    });
                    book.setAuthor(author);
                    log.info("book author's name for id {} updated ______________", id);
                    break;
                default:
                    log.warn("no such field update possible_____________");
                    throw new IllegalArgumentException("Invalid request, such field doesn't exist");
            }
        });
        repo.save(book);
        log.info("id {} updated successfully____________", id);
        return modelMapper.map(book, BookDto.class);
    }

    public List<BookDto> getBookByName(String bookName) {
        // first
//        Book tempBook = new Book();
//        tempBook.setBookName(bookName);
//        Example<Book> eg = Example.of(tempBook);
//        List<Book> temp = repo.findBy(eg, q -> q.all());
//        if(temp.isEmpty()){
//            System.out.println("NO SUCH BOOK EXIST _-_-_-_-_ ");
//        }
//        return temp;


//  // // second optimisation
        return repo.findByBookNameIgnoreCase(bookName)
                .stream()
                .map(bk -> modelMapper.map(bk, BookDto.class))
                .toList();


        // third optimisation
//        return repo.findAll()
//                .stream()
//                .filter(bk -> bk.getBookName().equals(bookName))
//                .map(bk -> modelMapper.map(bk, BookDto.class))
//                .toList();
    }

    @Override
    public PageResponse<BookDto> getBooks(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<Book> bookPage = repo.findAll(pageable);

        List<BookDto> content = bookPage.getContent()
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();

        PageResponse<BookDto> response = new PageResponse<>();

        response.setContent(content);
        response.setPage(bookPage.getNumber());
        response.setSize(bookPage.getSize());
        response.setTotalElements(bookPage.getTotalElements());
        response.setTotalPages(bookPage.getTotalPages());

        return response;
    }

    public List<BookDto> filterBooks(String author, String category, String genre) {
        Specification<Book> specification = Specification.where(null);
        if (author != null) {
            specification = specification.and(BookSpecification.hasAuthor(author));
        }
        if (category != null)
            specification = specification.and(BookSpecification.hasCategory(category));

        if(genre != null){
            specification = specification.and(BookSpecification.hasGenre(genre));
        }

        return repo.findAll(specification)
                .stream()
                .map(bk -> modelMapper.map(bk, BookDto.class))
                .toList();
    }

}
