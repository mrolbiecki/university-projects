-- Marcin Rolbiecki 
-- mr459488
-- gr. lab3
-- postgres

-- Firma X jest zależna od firmy Y jeśli firma Y ma udziały w firmie X, 
-- lub ma udziały w firmie, która ma udziały w firmie X, itd. 
-- Przyjmujemy również, że każda firma jest zależna sama od siebie. 
-- (Inaczej, relacja zależności jest domknięciem przechodnio-zwrotnym 
-- odwrotności relacji bycia udziałowcem.) 
-- Transakcja jest wewnętrzna, jeśli sprzedający i kupujący są zależni 
-- od tej samej firmy. Wylicz jaki procent sumarycznej ceny wszystkich 
-- transakcji stanowi sumaryczna cena transakcji wewnętrznych. 

with recursive zalezy as (
    select 
        name kto,
        name od
    from company

    union 

    select 
        kto,
        shareholder od
    from
        zalezy z
        join ownership o on z.od = o.company
),
suma_wew as (
    select sum(price)
    from sales
    where exists (
            select od from zalezy where kto = seller
            intersect
            select od from zalezy where kto = buyer
        )
)
select
    (select * from suma_wew) / sum(price) * 100 as procent
from sales;