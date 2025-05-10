package com.mervesaruhan.librarymanagementsystem.controller;


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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "User CRUD Operations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    @Operation(summary = "New user register by librarian- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<UserDto>> createUser(@Valid @RequestBody UserSaveRequestDto userSaveRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.of(userService.registerUserByLibrarian(userSaveRequestDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get user by using id- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<UserDto>> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(RestResponse.of(userService.getUserById(id)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    @Operation(summary = "Get all users with pagination- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<Page<UserDto>>> getAllUsers(@Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(RestResponse.of(userService.findAllUsers(pageable)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update user register information- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<UserDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUser(id, userUpdateRequestDto)));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','PATRON')")
    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password- ROLE: LIBRARIAN / PATRON")
    public ResponseEntity<RestResponse<UserDto>> updateUserPassword(@PathVariable Long id, @Valid @RequestBody UserPasswordUpdateRequestDto passwordUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserPassword(id, passwordUpdateDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<UserDto>> updateUserRole(@PathVariable Long id, @Valid @RequestBody UserRoleUpdateRequestDto roledUpdateDto) {
        return ResponseEntity.ok(RestResponse.of(userService.updateUserRole(id, roledUpdateDto)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/status")
    @Operation(
            summary = "Evaluate and update user's active status based on borrowing rules.- ROLE: LIBRARIAN",
            description = "Automatically sets user to passive if rule violation is found."
    )
    public ResponseEntity<RestResponse<UserDto>> checkAndUpdateUserActiveStatus(@PathVariable Long id){
        return ResponseEntity.ok(RestResponse.of(userService.checkUserEligibilityAndUpdateStatus(id)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}/eligibility")
    @Operation(summary = "Change user eligibility.- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<UserDto>> updateUserActiveStatus(@PathVariable Long id,@RequestParam Boolean eligibility){
        return ResponseEntity.ok(RestResponse.of(userService.updateUserEligibility(id, eligibility)));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User- ROLE: LIBRARIAN")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(RestResponse.empty());
    }

}
