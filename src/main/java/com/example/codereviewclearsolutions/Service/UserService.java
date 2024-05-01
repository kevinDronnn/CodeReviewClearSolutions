package com.example.codereviewclearsolutions.Service;

import com.example.codereviewclearsolutions.Dto.UserDto;
import com.example.codereviewclearsolutions.Entity.UserEntity;
import com.example.codereviewclearsolutions.Mapper.UserMapper;
import com.example.codereviewclearsolutions.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Value("${user.age}")
    int accessAge;

    @Transactional
    public void save(UserDto user) {
        validateUser(user);

        LocalDate current = LocalDate.now();
        int age = current.minusYears(18).compareTo(user.getBirthDate());

        if (age < 0) {
            throw new ArithmeticException("You must be 18 years old or older");
        }

        userRepository.save(UserMapper.INSTANCE.userDtoToUserEntity(user));
    }

    @Transactional
    public void updateUser(int id, UserDto user) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isPresent()) {
            UserEntity updatedUserEntity = UserMapper.INSTANCE.userDtoToUserEntity(user);
            updatedUserEntity.setId(id);
            userRepository.save(updatedUserEntity);
        } else {
            throw new NoSuchElementException("User with id " + id + " not found");
        }

    }


    public void deleteUser(int id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
    }


    public List<UserDto> findUserEntitiesByBirthDateBetween(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range: to date must be after from date");
        }

        List<UserEntity> userEntityList = userRepository.findUserEntitiesByBirthDateBetween(from, to);

        if (!userEntityList.isEmpty()) {
            return UserMapper.INSTANCE.userEntityToUserDto(userEntityList);
        } else {
            throw new NoSuchElementException("There are no users in this date range");
        }
    }

    public void updateUserFields(int id,
                                 String email,
                                 String firstName,
                                 String lastName,
                                 LocalDate birthDate,
                                 String address,
                                 String phoneNumber) {

        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if (userEntityOptional.isPresent()) {
            userRepository.save(checkAndSetValues(userEntityOptional.get(),userEntityOptional.get().getId(),email, firstName ,lastName ,birthDate, address , phoneNumber));
        } else {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
    }

    private void validateUser(UserDto user) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<UserDto> violation : violations) {
                errorMessage.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException(errorMessage.toString().trim());
        }
    }

    private UserEntity checkAndSetValues(UserEntity user, int id,
                                         String email,
                                         String firstName,
                                         String lastName,
                                         LocalDate birthDate,
                                         String address,
                                         String phoneNumber){

        user.setId(id);
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        if (firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            user.setLastName(lastName);
        }
        if (birthDate != null) {
            user.setBirthDate(birthDate);
        }
        if (address != null && !address.isEmpty()) {
            user.setAddress(address);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }

        return user;
    }
}
