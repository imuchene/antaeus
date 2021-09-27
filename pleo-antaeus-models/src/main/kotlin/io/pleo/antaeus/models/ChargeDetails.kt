package io.pleo.antaeus.models
import org.joda.time.DateTime

data class ChargeDetails (
  val invoiceId: Int,
  val paymentMethod: String,
  val createdAt: DateTime,
  val updatedAt: DateTime
)