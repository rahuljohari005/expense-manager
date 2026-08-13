import type {Dashboard,Expense,Rule} from './types';
const BASE='http://localhost:8080/api';
async function req<T>(url:string,options?:RequestInit):Promise<T>{const r=await fetch(BASE+url,options);if(!r.ok)throw new Error(await r.text()||'Request failed');return r.status===204?undefined as T:r.json();}
export const api={
 expenses:()=>req<Expense[]>('/expenses'),
 addExpense:(body:object)=>req<Expense>('/expenses',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}),
 importCsv:async(file:File)=>{const f=new FormData();f.append('file',file);return req<{imported:number;errors:string[]}>('/expenses/import',{method:'POST',body:f});},
 dashboard:(month:string)=>req<Dashboard>('/dashboard?month='+month),
 rules:()=>req<Rule[]>('/rules'),
 addRule:(body:object)=>req<Rule>('/rules',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)}),
 deleteRule:(id:number)=>req<void>('/rules/'+id,{method:'DELETE'})
};
