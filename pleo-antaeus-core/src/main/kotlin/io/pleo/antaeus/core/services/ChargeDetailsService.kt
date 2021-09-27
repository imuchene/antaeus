package io.pleo.antaeus.core.services
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice

class ChargeDetailsService(private val dal: AntaeusDal) {
     fun saveChargeDetails(invoice: Invoice): Boolean {
        val savedChargeDetails = dal.saveChargeDetails(invoice)

        if(savedChargeDetails is String){
            return true
        }
        else {
            return false
        }
    }
}
