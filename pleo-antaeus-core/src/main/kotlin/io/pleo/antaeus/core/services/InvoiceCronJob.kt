package io.pleo.antaeus.core.services

import org.quartz.Job;
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.InvoiceService

class InvoiceCronJob(): Job   {

  override fun execute(context: JobExecutionContext){
    try {

      val data: JobDataMap = context.getJobDetail().getJobDataMap()
      
      val invoiceService: InvoiceService = data.get("invoiceService") as InvoiceService
      val paymentProvider: PaymentProvider = data.get("paymentProvider") as PaymentProvider
      return chargeInvoices(invoiceService, paymentProvider)
    }
    catch(e: Exception) {
      throw JobExecutionException(e)
    }
  }


  fun chargeInvoices(invoiceService: InvoiceService, paymentProvider: PaymentProvider){
    val invoices = invoiceService.fetchAll()
    // TODO Use a queue to charge invoices individually
    for (invoice in invoices) {
        paymentProvider.charge(invoice)
    }
  }
}