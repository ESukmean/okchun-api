package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "credit_accounts", schema = "billing")
public class CreditAccount {
	@Id
	@Column(name = "org_id", nullable = false)
	private Long id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization organizations;

	@ColumnDefault("0")
	@Column(name = "balance_credit", nullable = false, precision = 20, scale = 4)
	private BigDecimal balanceCredit;

	@ColumnDefault("'USD'")
	@Column(name = "currency_code", nullable = false, length = 3)
	private String currencyCode;

	@ColumnDefault("0")
	@Column(name = "low_balance_threshold", precision = 20, scale = 4)
	private BigDecimal lowBalanceThreshold;

	@ColumnDefault("false")
	@Column(name = "auto_topup_enabled", nullable = false)
	private Boolean autoTopupEnabled = false;

	@Column(name = "auto_topup_amount", precision = 20, scale = 4)
	private BigDecimal autoTopupAmount;

	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

}