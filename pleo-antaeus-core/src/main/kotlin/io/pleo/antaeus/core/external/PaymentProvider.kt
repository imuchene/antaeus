/*
    This is the payment provider. It is a "mock" of an external service that you can pretend runs on another system.
    With this API you can ask customers to pay an invoice.

    This mock will succeed if the customer has enough money in their balance,
    however the documentation lays out scenarios in which paying an invoice could fail.
 */

package io.pleo.antaeus.core.external

import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.CustomerAccount
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.CurrencyService
import io.pleo.antaeus.core.services.ChargeDetailsService
import io.pleo.antaeus.core.services.CustomerAccountService
import io.pleo.antaeus.core.exceptions.NetworkException
import org.joda.time.DateTime
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.URI
import java.net.http.HttpResponse


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
    val currencyService: CurrencyService
    val chargeDetailsService: ChargeDetailsService
    val customerAccountService: CustomerAccountService

    fun charge(invoice: Invoice): Boolean {
      
      // Validate the customer by checking if he/she exists
      customerService.fetch(invoice.customerId)

      // Check if the invoice is pending, else stop further execution
      val isPending = invoiceService.isInvoicePending(invoice.id)

      if(isPending === null){
        return false
      }

      // Validate the currency the invoice is in
      currencyService.validateCurrency(invoice)

      // Save charge details
      val chargeDetails = chargeDetailsService.saveChargeDetails(invoice)
      
      if(!chargeDetails){
        return false
      }

      // Deduct invoice amount from the customer balance,
      // Then update the customer's account 
      val customerBalance = customerAccountService.fetchCustomerAccount(invoice)?.customerBalance

      val newCustomerBalance = customerBalance?.minus(invoice.amount.value.toFloat())

      val newCustomerAccountDetails = CustomerAccount(
        customerId = invoice.customerId, 
        customerBalance = newCustomerBalance, 
        createdAt = DateTime.now(), 
        updatedAt = DateTime.now()
      )

      customerAccountService.updateCustomerAccount(newCustomerAccountDetails)

      // Make http request (to example.com), with the invoice as a payload,
      // else throw a network exception

      makeHttpRequestToTestProvider(invoice)

      // Update invoice status to paid

      val updatedInvoice = invoice.copy(status = InvoiceStatus.PAID)

      invoiceService.updateInvoice(updatedInvoice)


      return true
    }

    fun makeHttpRequestToTestProvider(invoice: Invoice): String {
      try {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
          .uri(URI.create("http.example.com"))
          .POST(HttpRequest.BodyPublishers.ofString(invoice.toString()))
          .build()
  
          val response = client.send(request, HttpResponse.BodyHandlers.ofString())
  
          // return the response body if the request is successful
          if(response.statusCode() == 200)
          {
            return response.body()
          }
          else 
          {
            throw NetworkException()
          }
            
      }
      catch(e: NetworkException) {
        // Send email to support that an error has occurred 
        throw NetworkException()
      }
     
    }
    

    
}
