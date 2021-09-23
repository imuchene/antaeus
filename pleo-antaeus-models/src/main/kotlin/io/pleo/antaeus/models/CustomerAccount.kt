package io.pleo.antaeus.models
import java.time.LocalDateTime

data class CustomerAccount (
  val customerId: Int,
  val customerBalance: Float,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
)