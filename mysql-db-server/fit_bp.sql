use bp;

set foreign_key_checks = 0;

/*-------------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------*/
/*-----------------------------------------------TABLES DROPPING-----------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------*/

drop table if exists prihlasovacie_konto;
drop table if exists autorizacia_uzivatela;
drop table if exists pracujuci;
drop table if exists dolezite_udaje_pracujuceho;
drop table if exists pracovny_vztah;
drop table if exists pracovisko;
drop table if exists stupen_narocnosti;
drop table if exists minimalna_mzda;
drop table if exists pozicia;
drop table if exists pozicia_stupen_narocnosti;
drop table if exists dalsie_podmienky;
drop table if exists podmienky_pracovneho_vztahu;
drop table if exists forma_mzdy;
drop table if exists zakladna_mzda;
drop table if exists vyplatna_paska;
drop table if exists zakladna_zlozka;
drop table if exists pohybliva_zlozka;
drop table if exists typ_priplatku;
drop table if exists priplatok;
drop table if exists nahrada;
drop table if exists ina_zlozka_mzdy;
drop table if exists odvod;
drop table if exists zrazka;
drop table if exists odpracovany_rok;
drop table if exists odpracovany_mesiac;
drop table if exists odpracovany_mesiac_nepritomnost;
drop table if exists nepritomnost;
drop table if exists odpracovane_hodiny;
drop table if exists mzdove_konstanty;

set foreign_key_checks = 1;

/*-------------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------*/
/*------------------------------------------------TABLES CREATING----------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------*/

create table prihlasovacie_konto
(
    id int not null auto_increment,
    email varchar (255) not null,
    heslo varchar(255) not null,
    typ_prav varchar (15) not null  default 'zamestnanec'
        check (typ_prav = 'admin'  || typ_prav = 'riaditeľ'
                 || typ_prav = 'účtovník' || typ_prav = 'zamestnanec'),

    posledne_prihlasenie datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    vytvorene_v datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aktualizovane_v datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aktualne boolean not null default true,

    primary key (id),
    constraint u_konto unique (email)
);

create table autorizacia_uzivatela (
    id int NOT NULL auto_increment,
    prihlasovacie_konto int NOT NULL,
    token varchar(255) NOT NULL,
    klientska_sluzba varchar(255) not null,
    vyprsana_v datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    vytvorena_v datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    aktualizovana_v datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,

    primary key (id),
    constraint au_c1 foreign key (prihlasovacie_konto) references prihlasovacie_konto(id)
);

create table pracujuci
(
    id int not null auto_increment,
	meno varchar(255) not null,
    priezvisko varchar(255) not null,
    telefon char (10) not null check (CHAR_LENGTH(telefon)=10),
    rodne_cislo char(10) not null check ( CHAR_LENGTH(rodne_cislo)=10 ),
    datum_narodenia date not null,
    prihlasovacie_konto int null,

    primary key (id),
    constraint pracujuci_c1 foreign key (prihlasovacie_konto) references prihlasovacie_konto(id),
    constraint pracujuci_c2 unique (rodne_cislo)
);

create table dolezite_udaje_pracujuceho
(
    id int not null auto_increment,
	zdravotna_poistovna varchar(30) not null,
	/*cislo_zdravotnej_poistovni varchar(5) not null,*/

    /*trvaly pobyt*/
    mesto varchar(255) not null,
    ulica varchar(255) not null,
    cislo varchar(255) not null,

    pocet_deti_do_6_rokov varchar(2) not null,
    pocet_deti_nad_6_rokov varchar(2) not null,

    platnost_od date not null,
    platnost_do date null,
    pracujuci int not null,

    primary key (id),
    constraint dup_c1 foreign key (pracujuci) references pracujuci(id)
);

create table pracovny_vztah
(
    id int not null auto_increment,
	typ varchar(50) not null
	    check (typ = 'PP: na plný úväzok'
                || typ = 'PP: na kratší pracovný čas'
                || typ = 'PP: telepráca'
                || typ = 'D: o vykonaní práce'
                || typ = 'D: o pracovnej činnosti'
                || typ = 'D: o brig. práci študentov'),
    datum_vzniku date not null,
    datum_vyprsania date null,
    pracujuci int not null,


    primary key (id),
    constraint pracovny_vztah_c1 foreign key (pracujuci) references pracujuci(id)
);


create table pracovisko
(
    id int not null auto_increment,
	nazov varchar(255) not null,

	/*adresa*/
    mesto varchar(255) not null,
    ulica varchar(255) not null,
    cislo varchar(255) not null,

    primary key (id),
    constraint pracovisko_c1 unique  (nazov)
);

create table stupen_narocnosti
(
    id int not null auto_increment,
	cislo_stupna char(1) not null,
    charakteristika varchar(1024) not null,
    platnost_od date not null,
    platnost_do date null,

    primary key (id)
);

create table minimalna_mzda
(
    id int not null auto_increment,
    platnost_od date not null,
    platnost_do date null,
    hodinova_hodnota decimal(7,5) not null,
    mesacna_hodnota decimal(7,2) not null,
    stupen_narocnosti int not null,

    primary key (id),
    constraint minimalna_mzda_c1 foreign key (stupen_narocnosti) references stupen_narocnosti(id)
);

create table pozicia
(
    id int not null auto_increment,
	nazov varchar(255) not null,
    charakteristika varchar(1024) not null,
    pracovisko int not null,
   /* stupen_narocnosti int not null,*/

    primary key (id),
    constraint pozicia_c1 foreign key (pracovisko) references pracovisko(id),
    constraint pozicia_c2 unique (pracovisko, nazov)
    /*constraint pozicia_c2 foreign key (stupen_narocnosti) references stupen_narocnosti(id)*/
);

create table pozicia_stupen_narocnosti
(
    /*id int not null auto_increment,*/
	pozicia int not null,
    stupen_narocnosti int not null,

    primary key (pozicia, stupen_narocnosti),
    constraint pozicia_stupen_narocnosti_c1 foreign key (pozicia) references pozicia(id),
    constraint pozicia_stupen_narocnosti_c2 foreign key (stupen_narocnosti) references stupen_narocnosti(id)
);



create table dalsie_podmienky
(
    id int not null auto_increment,
	je_hlavny_pp boolean not null,
    vymera_dovolenky float not null,
    dohodnuty_tyzdenny_pracovny_cas float not null,
    je_pracovny_cas_rovnomerny boolean not null,
    skusobvna_doba int not null, /*dni*/
    vypovedna_doba int not null, /*dni*/

    ustanoveny_tyzdenny_pracovny_cas float not null,
    dohodnuty_denny_pracovny_cas float null,
    uplatnenie_odpocitatelnej_polozky boolean not null,

    primary key (id)
);

create table podmienky_pracovneho_vztahu
(
    id int not null auto_increment,
	platnost_od date not null,
    platnost_do date null,
    pracovny_vztah int not null,
    pozicia int not null,
    dalsie_podmienky int null,

    uplatnenie_nezdanitelnej_casti boolean not null,
    uplatnenie_danoveho_bonusu boolean not null,
    drzitel_tzp_preukazu boolean not null,
    poberatel_starobneho_vysluhoveho_dochodku boolean not null,
    poberatel_invalidneho_vysluhoveho_dochodku_nad_40 boolean not null,
    poberatel_invalidneho_vysluhoveho_dochodku_nad_70 boolean not null,
    poberatel_predcasneho_dochodku boolean not null,
    uplatnenie_odvodovej_vynimky boolean not null,

    posielanie_vyplaty_na_ucet boolean not null,
    cast_z_vyplaty_na_ucet float null check ( cast_z_vyplaty_na_ucet > 0 && cast_z_vyplaty_na_ucet <= 1 ),
    iban_uctu_pre_vyplatu char(24) null,

    primary key (id),
    constraint ppv_c1 foreign key (pracovny_vztah) references pracovny_vztah(id),
    constraint ppv_c2 foreign key (pozicia) references pozicia(id),
    constraint ppv_c3 foreign key (dalsie_podmienky) references dalsie_podmienky(id)
);

create table forma_mzdy
(
    id int not null auto_increment,
	nazov varchar(50) not null,
    jednotka_vykonu varchar(20) not null,
    skratka_jednotky varchar(5) not null,

    primary key (id)
);


