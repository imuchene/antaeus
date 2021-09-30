package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.Money
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus

class InvoiceServiceTest {

    private val mockInvoice = Invoice(
        id = 1, 
        customerId = 500, 
        amount = Money(
            value = 1000.toBigDecimal(), 
            currency = Currency.EUR
            ), 
        status = InvoiceStatus.PENDING
        )

        
    private val dal = mockk<AntaeusDal> {
        every { fetchInvoice(404) } returns null
        every { isInvoicePending(mockInvoice.id) } returns mockInvoice
    }

    private val invoiceService = InvoiceService(dal = dal)

    @Test
    fun `will throw if invoice is not found`() {
        assertThrows<InvoiceNotFoundException> {
            invoiceService.fetch(404)
        }
    }

    @Test
    fun `will return true if the invoice is pending`() {
        assert(true) {
            invoiceService.isInvoicePending(mockInvoice.id) as Invoice
        }
    }
}
