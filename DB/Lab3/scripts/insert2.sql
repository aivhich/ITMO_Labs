BEGIN;

-- 1. Новое место #1 (будет связано с событием-источником плохой эмоции)
INSERT INTO Places (name, coord_x, coord_y, description) VALUES
('Заброшенная теплотрасса', 200, 300, 'Ржавые трубы, пар из-под земли, запах газа');

-- 2. Новое место #2 (рядом с первым, координаты отличаются на 10 единиц, что < 25)
INSERT INTO Places (name, coord_x, coord_y, description) VALUES
('Люк теплотрассы', 210, 305, 'Открытый люк, ведущий в ту же сеть труб');

-- 3. Новое событие – источник плохой эмоции (accident)
INSERT INTO Events (event_type, description) VALUES
('accident', 'Взрыв метана у теплотрассы, Иван получил ожоги');

-- 4. Новое событие – похожее (расследование) – будет изменено триггером.
--    Используем допустимый тип 'discovery' (обнаружение/расследование)
INSERT INTO Events (event_type, description) VALUES
('discovery', 'Расследование причин взрыва, осмотр места');

-- 5. Условия для события-взрыва (связь с местом #1, суббота, 24 мая 2025, 20:00)
INSERT INTO Conditions (time, event_id, place_id) VALUES
('2025-05-24 20:00:00+03',
 (SELECT id FROM Events WHERE description = 'Взрыв метана у теплотрассы, Иван получил ожоги'),
 (SELECT id FROM Places WHERE name = 'Заброшенная теплотрасса'));

-- 6. Условия для расследования (связь с местом #2, следующая суббота, 31 мая 2025, 20:00)
INSERT INTO Conditions (time, event_id, place_id) VALUES
('2025-05-31 20:00:00+03',
 (SELECT id FROM Events WHERE description = 'Расследование причин взрыва, осмотр места'),
 (SELECT id FROM Places WHERE name = 'Люк теплотрассы'));

-- 7. Участия: Иван Петров участвует в обоих событиях
INSERT INTO Participations (person_id, event_id, participate_role) VALUES
((SELECT id FROM Persons WHERE name = 'Иван Петров'),
 (SELECT id FROM Events WHERE description = 'Взрыв метана у теплотрассы, Иван получил ожоги'),
 'victim'),
((SELECT id FROM Persons WHERE name = 'Иван Петров'),
 (SELECT id FROM Events WHERE description = 'Расследование причин взрыва, осмотр места'),
 'organizer');

-- 8. Мысли Ивана о расследовании (позитивные, до срабатывания триггера)
INSERT INTO Thoughts (person_id, event_id, text) VALUES
((SELECT id FROM Persons WHERE name = 'Иван Петров'),
 (SELECT id FROM Events WHERE description = 'Расследование причин взрыва, осмотр места'),
 'Нужно докопаться до истины, это важное дело.');

-- 9. Эмоция Ивана о расследовании (позитивная, чтобы триггер её изменил)
INSERT INTO Emotions (person_id, event_id, emotion_type, intensity) VALUES
((SELECT id FROM Persons WHERE name = 'Иван Петров'),
 (SELECT id FROM Events WHERE description = 'Расследование причин взрыва, осмотр места'),
 'joy', 7);

-- 10. ЗАПУСК ЦЕПОЧКИ: вставляем отрицательную эмоцию (fear) для события-взрыва
INSERT INTO Emotions (person_id, event_id, emotion_type, intensity) VALUES
((SELECT id FROM Persons WHERE name = 'Иван Петров'),
 (SELECT id FROM Events WHERE description = 'Взрыв метана у теплотрассы, Иван получил ожоги'),
 'fear', 10);

COMMIT;