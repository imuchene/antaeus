/*
    Defines database tables and their schemas.
    To be used by `AntaeusDal`.
 */

package io.pleo.antaeus.data

import org.jetbrains.exposed.sql.Table

object InvoiceTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val currency = varchar("currency", 3)
    val value = decimal("value", 1000, 2)
    val customerId = reference("customer_id", CustomerTable.id)
    val status = text("status")
}

object CustomerTable : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val currency = varchar("currency", 3)
}

object ChargeDetailsTable: Table() {
    val invoiceId = reference("invoice_id", InvoiceTable.id)
    val paymentMethod = varchar("payment_method", 45)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}

object CustomerAccountTable: Table() {
    val customerId = reference("customer_id", CustomerTable.id)
    val customerBalance = decimal("customer_balance", 1000, 2)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}
