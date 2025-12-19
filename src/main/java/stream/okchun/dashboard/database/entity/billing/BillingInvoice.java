package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "billing_invoice", schema = "billing")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class BillingInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id", nullable = false)
	private Long id;

	@JoinColumn(name = "org_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Organization org;

	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ColumnDefault("'START'")
	@Column(name = "status", nullable = false, length = 8)
	private String status;

	@Column(name = "tx_id")
	private Long txId;

	@Column(name = "name", nullable = false, length = 32)
	private String name;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	@Column(name = "total_pay_amount", nullable = false)
	private BigDecimal totalPayAmount;

	@Column(name = "currency", nullable = false, length = 4)
	private String currency;

	@Column(name = "pg_ref", length = 8)
	private String pgRef;
	@Column(name = "pg_ref_id", length = 32)
	private String pgRefId;
	@Column(name = "pg_log", length = Integer.MAX_VALUE)
	private String pgLog;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	private BillingAccount account;

	@PrePersist
	public void prePersist() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}
}