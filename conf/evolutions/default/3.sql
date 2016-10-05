# Update submissions

# --- !Ups
ALTER TABLE submissions ADD created_at TIMESTAMP;

# --- !Downs
ALTER TABLE submissions DROP created_at;