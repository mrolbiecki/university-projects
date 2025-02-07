-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- Bilans firmy to różnica między sumą cen jej sprzedaży a sumą cen jej zakupów.
-- Dla każdej firmy wypisz jej bilans, posortuj malejąco po bilansie.

with seller_sum as (
    select 
        seller,
        sum(price) sum
    from sales
    group by seller
),
buyer_sum as (
    select 
        buyer,
        sum(price) sum
    from sales
    group by buyer
)
select 
    name,
    coalesce(coalesce(ss.sum, 0) - coalesce(bs.sum, 0)) bilans
from 
    company c
    left join seller_sum ss on ss.seller = c.name
    left join buyer_sum bs on bs.buyer = c.name
order by bilans desc;