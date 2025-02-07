WITH d3 AS (
    SELECT g.tgt a, COUNT(*) c
    FROM e e
    JOIN e f ON e.tgt = f.src
    JOIN e g ON f.tgt = g.src
    WHERE e.src = 0
    GROUP BY g.tgt
),
d2 AS (
    SELECT h.src b, COUNT(*) c
    FROM e h
    JOIN e i ON h.tgt = i.src
    GROUP BY h.src
)
SELECT SUM(d2.c * d3.c) AS total_count
FROM d3
JOIN d2 ON a = b;
