
--tuples
insert into campus values ('Triolet','Montpellier');
insert into campus values ('St Priest','Montpellier');
insert into campus values ('Pharmacie','Montpellier');
insert into campus values ('Richter','Montpellier');
insert into campus values ('FDE Mende','Mende');
insert into campus values ('Medecine Nimes','Nimes');

insert into batiment values ('triolet_b36',2019,'Triolet');
insert into batiment values ('triolet_b16',1966,'Triolet');
insert into batiment values ('triolet_b05',1964,'Triolet');
insert into batiment values ('stPriest_b02',1982,'St Priest');

insert into salle values ('A36.03',120,'amphi','oui','rdc','triolet_b36');
insert into salle values ('A36.02',120,'amphi','oui','rdc','triolet_b36');
insert into salle values ('A36.01',120,'amphi','oui','rdc','triolet_b36');
insert into salle values ('TD36.202',40,'numerique','oui','2','triolet_b36');
insert into salle values ('TD36.203',40,'numerique','oui','2','triolet_b36');
insert into salle values ('TD36.204',40,'numerique','oui','2','triolet_b36');
insert into salle values ('SC36.04',80,'sc','oui','1','triolet_b36');
insert into salle values ('TD36.101',40,'td','oui','1','triolet_b36');
insert into salle values ('TD36.302',40,'td','oui','3','triolet_b36');
insert into salle values ('TD36.402',40,'td','oui','4','triolet_b36');
insert into salle values ('SC16.03',120,'amphi','oui','rdc','triolet_b16');
insert into salle values ('TD16.02',18,'td','oui','rdc','triolet_b16');
insert into salle values ('TPDeptInfo',40,'numerique','oui','rdc','triolet_b16');
insert into salle values ('TPBio',40,'tp','oui','rdc','triolet_b16');
insert into salle values ('SC16.05',48,'sc','oui','rdc','triolet_b16');
insert into salle values ('A5.02',275,'amphi','oui','1','triolet_b05');
insert into salle values ('TD5.125',20,'numerique','oui','rdc','triolet_b05');
insert into salle values ('TD5.126',31,'numerique','oui','rdc','triolet_b05');
insert into salle values ('TD5.210',40,'numerique','oui','1','triolet_b05');
insert into salle values ('TD5.201',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.202',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.203',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.204',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.205',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.206',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.207',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.208',40,'td','oui','rdc','triolet_b05');
insert into salle values ('TD5.209',40,'td','oui','rdc','triolet_b05');
insert into salle values ('A_JJMoreau',114,'amphi','oui','1','stPriest_b02');

insert into composante values ('FDS','Faculte des Sciences','JM. Marin');
insert into composante values ('IAE','Ecole Universitaire de Management','E Houze');
insert into composante values ('Polytech','Polytech Montpellier','L. Torres');

insert into exploite values ('FDS','triolet_b16');
insert into exploite values ('IAE','triolet_b16');
insert into exploite values ('FDS','triolet_b36');
insert into exploite values ('IAE','triolet_b05');
commit;




