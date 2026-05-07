CREATE OR REPLACE FUNCTION check_event_has_correct_conditions()
RETURNS TRIGGER AS $$
DECLARE
  coord_x int;
  coord_y int;
  event_time timestamptz;
BEGIN
  SELECT p.coord_x, p.coord_y
  INTO coord_x, coord_y
  FROM Places p
  WHERE p.id = NEW.place_id;

  IF coord_x IS NULL OR coord_y IS NULL THEN
    RAISE EXCEPTION 'Place with id % does not exist or has no coordinates', NEW.place_id;
  END IF;

  IF coord_x < 0 OR coord_y < 0 THEN
    RAISE EXCEPTION 'Invalid coordinates: (% , %)', coord_x, coord_y;
  END IF;

  event_time := NEW.time;

  IF event_time > CURRENT_TIMESTAMP THEN
    RAISE EXCEPTION 'Event cannot happen in the future';
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS chech_conditions ON Conditions;

CREATE TRIGGER check_conditions
BEFORE INSERT OR UPDATE ON Conditions
FOR EACH ROW 
EXECUTE FUNCTION check_event_has_correct_conditions();