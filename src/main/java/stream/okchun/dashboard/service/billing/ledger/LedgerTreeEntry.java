package stream.okchun.dashboard.service.billing.ledger;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

///  DB에 넣을때 entry_link부터 넣어야 함. tree 구조를 draw한 뒤에 entry를 넣어줘야 함.
public class LedgerTreeEntry<T extends LedgerEntryInterface> {
	/// null일 경우 root를 의미함
	@Getter
	protected final @Nullable LedgerTreeEntry<T> parent;
	@Getter
	protected final ArrayList<LedgerTreeEntry<T>> ledgerTreeEntries;
	@Getter
	protected final ArrayList<T> ledgerEntries;
	@Getter
	protected LedgerTreeType link_type;
	@Getter
	@Setter
	protected String comment;
	@Getter
	protected LedgerSide side;


	// root 생성시 사용
	public LedgerTreeEntry(@Nullable LedgerTreeEntry<T> parent, LedgerSide side) {
		this.parent = parent;
		this.side = side;
		this.link_type = LedgerTreeType.ROOT;

		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

	}

	// 내부 생성시 사용
	protected LedgerTreeEntry(@NonNull LedgerTreeEntry<T> parent,
							  @NonNull LedgerTreeType link_type, @Nullable String comment) {
		this.parent = parent;
		this.side = parent.side;
		ledgerTreeEntries = new ArrayList<>();
		ledgerEntries = new ArrayList<>();

		this.link_type = link_type;
		this.comment = comment;
	}


	public LedgerTreeEntry<T> createSubTree(LedgerTreeType link_type, String comment) {
		var tree = new LedgerTreeEntry<>(this, link_type, comment);
		ledgerTreeEntries.add(tree);

		return tree;
	}

	public void addLedgerEntry(T ledgerEntry) {
		ledgerEntries.add(ledgerEntry);
	}

	/// 좌변 우변 검증용. 변의 총 합을 구함.
	/// LedgerTreeEntity를 가지고 있는 Transaction에서 CreditRoot, DebitRoot가 있고, 각각 root를 가르킴
	/// 위쪽에서 creditRoot.calculateSum() == debitRoot.calculateSum()로 검증 해야 함.
	public HashMap<String, BigDecimal> calculateSum(HashMap<String, BigDecimal> container) {
		for (T ledgerEntry : ledgerEntries) {
			String currency = ledgerEntry.getCurrency();
			if (!container.containsKey(currency)) {
				container.put(currency, ledgerEntry.getAmount());
			}
		}
		for (LedgerTreeEntry<T> ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.calculateSum(container);
		}
		return container;
	}

	///  Side-Effect style로 container에 모든 Ledger Entry 보관 (DB에 넣을 수 있도록)
	public List<T> flatten(List<T> container) {
		container.addAll(ledgerEntries);
		for (LedgerTreeEntry<T> ledgerTreeEntry : ledgerTreeEntries) {
			ledgerTreeEntry.flatten(container);
		}

		return container;
	}

	protected boolean validate(LedgerTreeEntry<T> root) {
		for (T ledgerEntry : ledgerEntries) {
			if (!ledgerEntry.getSide().equals(root.side.toString())) return false;
			// if (!ledgerEntry.getTx().getId().equals(root.tx.getId())) return false;
		}

		for (LedgerTreeEntry<T> ledgerTreeEntry : ledgerTreeEntries) {
			if (!ledgerTreeEntry.validate(root)) {
				return false;
			}
		}
		return true;
	}
}
