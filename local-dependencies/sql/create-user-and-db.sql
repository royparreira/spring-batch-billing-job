-- Create user
CREATE USER <username> WITH PASSWORD <password> SUPERUSER;

-- Create database
CREATE DATABASE billing_job_db;

-- Grant privileges to the user for the database (optional)
GRANT ALL PRIVILEGES ON DATABASE billing_job_db TO username;