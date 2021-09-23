/*
    This is the payment provider. It is a "mock" of an external service that you can pretend runs on another system.
    With this API you can ask customers to pay an invoice.

    This mock will succeed if the customer has enough money in their balance,
    however the documentation lays out scenarios in which paying an invoice could fail.
 */

package io.pleo.antaeus.core.external

import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService

interface PaymentProvider {
    /*
        Charge a customer's account the amount from the invoice.

        Returns:
          `True` when the customer account was successfully charged the given amount.
          `False` when the customer account balance did not allow the charge.

        Throws:
          `CustomerNotFoundException`: when no customer has the given id.
          `CurrencyMismatchException`: when the currency does not match the customer account.
          `NetworkException`: when a network error happens.
     */

    val customerService: CustomerService
    val invoiceService: InvoiceService

    fun charge(invoice: Invoice): Boolean {
      
      // Validate the customer by checking if he/she exists
      customerService.fetch(invoice.customerId)

      // Check if the invoice is pending, else throw an already paid exception
      invoiceService.isInvoicePending(invoice.id)


      return true
    }

    
}
