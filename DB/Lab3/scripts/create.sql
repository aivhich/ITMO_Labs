BEGIN;
CREATE TYPE SexType AS ENUM ('male', 'female');
CREATE TYPE EventType AS ENUM ('meeting', 'conversation', 'journey', 'battle', 'celebration',  'accident', 'ritual', 'discovery', 'creation', 'destruction');
CREATE TYPE ObjectType AS ENUM ('tool', 'weapon', 'building', 'vehicle', 'artifact', 'natural', 'document', 'clothing', 'food', 'furniture');
CREATE TYPE ParticipateRole AS ENUM ('organizer', 'participant', 'witness', 'victim', 'protagonist', 'antagonist', 'helper', 'bystander', 'initiator', 'leader');
CREATE TYPE EmotionType AS ENUM ('joy', 'sadness', 'anger', 'fear', 'surprise', 'disgust');


CREATE TABLE IF NOT EXISTS Persons (
  id serial,
  name varchar(255) NOT NULL,
  sex SexType NOT NULL,
  birthdate date NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Events (
  id serial,
  event_type EventType NOT NULL,
  description text NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Places (
  id serial,
  name varchar(255) NOT NULL,
  coord_x int NOT NULL,
  coord_y int NOT NULL,
  description varchar(1024),
  PRIMARY KEY(id)
);

/**/

CREATE TABLE IF NOT EXISTS Thoughts (
  id serial,
  person_id int REFERENCES Persons(id) ON DELETE SET NULL,
  event_id int REFERENCES Events(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Objects(
  id serial,
  obj_type ObjectType NOT NULL,
  name varchar(255) NOT NULL,
  place_id int REFERENCES Places(id) ON DELETE CASCADE,
  PRIMARY KEY(id)
);

/**/

CREATE TABLE IF NOT EXISTS Emotions(
  id serial,
  person_id int REFERENCES Persons(id) ON DELETE CASCADE,
  event_id int REFERENCES Events(id) ON DELETE SET NULL,
  emotion_type EmotionType NOT NULL,
  intensity int CONSTRAINT rangeOfIntensity CHECK (intensity>0 and intensity<=10),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Participations(
  id serial,
  person_id int REFERENCES Persons(id) ON DELETE CASCADE,
  event_id int REFERENCES Events(id) ON DELETE CASCADE,
  participate_role ParticipateRole NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Conditions(
  id serial,
  time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  event_id int UNIQUE REFERENCES Events(id) ON DELETE CASCADE,
  place_id int REFERENCES Places(id) ON DELETE CASCADE,
  PRIMARY KEY(id)
);


COMMIT;