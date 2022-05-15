package com.unionbankng.future.futuremessagingservice.repositories;
import com.unionbankng.future.futuremessagingservice.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query(value = "DELETE FROM notifications where destination=:destination", nativeQuery = true)
    void clearAll(Long destination);
    @Query(value = "UPDATE notifications SET status='SE' where destination=:destination", nativeQuery = true)
    void updateAllAsSeen(Long destination);
    @Query(value = "SELECT * FROM notifications where destination=:id and status=:status order by id desc", nativeQuery = true)
    List<Notification> findNotificationsByStatus(Long id, String status);
    @Query(value = "SELECT * FROM notifications where destination=:id and priority=:priority order by id desc", nativeQuery = true)
    List<Notification> findAllNotSeenNotificationsByPriority(Long id, String priority);
    @Query(value = "SELECT TOP (:limit) * FROM notifications where destination=:id  order by id desc", nativeQuery = true)
    List<Notification> findTopNotifications(Long id, int limit);
    @Query(value = "SELECT TOP (:limit) * FROM notifications where destination=:id and id>:start order by id desc", nativeQuery = true)
    List<Notification> findAllNotifications(Long id, int start, int limit);
    @Query(value = "SELECT count(*) FROM notifications where destination=:id and status='NS'", nativeQuery = true)
    Long findActiveNotificationCount(Long id);
    @Query(value = "SELECT count(*) FROM notifications where destination=:id", nativeQuery = true)
    Long findNotificationCount(Long id);
}
