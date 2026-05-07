--TASK 1
SELECT "Н_ЛЮДИ"."ОТЧЕСТВО", "Н_СЕССИЯ"."ДАТА" FROM "Н_ЛЮДИ" 
  LEFT JOIN "Н_СЕССИЯ" ON "Н_ЛЮДИ"."ИД"="Н_СЕССИЯ"."ЧЛВК_ИД" 
  WHERE "Н_ЛЮДИ"."ФАМИЛИЯ"<'Петров' AND "Н_СЕССИЯ"."ЧЛВК_ИД">100622;

--TASK 2
SELECT "Н_ЛЮДИ"."ИМЯ", "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД" FROM "Н_ЛЮДИ" 
  RIGHT JOIN "Н_ОБУЧЕНИЯ" ON "Н_ЛЮДИ"."ИД"="Н_ОБУЧЕНИЯ"."ЧЛВК_ИД" 
  RIGHT JOIN "Н_УЧЕНИКИ" ON "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
  WHERE "Н_ЛЮДИ"."ИД"<163484 AND "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД"="105590" AND "Н_УЧЕНИКИ"."ИД"<1;
--?


--Task 3
SELECT COUNT(*) 
  FROM "Н_ЛЮДИ" 
  WHERE EXTRACT(YEAR FROM AGE("Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ", current_date))>25 
  AND "Н_ЛЮДИ"."ИД" IN (
    SELECT DISTINCT "Н_УЧЕНИКИ"."ЧЛВК_ИД" 
    FROM "Н_УЧЕНИКИ" 
    JOIN "Н_ПЛАНЫ"
    ON "Н_УЧЕНИКИ"."ПЛАН_ИД"="Н_ПЛАНЫ"."ИД" 
    WHERE "Н_ПЛАНЫ"."ОТД_ИД" IN (
      SELECT "ИД" FROM "Н_ОТДЕЛЫ" 
      WHERE "КОРОТКОЕ_ИМЯ"='КТиУ'
    )
);
--check it allso why left join

--TASK 4 old
SELECT "Н_ЛЮДИ"."ОТЧЕСТВО", COUNT(*) 
  FROM "Н_ЛЮДИ"
  WHERE "Н_ЛЮДИ"."ОТЧЕСТВО" IN (
    SELECT "Н_ЛЮДИ"."ОТЧЕСТВО" 
      FROM (
        SELECT "Н_ЛЮДИ"."ОТЧЕСТВО" 
        FROM "Н_ЛЮДИ" 
        WHERE "Н_ЛЮДИ"."ИД" IN (
          SELECT DISTINCT "Н_УЧЕНИКИ"."ЧЛВК_ИД" 
          FROM "Н_УЧЕНИКИ" 
          LEFT JOIN "Н_ПЛАНЫ" ON "Н_УЧЕНИКИ"."ПЛАН_ИД"="Н_ПЛАНЫ"."ИД" 
          WHERE "Н_ПЛАНЫ"."ОТД_ИД" IN (SELECT "ИД" FROM "Н_ОТДЕЛЫ" WHERE "КОРОТКОЕ_ИМЯ"='ВТ')
        )
      ) 
  GROUP BY "Н_ЛЮДИ"."ОТЧЕСТВО" HAVING COUNT("Н_ЛЮДИ"."ОТЧЕСТВО")>50
);


-- TASK 4
SELECT "ОТЧЕСТВО", COUNT(*) 
  FROM "Н_ЛЮДИ" 
  WHERE "ОТЧЕСТВО" IN (
    SELECT "ОТЧЕСТВО" 
      FROM (
        SELECT "ОТЧЕСТВО" 
        FROM "Н_ЛЮДИ" 
        WHERE "ИД" IN (
          SELECT DISTINCT "ЧЛВК_ИД" 
          FROM "Н_УЧЕНИКИ" 
          WHERE "ПЛАН_ИД" in (
            SELECT "ИД" 
            FROM "Н_ПЛАНЫ" 
            WHERE "НАПС_ИД" IN (
              SELECT "ИД" 
              FROM "Н_НАПРАВЛЕНИЯ_СПЕЦИАЛ" 
              WHERE "НС_ИД" IN(
                SELECT "ИД" 
                FROM "Н_НАПР_СПЕЦ" 
                WHERE lower("НАИМЕНОВАНИЕ")=('информатика и вычислительная техника')
              )
            )
          )
        )
      ) 
  GROUP BY "ОТЧЕСТВО" HAVING COUNT("ОТЧЕСТВО")>50)
GROUP BY "ОТЧЕСТВО";

