package stream.okchun.dashboard.service.billing.invoice;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.Setter;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;
import stream.okchun.dashboard.database.entity.billing.InvoiceLine;
import stream.okchun.dashboard.database.repos.billing.InvoiceLedgerEntryRepository;
import stream.okchun.dashboard.database.repos.billing.InvoiceLineRepository;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InvoiceLineEntry {
	private final List<InvoiceLineLedgerTree> debitSideLedger;
	private final List<InvoiceLineLedgerTree> creditSideLedger;
	private final BillingInvoice invoice;
	private final InvoiceLineEntry parent;
	private final String itemName;
	private final long quantity;
	private final BigDecimal unitPrice;
	private final BigDecimal amount;
	private final String currency = "USD";
	private final List<InvoiceLineEntry> children;
	private InvoiceLine invoiceLine;
	@Setter
	private String refItemType;
	@Setter
	private long refItemId;
	@Setter
	private String comment;

	public InvoiceLineEntry(BillingInvoice invoice, String itemName, long quantity, BigDecimal unitPrice,
							BigDecimal amount) {
		debitSideLedger = new ArrayList<>();
		creditSideLedger = new ArrayList<>();
		children = new ArrayList<>();

		this.parent = null;
		this.itemName = itemName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.amount = amount;
		this.invoice = invoice;
	}

	private InvoiceLineEntry(@NonNull InvoiceLineEntry parent, String itemName, long quantity,
							 BigDecimal unitPrice, BigDecimal amount) {
		this.parent = parent;
		this.itemName = itemName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.amount = amount;
		this.invoice = parent.invoice;

		debitSideLedger = new ArrayList<>();
		creditSideLedger = new ArrayList<>();
		children = new ArrayList<>();
	}

	public InvoiceLineEntry createChild(String itemName, long quantity, BigDecimal unitPrice,
										BigDecimal amount) {
		var entry = new InvoiceLineEntry(this, itemName, quantity, unitPrice, amount);
		children.add(entry);

		return entry;
	}

	public InvoiceLineLedgerTree createDebitSideLedger(String comment) {
		var ledger = new InvoiceLineLedgerTree(null, LedgerSide.DEBIT, this, comment);
		this.debitSideLedger.add(ledger);


		return ledger;
	}
	public InvoiceLineLedgerTree createCreditSideLedger(String comment) {
		var ledger = new InvoiceLineLedgerTree(null, LedgerSide.CREDIT, this, comment);
		this.creditSideLedger.add(ledger);


		return ledger;
	}


	@Transactional
	public void commit(InvoiceLineRepository lineRepo, InvoiceLedgerEntryRepository ledgerRepo)
			throws TransactionException {
		var parent = this.parent == null ? null : this.parent.invoiceLine;
		var line = InvoiceLine.builder()
				.name(itemName)
				.unitPrice(unitPrice)
				.quantity(quantity)
				.amount(amount)
				.currency(currency)
				.itemType(refItemType)
				.itemRef(refItemId)
				.parent(parent)
				.productComment("")
				.comment(comment)
				.invoice(invoice)
				.build();

		lineRepo.save(line);
		for (var child : children) {
			child.commit(lineRepo, ledgerRepo);
		}
	}
}