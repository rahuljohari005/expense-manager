import { useEffect, useState, useRef } from "react";
import { api } from "./api";
import type { Dashboard, Expense, Rule } from "./types";
const money = (n: number) =>
  `₹${n.toLocaleString("en-IN", { minimumFractionDigits: 2 })}`;

export default function App() {
  const [tab, setTab] = useState<"dashboard" | "expenses" | "import" | "rules">(
    "dashboard",
  );
  const [month, setMonth] = useState(new Date().toISOString().slice(0, 7));
  const [dashboard, setDashboard] = useState<Dashboard | null>(null);
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [rules, setRules] = useState<Rule[]>([]);
  const [message, setMessage] = useState("");
  const addFormRef = useRef<HTMLFormElement>(null);
  const impFormRef = useRef<HTMLFormElement>(null);
  const addRuleFormRef = useRef<HTMLFormElement>(null);
  const refresh = async () => {
    try {
      const [d, e, r] = await Promise.all([
        api.dashboard(month),
        api.expenses(),
        api.rules(),
      ]);
      setDashboard(d);
      setExpenses(e);
      setRules(r);
    } catch (e) {
      setMessage((e as Error).message);
    }
  };
  useEffect(() => {
    refresh();
  }, [month]);
  const add = async (ev: React.FormEvent<HTMLFormElement>) => {
    ev.preventDefault();
    const f = new FormData(ev.currentTarget);
    try {
      const x = await api.addExpense({
        date: f.get("date"),
        amount: Number(f.get("amount")),
        vendorName: f.get("vendorName"),
        description: f.get("description"),
      });
      setMessage(`Saved as ${x.category}${x.anomaly ? " — ANOMALY" : ""}`);
      if (addFormRef.current) addFormRef.current.reset();
      await refresh();
    } catch (e) {
      setMessage((e as Error).message);
    }
  };
  const imp = async (ev: React.FormEvent<HTMLFormElement>) => {
    ev.preventDefault();
    const input = ev.currentTarget.elements.namedItem(
      "file",
    ) as HTMLInputElement;
    if (!input.files?.[0]) return;
    try {
      const x = await api.importCsv(input.files[0]);
      setMessage(`${x.imported} imported; ${x.errors.length} rejected.`);
      if (impFormRef.current) impFormRef.current.reset();
      await refresh();
    } catch (e) {
      setMessage((e as Error).message);
    }
  };
  const addRule = async (ev: React.FormEvent<HTMLFormElement>) => {
    ev.preventDefault();
    const f = new FormData(ev.currentTarget);
    try {
      await api.addRule({
        vendorPattern: f.get("vendorPattern"),
        category: f.get("category"),
      });
      if (addRuleFormRef.current) addRuleFormRef.current.reset();
      await refresh();
    } catch (e) {
      setMessage((e as Error).message);
    }
  };
  return (
    <div className="app">
      <header>
        <div>
          <h1>Expense Manager</h1>
          <p>Daily expense tracking</p>
        </div>
        <nav>
          {(["dashboard", "expenses", "import", "rules"] as const).map((x) => (
            <button
              className={tab === x ? "active" : ""}
              onClick={() => setTab(x)}
              key={x}
            >
              {x}
            </button>
          ))}
        </nav>
      </header>
      {message && (
        <div className="message">
          {message}
          <button onClick={() => setMessage("")}>×</button>
        </div>
      )}
      {tab === "dashboard" && dashboard && (
        <main>
          <section className="toolbar">
            <label>
              Month{" "}
              <input
                type="month"
                value={month}
                onChange={(e) => setMonth(e.target.value)}
              />
            </label>
          </section>
          <div className="cards">
            <div className="card">
              <span>Monthly total</span>
              <strong>{money(dashboard.monthlyTotal)}</strong>
            </div>
            <div className="card">
              <span>Anomalies</span>
              <strong>{dashboard.anomalyCount}</strong>
            </div>
          </div>
          <div className="grid">
            <section className="panel">
              <h2>Monthly totals per category</h2>
              {dashboard.categoryTotals.map((x) => (
                <div className="row" key={x.category}>
                  <span>{x.category}</span>
                  <b>{money(x.total)}</b>
                </div>
              ))}
            </section>
            <section className="panel">
              <h2>Top 5 vendors</h2>
              {dashboard.topVendors.map((x) => (
                <div className="row" key={x.vendorName}>
                  <span>{x.vendorName}</span>
                  <b>{money(x.total)}</b>
                </div>
              ))}
            </section>
          </div>
          <section className="panel">
            <h2>Anomalies</h2>
            <ExpenseTable expenses={dashboard.anomalies} />
          </section>
        </main>
      )}
      {tab === "expenses" && (
        <main>
          <section className="panel">
            <h2>Add Expense</h2>
            <form className="form" ref={addFormRef} onSubmit={add}>
              <input
                name="date"
                type="date"
                required
                defaultValue={new Date().toISOString().slice(0, 10)}
              />
              <input
                name="amount"
                type="number"
                min="0.01"
                step="0.01"
                placeholder="Amount"
                required
              />
              <input name="vendorName" placeholder="Vendor Name" required />
              <input name="description" placeholder="Description" />
              <button>Add Expense</button>
            </form>
          </section>
          <section className="panel">
            <h2>Expenses</h2>
            <ExpenseTable expenses={expenses} />
          </section>
        </main>
      )}
      {tab === "import" && (
        <main>
          <section className="panel">
            <h2>Upload CSV</h2>
            <p>Required columns: date, amount, vendorName, description</p>
            <form ref={impFormRef} onSubmit={imp} className="form">
              <input name="file" type="file" accept=".csv" required />
              <button>Import CSV</button>
            </form>
          </section>
        </main>
      )}
      {tab === "rules" && (
        <main>
          <section className="panel">
            <h2>Vendor-to-category rules</h2>
            <form ref={addRuleFormRef} onSubmit={addRule} className="form">
              <input
                name="vendorPattern"
                placeholder="Vendor pattern"
                required
              />
              <input name="category" placeholder="Category" required />
              <button>Add Rule</button>
            </form>
            <table>
              <thead>
                <tr>
                  <th>Vendor pattern</th>
                  <th>Category</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {rules.map((r) => (
                  <tr key={r.id}>
                    <td>{r.vendorPattern}</td>
                    <td>{r.category}</td>
                    <td>
                      <button
                        onClick={async () => {
                          await api.deleteRule(r.id);
                          refresh();
                        }}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
        </main>
      )}
    </div>
  );
}
function ExpenseTable({ expenses }: { expenses: Expense[] }) {
  if (!expenses.length) return <p>No anomalies.</p>;
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Vendor</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          {expenses.map((e) => (
            <tr className={e.anomaly ? "anomaly" : ""} key={e.id}>
              <td>{e.date}</td>
              <td>{e.vendorName}</td>
              <td>{e.category}</td>
              <td>{money(e.amount)}</td>
              <td>{e.description}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
