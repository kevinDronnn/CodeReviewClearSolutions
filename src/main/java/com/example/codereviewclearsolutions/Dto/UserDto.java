package com.example.codereviewclearsolutions.Dto;


import java.time.LocalDate;
import java.util.Objects;

public class UserDto {
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

    public UserDto() {
    }

    public UserDto(String email, String firstName, String lastName, LocalDate birthDate, String address, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return email.equals(userDto.email) && firstName.equals(userDto.firstName) && lastName.equals(userDto.lastName) && birthDate.equals(userDto.birthDate) && Objects.equals(address, userDto.address) && Objects.equals(phoneNumber, userDto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, birthDate, address, phoneNumber);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
