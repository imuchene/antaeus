package io.pleo.antaeus.models
import org.joda.time.DateTime

data class CustomerAccount (
  val customerId: Int,
  val customerBalance: Float,
  val createdAt: DateTime,
  val updatedAt: DateTime
)