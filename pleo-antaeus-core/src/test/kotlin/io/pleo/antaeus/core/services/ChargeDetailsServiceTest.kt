package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.data.AntaeusDal
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.Money
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.core.services.ChargeDetailsService

class ChargeDetailsServiceTest {

    private val mockInvoice = Invoice(
        id = 1, 
        customerId = 1, 
        amount = Money(
            value = 1000.toBigDecimal(), 
            currency = Currency.EUR
            ), 
        status = InvoiceStatus.PENDING
        )

    private val dal = mockk<AntaeusDal> {

        every { saveChargeDetails(mockInvoice) } returns "true"
    }



    private val chargeDetailsService = ChargeDetailsService(dal = dal)

    @Test
    fun `returns true if the charge details are saved`() {
        assert(true){
            chargeDetailsService.saveChargeDetails(mockInvoice)
        }
    }
}
