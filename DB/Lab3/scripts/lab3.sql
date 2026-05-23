--Если человек(Person) получил плохую эмоцию(Emotion) на событие(Event), 
--то на все события в которых он участвовал ранее с похожими conditions (Тоже место или рядом, или тот же день недели или тот же день годом ранее)
--изменяется мысль(Thought) на, то что событие было ужасным + менется эмоция от события.  
CREATE OR REPLACE FUNCTION awful_event()
RETURNS TRIGGER AS $$
DECLARE
  v_event_id       int;
  v_event_desc     varchar;
  v_event_time     timestamptz;
  v_place_id       int;
  v_place_name     varchar;
  v_coord_x        int;
  v_coord_y        int;
  v_similar_events integer[];
  eid              int;
BEGIN
  IF NEW.emotion_type IN ('sadness', 'anger', 'fear', 'disgust') AND NEW.event_id IS NOT NULL THEN
    SELECT description INTO v_event_desc FROM Events WHERE id = NEW.event_id;
  
    SELECT place_id, time INTO v_place_id, v_event_time
    FROM Conditions
    WHERE event_id = NEW.event_id;

    SELECT coord_x, coord_y, name INTO v_coord_x, v_coord_y, v_place_name
    FROM Places
    WHERE id = v_place_id;

    v_similar_events := ARRAY(
      SELECT event_id FROM Conditions WHERE place_id = v_place_id
    );

    v_similar_events := v_similar_events || ARRAY(
      SELECT c.event_id
      FROM Conditions c
      WHERE c.place_id IN (
        SELECT p.id FROM Places p
        WHERE p.coord_x BETWEEN v_coord_x - 25 AND v_coord_x + 25
          AND p.coord_y BETWEEN v_coord_y - 25 AND v_coord_y + 25
      )
      AND (
        EXTRACT(DOW FROM c.time) = EXTRACT(DOW FROM v_event_time)
        OR TO_CHAR(c.time, 'DD-MM') = TO_CHAR(v_event_time, 'DD-MM')
      )
    );

    v_similar_events := ARRAY(
      SELECT DISTINCT unnest(v_similar_events)
      EXCEPT
      SELECT NEW.event_id
    );

    FOREACH eid IN ARRAY v_similar_events LOOP
        UPDATE Thoughts
        SET text = 'Мероприятие прошло ужасно'
        WHERE event_id = eid AND person_id = NEW.person_id;

        UPDATE Emotions
        SET emotion_type = NEW.emotion_type
        WHERE event_id = eid AND person_id = NEW.person_id;

        RAISE NOTICE 'Memory about event % updated', eid;
    END LOOP;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--DROP TRIGGER IF EXISTS track_awful_event ON emotions;

CREATE OR REPLACE TRIGGER  track_awful_event
AFTER INSERT ON emotions
FOR EACH ROW
WHEN (pg_trigger_depth() = 0)
EXECUTE FUNCTION awful_event();