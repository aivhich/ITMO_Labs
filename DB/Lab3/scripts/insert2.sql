BEGIN;

-- 8. Новое место (id=4, рядом с библиотекой)
INSERT INTO Places (id, name, coord_x, coord_y, description) VALUES
(4, 'Перекрёсток у библиотеки', 5, 0, 'Оживлённый перекрёсток, где произошла авария');

-- 9. Новое событие – авария (id=4)
INSERT INTO Events (id, event_type, description) VALUES
(4, 'accident', 'Авария на перекрёстке: столкновение двух автомобилей, Иван пострадал');

-- 10. Условия для аварии (id=4)
INSERT INTO Conditions (id, time, event_id, place_id) VALUES
(4, '2025-06-02 10:30:00+03', 4, 4);

-- 11. Участие Ивана в аварии (id=10)
INSERT INTO Participations (id, person_id, event_id, participate_role) VALUES
(10, 1, 4, 'victim');

-- 12. Мысль Ивана об аварии (id=10)
INSERT INTO Thoughts (id, person_id, event_id, text) VALUES
(10, 1, 4, 'Надо быть осторожнее на дорогах.');

-- 13. ВСТАВКА ОТРИЦАТЕЛЬНОЙ ЭМОЦИИ – ЗАПУСК ТРИГГЕРА
--     id=10, person_id=1 (Иван), event_id=4 (авария), emotion_type='fear'
INSERT INTO Emotions (id, person_id, event_id, emotion_type, intensity) VALUES
(10, 1, 4, 'fear', 10);

COMMIT;