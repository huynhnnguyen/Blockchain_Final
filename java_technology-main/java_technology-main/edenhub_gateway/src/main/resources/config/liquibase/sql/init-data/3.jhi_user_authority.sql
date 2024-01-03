--jhi_authority
INSERT INTO public.jhi_authority ("name") VALUES('ROLE_ADMIN');
INSERT INTO public.jhi_authority ("name") VALUES('ROLE_USER');
INSERT INTO public.jhi_authority ("name") VALUES('ROLE_SYS');
--jhi_user
INSERT INTO public.jhi_user (id, created_by, created_date, last_modified_by, last_modified_date, activated, activation_key, email, first_name, image_url, lang_key, last_name, last_resend_otp, login, number_incorrect_otp, number_send_otp, otp, password_hash, reset_date, reset_key) VALUES(1, 'luong.nguyen', '2023-01-01 00:00:00.000', 'luong.nguyen', '2023-01-01 00:00:00.000', true, NULL, 'farm.admin@edenhub.vn', 'Admin', NULL, 'en', 'Admin', '2023-01-01 00:00:00.000', 'farm.admin@edenhub.vn', 0, 0, NULL, '$2a$10$tMTASVEMzWMenRdJcj6azeZlVXzXkTLo9ZX.2bUGdhJWyLv/8ab2y', NULL, NULL);
INSERT INTO public.jhi_user (id, created_by, created_date, last_modified_by, last_modified_date, activated, activation_key, email, first_name, image_url, lang_key, last_name, last_resend_otp, login, number_incorrect_otp, number_send_otp, otp, password_hash, reset_date, reset_key) VALUES(10, 'luong.nguyen', '2023-01-01 00:00:00.000', 'luong.nguyen', '2023-01-01 00:00:00.000', true, NULL, 'farmer@edenhub.vn', 'Farmer', NULL, 'en', 'Farmer', '2023-01-01 00:00:00.000', '0123456789', 0, 0, NULL, '$2a$10$tMTASVEMzWMenRdJcj6azeZlVXzXkTLo9ZX.2bUGdhJWyLv/8ab2y', NULL, NULL);
--jhi_user_authority
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES(1, 'ROLE_ADMIN');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES(1, 'ROLE_USER');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES(1, 'ROLE_SYS');
INSERT INTO public.jhi_user_authority (user_id, authority_name) VALUES(10, 'ROLE_SYS');
