package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.entity.QReservation.reservation;

@Repository
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReservationRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Reservation> searchReservations(Long userId, Long itemId) {
        return queryFactory
                .selectFrom(reservation)
                .join(reservation.user).fetchJoin()
                .join(reservation.item).fetchJoin()
                .where(
                        userIdEq(userId),
                        itemIdEq(itemId)
                )
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? reservation.user.id.eq(userId) : null;
    }

    private BooleanExpression itemIdEq(Long itemId) {
        return itemId != null ? reservation.item.id.eq(itemId) : null;
    }
}
