package com.example.model

enum class PremiumPlan(
  val title: String,
  val priceFormatted: String,
  val billingPeriod: String,
  val badge: String?,
  val description: String,
  val isPopular: Boolean
) {
  MONTHLY(
    title = "Monthly",
    priceFormatted = "$1.99",
    billingPeriod = "/ month",
    badge = "Flexible",
    description = "Billed monthly. Cancel anytime without commitment.",
    isPopular = false
  ),
  YEARLY(
    title = "Yearly",
    priceFormatted = "$9.99",
    billingPeriod = "/ year",
    badge = "Save 58%",
    description = "Billed annually ($0.83/mo). 7-day free trial included.",
    isPopular = true
  ),
  LIFETIME(
    title = "Lifetime",
    priceFormatted = "$24.99",
    billingPeriod = "one-time",
    badge = "Best Value",
    description = "Pay once, cherish forever. All current and future features.",
    isPopular = false
  )
}