create table zakladna_mzda
(
    id int not null auto_increment,
    popis varchar(50) not null,
    vykon_eviduje_zamestnanec boolean not null,
    nutne_evidovanie_casu boolean not null,
    mozne_evidovanie_pohotovosti boolean not null default false,
	tarifa_za_jednotku_mzdy decimal(12,4) not null,
    sposob_vyplacania varchar(12) not null default 'pravidelne'
        check (sposob_vyplacania = 'pravidelne' || sposob_vyplacania = 'nepravidelne'),
    datum_vyplatenia date null,
    forma_mzdy int not null,
    podmienky_pracovneho_vztahu int not null,

    primary key (id),
    constraint zakladna_mzda_c1 foreign key (forma_mzdy) references forma_mzdy(id),
    constraint zakladna_mzda_c2 foreign key (podmienky_pracovneho_vztahu) references podmienky_pracovneho_vztahu(id)
);

create table mzdove_konstanty
(
    id int not null auto_increment,

    zakladny_tyzdenny_pracovny_cas decimal(12,4) not null,

    max_vymeriavaci_zaklad decimal(12,4) not null,
    min_vymeriavaci_zaklad decimal(12,4) not null,
    max_denny_vymeriavaci_zaklad decimal(12,4) not null,

	danovy_bonus_na_dieta_nad_6 decimal(12,4) not null,
	danovy_bonus_na_dieta_pod_6 decimal(12,4) not null,

    NCZD_na_danovnika decimal(12,4) not null,
    nasobok_zivotneho_minima_pre_preddavok decimal(12,4) not null,

    zaciatok_nocnej_prace time not null,
    koniec_nocnej_prace time not null,

    max_vymeriavaci_zaklad_pre_OP decimal(12,4) not null,
    max_vyska_OP decimal(12,4) not null,

    hranica_prekrocenia_OV decimal(12,4) not null,

    platnost_od date not null,
    platnost_do date null,

    primary key (id)
);

create table vyplatna_paska
(
    id int not null auto_increment,
    fond_hodin decimal(12,4)  not null,
    fond_dni decimal(12,4)  not null,
    odpracovane_hodiny decimal(12,4)  not null,
	odpracovane_dni decimal(12,4) not null,
    hruba_mzda decimal(12,4) not null,

    vymeriavaci_zaklad decimal(12,4) not null,
    poistne_zamestnanca decimal(12,4) not null,
    nezdanitelna_mzda decimal(12,4) not null,
    zdanitelna_mzda decimal(12,4) not null,
    preddavky_na_dan decimal(12,4) not null,
    danovy_bonus decimal(12,4) not null,
    cista_mzda decimal(12,4) not null,

    cena_prace decimal(12,4) not null,
    odvody_zamestnavatela decimal(12,4) not null,
    odvody_dan_zamestnanca decimal(12,4) not null,
    odvody_dan_spolu decimal(12,4) not null,

    k_vyplate decimal(12,4) not null,
    na_ucet decimal(12,4) not null,
    v_hotovosti decimal(12,4) not null,

    dolezite_udaje_pracujuceho int not null,
    vypracoval_pracujuci int not null,
    minimalna_mzda int not null,
    mzdove_konstanty int not null,

    priemerny_zarobok decimal(12,4) null,

    primary key (id),
    constraint vyplatna_paska_c1 foreign key (dolezite_udaje_pracujuceho) references dolezite_udaje_pracujuceho(id),
    constraint vyplatna_paska_c2 foreign key (vypracoval_pracujuci) references pracujuci(id),
    constraint vyplatna_paska_c3 foreign key (minimalna_mzda) references minimalna_mzda(id),
    constraint vyplatna_paska_c4 foreign key (mzdove_konstanty) references mzdove_konstanty(id)
);

create table zakladna_zlozka
(
    id int not null auto_increment,
	mnozstvo_jednotiek float not null,
	suma_za_mnozstvo decimal(12,4) not null,
	vyplatna_paska int not null,
    zakladna_mzda  int not null,

    primary key (id),
    constraint zakladna_zlozka_c1 foreign key (vyplatna_paska) references vyplatna_paska(id),
    constraint zakladna_zlozka_c2 foreign key (zakladna_mzda) references zakladna_mzda(id)
);

