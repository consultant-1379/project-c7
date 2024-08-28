CREATE TABLE IF NOT EXISTS job (
    id INTEGER PRIMARY KEY DEFAULT 0,
    job_type VARCHAR(150) NOT NULL,
    filter_time VARCHAR(150) NOT NULL,
    build_number INTEGER NOT NULL,
    number_of_deliveries INTEGER NOT NULL,
    duration FLOAT NOT NULL,
    failure_rate FLOAT NOT NULL,
    restore_time FLOAT NOT NULL
    );
ALTER TABLE job MODIFY COLUMN id INT auto_increment;