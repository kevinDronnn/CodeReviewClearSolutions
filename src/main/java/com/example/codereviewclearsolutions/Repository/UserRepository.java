package com.example.codereviewclearsolutions.Repository;

import com.example.codereviewclearsolutions.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findUserEntitiesByBirthDateBetween(LocalDate from, LocalDate to);

    @Query("UPDATE UserEntity u " +
            "SET " +
            "u.email = COALESCE(:email, u.email), " +
            "u.firstName = COALESCE(:firstName, u.firstName), " +
            "u.lastName = COALESCE(:lastName, u.lastName), " +
            "u.birthDate = COALESCE(:birthDate, u.birthDate), " +
            "u.address = COALESCE(:address, u.address), " +
            "u.phoneNumber = COALESCE(:phoneNumber, u.phoneNumber) " +
            "WHERE u.id = :id")
    void updateUserFields(@Param("id") int id,
                          @Param("email") String email,
                          @Param("firstName") String firstName,
                          @Param("lastName") String lastName,
                          @Param("birthDate") LocalDate birthDate,
                          @Param("address") String address,
                          @Param("phoneNumber") String phoneNumber);
}
