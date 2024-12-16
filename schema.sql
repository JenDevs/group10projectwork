use demo;

create table country
(
    country_code varchar(255) not null,
    country_name varchar(255) null,
    constraint pk_country primary key (country_code)
);

insert into country (country_code, country_name)
values ('dk', 'Denmark'),
       ('fi', 'Finland'),
       ('is', 'Iceland'),
       ('no', 'Norway'),
       ('se', 'Sweden');

create table city
(
    id           bigint auto_increment not null,
    city_name    varchar(255)          not null,
    population   int                   not null,
    country_code varchar(255)          not null,
    constraint pk_city primary key (id),
    constraint fk_city_country foreign key (country_code) references country (country_code)
);

insert into city (city_name, population, country_code)
values
    ('Copenhagen', 602481, 'dk'),
    ('Aarhus', 282910, 'dk'),
    ('Odense', 179601, 'dk'),
    ('Helsinki', 631695, 'fi'),
    ('Espoo', 283632, 'fi'),
    ('Tampere', 238140, 'fi'),
    ('Reykjavik', 131136, 'is'),
    ('Kopavogur', 36875, 'is'),
    ('Hafnarfjordur', 29272, 'is'),
    ('Oslo', 634293, 'no'),
    ('Bergen', 278556, 'no'),
    ('Stavanger', 144699, 'no'),
    ('Stockholm', 975551, 'se'),
    ('Gothenburg', 583056, 'se'),
    ('Malmo', 347949, 'se'),
    ('Uppsala', 233839, 'se'),
    ('Västerås', 154049, 'se'),
    ('Örebro', 156381, 'se'),
    ('Linköping', 164616, 'se'),
    ('Helsingborg', 149280, 'se'),
    ('Jönköping', 141081, 'se'),
    ('Norrköping', 143478, 'se'),
    ('Lund', 125154, 'se'),
    ('Umeå', 130224, 'se'),
    ('Gävle', 102904, 'se');
