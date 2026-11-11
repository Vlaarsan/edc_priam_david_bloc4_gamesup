package com.gamesup.backend.repository;

import com.gamesup.backend.entity.PurchaseLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, Long> {
}
