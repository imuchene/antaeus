package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider


import org.quartz.Scheduler
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobBuilder.*;
import org.quartz.TriggerBuilder.*;
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.CronTrigger
import org.quartz.Trigger
import org.quartz.CronScheduleBuilder
import org.quartz.CronScheduleBuilder.*
import org.quartz.Job
import io.pleo.antaeus.core.services.InvoiceCronJob


class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {

    fun invoicePaymentCronJob(){

        val schedule: Scheduler? = null

        val invoiceCronJob = InvoiceCronJob(invoiceService, paymentProvider)
        
        val job: JobDetail = newJob(invoiceCronJob.javaClass)
        .withIdentity("invoicePayment")
        .build()


        val trigger: CronTrigger = newTrigger()
        .withIdentity("runMonthly")
        .withSchedule(cronSchedule("0 0 1 * *"))
        .build()

        schedule?.scheduleJob(job, trigger)
    }
}
