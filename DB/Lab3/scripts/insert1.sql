BEGIN;

INSERT INTO Persons (name, sex, birthdate) VALUES
('Иван Петров', 'male', '1985-03-15'),
('Анна Смирнова', 'female', '1990-07-22'),
('Дмитрий Козлов', 'male', '1978-11-02'),
('Елена Морозова', 'female', '1995-09-12'),
('Сергей Новиков', 'male', '1982-04-18'),
('Ольга Васильева', 'female', '1988-12-30');


INSERT INTO Places (name, coord_x, coord_y, description) VALUES
('Заброшенная бензоколонка', 42, -73, 'Старая АЗС на окраине города, окружённая ржавыми цистернами и высокой травой'),
('Городская библиотека', 15, 20, 'Старинное здание с высокими потолками и запахом пыльных книг'),
('Окраина леса', -30, 55, 'Тёмный лес с вековыми дубами, место городских легенд'),
('Подземный бункер', 5, -120, 'Секретный объект времён холодной войны, вход скрыт среди руин');


INSERT INTO Events (event_type, description) VALUES
('meeting', 'Первая встреча группы в библиотеке, где обсуждали странные слухи о пропажах'),
('conversation', 'Разговор Ивана и Анны на окраине леса о таинственном запахе бензина'),
('journey', 'Путешествие к заброшенной бензоколонке на старом грузовике'),
('battle', 'Схватка с неизвестными существами в подземном бункере'),
('celebration', 'Празднование возвращения из опасной экспедиции в городском кафе'),
('accident', 'Взрыв цистерны на АЗС, едва не унёсший жизни участников'),
('ritual', 'Загадочный обряд, найденный в дневнике, который они провели на рассвете'),
('discovery', 'Обнаружение старинного артефакта в подвале библиотеки'),
('creation', 'Создание карты мест, связанных с аномальной активностью'),
('destruction', 'Уничтожение опасного механизма внутри бункера');


INSERT INTO Thoughts (person_id, event_id, text) VALUES
-- For event 'meeting' (id 1)
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'meeting'),
  'Странно, что нас собралось так много. Кажется, город сам выталкивает нас на поиски правды.'),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'meeting'),
 'Библиотекарь сказала, что эти книги никто не брал пятьдесят лет. Как они оказались на видном месте?'),

-- For event 'conversation' (id 2)
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'conversation'),
 'Парадоксально, но он испугал нас одновременно и больше и меньше предыдущего: сам по себе он был вполне зауряден, но с учетом места и обстоятельств -- невозможен, а потому заставил нас похолодеть от страха. Ведь пахло не чем иным, как бензином. Единственное, что приходило на ум, не связано ли это как-то с Гедни.'),
((SELECT id FROM Persons WHERE name = 'Елена Морозова'), (SELECT id FROM Events WHERE event_type = 'conversation'),
 'Я слышала это имя раньше. Гедни — так звали инженера, который проектировал тот бункер.'),

-- For event 'discovery' (id 8)
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'discovery'),
 'Артефакт пульсировал слабым светом. Моя рука онемела, но я не мог его отпустить.'),
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'discovery'),
 'Это не просто металл. Внутри будто бы заключена энергия, не поддающаяся объяснению.'),

-- For event 'battle' (id 4)
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'battle'),
 'Мы стреляли по теням. Самое страшное — они не издавали ни звука.'),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'battle'),
 'Если бы не артефакт, мы бы не выжили. Он создал барьер в последнюю секунду.');


