CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

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

