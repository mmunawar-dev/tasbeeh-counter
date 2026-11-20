package com.example.model

enum class PremiumPlan(
  val title: String,
  val priceFormatted: String,
  val billingPeriod: String,
  val badge: String?,
  val description: String,
  val isPopular: Boolean
) {
  WEEKLY(
    title = "Weekly Premium",
    priceFormatted = "Rs 820.00",
    billingPeriod = "/Week",
    badge = "3 Day Free Trial",
    description = "Billed weekly. Cancel anytime.",
    isPopular = false
  ),
  YEARLY(
    title = "Yearly Premium",
    priceFormatted = "Rs 5,600.00",
    billingPeriod = "/Year",
    badge = "Best Value",
    description = "(~Rs467 per month). 3 Day Free Trial included.",
    isPopular = true
  ),
  LIFETIME(
    title = "Lifetime",
    priceFormatted = "Rs 12,000.00",
    billingPeriod = "one-time",
    badge = "Lifetime Access",
    description = "Pay once, cherish forever.",
    isPopular = false
  )
}
