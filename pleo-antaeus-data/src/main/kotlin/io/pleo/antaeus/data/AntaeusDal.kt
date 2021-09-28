/*
    Implements the data access layer (DAL).
    The data access layer generates and executes requests to the database.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.ChargeDetails
import io.pleo.antaeus.models.CustomerAccount
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import io.pleo.antaeus.data.InvoiceTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.joda.time.DateTime


class AntaeusDal(private val db: Database) {
    fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }


    fun isInvoicePending(id: Int): Invoice? {
        return transaction(db) { 
            InvoiceTable
            .select {InvoiceTable.id.eq(id) }
            .andWhere {InvoiceTable.status.eq(InvoiceStatus.PENDING.toString()) }
            .firstOrNull()
            ?.toInvoice()
         }
    }

    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }

        return fetchInvoice(id)
    }

    fun fetchCustomer(id: Int): Customer? {
        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    fun createCustomer(currency: Currency): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
            } get CustomerTable.id
        }

        return fetchCustomer(id)
    }

    fun saveChargeDetails(invoice: Invoice): String? {
        val insertId: String;
        val id = transaction(db) {
            ChargeDetailsTable.insert {
                it[this.invoiceId] = invoice.id
                it[this.paymentMethod] = "Credit Card"
                it[this.createdAt] = DateTime.now()
                it[this.updatedAt] = DateTime.now()
            }
        }


        insertId = id.toString()
        return insertId
    }

    fun fetchCustomerAccount(id: Int): CustomerAccount? {
        return transaction(db) {
            CustomerAccountTable
                .select { CustomerAccountTable.customerId.eq(id) }
                .firstOrNull()
                ?.toCustomerAccount()
        }
    }

    fun updateCustomerAccount(customerAccount: CustomerAccount): Int? {
        val id = transaction(db) {
            CustomerAccountTable.update({CustomerAccountTable.customerId.eq(customerAccount.customerId)}) 
            { 
                it[this.customerId] =  customerAccount.customerId
                it[this.customerBalance] =  customerAccount.customerBalance!!.toBigDecimal()
                it[this.createdAt] =  customerAccount.createdAt
                it[this.updatedAt] =  customerAccount.updatedAt
            }
        }
        return id
    }

    fun updateInvoice(invoice: Invoice): Int? {
        val id = transaction(db) {
            InvoiceTable.update({InvoiceTable.id.eq(invoice.id)}) 
            { 
                it[this.id] =  invoice.id
                it[this.customerId] =  invoice.customerId
                it[this.currency] =  invoice.amount.currency.toString()
                it[this.value] =  invoice.amount.value
                it[this.status] =  invoice.status.toString()
            }
        }
        return id
    }
}
