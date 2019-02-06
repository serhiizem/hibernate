INSERT INTO users (id, user_name) VALUES (2, 'User#2');
INSERT INTO users (id, user_name) VALUES (3, 'User#3');

ALTER TABLE orders
  ADD COLUMN
  user_id BIGINT;
ALTER TABLE orders
  ADD COLUMN
  description VARCHAR(255);

ALTER TABLE orders
  ADD CONSTRAINT orders_user_fk
FOREIGN KEY (user_id) REFERENCES users;


CREATE OR REPLACE FUNCTION update_request_last_modified_date_function()
  RETURNS TRIGGER
AS
$BODY$
BEGIN
  NEW.LAST_MODIFIED_DATE = now();
  RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql;

CREATE TRIGGER request_description_update_trigger
BEFORE UPDATE OF description
  ON requests
FOR EACH ROW EXECUTE PROCEDURE update_request_last_modified_date_function();

ALTER TABLE requests
  ADD COLUMN AIR_DISTANCE DOUBLE PRECISION;

ALTER TABLE requests
  ADD COLUMN AIR_DISTANCE_UNIT VARCHAR(2);

ALTER TABLE requests
  ADD COLUMN LAND_DISTANCE DOUBLE PRECISION;

ALTER TABLE requests
  ADD COLUMN LAND_DISTANCE_UNIT VARCHAR(2);

INSERT INTO public.joined_billing_details (id, owner)
VALUES (1148, 'Test owner');
INSERT INTO public.joined_credit_card (card_number, exp_month, exp_year, cc_id)
VALUES ('555 666 111', '06', '2020', 1148);
INSERT INTO public.users (id, user_name, default_billing_details_id, card_number) VALUES (1, NULL, 1148, NULL);

INSERT INTO public.orders (id, name, user_id, description) VALUES (3, 'ORDER#3', 1, NULL);
INSERT INTO public.orders (id, name, user_id, description) VALUES (5, 'ORDER#5', 2, NULL);
INSERT INTO public.orders (id, name, user_id, description) VALUES (1, 'ORDER#1', 1,
                                                                   'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO public.orders (id, name, user_id, description) VALUES (2, 'ORDER#2', 1,
                                                                   'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');
INSERT INTO public.orders (id, name, user_id, description) VALUES (4, 'ORDER#4', 2,
                                                                   'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.');

INSERT INTO public.requests (id, creation_date, city, street, zip_code, description, from_city, from_street, from_zip_code, last_modified_date, price, status, air_distance, air_distance_unit, land_distance, land_distance_unit, height, length, width, dimensions_name, dimensions_symbol, weight, weight_name, weight_symbol)
VALUES (1179, '2019-02-05 00:00:00.000000', 'City #22', 'Street #2222', '5326', 'Test description', 'City #11',
              'Street #1111', '6235', NULL, '15.0 USD', 'PROCESSING', NULL, NULL, NULL, NULL, 15.00, 20.00, 30.00,
                                                        'centimetre', 'cm', 74.00, 'pound', 'lb');

CREATE TABLE comments (
  id         BIGSERIAL PRIMARY KEY,
  author     VARCHAR(55),
  text       VARCHAR(255),
  date       TIMESTAMP,
  request_id BIGINT
);

ALTER TABLE comments
  ADD CONSTRAINT comment_request_fk
FOREIGN KEY (request_id) REFERENCES requests (id);

INSERT INTO comments (author, text, date, request_id)
VALUES ('Author2', 'Desc51316', TIMESTAMP '2019-05-21 15:36:38', 1179);
INSERT INTO comments (author, text, date, request_id)
VALUES ('Author1', 'Desc23257835', TIMESTAMP '2019-05-21 15:21:01', 1179);
INSERT INTO comments (author, text, date, request_id)
VALUES ('Author3', 'Desc23257835', TIMESTAMP '2019-05-21 15:25:35', 1179);

CREATE TABLE notification_recipients (
  request_id BIGINT,
  email      VARCHAR(55)
);

ALTER TABLE notification_recipients
  ADD CONSTRAINT notification_recipients_pk PRIMARY KEY (request_id, email);

ALTER TABLE notification_recipients
  ADD CONSTRAINT recipients_requests_fk
FOREIGN KEY (request_id) REFERENCES requests (id);

INSERT INTO notification_recipients (email, request_id) VALUES ('test1@gmail.com', 1179);
INSERT INTO notification_recipients (email, request_id) VALUES ('test3@gmail.com', 1179);

CREATE TABLE contact_methods (
  user_id     BIGINT,
  method_type VARCHAR(55),
  value       VARCHAR(255)
);

ALTER TABLE contact_methods
  ADD CONSTRAINT contact_methods_pk
PRIMARY KEY (user_id, method_type);

ALTER TABLE contact_methods
  ADD CONSTRAINT contact_method_user_fk
FOREIGN KEY (user_id) REFERENCES users (id);

INSERT INTO contact_methods (user_id, method_type, value)
VALUES (1, 'SKYPE', 'live:test1skype');
INSERT INTO contact_methods (user_id, method_type, value)
VALUES (1, 'PHONE', '+3809215354134');
INSERT INTO contact_methods (user_id, method_type, value)
VALUES (2, 'PHONE', '+35254254262462');
INSERT INTO contact_methods (user_id, method_type, value)
VALUES (2, 'EMAIL', 'test3@gmail.com');
INSERT INTO contact_methods (user_id, method_type, value)
VALUES (3, 'SKYPE', 'test4skype');

INSERT INTO user_contracts (user_id, generation_date, signing_date, file_extension, file_name)
VALUES (1, TIMESTAMP '2019-03-16 07:25:59', TIMESTAMP '2019-03-16 09:35:21', 'doc', 'contract112234');







