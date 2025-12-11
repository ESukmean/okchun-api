package stream.okchun.dashboard.database.entity.billing;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ledger_entry_link", schema = "billing", indexes = {
		@Index(name = "ledger_entry_link_parent_ledger_id_idx",
				columnList = "ledger_id_parent"),
		@Index(name = "ledger_entry_link_child_ledger_id_idx",
				columnList = "ledger_id_child")})
public class LedgerEntryLink {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk", nullable = false)
	private Long id;

	@Column(name = "comment")
	private String comment;

	@Column(name = "link_type", nullable = false, length = 32)
	private String linkType;


}