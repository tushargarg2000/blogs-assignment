package com.driver.repositories;

import com.driver.models.Blog;
import com.driver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying
    @Query("update User u set u.username = :#{#user.username}, " +
            "u.password = :#{#user.password}, " +
            "u.firstName = :#{#user.firstName} ," +
            "u.lastName = :#{#user.lastName} ")
    void update(User user);
    @Modifying
    @Query(value = "select * from user u where u.username =:username",nativeQuery = true)
    User findByUsername(String username);

    @Modifying
    @Query(value = "select * from user u where u.id =:id",nativeQuery = true)
    User findById(int id);
}
