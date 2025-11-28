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
@Table(name = "credit_ledger", schema = "billing", indexes = {
		@Index(name = "idx_credit_ledger_org_created", columnList = "org_id, created_at")
})
public class CreditLedger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "org_id", nullable = false)
	private Organization org;

	@Column(name = "delta_credit", nullable = false, precision = 20, scale = 4)
	private BigDecimal deltaCredit;

	@Column(name = "balance_after", nullable = false, precision = 20, scale = 4)
	private BigDecimal balanceAfter;

	@Column(name = "reference_type", length = 32)
	private String referenceType;

	@Column(name = "reference_id")
	private Long referenceId;

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description;
	@ColumnDefault("now()")
	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

/*
 TODO [Reverse Engineering] create field to map the 'entry_type' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "entry_type", columnDefinition = "billing_entry_type not null")
    private Object entryType;
*/
}