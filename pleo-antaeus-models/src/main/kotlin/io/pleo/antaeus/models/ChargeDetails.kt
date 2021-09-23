package io.pleo.antaeus.models
import java.time.LocalDateTime

data class ChargeDetails (
  val invoiceId: Int,
  val paymentMethod: String,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)