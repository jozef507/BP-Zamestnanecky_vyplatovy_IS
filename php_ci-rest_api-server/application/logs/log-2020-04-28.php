<?php defined('BASEPATH') OR exit('No direct script access allowed'); ?>

ERROR - 2020-04-28 18:23:18 --> Severity: Notice --> Undefined offset: 0 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 51
ERROR - 2020-04-28 18:23:18 --> Severity: Notice --> Undefined offset: 0 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 52
ERROR - 2020-04-28 18:23:18 --> Severity: Notice --> Undefined offset: 0 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 77
ERROR - 2020-04-28 18:23:18 --> Severity: Notice --> Undefined offset: 0 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 85
ERROR - 2020-04-28 18:23:18 --> Query error: You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'and om.je_mesiac_uzatvoreny=false order by om.id desc' at line 1 - Invalid query: select o.id as o_id, o.*, om.id as om_id, om.* from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where o.podmienky_pracovneho_vztahu= and om.je_mesiac_uzatvoreny=false order by om.id desc
ERROR - 2020-04-28 18:23:18 --> Severity: error --> Exception: Call to a member function result_array() on bool /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 87
