package com.github.liurui.repositories;


import com.github.liurui.entities.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

}
