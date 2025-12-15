package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "invoice_line", schema = "billing")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_line_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 32)
	private String name;

	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;

	@Column(name = "quantity", nullable = false)
	private Long quantity;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "currency", nullable = false, length = 3)
	private String currency;

	@Column(name = "item_type", nullable = false, length = 16)
	private String itemType;

	@Column(name = "item_ref")
	private Long itemRef;

	@JoinColumn(name = "parent")
	@ManyToOne(fetch = FetchType.LAZY)
	private InvoiceLine parent;

	@Column(name = "product_comment")
	private String productComment;

	@Column(name = "comment")
	private String comment;

	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "invoice_id", nullable = false)
	private BillingInvoice invoice;

	@PrePersist
	public void prePersist() {
		createdAt = OffsetDateTime.now();
	}
}