package com.mervesaruhan.librarymanagementsystem.model.mapper;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowingDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import jakarta.persistence.MappedSuperclass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BorrowingMapper {

    Borrowing toEntity(BorrowingSaveRequestDto borrowingSaveRequestDto);

    @Mapping(target = "userFullName", expression = "java(borrowing.getUser().getName() + \\\" \\\" + borrowing.getUser().getSurname())" )
    @Mapping(target ="bookTitle", expression = "java(borrowing.getBook().getTitle())")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    BorrowingDto toBorrowingDto(Borrowing borrowing);

    List<BorrowingDto> toBorrowingDtoList(List<Borrowing> borrowings);
}
