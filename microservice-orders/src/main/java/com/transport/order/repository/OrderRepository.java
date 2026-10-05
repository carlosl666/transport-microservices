package com.transport.order.repository;

import com.transport.order.entity.Order;
import com.transport.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
            SELECT o FROM Order o
            WHERE (:status IS NULL OR o.status = :status)
              AND (CAST(:start AS timestamp) IS NULL OR o.createdAt >= :start)
              AND (CAST(:end   AS timestamp) IS NULL OR o.createdAt <= :end)
              AND (CAST(:origin AS string) IS NULL
                   OR LOWER(o.origin) LIKE LOWER(CONCAT('%', CAST(:origin AS string), '%')))
              AND (CAST(:destination AS string) IS NULL
                   OR LOWER(o.destination) LIKE LOWER(CONCAT('%', CAST(:destination AS string), '%')))
            """)
    List<Order> findWithFilters(
            @Param("status") OrderStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("origin") String origin,
            @Param("destination") String destination);
}