create table pohybliva_zlozka
(
    id int not null auto_increment,
	typ varchar(15) not null check (typ='prémia' || typ='odmena' || typ='bonus' || typ='iné'),
	popis varchar(50) null,
	suma decimal(12,4) not null,
	vyplatna_paska int not null,

    primary key (id),
    constraint pohybliva_zlozka_c1  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table typ_priplatku
(
    id int not null auto_increment,
	nazov varchar(10) not null
	    check (nazov='sobota' || nazov='nedeľa' || nazov='noc' || nazov='sviatok' || nazov='nadčas'),
    popis varchar(20) null,
	percentualna_cast float not null,
	pocitany_zo varchar(25) not null check (pocitany_zo='minimálna mzda' || pocitany_zo='priemerná mzda'),
	platnost_od date not null,
	platnost_do date null,

    primary key (id)
);

create table priplatok
(
    id int not null auto_increment,
	mnozstvo_jednotiek float not null,
	suma decimal(12,4) not null,
	typ_priplatku int not null,
	vyplatna_paska int not null,

    primary key (id),
    constraint priplatok_c1  foreign key (typ_priplatku) references typ_priplatku(id),
    constraint priplatok_c2  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table nahrada
( /*todo -typ check*/
    id int not null auto_increment,
	typ varchar(50) not null,
	pocet_dni float not null,
	mnozstvo_jednotiek float not null,
	suma decimal(12,4) not null,
	vyplatna_paska int not null,

    primary key (id),
    constraint nahrada_c1  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table ina_zlozka_mzdy
(
    id int not null auto_increment,
    nazov varchar(100) not null,
    suma decimal(12,4) not null,
    vyplatna_paska int not null,

    primary key (id),
    constraint ina_zlozka_mzdy_c1  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table odvod
(
    id int not null auto_increment,
    nazov varchar(100) not null,
    vymeriavaci_zaklad decimal(12,4) not null,
    suma_zamestnanec decimal(12,4) not null,
	suma_zamestnavatel decimal(12,4) not null,
	vyplatna_paska int not null,

    primary key (id),
    constraint odvod_c1  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table zrazka
(
    id int not null auto_increment,
	nazov varchar(50) not null,
	suma decimal(12,4) not null,
	vyplatna_paska int not null,

    primary key (id),
    constraint zrazka_c1  foreign key (vyplatna_paska) references vyplatna_paska(id)
);

create table odpracovany_rok
(
    id int not null auto_increment,
    rok year not null,
    narok_na_dovolenku float not null,
    narok_na_dovolenku_z_minuleho_roka float not null,
	vycerpana_dovolenka float not null,
	priemerna_mzda_1 decimal(12,4) null,
	priemerna_mzda_2 decimal(12,4) null,
	priemerna_mzda_3 decimal(12,4) null,
	priemerna_mzda_4 decimal(12,4) null,
	podmienky_pracovneho_vztahu int not null,

    primary key (id),
    constraint odpracovany_rok_c1  foreign key (podmienky_pracovneho_vztahu) references podmienky_pracovneho_vztahu(id),
    constraint odpracovany_rok_c2  unique (rok, podmienky_pracovneho_vztahu)
);

create table odpracovany_mesiac
(
    id int not null auto_increment,
    poradie_mesiaca smallint not null check (poradie_mesiaca<13 && poradie_mesiaca>0),
	odpracovany_rok int not null,
	vyplatna_paska int null,
	je_mesiac_uzatvoreny boolean not null default false,

    primary key (id),
    constraint odpracovany_mesiac_c1  foreign key (odpracovany_rok) references odpracovany_rok(id),
    constraint odpracovany_mesiac_c2  foreign key (vyplatna_paska) references vyplatna_paska(id),
    constraint odpracovany_mesiac_c3  unique (poradie_mesiaca, odpracovany_rok)
);


create table nepritomnost
( /*todo - typ*/
    id int not null auto_increment,
    od date not null,
    do date not null,
	je_polovica_dna boolean not null default false,
	typ_dovodu varchar(256) not null check(typ_dovodu='PN' || typ_dovodu='OČR'
        || typ_dovodu='dovolenka' || typ_dovodu='náhradné voľno' || typ_dovodu='prekážka z dôvodu všeobecného záujmu'
        || typ_dovodu='dôležitá osovná prekážka' || typ_dovodu='prekážka na strane zamestnávateľa' || typ_dovodu='iný'),
	popis_dovodu varchar(1024) not null,

	aktualizovane timestamp not null,

    primary key (id)
);

create table odpracovany_mesiac_nepritomnost
(
    /*id int not null auto_increment,*/
    odpracovany_mesiac int not null,
    nepritomnost int not null,

    primary key (odpracovany_mesiac, nepritomnost),
    constraint odpracovany_mesiac_nepritomnost_c1 foreign key (odpracovany_mesiac) references odpracovany_mesiac(id),
    constraint odpracovany_mesiac_nepritomnost_c2 foreign key (nepritomnost) references nepritomnost(id)
);

create table odpracovane_hodiny
(
    id int not null auto_increment,
    datum date not null,
    od time  null,
	do time  null,
	z_toho_nadcas float null,
	pocet_vykonanych_jednotiek float  null,
	zaklad_podielovej_mzdy decimal(16,2)  null,
	druh_casti_pohotovosti varchar(30) null default null check(druh_casti_pohotovosti is null || druh_casti_pohotovosti='aktívna'
	                                                            || druh_casti_pohotovosti='neaktívna - na pracovisku' || druh_casti_pohotovosti='neaktívna - mimo pracoviska' ),
    aktualizovane timestamp not null,
    odpracovany_mesiac int not null,
    zakladna_mzda int not null,

    primary key (id),
    constraint odpracovane_hodiny_c1  foreign key (odpracovany_mesiac) references odpracovany_mesiac(id),
    constraint odpracovane_hodiny_c2  foreign key (zakladna_mzda) references zakladna_mzda(id)
);




delimiter //

drop procedure  if exists pridaj_pracujuceho//
create procedure pridaj_pracujuceho(fheslo varchar(255), ftyp_prav varchar(15), femail varchar(255), fmeno varchar(255), fpriezvisko varchar(255),
                                    ftelefon char(10), frodne_cislo char(10), fdatum_narodenia date )
begin
    declare exit handler for sqlexception
        begin
            show errors;
            rollback;
        end;

    start transaction ;
    insert into prihlasovacie_konto(heslo, email, typ_prav) values(fheslo, femail, ftyp_prav);
    insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values (fmeno, fpriezvisko, ftelefon, frodne_cislo, fdatum_narodenia, LAST_INSERT_ID() );
    commit work ;
end//

drop procedure  if exists edituj_pracujuceho//
create procedure edituj_pracujuceho(id_pracujuceho int, femail varchar(255), fmeno varchar(255), fpriezvisko varchar(255),
                                    ftelefon char(10), frodne_cislo char(10), fdatum_narodenia date )
begin
    declare fid varchar(255);
    declare exit handler for sqlexception
        begin
            show errors;
            rollback;
        end;

    start transaction ;
    if femail is not null then
        select prihlasovacie_konto into fid from pracujuci p join prihlasovacie_konto pk on p.prihlasovacie_konto = pk.id where p.id=id_pracujuceho;
        update prihlasovacie_konto set email = femail where id = fid;
    end if;
    if fmeno is not null then
        update pracujuci set meno = fmeno where id = id_pracujuceho;
    end if;
    if fpriezvisko is not null then
        update pracujuci set priezvisko = fpriezvisko where id = id_pracujuceho;
    end if;
    if ftelefon is not null then
        update pracujuci set telefon = ftelefon where id = id_pracujuceho;
    end if;
    if frodne_cislo is not null then
        update pracujuci set rodne_cislo = frodne_cislo where id = id_pracujuceho;
    end if;
    if fdatum_narodenia is not null then
        update pracujuci set datum_narodenia = fdatum_narodenia where id = id_pracujuceho;
    end if;
    commit work ;
end//

drop procedure if exists odstran_pracujuceho//
create procedure odstran_pracujuceho(id_pracujuceho int)
begin
    declare fid varchar(255);
    declare exit handler for sqlexception
        begin
            show errors;
            rollback;
        end;

    select prihlasovacie_konto into fid from pracujuci p join prihlasovacie_konto pk on p.prihlasovacie_konto = pk.id
    where p.id=id_pracujuceho;
    start transaction ;
    delete from pracujuci where id=id_pracujuceho;
    delete from prihlasovacie_konto where id=fid;
    commit work ;
end//

drop procedure if exists podmienky_pracovneho_vztahu//
create procedure podmienky_pracovneho_vztahu(id_pracovneho_vztahu int, datum date, out ret int )
begin
    select ppv.id into ret from pracovny_vztah pv join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah
    where  id_pracovneho_vztahu = pv.id and datum > ppv.platnost_od
      and (datum < ppv.platnost_do or ppv.platnost_do is null);
end//

drop procedure if exists podmienky_pracovneho_vztahu1//
create procedure podmienky_pracovneho_vztahu1(id_pracovneho_vztahu int, datum date)
begin
    select * from pracovny_vztah pv join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah
    where  id_pracovneho_vztahu = pv.id and datum > ppv.platnost_od
      and (datum < ppv.platnost_do or ppv.platnost_do is null);
end//

drop procedure if exists rok_podmienok_pv//
create procedure rok_podmienok_pv(id_podmienok_pv int, datum date, out id_rok int )
begin
    select o.id into id_rok from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu
    where id_podmienok_pv=ppv.id and YEAR(datum)=o.rok;
end//

drop procedure if exists mesiac_roka_podmienok//
create procedure mesiac_roka_podmienok(id_roka int, datum date, out id_mesiac int )
begin
    select o.id into id_mesiac from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok
    where id_roka=o.id and MONTH(datum)=om.poradie_mesiaca;
end//

drop procedure if exists pridat_odpracovane_hodiny//
create procedure pridat_odpracovane_hodiny(id_mesiaca int, id_zakladna_mzda int, datum date, cas_od time, cas_do time, z_toho_nadcas time,
                                           pocet_vykonanych_jednotiek float, zaklad_podielovej_mzdy decimal(16,2), druh_casti_pohotovosti varchar(10))
BEGIN
    INSERT INTO odpracovane_hodiny(datum, od, do, z_toho_nadcas, pocet_vykonanych_jednotiek, zaklad_podielovej_mzdy, druh_casti_pohotovosti, odpracovany_mesiac, zakladna_mzda)
    VALUES (datum, cas_od, cas_do, z_toho_nadcas, pocet_vykonanych_jednotiek, zaklad_podielovej_mzdy, druh_casti_pohotovosti,id_mesiaca, id_zakladna_mzda);
END//

delimiter ;


/*call pridaj_pracujuceho('apo', 'riaditeľ', 'apo@apo', 'Jozef', 'Jozef', '0910000000', '9805548135', '1980-04-04');
call odstran_pracujuceho(1);*/




insert into prihlasovacie_konto(heslo, email, typ_prav, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ('5731d026710d223ef4b2494fc3e975a9', 'vlado@gmail.com', 'riaditeľ', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto(heslo, email, typ_prav, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ('21d2124db4d2f47684c5398ad74e4e2b', 'helenka@gmail.com', 'účtovník', '2020-02-24 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( 'a32afbe54e4fbab0c8c44c01f5b90792', 'martinka@gmail.com', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ('85304e67de348d25787325d9dd74db9f', 'milka@gmail.com', '2020-02-24 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( '6e9890b85ac8c048cbbdcc7b3b280220', 'marcelka@gmail.com', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( '441b680560ed6c0c5e49ff4c554a63e7', 'valent@gmail.com', '2020-02-24 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( 'e7e312434382146f577b8154f9e7b091', 'monicka@gmail.com', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( 'b4285951608f1297e3a26254fc2fda51', 'primarka@gmail.com', '2020-02-24 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( 'b4285951608f1297e3a26254fc2fda51', 'maja@gmail.com', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( 'c00b6488be9f58ea9ac154cd24142ed1', 'palko@gmail.com', '2020-02-24 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');
insert into prihlasovacie_konto( heslo, email, typ_prav, posledne_prihlasenie, vytvorene_v, aktualizovane_v)
    values ( '256f035bd7cf72238fad007fb9199c66', 'jozko@gmail.com', 'admin', '2020-02-20 11:30:55', '2015-12-25 10:35:16', '2015-12-25 10:35:16');


insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Vlado', 'Ondria', '0910000000', '9804048135', '1980-04-04', 1);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Helena', 'Apova', '0920000000', '9804048136', '1980-04-04', 2);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Martina', 'Ondriová', '0930000000', '9804048137', '1980-04-04', 3);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Miloslava', 'Smolkova', '0940000000', '9804048138', '1980-04-04', 4);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Marcela', 'Uličná', '0950000000', '9804048139', '1980-04-04', 5);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Valentin', 'Sirovič', '0960000000', '9804048115', '1980-04-04', 6);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Monika', 'Ondriova', '0970000000', '9804048125', '1980-04-04',7);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Maria', 'Ulična', '0980000000', '9804048145', '1980-04-04', 8);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Maria', 'Galdunová', '0990000000', '9804048155', '1980-04-04', 9);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Pavol', 'Bednárik', '0901000000', '9804048165', '1980-04-04', 10);
insert into pracujuci(meno, priezvisko, telefon, rodne_cislo, datum_narodenia, prihlasovacie_konto)
    values ('Jozef', 'Ondria', '0902000000', '9804028175', '1998-04-02', 11);

/*insert into autorizacia_uzivatela(prihlasovacie_konto, token, vyprsana_v, vytvorena_v, aktualizovana_v)
    values (1, '$1$6fjNSBRR$7lx.mxo/q1LbNO7f5.7w8.', '2020-02-24 23:28:00', '2020-02-24 11:28:00', '2020-02-24 11:28:00');
insert into autorizacia_uzivatela(prihlasovacie_konto, token, vyprsana_v, vytvorena_v, aktualizovana_v)
    values (2, '$1$HY2H7rB0$2U.dlCsoHX21s/gvjCypG/', '2020-02-24 23:28:10', '2020-02-24 11:28:10', '2020-02-24 11:28:10');*/

insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '506', '0', '2', '2005-01-01', '2018-12-31', 1);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '506', '2', '2', '2019-01-01', null, 1);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov,  platnost_od, platnost_do, pracujuci)
    values ('Vseobecna zdravotna poistovna', 'Hermanovce', 'Hermanovce', '500', '0', '0', '2010-01-01', null, 2);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov,  platnost_od, platnost_do, pracujuci)
    values ('Dovera', 'Vitaz', 'Vitaz', '507', '0', '1', '2016-01-01', '2019-12-31', 3);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '507', '0', '0', '2020-01-01', null, 3);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Dovera', 'Vitaz', 'Vitaz', '200', '0', '1', '2015-01-01', '2019-12-31', 4);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Dovera', 'Vitaz', 'Vitaz', '200', '0', '0', '2020-01-01', null, 4);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '100', '0', '1', '2012-01-01', null, 5);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '400', '2', '0', '2008-01-01', '2019-12-31', 6);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Vitaz', 'Vitaz', '400', '0', '2', '2020-01-01', null, 6);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Vseobecna zdravotna poistovna', 'Vitaz', 'Vitaz', '220', '0', '0','2004-01-01', null, 7);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Vseobecna zdravotna poistovna', 'Vitaz', 'Vitaz', '50', '0', '0', '2019-01-01', null, 8);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Vseobecna zdravotna poistovna', 'Vitaz', 'Vitaz', '600', '2', '0', '2019-01-01', null, 9);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Vseobecna zdravotna poistovna', 'Ovčie', 'Ovčie', '150', '0', '0', '2016-01-01', null, 10);
