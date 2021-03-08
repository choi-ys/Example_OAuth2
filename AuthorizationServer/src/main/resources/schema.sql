create table IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(256),
  token bytea,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication bytea,
  refresh_token VARCHAR(256)
);

create table IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(256),
  token bytea,
  authentication bytea
);

--
-- SELECT
--     access_token.token_id AS access_token_id
--      , access_token.token as access_token
--      , access_token.authentication_id as authentication_id
--      , access_token.authentication AS authentication
--      , access_token.user_name as user_name
--      , access_token.client_id as client_id
--      , access_token.refresh_token as refresh_token_id
--      , refresh_token.token as refresh_token
-- FROM
--     oauth_access_token AS access_token
--         JOIN oauth_refresh_token as refresh_token
--              ON access_token.refresh_token = refresh_token.token_id
--              AND access_token.authentication = refresh_token.authentication;