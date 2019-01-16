INSERT INTO users (id, card_number, user_name) VALUES (1, 'User#1', null);
INSERT INTO users (id, card_number, user_name) VALUES (2, 'User#2', null);
INSERT INTO users (id, card_number, user_name) VALUES (3, 'User#3', null);

ALTER TABLE orders
  ADD COLUMN
  user_id BIGINT;
ALTER TABLE orders
  ADD COLUMN
  description VARCHAR(255);

ALTER TABLE orders
  ADD CONSTRAINT orders_user_fk
FOREIGN KEY (user_id) REFERENCES users;

INSERT INTO public.orders (id, name, user_id, description) VALUES (3, 'ORDER#3', 1, null);
INSERT INTO public.orders (id, name, user_id, description) VALUES (5, 'ORDER#5', 2, null);
INSERT INTO public.orders (id, name, user_id, description) VALUES (1, 'ORDER#1', 1,
                                                                   'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO public.orders (id, name, user_id, description) VALUES (2, 'ORDER#2', 1,
                                                                   'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');
INSERT INTO public.orders (id, name, user_id, description) VALUES (4, 'ORDER#4', 2,
                                                                   'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.');

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