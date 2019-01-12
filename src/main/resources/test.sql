update sys_user set card_number = pgp_sym_encrypt('1234 5678 9012 3456', 'mySecretKey');


select * from public.sys_user;
