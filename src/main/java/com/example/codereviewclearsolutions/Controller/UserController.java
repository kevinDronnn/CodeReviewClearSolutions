package com.example.codereviewclearsolutions.Controller;

import com.example.codereviewclearsolutions.Dto.UserDto;
import com.example.codereviewclearsolutions.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDto user) {
        try {
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/updateAllFieldsOfUser/{id}")
    public ResponseEntity<String> updateAllFieldsOfUser(@PathVariable int id, @RequestBody UserDto user) {
        try {
            userService.updateUser(id, user);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/updateUserFields/{id}")
    public ResponseEntity<String> updateUserFields(@PathVariable int id,
                                                   @RequestParam(required = false) String email,
                                                   @RequestParam(required = false) String firstName,
                                                   @RequestParam(required = false) String lastName,
                                                   @RequestParam(required = false) LocalDate birthDate,
                                                   @RequestParam(required = false) String address,
                                                   @RequestParam(required = false) String phoneNumber) {
        try {
            userService.updateUserFields(id, email, firstName, lastName, birthDate, address, phoneNumber);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/findUsersInDateRange")
    public ResponseEntity<List<UserDto>> findUsersInDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        try {
            List<UserDto> list = userService.findUserEntitiesByBirthDateBetween(from, to);
            return ResponseEntity.ok().body(list);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }





}
