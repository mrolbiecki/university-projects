-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- Wypisz w kolejności alfabetycznej nazwy firm, które sprzedały coś 
-- każdemu swojemu udziałowcowi. 
-- (Jeśli firma nie ma udziałowców, to oczywiście spełnia ten warunek.)

with shareholder_no_sale as (
    select
        company,
        shareholder
    from ownership o
    where 
        shareholder not in (
            select buyer
            from sales s
            where seller = company
        )
)
select name
from company
where
    name not in (
        select company
        from shareholder_no_sale
    )
order by name asc;