INSERT INTO Objects (obj_type, name, place_id) VALUES
('tool', 'Ржавый гаечный ключ', (SELECT id FROM Places WHERE name = 'Заброшенная бензоколонка')),
('weapon', 'Старый пистолет', (SELECT id FROM Places WHERE name = 'Подземный бункер')),
('building', 'Сторожевая вышка', (SELECT id FROM Places WHERE name = 'Окраина леса')),
('vehicle', 'Грузовик ГАЗ-66', (SELECT id FROM Places WHERE name = 'Заброшенная бензоколонка')),
('artifact', 'Светящийся шар', (SELECT id FROM Places WHERE name = 'Городская библиотека')),
('natural', 'Корень мандрагоры', (SELECT id FROM Places WHERE name = 'Окраина леса')),
('document', 'Дневник инженера Гедни', (SELECT id FROM Places WHERE name = 'Подземный бункер')),
('clothing', 'Плащ-невидимка', (SELECT id FROM Places WHERE name = 'Подземный бункер')),
('food', 'Консервы 1985 года', (SELECT id FROM Places WHERE name = 'Подземный бункер')),
('furniture', 'Старинный письменный стол', (SELECT id FROM Places WHERE name = 'Городская библиотека'));



INSERT INTO Emotions (person_id, event_id, emotion_type, intensity) VALUES
-- accident event (id 5)
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'accident'), 'fear', 10),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'accident'), 'fear', 9),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'accident'), 'surprise', 7),
-- discovery event (id 8)
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'discovery'), 'joy', 8),
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'discovery'), 'surprise', 9),
-- battle event (id 4)
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'battle'), 'anger', 7),
((SELECT id FROM Persons WHERE name = 'Елена Морозова'), (SELECT id FROM Events WHERE event_type = 'battle'), 'fear', 8),
-- celebration event (id 5)
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'celebration'), 'joy', 10),
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'celebration'), 'joy', 9),
-- ritual event (id 6)
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'ritual'), 'disgust', 6),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'ritual'), 'fear', 8);



INSERT INTO Participations (person_id, event_id, participate_role) VALUES
-- meeting
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'meeting'), 'organizer'),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'meeting'), 'participant'),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'meeting'), 'initiator'),
-- conversation
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'conversation'), 'protagonist'),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'conversation'), 'protagonist'),
-- journey
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'journey'), 'leader'),
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'journey'), 'participant'),
((SELECT id FROM Persons WHERE name = 'Елена Морозова'), (SELECT id FROM Events WHERE event_type = 'journey'), 'helper'),
-- battle
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'battle'), 'antagonist'),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'battle'), 'victim'),
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'battle'), 'helper'),
-- celebration
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'celebration'), 'organizer'),
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'celebration'), 'participant'),
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'celebration'), 'bystander'),
-- accident
((SELECT id FROM Persons WHERE name = 'Елена Морозова'), (SELECT id FROM Events WHERE event_type = 'accident'), 'victim'),
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'accident'), 'witness'),
-- ritual
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'ritual'), 'initiator'),
((SELECT id FROM Persons WHERE name = 'Ольга Васильева'), (SELECT id FROM Events WHERE event_type = 'ritual'), 'participant'),
-- discovery
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'discovery'), 'protagonist'),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'discovery'), 'witness'),
((SELECT id FROM Persons WHERE name = 'Елена Морозова'), (SELECT id FROM Events WHERE event_type = 'discovery'), 'helper'),
-- creation
((SELECT id FROM Persons WHERE name = 'Дмитрий Козлов'), (SELECT id FROM Events WHERE event_type = 'creation'), 'leader'),
((SELECT id FROM Persons WHERE name = 'Сергей Новиков'), (SELECT id FROM Events WHERE event_type = 'creation'), 'participant'),
-- destruction
((SELECT id FROM Persons WHERE name = 'Иван Петров'), (SELECT id FROM Events WHERE event_type = 'destruction'), 'antagonist'),
((SELECT id FROM Persons WHERE name = 'Анна Смирнова'), (SELECT id FROM Events WHERE event_type = 'destruction'), 'helper');


INSERT INTO Conditions (time, place_id) VALUES
('2024-09-15 10:00:00+03', (SELECT id FROM Places WHERE name = 'Городская библиотека')),
('2024-09-16 14:30:00+03', (SELECT id FROM Places WHERE name = 'Окраина леса')),
('2024-09-17 09:15:00+03', (SELECT id FROM Places WHERE name = 'Заброшенная бензоколонка')),
('2024-09-18 22:00:00+03', (SELECT id FROM Places WHERE name = 'Подземный бункер'));

COMMIT;