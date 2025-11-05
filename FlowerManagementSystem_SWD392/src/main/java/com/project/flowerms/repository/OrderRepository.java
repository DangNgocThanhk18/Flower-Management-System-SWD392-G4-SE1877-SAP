package com.project.flowerms.repository;

import com.project.flowerms.entity.Orders;
import com.project.flowerms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUser(User user);
    Orders findByOrderIdAndUser(Integer orderId, User user);
    List<Orders> findByStatus(String status);
    List<Orders> findByShipperAndStatusIn(User shipper, List<String> statuses);
    boolean existsByShipper_UserIdAndStatus(Integer shipperId, String status);
    @Query("SELECT o FROM Orders o " +
            "WHERE (:status IS NULL OR :status = '' OR o.status = :status) " +
            "AND (:keyword IS NULL OR :keyword = '' " +
            "OR CAST(o.orderId AS string) LIKE %:keyword% " +
            "OR o.orderDate LIKE %:keyword% " +
            "OR o.shipper.fullname LIKE %:keyword%)")
    List<Orders> searchAndFilter(@Param("keyword") String keyword,
                                 @Param("status") String status);
}


