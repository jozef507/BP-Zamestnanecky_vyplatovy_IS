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


