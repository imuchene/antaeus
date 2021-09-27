package io.pleo.antaeus.core.services
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.CustomerAccount

class CustomerAccountService(private val dal: AntaeusDal) {
  fun fetchCustomerAccount(invoice: Invoice): CustomerAccount? {
    return dal.fetchCustomerAccount(invoice.customerId)
  }

  fun updateCustomerAccount(customerAccount: CustomerAccount): Int? {
    return dal.updateCustomerAccount(customerAccount)
  }
}
