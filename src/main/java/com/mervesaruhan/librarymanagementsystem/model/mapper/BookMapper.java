package com.mervesaruhan.librarymanagementsystem.model.mapper;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.BookUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    Book toBookEntity(BookSaveRequestDto bookSaveRequestDto);

    @Mapping(target = "available", expression = "java(book.getInventoryCount() != null && book.getInventoryCount() > 0)")
    BookDto toBookDto(Book book);

    List<BookDto> toBookDtoList(List<Book> books);

}
