package com.mervesaruhan.librarymanagementsystem.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum BookSearchField {
    @Schema(description = "Kitap başlığına göre göre arama yapmak için seçiniz.")
    TITLE,

    @Schema(description = "Yazara göre göre arama yapmak için seçiniz.")
    AUTHOR,
    @Schema(description = "ISBN'ye göre göre arama yapmak için seçiniz.")
    ISBN,
    @Schema(description = "Kitap türlerine göre arama yapmak için seçiniz.")
    GENRE;
}