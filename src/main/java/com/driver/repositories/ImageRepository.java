package com.driver.repositories;

import com.driver.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Modifying
    @Query("select * from image i where i.id =:id")
    Image findById(int id);
}
