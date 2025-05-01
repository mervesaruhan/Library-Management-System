package com.mervesaruhan.librarymanagementsystem.controller;


import com.mervesaruhan.librarymanagementsystem.model.dto.response.BookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import com.mervesaruhan.librarymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "User CRUD işlemleri")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "New user register")
    public ResponseEntity<RestResponse<UserDto>> registerUser(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(userService.registerUser(userSaveRequestDto)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by using id")
    public ResponseEntity<RestResponse<UserDto>> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(RestResponse.of(userService.getUserById(id)));
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public ResponseEntity<RestResponse<Page<UserDto>>> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(RestResponse.of(userService.findAllUsers(pageable)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user register information")
    public ResponseEntity<RestResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUser(id, userUpdateRequestDto)));
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password")
    public ResponseEntity<RestResponse<UserDto>> updateUserPassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateRequestDto passwordUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserPassword(id, passwordUpdateDto)));
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<RestResponse<UserDto>> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequestDto roledUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserRole(id, roledUpdateDto)));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Change user's active status")
    public ResponseEntity<RestResponse<UserDto>> updateUserActiveStatus(@PathVariable Long id, @RequestParam Boolean active){
        return ResponseEntity.ok(RestResponse.of(userService.updateUserActiveStatus(id, active)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(RestResponse.empty());
    }

}