--TASK 5
SELECT "ГРУППА", AVG("age") 
  FROM (
    SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) AS "age"
      FROM "Н_ЛЮДИ" 
      JOIN "Н_УЧЕНИКИ" 
      ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
      WHERE "ДАТА_РОЖДЕНИЯ"<current_date
  ) 
  GROUP BY ГРУППА 
  HAVING AVG("age")=(
    SELECT AVG("age") 
    FROM (
      SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) as "age" 
      FROM "Н_ЛЮДИ" 
      JOIN "Н_УЧЕНИКИ" 
      ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
      WHERE "ДАТА_РОЖДЕНИЯ"<current_date
    ) 
    WHERE "ГРУППА"='3100' 
    GROUP BY "ГРУППА"
  );

--
SELECT "ГРУППА", AVG("age") 
  FROM (
    SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) AS "age" 
    FROM "Н_ЛЮДИ" 
    JOIN "Н_УЧЕНИКИ" 
    ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
    WHERE "ДАТА_РОЖДЕНИЯ"<current_date
  ) 
  GROUP BY "ГРУППА" 
  HAVING AVG("age")=(
    SELECT AVG("age") 
    FROM (
        SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) as "age" 
        FROM "Н_ЛЮДИ" 
        JOIN "Н_УЧЕНИКИ" 
        ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
        WHERE "ДАТА_РОЖДЕНИЯ"<current_date
    ) 
    WHERE "ГРУППА"='3100' 
    GROUP BY "ГРУППА");

--
SELECT "ГРУППА", AVG("age") 
  FROM (
    SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) AS "age" 
    FROM "Н_ЛЮДИ" 
    JOIN "Н_УЧЕНИКИ" 
    ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
    WHERE "ДАТА_РОЖДЕНИЯ"<current_date) 
    GROUP BY "ГРУППА" 
    HAVING ROUND(AVG("age"), 2)=ROUND(
      (
        SELECT AVG("age") 
        FROM (
          SELECT "ГРУППА", EXTRACT(YEAR FROM AGE(current_date, "ДАТА_РОЖДЕНИЯ")) as "age" 
          FROM "Н_ЛЮДИ" 
          JOIN "Н_УЧЕНИКИ" 
          ON "Н_ЛЮДИ"."ИД"="Н_УЧЕНИКИ"."ЧЛВК_ИД" 
          WHERE "ДАТА_РОЖДЕНИЯ"<current_date
        ) 
        WHERE "ГРУППА"='3100' 
        GROUP BY "ГРУППА"
      ), 
  2);


-- TASK 6
SELECT 
    "Н_УЧЕНИКИ"."ГРУППА",
    "Н_ЛЮДИ"."ИД",
    "Н_ЛЮДИ"."ФАМИЛИЯ",
    "Н_ЛЮДИ"."ИМЯ",
    "Н_ЛЮДИ"."ОТЧЕСТВО",
    "Н_УЧЕНИКИ"."СОСТОЯНИЕ",
    "Н_УЧЕНИКИ"."В_СВЯЗИ_С"
FROM "Н_ЛЮДИ",
     "Н_УЧЕНИКИ"
WHERE "Н_УЧЕНИКИ"."ЧЛВК_ИД" = "Н_ЛЮДИ"."ИД"
  AND "Н_УЧЕНИКИ"."ПЛАН_ИД" IN (
        SELECT "Н_ПЛАНЫ"."ИД"
        FROM "Н_ПЛАНЫ"
        WHERE "Н_ПЛАНЫ"."ФО_ИД" IN (
                SELECT "Н_ФОРМЫ_ОБУЧЕНИЯ"."ИД"
                FROM "Н_ФОРМЫ_ОБУЧЕНИЯ"
                WHERE lower("Н_ФОРМЫ_ОБУЧЕНИЯ"."НАИМЕНОВАНИЕ") = 'заочная'
        )AND "Н_ПЛАНЫ"."КУРС" = 1
  )
  AND "Н_УЧЕНИКИ"."НАЧАЛО" = '2012-09-01'::timestamp;
--PLEASE CHECK IT

--TASK 7
SELECT * FROM "Н_ЛЮДИ" 
  WHERE "Н_ЛЮДИ"."ИД" NOT IN (
    SELECT DISTINCT "Н_УЧЕНИКИ"."ЧЛВК_ИД" 
    FROM "Н_УЧЕНИКИ" 
    JOIN "Н_ПЛАНЫ" 
    ON "Н_УЧЕНИКИ"."ПЛАН_ИД"="Н_ПЛАНЫ"."ИД" 
    WHERE "Н_ПЛАНЫ"."ОТД_ИД" IN (
      SELECT "ИД" FROM "Н_ОТДЕЛЫ" WHERE "КОРОТКОЕ_ИМЯ"='КТиУ'
    )
  )
);