package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction_prepare", schema = "billing")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPrepare {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tx_prepare_id", nullable = false)
	private Long id;

	@Column(name = "tx_type", nullable = false, length = 32)
	private String txType;

	@Column(name = "tx_name", nullable = false)
	private String txName;

	@Column(name = "tx_comment", length = Integer.MAX_VALUE)
	private String txComment;

	@Column(name = "status", nullable = false, length = 16)
	private String status;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "issued_by", nullable = false)
	private BillingAccount issuedBy;

	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = Instant.now();
	}
}