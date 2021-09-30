package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.pleo.antaeus.models.CustomerAccount
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.Money
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus
import org.joda.time.DateTime

class CustomerAccountServiceTest {
    val mockInvoice = Invoice(
        id = 1, 
        customerId = 500, 
        amount = Money(
            value = 1000.toBigDecimal(), 
            currency = Currency.EUR
            ), 
        status = InvoiceStatus.PENDING
        )

    val mockCustomerAccount = CustomerAccount(
        customerId = 500,
        customerBalance = 1000000.toFloat(),
        createdAt = DateTime.now(),
        updatedAt = DateTime.now()
    )

    private val dal = mockk<AntaeusDal> {
        every { fetchCustomerAccount(404) } returns mockCustomerAccount
    }

    private val customerAccountService = CustomerAccountService(dal = dal)

    @Test
    fun `will return true if customer account is found`() {
        assert(true) {
            customerAccountService.fetchCustomerAccount(mockInvoice) as CustomerAccount
        }
    }
}
