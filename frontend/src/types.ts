export type Expense={id:number;date:string;amount:number;vendorName:string;description:string;category:string;anomaly:boolean};
export type Rule={id:number;vendorPattern:string;category:string};
export type Dashboard={month:string;monthlyTotal:number;categoryTotals:{category:string;total:number}[];topVendors:{vendorName:string;total:number}[];anomalyCount:number;anomalies:Expense[]};
