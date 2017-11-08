package com.github.liurui.repositories;

import com.github.liurui.App;
import com.github.liurui.entities.Book;
import org.elasticsearch.common.unit.DistanceUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.elasticsearch.index.query.QueryBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testUpsert() {
        Book book = new Book();
        book.setId("3");
        book.setAge(10);
//        book.setContent("hello1111111111");
//        book.setLocation(new Float[]{-12f, 34f});
//        book.setCreateDate(new Date());
        bookRepository.save(book);

        book = bookRepository.findById("3").get();

        System.out.println(book);
    }


    @Test
    public void testTextSearch() {
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchAllQuery())
//                .withFilter( boolQuery().must(existsQuery("中国")))
//                .withPageable(PageRequest.of(0,10))
//                .build();


        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("content", "中国"))
                .build();

        Page<Book> books = bookRepository.search(searchQuery);

        for (Book book : books.getContent()) {
            System.out.println(book);
        }
    }

    @Test
    public void testGeoSearch() {
//        Book book = new Book();
//        book.setLocation(new Float[]{  });
//
//        bookRepository.save(book);
//        System.out.println(book.getId());

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withFilter(geoDistanceQuery("location")
                        .distance(100, DistanceUnit.METERS)
                        .point(41.12f, -71.34f))
                .build();

        Page<Book> books = bookRepository.search(searchQuery);

        for (Book book : books.getContent()) {
            System.out.println(book);
        }
    }

    @Test
    public void testDate() {
//        Book book = new Book();
//        book.setCreateDate( new Date());
//
//        bookRepository.save(book);
//        System.out.println(book.getId());

//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchAllQuery())
//                .withFilter(rangeQuery("createDate")
//                .gte(System.currentTimeMillis() - 15 * 60 * 1000))
//                .build();
//
//        Page<Book> books = bookRepository.search(searchQuery);
//
//        for (Book book : books.getContent()) {
//            System.out.println(book);
//        }
    }
}