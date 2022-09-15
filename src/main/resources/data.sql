INSERT INTO authority (authority_name) values ('ROLE_USER');
INSERT INTO authority (authority_name) values ('ROLE_ADMIN');

-- user 1
insert into users (user_id,  account_state, email, password, age, age_visible, gender, gender_visible, img_url, nickname, self_introduction, filter_all, filter_followee, filter_follower, notification_comment, notification_follow, notification_follow_accept, notification_chat, notification_like, warning_count) values (default, 'ACTIVE', 'abc@naver.com', '$2a$10$UALX0n7xu/HwfelvzW2h..9S.lmKKrZHEm6jCWnGdtqJONfJNsosy', NULL, false, 'MALE', false, NULL, '홍길동', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,  0);
insert into user_authority (user_authority_id, authority_name, user_id) values (default, 'ROLE_USER', 1);
-- user 2
insert into users (user_id,  account_state, email, password, age, age_visible, gender, gender_visible, img_url, nickname, self_introduction, filter_all, filter_followee, filter_follower, notification_comment, notification_follow, notification_follow_accept, notification_chat, notification_like, warning_count) values (default, 'ACTIVE', 'sky114477@naver.com', '$2a$10$UALX0n7xu/HwfelvzW2h..9S.lmKKrZHEm6jCWnGdtqJONfJNsosy', NULL, false, 'MALE', false, NULL, '임채민', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,  0);
insert into user_authority (user_authority_id, authority_name, user_id) values (default, 'ROLE_USER', 1);
-- user 3
insert into users (user_id,  account_state, email, password, age, age_visible, gender, gender_visible, img_url, nickname, self_introduction, filter_all, filter_followee, filter_follower, notification_comment, notification_follow, notification_follow_accept, notification_chat, notification_like, warning_count) values (default, 'ACTIVE', 'sky1144@naver.com', '$2a$10$UALX0n7xu/HwfelvzW2h..9S.lmKKrZHEm6jCWnGdtqJONfJNsosy', NULL, false, 'MALE', false, NULL, '임채민2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,  0);
insert into user_authority (user_authority_id, authority_name, user_id) values (default, 'ROLE_USER', 1);
-- user 4
insert into users (user_id,  account_state, email, password, age, age_visible, gender, gender_visible, img_url, nickname, self_introduction, filter_all, filter_followee, filter_follower, notification_comment, notification_follow, notification_follow_accept, notification_chat, notification_like, warning_count) values (default, 'ACTIVE', 'sky@naver.com', '$2a$10$UALX0n7xu/HwfelvzW2h..9S.lmKKrZHEm6jCWnGdtqJONfJNsosy', NULL, false, 'MALE', false, NULL, '임채민3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,  0);
insert into user_authority (user_authority_id, authority_name, user_id) values (default, 'ROLE_USER', 1);


-- insert into page (page_id,  content, like_count, comment_read_auth, comment_write_auth, like_read_auth, notification_comment, notification_like, read_auth, user_id, warning_count) values (default,  '내용1', 0, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0);
-- insert into page (page_id,  content, like_count, comment_read_auth, comment_write_auth, like_read_auth, notification_comment, notification_like, read_auth, user_id, warning_count) values (default,  '내용2', 0, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0);
--
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.11111', 'first comment', 1, 0, false, 0, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.22222', 'first nested comment', 1, 0, true, 1, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.33333', 'second nested comment', 1, 0, true, 2, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.44444', 'third nested comment', 1, 0, true, 3, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.55555', 'first comment', 2, 0, false, 0, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.66666', 'first comment', 3, 0, false, 0, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.77777', 'first nested comment', 3, 0, true, 1, 1, 1, true, 0);
-- insert into comment (comment_id, created_date, content, comment_group, like_count, nested, orders, page_id, user_id, visible, warning_count) values (default, '2022-08-04 23:45:55.88888', 'first comment', 4, 0, false, 0, 1, 1, true, 0);
-- --
-- insert into follow (follow_id, user_id, target_user_id) values (default, 2, 1);



