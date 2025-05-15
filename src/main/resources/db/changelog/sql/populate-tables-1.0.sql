INSERT INTO countries (name)
VALUES ('Spain'),
       ('Germany'),
       ('England');

INSERT INTO users (email, password)
VALUES ('user1@example.com', '$2a$04$JzCV4sv7FANkRbFsen9x6O4PbNJACy6nTorh2Wq1dD7tRaT3Es7hi'),
       ('user2@example.com', '$2a$04$JzCV4sv7FANkRbFsen9x6O4PbNJACy6nTorh2Wq1dD7tRaT3Es7hi'),
       ('user3@example.com', '$2a$04$JzCV4sv7FANkRbFsen9x6O4PbNJACy6nTorh2Wq1dD7tRaT3Es7hi');

INSERT INTO teams (name, user_id, country_id, budget, team_value)
SELECT CONCAT('Team_', u.id),
       u.id,
       c.id,
       3000000,
       20 * 500000
FROM users u
         JOIN countries c ON c.id = u.id;

CREATE TEMP TABLE first_names
(
    name TEXT
);
INSERT INTO first_names
VALUES ('Leo'),
       ('Cristiano'),
       ('Neymar'),
       ('Kylian'),
       ('Erling'),
       ('Luka'),
       ('Kevin'),
       ('Mohamed'),
       ('Karim'),
       ('Vinicius');

CREATE TEMP TABLE last_names
(
    name TEXT
);
INSERT INTO last_names
VALUES ('Messi'),
       ('Ronaldo'),
       ('Junior'),
       ('Mbappe'),
       ('Haaland'),
       ('Modric'),
       ('DeBruyne'),
       ('Salah'),
       ('Benzema'),
       ('Jr');

CREATE TEMP TABLE position_counts
(
    position TEXT,
    count    INT
);
INSERT INTO position_counts
VALUES ('GLK', 3),
       ('DEF', 5),
       ('MID', 6),
       ('ATT', 6);

DO
$$
    DECLARE
        team_rec  RECORD;
        pos_rec   RECORD;
        i         INT;
        fn        TEXT;
        ln        TEXT;
        player_id BIGINT;
    BEGIN
        FOR team_rec IN SELECT * FROM teams
            LOOP
                FOR pos_rec IN SELECT * FROM position_counts
                    LOOP
                        FOR i IN 1..pos_rec.count
                            LOOP

                                SELECT name INTO fn FROM first_names ORDER BY random() LIMIT 1;
                                SELECT name INTO ln FROM last_names ORDER BY random() LIMIT 1;

                                INSERT INTO players (first_name, last_name, age, position, market_value)
                                VALUES (fn, ln, FLOOR(random() * 18 + 18), pos_rec.position, 500000)
                                RETURNING id INTO player_id;

                                INSERT INTO team_players (team_id, player_id, joined_at)
                                VALUES (team_rec.id, player_id, NOW());
                            END LOOP;
                    END LOOP;
            END LOOP;
    END
$$;
