package stream.okchun.dashboard.service.billing.invoice;

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import stream.okchun.dashboard.database.entity.billing.InvoiceLedgerEntry;
import stream.okchun.dashboard.database.entity.billing.LedgerEntry;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeEntry;
import stream.okchun.dashboard.service.billing.ledger.LedgerTreeType;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionLedger;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvoiceLineLedgerTree extends LedgerTreeEntry<InvoiceLedgerEntry> {
	private LedgerTreeType treeType;

	@Getter
	private final InvoiceLineEntry linkedLine;

	public InvoiceLineLedgerTree(@Nullable LedgerTreeEntry<InvoiceLedgerEntry> parent,
								 LedgerSide side, InvoiceLineEntry linkedLine,
								 String comment) {
		super(parent, side);
		this.linkedLine = linkedLine;
		this.treeType = LedgerTreeType.ROOT;
		this.comment = comment;
	}

	private InvoiceLineLedgerTree(@Nullable LedgerTreeEntry<InvoiceLedgerEntry> parent,
								 LedgerSide side, LedgerTreeType link_type, InvoiceLineEntry linkedLine,
								 String comment) {
		super(parent, side);
		this.linkedLine = linkedLine;
		this.treeType = LedgerTreeType.ROOT;
		this.comment = comment;
	}

	public InvoiceLineLedgerTree createSubTree(LedgerTreeType link_type, String comment) {
		var tree = new InvoiceLineLedgerTree(this, this.side, link_type, this.linkedLine, comment);
		ledgerTreeEntries.add(tree);

		return tree;
	}

	@Transactional
	public boolean commit() throws TransactionException {
//		debitLedgerEntry.commitDB(tx, ledgerEntryLinkRepository, ledgerEntryRepository);
//		creditLedgerEntry.commitDB(tx, ledgerEntryLinkRepository, ledgerEntryRepository);

		return true;
	}
}
