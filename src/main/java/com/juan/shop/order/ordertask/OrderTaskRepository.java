package com.juan.shop.order.ordertask;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author guanhuan_li
 */
public interface OrderTaskRepository extends JpaRepository<OrderTask, Long> {

    Optional<OrderTask> findByExeDate(Date exeDate);

    List<OrderTask> findByExeDateBetween(Date start, Date end);
}
