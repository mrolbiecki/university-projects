-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- Dla każdej firmy wypisz liczbę takich zakupów, 
-- które są co najmniej dwa razy droższe niż kolejny względem ceny 
-- zakup tej firmy (kolejny zakup musi istnieć). 
-- Posortuj wyniki malejąco względem tej liczby.

with ex_ranked as (
    select
        buyer,
        price,
        row_number() over (partition by buyer order by price desc) r
    from sales s
),
big_ex as (
    select
        buyer,
        price
    from ex_ranked er
    where
        price >= 2 * (
            select price 
            from ex_ranked er2 
            where 
                er2.buyer = er.buyer
                and er2.r = er.r + 1
            limit 1 
        )
)
select 
    name,
    count(price) cnt
from
    company c
    left join big_ex be on be.buyer = c.name
group by name
order by cnt desc;