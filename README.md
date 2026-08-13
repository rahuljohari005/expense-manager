# Mini Expense Manager

## Technologies
- React + TypeScript + Vite
- Java 25 + Maven
- PostgreSQL
- Spring Boot
- Spring Data JPA
- Apache Commons CSV

## Features
- Manual expense entry: date, amount, vendor and description.
- Automatic vendor-to-category categorization.
- CSV import for multiple expenses.
- Vendor/category rule maintenance.
- Anomaly detection: amount strictly greater than 3x the existing average for its category.
- Dashboard: monthly category totals, top 5 vendors, anomaly count and anomaly list.

## Setup

### Database

Run:

```sql
CREATE DATABASE expense_manager;
