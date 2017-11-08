package com.github.liurui.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Document(indexName = "book", type = "mybook")
public class Book {

    @Id
    private String id;

    @Field(type = FieldType.text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;


    @GeoPointField
    private Float[] location;


    @Field(type = FieldType.Date,
            store = true)
    private Date createDate;

    @Field(type = FieldType.Integer)
    private Integer age;

    public Book() {

    }

    public Book(String content) {
        this.content = content;
    }

    public Book(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float[] getLocation() {
        return location;
    }

    public void setLocation(Float[] location) {
        this.location = location;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", location=" + Arrays.toString(location) +
                ", createDate=" + createDate +
                ", age=" + age +
                '}';
    }
}
