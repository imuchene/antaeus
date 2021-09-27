package io.pleo.antaeus.core.services
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import org.jetbrains.exposed.sql.statements.InsertStatement

class ChargeDetailsService(private val dal: AntaeusDal) {
    fun saveChargeDetails(invoice: Invoice): InsertStatement<Number>?{
        return dal.saveChargeDetails(invoice)
    }
}
