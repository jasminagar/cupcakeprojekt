CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

alter table users
add balance int;

ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'user';

ALTER TABLE users ADD COLUMN email VARCHAR(255) UNIQUE;
UPDATE users
SET email = name || '@example.com'
WHERE email IS NULL;

ALTER TABLE users ADD COLUMN email VARCHAR(255) UNIQUE NOT NULL;

ALTER TABLE users ALTER COLUMN email SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE(email);

create table bottoms(
id serial primary key,
flavour varchar(50),
price int
);

create table toppings(
id serial primary key,
flavour varchar(50),
price int
);

create table orders(
    id serial primary key,
    user_id int references users(id),
    pickup_time timestamp not null,
    total_price int not null
);

create table order_lines(
    id serial primary key,
    order_id int references orders(id),
    bottom_id int references bottoms(id),
    topping_id int references toppings(id),
    quantity int not null,
    line_price int not null
);

ALTER TABLE users ALTER COLUMN balance TYPE numeric(10,2);
ALTER TABLE bottoms ALTER COLUMN price TYPE numeric(10,2);
ALTER TABLE toppings ALTER COLUMN price TYPE numeric(10,2);
ALTER TABLE orders ALTER COLUMN total_price TYPE numeric(10,2);
ALTER TABLE order_lines ALTER COLUMN line_price TYPE numeric(10,2);

INSERT INTO bottoms (flavour, price) VALUES
('Chocolate', 5),
('Vanilla', 5),
('Nutmeg', 5),
('Pistacio', 6),
('Almond', 7);


INSERT INTO toppings (flavour, price) VALUES
('Chocolate', 5),
('Blueberry', 5),
('Rasberry', 5),
('Crispy', 6),
('Strawberry', 6),
('Rum/Raisin', 7),
('Orange', 8),
('Lemon', 8),
('Blue cheese', 9);

***************************
delete from order_lines
where order_id in (
    select id from orders
    where user_id in (
        select id from users
        where balance is null
    )
);

delete from orders
where user_id in (
    select id from users
    where balance is null
);

delete from users
where balance is null;

