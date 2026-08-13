# Mini Expense Manager

## Technologies
- React + TypeScript + Vite
- Java 25 + Maven
- PostgreSQL
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
```

Or use the included Docker Compose:
```bash
docker compose up -d postgres
```

### Backend
Requires Java 21 and Maven.
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`.

### Frontend
Requires Node.js 20+.
```bash
cd frontend
npm install
npm run dev
```

### CSV
```csv
date,amount,vendorName,description
2026-08-01,450,Swiggy,Lunch
2026-08-02,1200,Amazon,Headphones
```

## Assumptions
- Vendor rules are case-insensitive substring matches.
- Unmatched vendors use `Other`.
- Resolved category is stored on each expense.
- The average for anomaly detection is calculated from existing expenses before the new expense is saved.
- A category with no prior expenses cannot produce an anomaly.
- Authentication and multi-user support are outside the requested scope.
