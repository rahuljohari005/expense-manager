# Short Design Note

1. Vendor categorization uses a database-backed vendor-to-category rule table with case-insensitive substring matching.
2. The resolved category is stored on every expense so historical records stay stable when rules change.
3. Before saving an expense, the backend calculates the existing average amount for its category.
4. An expense is an anomaly when its amount is strictly greater than three times that average.
5. CSV imports call the same expense service used by manual entry, keeping the business rules identical.
6. Dashboard totals and vendor rankings are calculated by backend aggregation queries.
7. The persistent model is intentionally small: expenses and vendor/category rules.
8. Authentication, multi-user accounts and cloud deployment are omitted because they are not core requirements.
