package io.pleo.antaeus.core.services

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import io.pleo.antaeus.core.external.PaymentProvider

class InvoiceCronJob(
  private val invoiceService: InvoiceService,
  private val paymentProvider: PaymentProvider
  ): Job   {
  override fun execute(context: JobExecutionContext){

    try {
      return chargeInvoices()
    }
    catch(e: Exception) {
      throw JobExecutionException(e)
    }
  }


  fun chargeInvoices(){
    val invoices = invoiceService.fetchAll()

    for (invoice in invoices) {
        paymentProvider.charge(invoice)
    }
}
}