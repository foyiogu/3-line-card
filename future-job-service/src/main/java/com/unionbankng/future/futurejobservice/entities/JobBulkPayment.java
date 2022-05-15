package com.unionbankng.future.futurejobservice.entities;
import lombok.*;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name="job_bulk_transfers")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobBulkPayment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String executedBy;
	private String executedByUsername;
	private String executedFor;
	@NotNull
	private String contractReference;
	@Column(columnDefinition="TEXT")
	private String remark;
	@NotNull
	private String initialPaymentReference;
	private String paymentReference;
	private String transactionId;
	@NotNull
	private String accountNumber;
	@NotNull
	private String accountType;
	@NotNull
	private String accountName;
	@NotNull
	private String accountBranchCode;
	@NotNull
	private String accountBankCode;
	@Column(columnDefinition="TEXT")
	private String narration;
	private String instrumentNumber;
	@NotNull
	double amount;
	private String valueDate;
	private String crDrFlag;
	private String feeOrCharges;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@PrePersist
	public void setCreatedAt() {
		createdAt = new Date();
	}
}