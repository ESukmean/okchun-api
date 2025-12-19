package stream.okchun.dashboard.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito; // Keep Mockito import for any static methods if needed, though mostly using instance mocks now
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import stream.okchun.dashboard.database.entity.auth.User;
import stream.okchun.dashboard.database.entity.billing.BillingAccount;
import stream.okchun.dashboard.database.entity.billing.BillingInvoice;
import stream.okchun.dashboard.database.entity.org.Organization;
import stream.okchun.dashboard.database.repos.auth.UserRepository;
import stream.okchun.dashboard.database.repos.billing.BillingAccountRepository;
import stream.okchun.dashboard.database.repos.billing.BillingInvoiceRepository;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryLinkRepository;
import stream.okchun.dashboard.database.repos.billing.LedgerEntryRepository;
import stream.okchun.dashboard.database.repos.billing.TransactionPrepareRepository;
import stream.okchun.dashboard.database.entity.billing.TransactionPrepare;
import stream.okchun.dashboard.database.repos.billing.TransactionRepository;
import stream.okchun.dashboard.database.repos.org.OrganizationRepository;
import stream.okchun.dashboard.exception.billing.TransactionException;
import stream.okchun.dashboard.service.billing.pg.InvoiceCreatedResult;
import stream.okchun.dashboard.service.billing.pg.PaymentGateway;
import stream.okchun.dashboard.service.billing.tx.BillingAccountType;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionInstance;
import stream.okchun.dashboard.service.billing.tx.BillingTransactionLedger;
import stream.okchun.dashboard.service.billing.ledger.LedgerSide;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest // Removed as we are using direct Mockito mocks
@Transactional // Keep for transaction management if needed, but in pure unit test, it might be optional
class BillingServiceTest {
	@Autowired
	private BillingService billingService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private BillingAccountRepository billingAccountRepository;

	@Autowired
	private BillingInvoiceRepository billingInvoiceRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionPrepareRepository transactionPrepareRepository;

	@Autowired
	private LedgerEntryLinkRepository ledgerEntryLinkRepository;

	@Autowired
	private LedgerEntryRepository ledgerEntryRepository;


	private User testUser;
	private Organization testOrg;
	private BillingAccount orgBillingAccount;
	private BillingAccount pgBillingAccount;

	@BeforeEach
	public void setup() {
		this.testUser = userRepository.findById(1L).get();
		this.testOrg = organizationRepository.findById(1L).get();
		this.orgBillingAccount = billingAccountRepository.findById(1L).get();
		this.pgBillingAccount = billingAccountRepository.findById(6L).get();
	}

	@Test
	@DisplayName("인보이스 생성 및 결제 성공 테스트")
	void testDeclareInvoiceAndPaymentSuccess() throws TransactionException {
		long amount = 5000;
		String invoiceName = "Test Invoice";

		// 1. 인보이스 선언
		InvoiceCreatedResult pgResult = billingService.declareInvoice(amount, orgBillingAccount, testOrg, testUser, invoiceName, "Test Description");

		// Verify the invoice creation
		// The pgResult.order_id() holds the internal invoice ID we passed to createInvoice
		long invoiceId = Long.parseLong(pgResult.order_id());
		Optional<BillingInvoice> createdInvoiceOpt = billingInvoiceRepository.findById(invoiceId);
		assertTrue(createdInvoiceOpt.isPresent(), "생성된 인보이스를 찾을 수 없습니다.");
		BillingInvoice createdInvoice = createdInvoiceOpt.get();

		if (pgResult.success()) {
			assertEquals("PG_CREATE", createdInvoice.getStatus());
		} else{
			assertEquals("PG_CREATE_FAIL", createdInvoice.getStatus());
		}
		assertEquals(testOrg.getId(), createdInvoice.getOrg().getId());
		assertEquals(invoiceId, createdInvoice.getId());

		if (!pgResult.success()) {
			return;
		}

		// Verify that a TransactionPrepare was created and linked
		assertNotNull(createdInvoice.getTxId());
		BillingTransactionInstance tx = billingService.getTransaction(createdInvoice.getTxId());
		assertNotNull(tx);


		// 2. 결제 성공 처리
		billingService.invoicePaymentSuccess(invoiceId);

		// 3. 결과 검증
		// Retrieve the latest state of the mocked accounts.
		// Since we are mocking, the objects themselves are updated if passed by reference.
		// No need to re-fetch from mocked repository if the mock objects are the same instances.

		// The initial balance for both accounts is 0.
		// As per invoicePaymentSuccess logic:
		// orgBillingAccount is DEBITED by the amount (decreases balance)
		// pgBillingAccount is CREDITED by the amount (increases balance)

		// update
		var orgBillingAccount_new = billingAccountRepository.findById(1L).get();
		var pgBillingAccount_new = billingAccountRepository.findById(6L).get();

		assertEquals(orgBillingAccount.getBalance().add(BigDecimal.valueOf(5000)),
				orgBillingAccount_new.getBalance(), "조직 계정의 잔액이 예상과 다릅니다.");
		assertEquals(pgBillingAccount.getBalance().add(BigDecimal.valueOf(-5000)), pgBillingAccount_new.getBalance(),
				"조직 계정의 잔액이 예상과 다릅니다.");

		// Also verify interactions for transaction commit if necessary, but balance checks are primary.
		// For example, verify that transactionRepository.save was called with the final transaction.
		// Mockito.verify(transactionRepository, Mockito.times(1)).save(any(Transaction.class));
	}
}
