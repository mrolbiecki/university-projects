-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- Wypisz w kolejności alfabetycznej nazwy firm, 
-- które zawarły trzy lub mniej transakcji kupna, 
-- których łączna cena stanowi co najmniej 90% ich łącznych wydatków. 
-- (Firmy, które zawarły co najwyżej 3 transakcje kupna oczywiście spełniają ten warunek).

with total_ex as (
    select 
        buyer,
        sum(price) ex
    from sales s
    group by buyer
),
ex_ranked as (
    select
        buyer,
        price,
        row_number() over (partition by buyer order by price desc) r
    from sales s
),
top_3_ex as (
    select
        buyer,
        sum(price) ex
    from ex_ranked
    where r <= 3
    group by buyer
)
select c.name
from
    company c
    left join total_ex te on te.buyer = c.name
    left join top_3_ex t3 on t3.buyer = c.name
where 
    coalesce(t3.ex, 0.0) >= 0.9 * coalesce(te.ex, 0.0)
order by c.name asc;
