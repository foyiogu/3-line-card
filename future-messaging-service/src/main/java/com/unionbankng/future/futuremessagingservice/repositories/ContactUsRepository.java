
package com.unionbankng.future.futuremessagingservice.repositories;

import com.unionbankng.future.futuremessagingservice.entities.ContactUs;
import com.unionbankng.future.futuremessagingservice.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs,Long> {

}
