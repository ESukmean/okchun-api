package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
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
@Table(name = "invoice_ledger_entry", schema = "billing")
public class InvoiceLedgerEntry implements LedgerEntryInterface {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_ledger_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.RESTRICT)
	@JoinColumn(name = "invoice_id", nullable = false)
	private BillingInvoice invoice;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "side", nullable = false, length = Integer.MAX_VALUE)
	private String side;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "comment")
	private String comment;

	@Column(name = "log")
	private String log;

	@Column(name = "entry_link", nullable = false)
	private Long entryLink;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "invoice_line_ref")
	private InvoiceLine invoiceLineRef;


}