insert into dolezite_udaje_pracujuceho(zdravotna_poistovna,  mesto, ulica, cislo, pocet_deti_do_6_rokov, pocet_deti_nad_6_rokov, platnost_od, platnost_do, pracujuci)
    values ('Union', 'Víťaz', 'Víťaz', '507', '0', '0', '2016-01-01', null, 11);

insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2005-01-01', null, 2); /*2*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2015-01-01', null, 3); /*3*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('D: o pracovnej činnosti', '2019-01-01', '2019-12-31', 4); /*4*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('D: o pracovnej činnosti', '2020-01-01', '2020-12-31', 4); /*4*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2012-01-01', null, 5); /*5*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2008-01-01', null, 6); /*6*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2004-01-01', null, 7); /*7*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na kratší pracovný čas', '2019-01-01', null, 8); /*8*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na plný úväzok', '2019-01-01', null, 9); /*9*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na kratší pracovný čas', '2016-01-01', null, 10); /*10*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('PP: na kratší pracovný čas', '2016-01-01', null, 11); /*11*/
insert into pracovny_vztah(typ, datum_vzniku, datum_vyprsania, pracujuci)
    values ('D: o pracovnej činnosti', '2020-01-01','2020-12-31', 11); /*11*/

insert into pracovisko(nazov, mesto, ulica, cislo)
    values ('Kancelária - sídlo firmy', 'Víťaz', 'Víťaz', '507');
insert into pracovisko(nazov, mesto, ulica, cislo)
    values ('Reštaurácia Armstrong', 'Víťaz', 'Víťaz', '520');
insert into pracovisko(nazov, mesto, ulica, cislo)
    values ('Vároba parených várobkov Veronka', 'Víťaz', 'Víťaz', '220');

insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('1', 'dokladač tovaru, upratovačka, skladník','2000-01-01', null);
insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('2', 'predavač, pokladník','2000-01-01', null);
insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('3', 'elektrikár, ekonóm, účtovník, vodič autobusu','2000-01-01', null);
insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('4', 'hlavný účtovník, vedúci prevádzky','2000-01-01', null);
insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('5', 'všeobecný lekár, obchodný manažér','2000-01-01', null);
insert into stupen_narocnosti(cislo_stupna, charakteristika, platnost_od , platnost_do)
    values ('6', 'generálny riaditeľ','2000-01-01', null);

insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2019-01-01', '2019-12-31', 2.9890, 520, 1);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ( '2019-01-01', '2019-12-31', 3.5868, 624, 2);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ( '2019-01-01', '2019-12-31', 4.1846, 728.00, 3);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ( '2019-01-01', '2019-12-31', 4.7824, 832, 4);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ( '2019-01-01', '2019-12-31', 5.3802, 936, 5);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2019-01-01', '2019-12-31', 5.9780, 1040, 6);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2020-01-01', '2020-12-31', 3.3330, 580, 1);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ( '2020-01-01', '2020-12-31', 3.9996, 696, 2);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2020-01-01', '2020-12-31', 4.6662, 812, 3);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2020-01-01', '2020-12-31', 5.3328, 928, 4);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2020-01-01', '2020-12-31', 5.9994, 1044, 5);
insert into minimalna_mzda(platnost_od, platnost_do, hodinova_hodnota, mesacna_hodnota, stupen_narocnosti)
    values ('2020-01-01', '2020-12-31', 6.6660, 1160, 6);

insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Hlavný účtovník', 'Pracovník zodpovedný za ekonomické právne zaležitosti firmy a výplaty zamestnancov.', 1);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Vedúci prevádzky', 'Pracovník zodpovedný za správny chod prevádzky.', 3);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Výrobca parených výrobkov', 'Pracovník, ktorého úlohou je výroba parených výrobkov Veronka.', 3);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Šefkuchár', 'Pracovník, ktorého ulohou je nie len príprava pokrmov v reštarácii Armstrongs, ale aj zodpovednosť za správny chod kuchyne a jej personálu.', 2);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Asistent kuchára', 'Pracovník, ktorý sa podieľa na príprave pokrmov v reštauracií Armstrong a to na základe pokynov od šefkuchára.', 2);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Čašník', 'Pracovník, ktorého úlohou je obsluha hostí reštarurácie Armstrong.', 2);
insert into pozicia(nazov, charakteristika, pracovisko)
    values ('Rozvozca', 'Pracovník, ktorého úlohou je rozvoz pripravených pokrmov reštarurácie Armstrong.', 2);

insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (1, 4);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (2, 4);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (3, 1);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (4, 3);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (5 ,1);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (6, 2);
insert into pozicia_stupen_narocnosti(pozicia, stupen_narocnosti)
    values (7, 2);

insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, null); /*2*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, null); /*3*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, null); /*5*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, null); /*6*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, null); /*7*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 30, true, 90, 90, true, 38.750, null); /*8*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 40, true, 90, 90, false, 40, 12); /*9*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 30, false, 90, 90, false, 40, null); /*10*/
insert into dalsie_podmienky(je_hlavny_pp, vymera_dovolenky, dohodnuty_tyzdenny_pracovny_cas, je_pracovny_cas_rovnomerny, skusobvna_doba, vypovedna_doba, uplatnenie_odpocitatelnej_polozky, ustanoveny_tyzdenny_pracovny_cas, dohodnuty_denny_pracovny_cas)
    values (true, 21, 37.5, true, 90, 90, false, 40, null); /*11*/

insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2005-01-01', null, 1, 1, 1, false, false, false, false, false, false, true, false, true, 1, 'SK9011000000002600000126'); /*2*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2015-01-01', null, 2, 2, 2, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*3*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2019-01-01', '2019-12-31', 3, 3, null, false, false, false, false, true, false, false, false, true, 1, 'SK9011000000002600000126'); /*4*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2020-01-01', '2020-12-31', 4, 3, null, false, false, false, false, true, false, false, false, true, 1, 'SK9011000000002600000126'); /*4*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2012-01-01', null, 5, 3, 3, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*5*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2008-01-01', null, 6, 4, 4, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*6*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2004-01-01', null, 7, 5, 5, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*7*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2019-01-01', null, 8, 5, 6, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*8*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2019-01-01', null, 9, 6, 7, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*9*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2016-01-01', null, 10, 7, 8, false, false, false, true, false, false, false, false, false, null, null); /*10*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2016-01-01', null, 11, 2, 9, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*11*/
insert into podmienky_pracovneho_vztahu(platnost_od, platnost_do, pracovny_vztah, pozicia, dalsie_podmienky, uplatnenie_nezdanitelnej_casti, uplatnenie_danoveho_bonusu, drzitel_tzp_preukazu, poberatel_starobneho_vysluhoveho_dochodku, poberatel_invalidneho_vysluhoveho_dochodku_nad_40, poberatel_invalidneho_vysluhoveho_dochodku_nad_70, poberatel_predcasneho_dochodku, uplatnenie_odvodovej_vynimky, posielanie_vyplaty_na_ucet, cast_z_vyplaty_na_ucet, iban_uctu_pre_vyplatu)
    values ('2020-01-01', '2020-12-31', 12, 7, null, false, false, false, false, false, false, false, false, true, 1, 'SK9011000000002600000126'); /*11*/

insert into forma_mzdy(nazov, jednotka_vykonu, skratka_jednotky)
    values ('časová', 'hodina', 'hod');
insert into forma_mzdy(nazov, jednotka_vykonu, skratka_jednotky)
    values ('časová', 'mesiac', 'mes');
insert into forma_mzdy(nazov, jednotka_vykonu, skratka_jednotky)
    values ('výkonová', 'kus', 'ks');
insert into forma_mzdy(nazov, jednotka_vykonu, skratka_jednotky)
    values ('podielová', 'euro', '€');
insert into forma_mzdy(nazov, jednotka_vykonu, skratka_jednotky)
    values ('výkon práce', 'dosiahnutie cieľa', 'cieľ');

insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
    values (950, 'pravidelne', 'časová mzda',true, true, null, 2, 1); /*2*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
    values (950, 'pravidelne','časová mzda',true, true, null, 2, 2); /*3*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (2.9890, 'pravidelne','časová mzda', true, true, null, 1, 3);  /*4*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (3.3400, 'pravidelne','časová mzda', true,true, null, 1, 4);  /*4*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (3.3400, 'pravidelne','časová mzda',true, true, null, 1, 5);  /*5*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (5.5000, 'pravidelne','časová mzda',true, true, null, 1, 6);  /*6*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (3.5000, 'pravidelne', 'časová mzda',true, true, null, 1, 7);  /*7*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (3.3400, 'pravidelne','časová mzda',true, true, null, 1, 8);  /*8*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (1.5000, 'pravidelne','časová mzda',true, true, null, 1, 9);  /*9*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (0.0500, 'pravidelne', 'podielová mzda - bar',false, false, null, 4, 9);  /*9*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (0.0500, 'pravidelne', 'podielová mzda - kuchyňa',false, false, null, 4, 9);  /*9*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (0.3000, 'pravidelne', 'vykonná mzda', true, true, null, 3, 10);  /*10*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (950, 'pravidelne', 'časová mzda', true, true, null, 2, 11);  /*11*/
insert into zakladna_mzda(tarifa_za_jednotku_mzdy, sposob_vyplacania, popis, vykon_eviduje_zamestnanec, nutne_evidovanie_casu,  datum_vyplatenia, forma_mzdy, podmienky_pracovneho_vztahu)
     values (0.3500, 'pravidelne', 'vykonná mzda', true, true, null, 3, 12);  /*11*/

insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 1);  /*2*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 2);  /*3*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 3);  /*4*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 5);  /*5*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 6);  /*6*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 7);  /*7*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 8);  /*8*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 9);  /*9*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 10);  /*10*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 11);  /*11*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2019',5, 21, 0, null, null, null, null, 12);  /*11*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 1);  /*2*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 2);  /*3*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 4);  /*4*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 5);  /*5*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 6);  /*6*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 7);  /*7*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 8);  /*8*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 9);  /*9*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 10);  /*10*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
     values ('2020',5, 21, 0, null, null, null, null, 11);  /*11*/
insert into odpracovany_rok(rok, narok_na_dovolenku_z_minuleho_roka, narok_na_dovolenku, vycerpana_dovolenka, priemerna_mzda_1, priemerna_mzda_2, priemerna_mzda_3, priemerna_mzda_4, podmienky_pracovneho_vztahu)
    values ('2020',5, 21, 0, null, null, null, null, 12);  /*11*/

