package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import stream.okchun.dashboard.database.entity.org.Organization;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "billing_invoice", schema = "billing")
public class BillingInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id", nullable = false)
	private Long id;

	@JoinColumn(name = "org_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Organization org;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

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


}