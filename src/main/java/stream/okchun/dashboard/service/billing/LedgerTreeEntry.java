package stream.okchun.dashboard.service.billing;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
	LedgerTreeType link_type;
	@Getter
	@Setter
	String comment;
	@Getter
	String side;
	@Getter
	@Setter
	private ArrayList<LedgerTreeEntry> ledgerTreeEntries;
	@Getter
	@Setter
	private ArrayList<LedgerEntry> ledgerEntries;
	@Getter
	@Setter
	private Transaction tx;
	@Getter
	private LedgerEntryLink dbLinkEntity;


	public LedgerTreeEntry(TransactionPrepare prep, @Nullable LedgerTreeEntry parent, String side) {
		this.parent = parent;
		this.prep = prep;
		this.side = side;

		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

	}

	public LedgerTreeEntry(TransactionPrepare prep, @Nullable LedgerTreeEntry parent,
						   LedgerTreeType link_type, String comment) {
		this.parent = parent;
		this.prep = prep;
		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

		this.link_type = link_type;
		this.comment = comment;
	}


	public LedgerTreeEntry createSubTree(LedgerTreeType link_type, String comment) {
		var tree = new LedgerTreeEntry(this.prep, this.parent, link_type, comment);
		ledgerTreeEntries.add(tree);

		return tree;
	}

	public void addLedgerEntry(LedgerEntry ledgerEntry) {
		ledgerEntries.add(ledgerEntry);
	}

	/// 좌변 우변 검증용. 변의 총 합을 구함
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
	private void commitTree(EntityManager em, LedgerEntryLinkRepository repo) {
		var parent = this.parent != null ? this.parent.dbLinkEntity : null;
		LedgerEntryLink ledgerEntryLink = new LedgerEntryLink(null, this.comment, this.link_type.name(),
				this.tx, this.side, parent);

		this.dbLinkEntity = repo.save(ledgerEntryLink);

		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.commitTree(em, repo);
		}
	}

	/// 실제 entry를 저장함
	private void commitEntry(EntityManager em, LedgerEntryRepository repo) {
		for (LedgerEntry ledgerEntry : ledgerEntries) {
			ledgerEntry.setEntryLink(this.dbLinkEntity);
			repo.save(ledgerEntry);
		}
	}

	// 짜피 class 내부에서는 proxy가 안돼서 Transactional 안됨. 그러므로 실질적으로 외부에서 딱 한번 Transactional이 실행됨
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void commitDB(EntityManager em, LedgerEntryLinkRepository repo_link,
						 LedgerEntryRepository repo_entry) {
		this.commitTree(em, repo_link);
		this.commitEntry(em, repo_entry);

		for (LedgerTreeEntry ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.commitDB(em, repo_link, repo_entry);
		}
	}
}