insert into odpracovany_mesiac(poradie_mesiaca, odpracovany_rok, vyplatna_paska)
    values (12,  1, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  2, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  3, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  4, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  5, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  6, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  7, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  8, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  9, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  10, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (12,  11, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  12, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  13, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  14, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  15, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  16, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  17, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  18, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  19, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  20, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  21, null);
insert into odpracovany_mesiac(poradie_mesiaca,  odpracovany_rok, vyplatna_paska)
    values (1,  22, null);


set @s = 0;
call podmienky_pracovneho_vztahu(13, '2019-06-06', @s);
/*select @s;*/

call podmienky_pracovneho_vztahu1(7, '2020-06-06');

set @r =0;
set @m = 0;
call rok_podmienok_pv(1, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 1,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-18', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 1,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(1, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 1,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m,1, '2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 1,'2020-01-31', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(2, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 2, '2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(2, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 2, '2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-15', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 2, '2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-28', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 2, '2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 2, '2020-01-31', '8:00', '16:00', null, null,null, null);


set @r =0;
set @m = 0;
call rok_podmienok_pv(3, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 3,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 3,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(4, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 4,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 4,'2020-01-31', '8:00', '16:00', null, null,null, null);


set @r =0;
set @m = 0;
call rok_podmienok_pv(5, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 5,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(5, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 5,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 5,'2020-01-31', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(6, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 6,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(6, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 6,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-25', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-26', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 6,'2020-01-31', '8:00', '16:00', null, null,null, null);


set @r =0;
set @m = 0;
call rok_podmienok_pv(7, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 7,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-05', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 7,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(7, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 7,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-13', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 7,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-23', '8:00', '16:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 7,'2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 7,'2020-01-31', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(8, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 8,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(8, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 8,'2020-01-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-21', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-24', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 8,'2020-01-31', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 9,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-14', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-15', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-28', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-29', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 10,'2019-12-01', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-02', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-03', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-04', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-05', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-06', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-09', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-10', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-11', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-14', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-15', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-16', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-17', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-18', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-19', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-20', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-28', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-29', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2019-12-30', null, null, null, null,900, null);


set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 11,'2019-12-01', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-02', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-03', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-04', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-05', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-06', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-09', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-10', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-11', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-14', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-15', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-16', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-17', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-18', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-19', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-20', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-28', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-29', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2019-12-30', null, null, null, null,900, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2020-01-02', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 9,'2020-01-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-07', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-08', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-14', '16:00', '00:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-15', '18:00', '02:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-20', '18:00', '02:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-21', '18:00', '02:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-22', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-25', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-26', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-28', '18:00', '02:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-29', '18:00', '02:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-30', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 9,'2020-01-31', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2020-01-02', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 10,'2020-01-01', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-03', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-07', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-08', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-11', null, null, 2.00, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-12', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-13', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-14', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-15', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-16', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-17', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-20', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-21', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-22', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-25', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-26', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-27', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-28', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-29', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-30', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 10,'2020-01-31', null, null, null, null,900, null);


set @r =0;
set @m = 0;
call rok_podmienok_pv(9, '2020-01-02', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 11,'2020-01-01', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-03', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-07', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-08', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-11', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-12', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-13', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-14', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-15', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-16', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-17', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-20', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-21', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-22', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-25', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-26', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-27', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-28', null, null, null, null,1000, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-29', null, null, null, null,1100, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-30', null, null, null, null,900, null);
call pridat_odpracovane_hodiny(@m, 11,'2020-01-31', null, null, null, null,900, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(10, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 12,'2019-12-01', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-02', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-03', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-04', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-05', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-06', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-09', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-10', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-11', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-12', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-13', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-16', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-17', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-18', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-19', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-20', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-23', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-27', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2019-12-30', '10:00', '13:00', null, 250,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(10, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 12,'2020-01-02', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-03', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-07', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-08', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-09', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-10', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-13', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-14', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-15', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-16', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-17', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-20', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-21', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-22', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-23', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-24', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-27', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-28', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-29', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-30', '10:00', '13:00', null, 250,null, null);
call pridat_odpracovane_hodiny(@m, 12,'2020-01-31', '10:00', '13:00', null, 250,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(11, '2019-12-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 13,'2019-12-01', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-02', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-03', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-04', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-05', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-06', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-09', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-10', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-11', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-12', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-13', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-16', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-17', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-18', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-19', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-20', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-23', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-27', '8:00', '16:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2019-12-30', '8:00', '16:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(11, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 13,'2020-01-02', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-03', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-07', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-08', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-09', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-10', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-13', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-14', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-15', '13:00', '18:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 13,'2020-01-16', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-17', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-20', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-21', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-22', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-23', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-24', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-27', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-28', '13:00', '18:00', null, null,null, null);;
call pridat_odpracovane_hodiny(@m, 13,'2020-01-29', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-30', '13:00', '18:00', null, null,null, null);
call pridat_odpracovane_hodiny(@m, 13,'2020-01-31', '13:00', '18:00', null, null,null, null);

set @r =0;
set @m = 0;
call rok_podmienok_pv(12, '2020-01-31', @r);
/*select @r;*/
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
/*select @m;*/
call pridat_odpracovane_hodiny(@m, 14,'2020-01-02', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-03', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-07', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-08', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-09', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-10', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-13', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-14', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-15', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-16', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-17', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-20', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-21', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-22', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-23', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-24', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-27', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-28', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-29', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-30', '10:00', '13:00', null, 150,null, null);
call pridat_odpracovane_hodiny(@m, 14,'2020-01-31', '10:00', '13:00', null, 150,null, null);

insert into mzdove_konstanty(zakladny_tyzdenny_pracovny_cas, max_vymeriavaci_zaklad, min_vymeriavaci_zaklad, max_denny_vymeriavaci_zaklad, danovy_bonus_na_dieta_nad_6, danovy_bonus_na_dieta_pod_6, NCZD_na_danovnika, nasobok_zivotneho_minima_pre_preddavok, platnost_od, platnost_do, zaciatok_nocnej_prace, koniec_nocnej_prace, max_vymeriavaci_zaklad_pre_OP, max_vyska_OP, hranica_prekrocenia_OV)
    values (40, 6678.00, 0.00, 66.6083, 22.17, 44.34, 367.58, 3096.95, '2019-07-01', '2019-12-31', '22:00', '06:00', 570, 380, 200);
insert into mzdove_konstanty(zakladny_tyzdenny_pracovny_cas, max_vymeriavaci_zaklad, min_vymeriavaci_zaklad, max_denny_vymeriavaci_zaklad, danovy_bonus_na_dieta_nad_6, danovy_bonus_na_dieta_pod_6, NCZD_na_danovnika, nasobok_zivotneho_minima_pre_preddavok, platnost_od, platnost_do, zaciatok_nocnej_prace, koniec_nocnej_prace, max_vymeriavaci_zaklad_pre_OP, max_vyska_OP, hranica_prekrocenia_OV)
    values (40, 7091.00, 0.00, 66.6083, 22.72, 45.44, 367.58, 3096.95, '2020-01-01', '2020-06-30', '22:00', '06:00', 570, 380, 200);

insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sobota', 'nepravidelné vyk.', 0.5, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sobota', 'pravidelné vyk.', 0.45, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nedeľa', 'nepravidelné vyk.', 1, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nedeľa', 'pravidelné vyk.', 0.9, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'nerizik - nepravid', 0.4, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'nerizik - pravid', 0.35, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'rizik.', 0.5, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sviatok', '.', 1, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nadčas', 'nerizik.', 0.25, 'minimálna mzda', '2019-05-01', '2019-12-31');
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nadčas', 'rizik.', 0.35, 'minimálna mzda', '2019-05-01', '2019-12-31');

insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sobota', 'nepravidelné vyk.', 0.5, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sobota', 'pravidelné vyk.', 0.45, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nedeľa', 'nepravidelné vyk.', 1, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nedeľa', 'pravidelné vyk.', 0.9, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'nerizik - nepravid', 0.4, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'nerizik - pravid', 0.35, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('noc', 'rizik.', 0.5, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sviatok', 'dohodár', 1, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('sviatok', 'zamestnanec', 1, 'priemerná mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nadčas', 'nerizik.', 0.25, 'minimálna mzda', '2020-01-01', null);
insert into typ_priplatku(nazov, popis, percentualna_cast, pocitany_zo, platnost_od, platnost_do)
    values('nadčas', 'rizik.', 0.35, 'minimálna mzda', '2020-01-01', null);















































select p.id, p.meno, p.priezvisko, pv.id, pv.typ, om.id, om.poradie_mesiaca, zm.popis, oh.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok join odpracovane_hodiny oh on om.id = oh.odpracovany_mesiac join zakladna_mzda zm on ppv.id = zm.podmienky_pracovneho_vztahu where p.id=9;




/*select p.nazov, sn.cislo_stupna from pozicia p join pozicia_stupen_narocnosti psn join stupen_narocnosti sn on p.id=psn.pozicia and psn.stupen_narocnosti = sn.id;*/
select p.*, count(v.id) as c from pracujuci p join pracovny_vztah v on p.id = v.pracujuci where now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null) group by p.id;
select p.*, count(v.id) as c from pracujuci p left join pracovny_vztah v on p.id = v.pracujuci  where (now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null)) or v.pracujuci is null group by p.id;
select p.*, count(v.id) as c from pracujuci p left join pracovny_vztah v on p.id = v.pracujuci  group by p.id;

/*select v.typ, pr.nazov from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia po join pracovisko pr
on p.id = v.pracujuci and v.id = ppv.pracovny_vztah and ppv.pozicia = po.id and po.pracovisko = pr.id
where now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null) and p.id = 11;*/

select pk.id as pk_id, pk.*, p.id as p_id, p.*, du.id as du_id, du.* from prihlasovacie_konto pk join pracujuci p join dolezite_udaje_pracujuceho du on p.id = du.pracujuci and p.prihlasovacie_konto = pk.id where p.id = 1 and now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null);

select p.id, v.*, ppv.id, ppv.dalsie_podmienky, poz.nazov, pr.nazov from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id
where (p.id = 1)  /*and (now() < v.datum_vyprsania or v.datum_vyprsania is null) and  (now() < ppv.platnost_do or ppv.platnost_do is null)*/ ;
select p.id, v.*, ppv.id, ppv.dalsie_podmienky, poz.nazov, pr.nazov from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id
where   (p.id = 1)  and (now() < v.datum_vyprsania or v.datum_vyprsania is null or v.datum_vyprsania = '0000-00-00') and  (now() < ppv.platnost_do or ppv.platnost_do is null or ppv.platnost_do = '0000-00-00') ;

select count(vp.id) as c from dolezite_udaje_pracujuceho dup join vyplatna_paska vp on dup.id = vp.dolezite_udaje_pracujuceho where dup.id = 1;

select dup.id from pracujuci p join dolezite_udaje_pracujuceho dup on p.id = dup.pracujuci where dup.platnost_do is not null and p.id = 1 order by dup.platnost_od desc limit 1;
select dup.* from pracujuci p join dolezite_udaje_pracujuceho dup on p.id = dup.pracujuci where p.id=1 order by dup.platnost_od;

select dp.*, ppv.*, dp.id as dp_id, ppv.id as ppv_id, pr.nazov as pr_nazov, po.nazov as po_nazov from dalsie_podmienky dp
    right outer join podmienky_pracovneho_vztahu ppv on dp.id = ppv.dalsie_podmienky
    join pozicia po on ppv.pozicia = po.id
    join pracovisko pr on po.pracovisko = pr.id
    where ppv.pracovny_vztah=2;

select p.id, v.*, ppv.id, ppv.dalsie_podmienky, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id where  p.id = 11 order by v.datum_vzniku desc;
select * from zakladna_mzda zm where zm.podmienky_pracovneho_vztahu = 9;

select count(id) from podmienky_pracovneho_vztahu where pracovny_vztah = 2;

select pk.id as pk_id, pk.email, pk.typ_prav, p.id as p_id, p.*, du.id as du_id, du.*,  DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as nice_date1, DATE_FORMAT(du.platnost_od,'%d.%m.%Y') as nice_date2 from prihlasovacie_konto pk right join pracujuci p  on p.prihlasovacie_konto = pk.id join dolezite_udaje_pracujuceho du on p.id = du.pracujuci where p.id = 13 and now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null or du.platnost_do = '0000-00-00');

/*ALTER TABLE prihlasovacie_konto
ADD COLUMN aktualne boolean not null default true;*/


select p.*,  v.typ as p2_nazov, p3.nazov as p3_nazov from pracovisko p3 join pozicia p2 on p2.pracovisko = p3.id join podmienky_pracovneho_vztahu ppv on ppv.pozicia = p2.id join pracovny_vztah v on v.id = ppv.pracovny_vztah right join pracujuci p on  p.id = v.pracujuci order by p.id;

select p.*,  v.typ as p2_nazov, p3.nazov as p3_nazov from pracujuci p left join pracovny_vztah v on p.id = v.pracujuci join podmienky_pracovneho_vztahu ppv on v.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id order by p.id;

select po.*, pr.nazov, sn.cislo_stupna from pracovisko pr join pozicia po on pr.id = po.pracovisko join pozicia_stupen_narocnosti psn on po.id = psn.pozicia join stupen_narocnosti sn on psn.stupen_narocnosti = sn.id where sn.platnost_od <= now() and sn.platnost_do>= now() or sn.platnost_do is null;
select sn.* from stupen_narocnosti sn where sn.platnost_od <= now() and sn.platnost_do>= now() or sn.platnost_do is null;

select * from minimalna_mzda where stupen_narocnosti = 1 order by platnost_od desc limit 1;

select pk.id as pk_id, pk.email, pk.typ_prav, p.id as p_id, p.*, du.id as du_id, du.*,  DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as nice_date1, DATE_FORMAT(du.platnost_od,'%d.%m.%Y') as nice_date2
from prihlasovacie_konto pk
         right join pracujuci p  on p.prihlasovacie_konto = pk.id
         left join dolezite_udaje_pracujuceho du on p.id = du.pracujuci
where p.id = 12  and  ((now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null)));

select pk.id as pk_id, pk.email, pk.typ_prav, p.id as p_id, p.*,  DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as nice_date1 from prihlasovacie_konto pk right join pracujuci p  on p.prihlasovacie_konto = pk.id where p.id = 12 ;

select du.id as du_id, du.*, DATE_FORMAT(du.platnost_od,'%d.%m.%Y') as nice_date2 from dolezite_udaje_pracujuceho du where du.pracujuci = 12  and  ((now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null)));

select count(v.id) as count from pracovny_vztah v where pracujuci = 11;

select fm.id as fm_id, fm.nazov as fm_nazov, zm.id as zm_id, zm.popis as zm_popis, oh.*,  DATE_FORMAT(oh.datum,'%d.%m.%Y') as nice_date1, TIME_FORMAT(oh.od, '%H:%i') as nice_time1, om.id as om_id, om.poradie_mesiaca,
    orr.id as orr_id, orr.rok, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov
from forma_mzdy fm
    join zakladna_mzda zm on fm.id = zm.forma_mzdy
    join odpracovane_hodiny oh on zm.id = oh.zakladna_mzda
    join odpracovany_mesiac om on oh.odpracovany_mesiac = om.id
    join odpracovany_rok orr on om.odpracovany_rok = orr.id
    join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id
    join pracovny_vztah pv on ppv.pracovny_vztah = pv.id
    join pracujuci p on pv.pracujuci = p.id
    join pozicia po on ppv.pozicia = po.id
    join pracovisko pr on po.pracovisko = pr.id
where oh.datum = '2020-01-02' order by p_id;

UPDATE odpracovane_hodiny SET do = '16:30:00' WHERE odpracovane_hodiny.id = 1;

select p.id as p_id, p.meno as p_meno, p.priezvisko as p_priezvisko, pv.*, ppv.id as ppv_id, ppv.platnost_od as ppv_platnost_od, ppv.platnost_do as ppv_platnost_do, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where ppv.id=1;
select om.* from podmienky_pracovneho_vztahu ppv left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok
where ppv.id = 12 and o.rok=2020 and om.poradie_mesiaca=2;

select fm.id as fm_id, fm.nazov as fm_nazov, zm.id as zm_id, zm.popis as zm_popis, oh.*, oh.id as oh_id,  DATE_FORMAT(oh.datum,'%d.%m.%Y') as nice_date1, TIME_FORMAT(oh.od, '%H %i') as nice_time1,
       TIME_FORMAT(oh.do, '%H %i') as nice_time2,  om.id as om_id, om.poradie_mesiaca, orr.id as orr_id, orr.rok, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id,
       p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov
from forma_mzdy fm
    join zakladna_mzda zm on fm.id = zm.forma_mzdy
    join odpracovane_hodiny oh on zm.id = oh.zakladna_mzda
    join odpracovany_mesiac om on oh.odpracovany_mesiac = om.id
    join odpracovany_rok orr on om.odpracovany_rok = orr.id
    join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id
    join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id
    join pozicia po on ppv.pozicia = po.id
    join pracovisko pr on po.pracovisko = pr.id
where oh.aktualizovane <= now() and oh.aktualizovane >= DATE_SUB(now(), INTERVAL 3 DAY) order by oh.aktualizovane desc;




select n.*, n.id as n_id,  DATE_FORMAT(n.od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(n.do,'%d.%m.%Y') as nice_date2,  om.id as om_id, om.poradie_mesiaca, orr.id as orr_id, orr.rok, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from nepritomnost n join odpracovany_mesiac_nepritomnost omn on n.id=omn.nepritomnost join odpracovany_mesiac om on omn.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where n.aktualizovane <= now() and n.aktualizovane >= DATE_SUB(now(), INTERVAL 3 DAY)  group by n.id order by n.aktualizovane desc;
select n.*, n.id as n_id,  DATE_FORMAT(n.od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(n.do,'%d.%m.%Y') as nice_date2, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from nepritomnost n join odpracovany_mesiac_nepritomnost omn on n.id=omn.nepritomnost join odpracovany_mesiac om on omn.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where n.aktualizovane <= now() and n.aktualizovane >= DATE_SUB(now(), INTERVAL 3 DAY)  group by n.id order by n.aktualizovane desc;

select p.id as p_id, v.*, v.id as v_id, ppv.id as ppv_id, ppv.dalsie_podmienky, ppv.platnost_od, ppv.platnost_do, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id where  p.id = 13 order by v.datum_vzniku desc;

/*vypise vztahy*/
select p.id as p_id, v.*, v.id as v_id,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracujuci p join pracovny_vztah v on v.pracujuci=p.id where  p.id = 13 order by v.datum_vzniku desc;
/*pre aktualny vztah*/
select  ppv.id as ppv_id, ppv.dalsie_podmienky, ppv.platnost_od, ppv.platnost_do, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as nice_date_platnost_od,  DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as nice_date_platnost_do from podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on ppv.pozicia=poz.id and poz.pracovisko=pr.id where  ppv.pracovny_vztah = 13 and ((now() between ppv.platnost_od and ppv.platnost_do) or (now()>=ppv.platnost_od and ppv.platnost_do is NULL));
/*pre buduci vztah*/
select  ppv.id as ppv_id, ppv.dalsie_podmienky, ppv.platnost_od, ppv.platnost_do, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as nice_date_platnost_od,  DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as nice_date_platnost_do from podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on ppv.pozicia=poz.id and poz.pracovisko=pr.id where  ppv.pracovny_vztah = 13 order by ppv.platnost_od asc limit 1;
/*pre minuly vztah*/
select  ppv.id as ppv_id, ppv.dalsie_podmienky, ppv.platnost_od, ppv.platnost_do, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as nice_date_platnost_od,  DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as nice_date_platnost_do from podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on ppv.pozicia=poz.id and poz.pracovisko=pr.id where  ppv.pracovny_vztah = 13 order by ppv.platnost_od desc limit 1;





select p.*, v.id as v_id, v.typ as p2_nazov, p3.nazov as p3_nazov, ppv.platnost_od, ppv.platnost_do from pracovisko p3 join pozicia p2 on p2.pracovisko = p3.id join podmienky_pracovneho_vztahu ppv on ppv.pozicia = p2.id join pracovny_vztah v on v.id = ppv.pracovny_vztah right join pracujuci p on  p.id = v.pracujuci order by p.id, platnost_od;

select rok from odpracovany_rok group by rok order by rok;

select p.id as p_id, p.priezvisko, p.meno, pv.id as pv_id, pv.typ, ppv.id as ppv_id, ppv.platnost_od, ppv.platnost_do, p2.id as p2_id, p2.nazov as p2_nazov, p3.id as p3_id, p3.nazov as p3_nazov, o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca, om.je_mesiac_uzatvoreny, vp.id as vp_id, vp.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where (('2020-01-01' between ppv.platnost_od and ppv.platnost_do) or (ppv.platnost_od<='2020-01-01' and platnost_do is null)) and o.rok = 2020 and om.poradie_mesiaca = 1 order by p.priezvisko, p.meno, p.id;

select * from podmienky_pracovneho_vztahu;



/*vypis mzdovych konstant*/
select *
from mzdove_konstanty mk
where (('2020-01-01' between mk.platnost_od and mk.platnost_do) or ('2020-01-01'>=mk.platnost_od and mk.platnost_do is null));

/*vypis najnizsej minimalnej mzdy pre dohodarov*/
select *
from minimalna_mzda mm
where stupen_narocnosti = 1 and (('2020-01-01' between mm.platnost_od and mm.platnost_do) or ('2020-01-01'>=mm.platnost_od and mm.platnost_do is null));



/*pre PP kedze dohody maju inú minimalnu mzdu (id mesiaca, datum prveho dna mesiaca v ktorom sa bude pocitat mzda)*/
select dup.id as dup_id, dup.mesto as dup_mesto, dup.ulica as dup_ulica, dup.cislo as dup_cislo, dup.*,
    p.id as p_id, DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as p_datum_narodenia, p.*,
    pv.id as pv_id, DATE_FORMAT(pv.datum_vzniku,'%d.%m.%Y') as pv_datum_vzniku, DATE_FORMAT(pv.datum_vyprsania,'%d.%m.%Y') as pv_datum_vyprsania, pv.*,
    ppv.id as ppv_id, DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as ppv_platnost_od, DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as ppv_platnost_do, ppv.*,
    dp.id as dp_id, dp.*,
    p2.id as p2_id, p2.nazov as p2_nazov, p2.charakteristika as p2_charakteristika, p2.*,
    p3.id as p3_id, p3.nazov as p3_nazov, p3.*,
    sn.id as sn_id, sn.charakteristika as sn_charakteristika, sn.*,
    mm.id as mm_id, DATE_FORMAT(mm.platnost_od,'%d.%m.%Y') as mm_platnost_od, DATE_FORMAT(mm.platnost_do,'%d.%m.%Y') as mm_platnost_do, mm.*,
    o.id as o_id, o.*,
    om.id as om_id, om.*
from dolezite_udaje_pracujuceho dup
    join pracujuci p on dup.pracujuci = p.id
    join pracovny_vztah pv on p.id = pv.pracujuci
    join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah
    left join dalsie_podmienky dp on ppv.dalsie_podmienky = dp.id
    join pozicia p2 on ppv.pozicia = p2.id
    join pracovisko p3 on p2.pracovisko = p3.id
    join pozicia_stupen_narocnosti psn on p2.id = psn.pozicia
    join stupen_narocnosti sn on psn.stupen_narocnosti = sn.id
    join minimalna_mzda mm on sn.id = mm.stupen_narocnosti
    join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu
    join odpracovany_mesiac om on o.id = om.odpracovany_rok
where (om.id=22)
    and (('2020-01-01' between dup.platnost_od and dup.platnost_do) or ('2020-01-01'>=dup.platnost_od and dup.platnost_do is null))
    and (('2020-01-01' between mm.platnost_od and mm.platnost_do) or ('2020-01-01'>=mm.platnost_od and mm.platnost_do is null));

/*pre vypis zakladnych miezd vztahu*/
select zm.id as zm_id, zm.*, fm.id as fm_id, fm.*
from odpracovany_mesiac om
    join odpracovany_rok o on om.odpracovany_rok = o.id
    join podmienky_pracovneho_vztahu ppv on o.podmienky_pracovneho_vztahu = ppv.id
    join zakladna_mzda zm on ppv.id = zm.podmienky_pracovneho_vztahu
    join forma_mzdy fm on zm.forma_mzdy = fm.id
where (om.id=19);

select o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca
from odpracovany_rok o
    join odpracovany_mesiac om on o.id = om.odpracovany_rok
where o.podmienky_pracovneho_vztahu=12
    and om.je_mesiac_uzatvoreny=false;

select om.id as om_id, oh.id as oh_id, oh.*, TIME_FORMAT(oh.od, '%H:%i'), TIME_FORMAT(oh.do, '%H:%i'), zm.id as zm_id
from odpracovany_mesiac om
    join odpracovane_hodiny oh on om.id = oh.odpracovany_mesiac
    join zakladna_mzda zm on oh.zakladna_mzda = zm.id
    join forma_mzdy fm on zm.forma_mzdy = fm.id
where om.id = 19
order by zm.id, oh.datum, oh.od;

select o.*
from pracovny_vztah pv
    join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah
    join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu
where pv.id=1 and o.rok=2020
order by ppv.platnost_od desc;

select o.rok, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where om.id = 1;


select tp.*, DATE_FORMAT(tp.platnost_od,'%d.%m.%Y') as platnost_od , DATE_FORMAT(tp.platnost_do,'%d.%m.%Y') as platnost_do
from typ_priplatku tp where ('2020-01-01' between platnost_od and platnost_do) or ('2020-01-01'>= tp.platnost_od and tp.platnost_do is null);

select * from typ_priplatku;

select mk.* from mzdove_konstanty mk where (('2020-01-01' between mk.platnost_od and mk.platnost_do) or ('2020-01-01'>=mk.platnost_od and mk.platnost_do is null));

select * from odpracovany_mesiac_nepritomnost omn join nepritomnost n on omn.nepritomnost = n.id where omn.odpracovany_mesiac=19;

select om.*, vp.*
from pracovny_vztah pv
    join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah
    join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu
    join odpracovany_mesiac om on o.id = om.odpracovany_rok
    left join vyplatna_paska vp on om.vyplatna_paska = vp.id
where pv.id=1 and o.rok=2019 and om.poradie_mesiaca=12;

select * from vyplatna_paska;


select * from vyplatna_paska where id=4;
select pv.typ, dp.dohodnuty_tyzdenny_pracovny_cas, p2.nazov, p3.priezvisko, p3.meno as pracovisko, p.nazov as pozicia from vyplatna_paska vp join odpracovany_mesiac om on vp.id = om.vyplatna_paska join odpracovany_rok o on om.odpracovany_rok = o.id join podmienky_pracovneho_vztahu ppv on o.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pozicia p on ppv.pozicia = p.id join pracovisko p2 on p.pracovisko = p2.id join pracujuci p3 on pv.pracujuci = p3.id left join dalsie_podmienky dp on ppv.dalsie_podmienky = dp.id where vp.id=4 limit 1;


select o.rok, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where om.vyplatna_paska=4;

select zakladna_zlozka.*, zakladna_mzda.popis, zakladna_mzda.tarifa_za_jednotku_mzdy, forma_mzdy.nazov, forma_mzdy.skratka_jednotky from zakladna_zlozka join zakladna_mzda on zakladna_zlozka.zakladna_mzda = zakladna_mzda.id join forma_mzdy on zakladna_mzda.forma_mzdy = forma_mzdy.id where vyplatna_paska=4;
select * from pohybliva_zlozka where vyplatna_paska=4;
select * from nahrada where vyplatna_paska=4;
select priplatok.*, typ_priplatku.nazov from priplatok join typ_priplatku on priplatok.typ_priplatku = typ_priplatku.id where vyplatna_paska=4;
select * from ina_zlozka_mzdy where vyplatna_paska=4;
select * from odvod where vyplatna_paska=4;
select * from zrazka where vyplatna_paska=4;


select * from pracujuci p join dolezite_udaje_pracujuceho dup on p.id = dup.pracujuci
where p.prihlasovacie_konto = 1 and now() > dup.platnost_od and (now() < dup.platnost_do or dup.platnost_do is null);

select p.prihlasovacie_konto from podmienky_pracovneho_vztahu ppv join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id where ppv.id=2;
select zm.id as zm_id, zm.*, fm.id as fm_id from  podmienky_pracovneho_vztahu ppv join zakladna_mzda zm on ppv.id = zm.podmienky_pracovneho_vztahu join forma_mzdy fm on zm.forma_mzdy = fm.id where ppv.id=9;