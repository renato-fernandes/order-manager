package org.aubay.challenge.repository;

import org.aubay.challenge.entity.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findByName(String name);
}
