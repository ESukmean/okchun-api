package stream.okchun.dashboard.service.billing;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.LedgerEntryLink;
import stream.okchun.dashboard.database.entity.billing.Transaction;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryLinkRepository;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryRepository;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

///  DB에 넣을때 entry_link부터 넣어야 함. tree 구조를 draw한 뒤에 entry를 넣어줘야 함.
public class LedgerTreeEntry {
	/// null일 경우 root를 의미함
	@Getter
	private final @Nullable LedgerTreeEntry parent;
	@Getter
	private final TransactionPrepare prep;
	@Getter
	private final ArrayList<LedgerTreeEntry> ledgerTreeEntries;
	@Getter
	private final ArrayList<LedgerEntry> ledgerEntries;
	@Getter
	LedgerTreeType link_type;
	@Getter
	@Setter
	String comment;
	@Getter
	LedgerSide side;
	@Getter
	@Setter
	private Transaction tx;
	@Getter
	private LedgerEntryLink dbLinkEntity;


	// root 생성시 사용
	public LedgerTreeEntry(TransactionPrepare prep, @Nullable LedgerTreeEntry parent, LedgerSide side) {
		this.parent = parent;
		this.prep = prep;
		this.side = side;
		this.link_type = LedgerTreeType.ROOT;

		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

	}

	// 내부 생성시 사용
	private LedgerTreeEntry(TransactionPrepare prep, @Nullable LedgerTreeEntry parent,
							@NonNull LedgerTreeType link_type, @Nullable String comment) {
		this.parent = parent;
		this.prep = prep;
		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

		this.link_type = link_type;
		this.comment = comment;
	}


	public LedgerTreeEntry createSubTree(LedgerTreeType link_type, String comment) {
		var tree = new LedgerTreeEntry(this.prep, this, link_type, comment);
		ledgerTreeEntries.add(tree);

		return tree;
	}

	public void addLedgerEntry(LedgerEntry ledgerEntry) {
		ledgerEntries.add(ledgerEntry);
	}

	/// 좌변 우변 검증용. 변의 총 합을 구함.
	/// LedgerTreeEntity를 가지고 있는 Transaction에서 CreditRoot, DebitRoot가 있고, 각각 root를 가르킴
	/// 위쪽에서 creditRoot.calculateSum() == debitRoot.calculateSum()로 검증 해야 함.
	public BigDecimal calculateSum() {
		BigDecimal sum = BigDecimal.ZERO;
		for (LedgerEntry ledgerEntry : ledgerEntries) {
			sum = sum.add(ledgerEntry.getAmount());
		}
		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			sum = sum.add(ledgerTreeEntry.calculateSum());
		}
		return sum;
	}

	///  Side-Effect style로 container에 모든 Ledger Entry 보관 (DB에 넣을 수 있도록)
	public List<LedgerEntry> flatten(List<LedgerEntry> container) {
		container.addAll(ledgerEntries);
		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.flatten(container);
		}

		return container;
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
			ledgerTreeEntry.commitTree(tx, repo);
		}
	}

	/// 실제 entry를 저장함
	private void commitEntry(Transaction tx, LedgerEntryRepository repo) {
		for (LedgerEntry ledgerEntry : ledgerEntries) {
			ledgerEntry.setEntryLink(this.dbLinkEntity);
			repo.save(ledgerEntry);
		}
		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.commitEntry(tx, repo);
		}
	}

	private boolean validate(LedgerTreeEntry root) {
		for (LedgerEntry ledgerEntry : ledgerEntries) {
			if (!ledgerEntry.getSide().equals(root.side.toString())) return false;
			if (!ledgerEntry.getTx().getId().equals(root.tx.getId())) return false;
		}

		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			if (!ledgerTreeEntry.validate(root)) {
				return false;
			}
		}
		return true;
	}

	///  바깥쪽에서 @Transactional 처리 필요. 최상위 root에서 호출돼야 함
	public void commitDB(Transaction tx, LedgerEntryLinkRepository repo_link,
						 LedgerEntryRepository repo_entry) {
		if (this.parent != null) {
			// throw: root가 아님.
			return;
		}

		this.tx = tx;
		if (!validate(this)) {
			// throw는 일단 나중에 생성
			return;
		}

		this.commitTree(tx, repo_link);
		this.commitEntry(tx, repo_entry);
	}
}
