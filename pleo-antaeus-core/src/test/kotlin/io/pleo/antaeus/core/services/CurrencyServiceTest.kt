package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.data.AntaeusDal
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Money
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus

class CurrencyServiceTest {
    val mockInvoice = Invoice(
        id = 1, 
        customerId = 1, 
        amount = Money(
            value = 1000.toBigDecimal(), 
            currency = Currency.EUR
            ), 
        status = InvoiceStatus.PENDING
        )

    val mockCustomer = Customer(id = 1, currency= Currency.USD)

    private val dal = mockk<AntaeusDal> {
        every { fetchCustomer(mockCustomer.id) } returns mockCustomer
    }

    private val currencyService = CurrencyService(dal = dal)

    @Test
    fun `will throw an exception if there is a currency mismatch`() {
        assertThrows<CurrencyMismatchException> {
            currencyService.validateCurrency(mockInvoice)
        }
    }
}
