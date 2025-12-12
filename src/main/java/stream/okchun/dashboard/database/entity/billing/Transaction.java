package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "billing")
@NoArgsConstructor
@AllArgsConstructor
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

	@JoinColumn(name = "related_tx_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Transaction relatedTxId;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@Column(name = "comment_system")
	private String commentSystem;

	public static Transaction of (TransactionPrepare prep, String currency, BigDecimal amountTotal,
								  @Nullable Transaction relatedTx, @Nullable String comment) {
		return new Transaction(prep.getId(), currency, amountTotal, prep.getIssuedBy(),
				relatedTx, null, comment);
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = OffsetDateTime.now();
	}
}