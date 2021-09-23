package io.pleo.antaeus.core.exceptions

class InvoiceNotFoundException(id: Int) : EntityNotFoundException("Invoice", id)

class InvoiceAlreadyPaidException() : Exception("Invoice Already Paid")

