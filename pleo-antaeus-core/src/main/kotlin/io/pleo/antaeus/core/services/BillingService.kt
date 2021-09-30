package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider


import org.quartz.Scheduler
import org.quartz.Scheduler.*
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

        val invoiceCronJob = InvoiceCronJob()
        
        val job: JobDetail = newJob(invoiceCronJob.javaClass)
        .withIdentity("invoicePayment")
        .build()

        job.jobDataMap.put("invoiceService", invoiceService)
        job.jobDataMap.put("paymentProvider", paymentProvider)

        // Create a cron trigger that runs at midnight on the first
        // of every month
        val trigger: CronTrigger = newTrigger()
        .withIdentity("runMonthly")
        .withSchedule(cronSchedule("0 0 12 1 1/1 ? *"))
        .build()

        val schedule: Scheduler = StdSchedulerFactory().getScheduler()
        schedule.start();

        schedule.scheduleJob(job, trigger)
    }
}
