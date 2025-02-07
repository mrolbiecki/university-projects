-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- (*) Zysk firmy obliczamy następująco. 
-- Każda firma rozpoczyna od swojego bilansu. 
-- Następnie, przekazuje każdemu swojemu udziałowcowi ułamek bilansu 
-- zgodny z udziałem danego udziałowca. 
-- Następnie, każdy udziałowiec przekazuje stosowne ułamki 
-- otrzymanych w ten sposób kwot swoim udziałowcom, 
-- którzy przekazują stosowne ułamki swoim udziałowcom, itd. 
-- Dzięki założeniu, że struktura własności jest acykliczna, 
-- procedura ta dobiegnie końca w skończonej liczbie kroków, 
-- ograniczonej przez liczbę firm w bazie. 
-- Dla każdej firmy wylicz jej zysk, posortuj malejąco po zysku. 

with recursive seller_sum as (
    select 
        seller,
        sum(price) sum
    from 
        sales
    group by seller
),
buyer_sum as (
    select 
        buyer,
        sum(price) sum
    from
        sales
    group by
        buyer
),
bilans as (
    select 
        name,
        coalesce(coalesce(ss.sum, 0) - coalesce(bs.sum, 0)) bil
    from 
        company c
        left join seller_sum ss on ss.seller = c.name
        left join buyer_sum bs on bs.buyer = c.name
    order by
        bil desc
),
zalezy as (
    select 
        name kto,
        name od,
        bil zys
    from bilans

    union 

    select 
        kto,
        shareholder od,
        zys * fraction
    from
        zalezy z
        join ownership o on z.od = o.company
)
select 
    od nazwa,
    sum(zys) zysk
from zalezy
group by nazwa
order by zysk desc;
