package stream.okchun.dashboard.service.billing.invoice;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;
import stream.okchun.dashboard.database.repos.billing.InvoiceLedgerEntryRepository;
import stream.okchun.dashboard.database.repos.billing.InvoiceLineRepository;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillingInvoiceInstance {
	private final BillingInvoice invoice;
	private final List<InvoiceLineEntry> entries = new ArrayList<>(16);

	private final InvoiceLineRepository lineRepo;
	private final InvoiceLedgerEntryRepository ledgerRepo;

	public BillingInvoiceInstance(BillingInvoice invoice, InvoiceLineRepository lineRepo,
								  InvoiceLedgerEntryRepository ledgerRepo) {
		this.invoice = invoice;
		this.lineRepo = lineRepo;
		this.ledgerRepo = ledgerRepo;
	}

	// 직접 List에 접근하지 못하도록.. invoice의 ref만 내보냄
	public InvoiceLineEntry createInvoiceLine(String itemName,
											   long quantity,
											   BigDecimal unitPrice,
											   BigDecimal amount) {
		InvoiceLineEntry entry = new InvoiceLineEntry(invoice, itemName, quantity, unitPrice, amount);
		this.entries.add(entry);

		return entry;
	}

	@Transactional
	public void commit() {
		for (var entry : entries) {
			entry.commit(lineRepo, ledgerRepo);
		}
	}
}
