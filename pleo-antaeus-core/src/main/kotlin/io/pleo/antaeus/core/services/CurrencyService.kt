package io.pleo.antaeus.core.services
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.data.AntaeusDal



class CurrencyService(private val dal: AntaeusDal) {
    fun validateCurrency(invoice: Invoice): Boolean {
        val  customer = dal.fetchCustomer(invoice.customerId);
        if (customer?.currency === invoice.amount.currency){
            return true
        }
        else {
            return false
        }

    }
}
