package com.unionbankng.future.paymentservice.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.unionbankng.future.paymentservice.enums.PaymentGateway;
import com.unionbankng.future.paymentservice.enums.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentReference implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 70)
	private String ref;
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	@Column(nullable = false)
	private Double amount;
	@Column(nullable = false, length = 70)
	private String userUUID;
	@Enumerated(EnumType.STRING)
	private PaymentGateway paymentGateway;
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	
	@PrePersist
	private void setCreatedAt() {
		createdAt = new Date();
	}
	
	@PreUpdate
	private void setUpdatedAt() {
		updatedAt = new Date();
	}

	@Override
	public boolean equals(Object paymentReference) {
		return this.id.equals(((PaymentReference)paymentReference).getId()) ;
		
	}
	
}
