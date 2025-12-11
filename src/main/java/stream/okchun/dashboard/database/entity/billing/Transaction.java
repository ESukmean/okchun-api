package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "billing")
public class Transaction {
	@Id
	@Column(name = "tx_id", nullable = false)
	private Long id;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@Column(name = "amount_total", nullable = false)
	private BigDecimal amountTotal;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	@JoinColumn(name = "issued_by", nullable = false)
	private BillingAccount issuedBy;

	@Column(name = "related_tx_id", nullable = false)
	private Long relatedTxId;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "comment_system")
	private String commentSystem;


}