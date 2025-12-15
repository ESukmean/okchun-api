package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "ledger_entry_link", schema = "billing", indexes = {
		@Index(name = "ledger_entry_link_parent_ledger_id_idx",
				columnList = "ledger_id_parent"),
		@Index(name = "ledger_entry_link_child_ledger_id_idx",
				columnList = "ledger_id_child")})
@AllArgsConstructor
@NoArgsConstructor
public class LedgerEntryLink {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk", nullable = false)
	private Long id;

	@Column(name = "comment")
	private String comment;

	@Column(name = "link_type", nullable = false, length = 32)
	private String linkType;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "tx_id", nullable = false)
	private Transaction tx;

	@Column(name = "side", nullable = false, length = 1)
	private String side;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "ledger_link_parent")
	private LedgerEntryLink ledgerLinkParent;
}