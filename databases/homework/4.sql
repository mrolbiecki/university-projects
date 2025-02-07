WITH RECURSIVE erdos AS (
	SELECT 'Pilipczuk Mi'::varchar AS Autor, 0 AS Liczba
	UNION
	SELECT a.Autor, e.Liczba + 1 AS Liczba
	FROM Autorstwo a
	JOIN erdos e ON a.Praca IN (SELECT Praca FROM Autorstwo WHERE Autor = e.Autor)
	WHERE e.Liczba < 10
)
SELECT
ID as autor,
MIN(COALESCE(e.Liczba)) AS liczba_pilipczuka
FROM Autor a
LEFT JOIN erdos e ON a.ID = e.Autor
GROUP BY a.ID
ORDER BY liczba_pilipczuka;
