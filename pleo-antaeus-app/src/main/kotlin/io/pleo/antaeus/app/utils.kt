
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.CurrencyService
import io.pleo.antaeus.core.services.ChargeDetailsService
import io.pleo.antaeus.core.services.CustomerAccountService
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import java.math.BigDecimal
import kotlin.random.Random

// This will create all schemas and setup initial data
internal fun setupInitialData(dal: AntaeusDal) {
    val customers = (1..100).mapNotNull {
        dal.createCustomer(
            currency = Currency.values()[Random.nextInt(0, Currency.values().size)]
        )
    }

    customers.forEach { customer ->
        (1..10).forEach {
            dal.createInvoice(
                amount = Money(
                    value = BigDecimal(Random.nextDouble(10.0, 500.0)),
                    currency = customer.currency
                ),
                customer = customer,
                status = if (it == 1) InvoiceStatus.PENDING else InvoiceStatus.PAID
            )
        }
    }
}

// This is the mocked instance of the payment provider
internal fun getPaymentProvider(dal: AntaeusDal): PaymentProvider {
    return object : PaymentProvider {
        override  val customerService: CustomerService = CustomerService(dal)
        override val invoiceService: InvoiceService = InvoiceService(dal)
        override val currencyService: CurrencyService = CurrencyService(dal)
        override val chargeDetailsService: ChargeDetailsService = ChargeDetailsService(dal)
        override val customerAccountService: CustomerAccountService = CustomerAccountService(dal)

        override fun charge(invoice: Invoice): Boolean {
                return super.charge(invoice)
        }

    }
}
