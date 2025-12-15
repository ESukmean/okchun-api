package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import stream.okchun.dashboard.service.billing.ledger.LedgerEntryInterface;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ledger_entry", schema = "billing")
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntry implements LedgerEntryInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ledger_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	@JoinColumn(name = "tx_id", nullable = false)
	private Transaction tx;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	@JoinColumn(name = "account_id", nullable = false)
	private BillingAccount account;

	@Column(name = "side", nullable = false, length = Integer.MAX_VALUE)
	private String side;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "account_balance_before", nullable = false)
	private BigDecimal accountBalanceBefore;

	@Column(name = "account_balance_after", nullable = false)
	private BigDecimal accountBalanceAfter;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "comment")
	private String comment;

	@Column(name = "log")
	private String log;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	@JoinColumn(name = "entry_link", nullable = false)
	private LedgerEntryLink entryLink;

	@Override
	public String getCurrency() {
		return this.account.getCurrency();
	}
}