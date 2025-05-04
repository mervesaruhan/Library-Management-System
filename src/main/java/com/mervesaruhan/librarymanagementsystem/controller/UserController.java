package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestByPatronDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "User CRUD işlemleri")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;


    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    @Operation(summary = "New user register by librarian")
    public ResponseEntity<RestResponse<UserDto>> registerUserByLibrarian(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(userService.registerUser(userSaveRequestDto)));
    }



    @PostMapping("/patronRegister")
    @Operation(summary = "New user register by patron")
    @PreAuthorize("hasAnyRole('LIBRARIAN','PATRON')")
    public ResponseEntity<RestResponse<UserDto>>  registerUserByPatron(@Valid @RequestBody UserSaveRequestByPatronDto patronRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(userService.registerUserByPatron(patronRequestDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get user by using id")
    public ResponseEntity<RestResponse<UserDto>> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(RestResponse.of(userService.getUserById(id)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public ResponseEntity<RestResponse<Page<UserDto>>> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(RestResponse.of(userService.findAllUsers(pageable)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update user register information")
    public ResponseEntity<RestResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUser(id, userUpdateRequestDto)));
    }


    @PreAuthorize("hasAnyRole('LIBRARIAN','PATRON')")
    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password")
    public ResponseEntity<RestResponse<UserDto>> updateUserPassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateRequestDto passwordUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserPassword(id, passwordUpdateDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<RestResponse<UserDto>> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequestDto roledUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserRole(id, roledUpdateDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/status")
    @Operation(summary = "Change user's active status")
    public ResponseEntity<RestResponse<UserDto>> updateUserActiveStatus(@PathVariable Long id, @RequestParam Boolean active){
        return ResponseEntity.ok(RestResponse.of(userService.updateUserActiveStatus(id, active)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(RestResponse.empty());
    }

}
