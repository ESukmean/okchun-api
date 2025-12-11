package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "billing_account", schema = "billing", indexes = {@Index(name = "billing_account_type_idx",
		columnList = "account_type, account_ref")})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "billing_account_id", nullable = false)
	private Long id;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@ColumnDefault("0")
	@Column(name = "balance", nullable = false)
	private BigDecimal balance;

	@Column(name = "account_type", nullable = false, length = 8)
	private String accountType;

	@Column(name = "account_ref", nullable = false)
	private Long accountRef;

	@Column(name = "system_comment")
	private String systemComment;

	@Column(name = "comment")
	private String comment;

	@ColumnDefault("now()")
	@Column(name = "created", nullable = false)
	private Instant created;

	@ColumnDefault("now()")
	@Column(name = "updated", nullable = false)
	private Instant updated;

	@PrePersist
	public void prePersist() {
		this.created = Instant.now();
		this.updated = Instant.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updated = Instant.now();
	}
}