package stream.okchun.dashboard.service.billing.tx;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.LedgerEntryLink;
import stream.okchun.dashboard.database.entity.billing.Transaction;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryLinkRepository;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryRepository;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeEntry;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeType;

public class BillingTransactionLedger extends LedgerTreeEntry<LedgerEntry> {
	@Getter
	protected final TransactionPrepare prep;

	@Getter
	@Setter
	protected Transaction tx;
	@Getter
	protected LedgerEntryLink dbLinkEntity;
	protected BillingTransactionLedger parent;

	public BillingTransactionLedger(TransactionPrepare prep,
									@Nullable BillingTransactionLedger parent, LedgerSide side) {
		super(parent, side);
		this.prep = prep;
	}

	protected BillingTransactionLedger(TransactionPrepare prep, @NonNull LedgerTreeEntry parent,
									   @NonNull LedgerTreeType link_type, @javax.annotation.Nullable String comment) {
		super(parent, parent.getSide());

		this.link_type = link_type;
		this.comment = comment;
		this.prep = prep;
	}

	public BillingTransactionLedger createSubTree(LedgerTreeType link_type, String comment) {
		var tree = new BillingTransactionLedger(this.prep, this, link_type, comment);
		ledgerTreeEntries.add(tree);

		return tree;
	}

	/// 실제 entry를 저장함
	private void commitEntry(Transaction tx, LedgerEntryRepository repo) {
		for (LedgerEntry ledgerEntry : super.ledgerEntries) {
			ledgerEntry.setEntryLink(this.dbLinkEntity);
			repo.save(ledgerEntry);
		}
		for (LedgerTreeEntry ledgerTreeEntry : (ledgerTreeEntries)) {
			((BillingTransactionLedger)ledgerTreeEntry).commitEntry(tx, repo);
		}
	}

	/// DB에 Tree 구조를 저장함. Tree 구조를 저장한 뒤에 실제 LedgerEntry를 저장 해야함.
	private void commitTree(Transaction tx, LedgerEntryLinkRepository repo) {
		this.tx = tx;
		LedgerEntryLink parent_link = null;
		if (this.parent != null) {
			// root가 아니면 다 root거로 설정 받아옴
			this.tx = parent.tx;
			this.side = parent.side;
			parent_link = this.parent.dbLinkEntity;
		}


		LedgerEntryLink ledgerEntryLink = new LedgerEntryLink(null, this.comment, this.link_type.name(),
				this.tx, this.side.toString(), parent_link);

		this.dbLinkEntity = repo.save(ledgerEntryLink);

		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			((BillingTransactionLedger)ledgerTreeEntry).commitTree(tx, repo);
		}
	}

	///  바깥쪽에서 @Transactional 처리 필요. 최상위 root에서 호출돼야 함
	public void commitDB(Transaction tx, LedgerEntryLinkRepository repo_link,
						 LedgerEntryRepository repo_entry) {
		if (this.parent != null) {
			// throw: root가 아님.
			return;
		}

		this.tx = tx;
		if (isInvalid(this)) {
			// throw는 일단 나중에 생성
			return;
		}

		this.commitTree(tx, repo_link);
		this.commitEntry(tx, repo_entry);
	}

	protected boolean isInvalid(BillingTransactionLedger root) {
		for (LedgerEntry ledgerEntry : ledgerEntries) {
			if (!ledgerEntry.getSide().equals(root.side.toString())) return false;
			if (!ledgerEntry.getTx().getId().equals(root.tx.getId())) return false;
		}

		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			if (!((BillingTransactionLedger)ledgerTreeEntry).isInvalid(root)) {
				return false;
			}
		}
		return true;
	}
}
