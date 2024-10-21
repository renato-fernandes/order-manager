-- Items
CREATE TABLE IF NOT EXISTS items(
    id_item serial primary key,
    name varchar(100) not null,
    current_stock int not null
);

-- Users
CREATE TABLE IF NOT EXISTS users(
    id_user serial primary key,
    name varchar(100) not null,
    email varchar(255) not null
);

-- Order
CREATE TABLE IF NOT EXISTS orders(
    id_order serial primary key,
    creation_date timestamp not null,
    item_id int not null,
    foreign key(item_id) references items(id_item),
    quantity int not null,
    user_id int not null,
    foreign key(user_id) references users(id_user),
    is_complete smallint
);

-- StockMovements
CREATE TABLE IF NOT EXISTS stock_movements(
    id_stock serial primary key,
    creation_date timestamp not null,
    item_id int not null,
    foreign key(item_id) references items(id_item),
    quantity int not null,
    order_id int not null,
    foreign key(order_id) references orders(id_order)
);

