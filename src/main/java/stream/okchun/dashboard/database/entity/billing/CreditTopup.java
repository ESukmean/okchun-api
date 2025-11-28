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
@Table(name = "credit_topups", schema = "billing", indexes = {
		@Index(name = "idx_topups_org_created", columnList = "org_id, created_at")
})
public class CreditTopup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@Column(name = "amount_credit", nullable = false, precision = 20, scale = 4)
	private BigDecimal amountCredit;

	@Column(name = "amount_currency", nullable = false, precision = 20, scale = 4)
	private BigDecimal amountCurrency;

	@Column(name = "currency_code", nullable = false, length = 3)
	private String currencyCode;

	@Column(name = "provider", nullable = false, length = 32)
	private String provider;

	@Column(name = "provider_checkout_id", length = 128)
	private String providerCheckoutId;

	@Column(name = "provider_transaction_id", length = 128)
	private String providerTransactionId;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
	@ColumnDefault("now()")
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'PENDING'")
    @Column(name = "status", columnDefinition = "billing_topup_status not null")
    private Object status;
*/
}