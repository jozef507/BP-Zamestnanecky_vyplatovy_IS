


delimiter //

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


set @r =0;
set @m = 0;
call rok_podmienok_pv(1, '2019-12-31', @r);
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-02', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2019-12-31', @m);
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

set @r = 0;
set @m = 0;
call rok_podmienok_pv(11, '2020-01-31', @r);
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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
call mesiac_roka_podmienok(@r, '2020-01-31', @m);
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

