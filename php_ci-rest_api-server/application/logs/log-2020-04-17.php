<?php defined('BASEPATH') OR exit('No direct script access allowed'); ?>

ERROR - 2020-04-17 13:56:33 --> Severity: Notice --> Undefined variable: interesting_unclosed_months_num /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 104
ERROR - 2020-04-17 13:56:33 --> Severity: Notice --> Undefined variable: interesting_unclosed_months /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 110
ERROR - 2020-04-17 13:56:33 --> Severity: Warning --> array_merge(): Expected parameter 2 to be an array, null given /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 110
ERROR - 2020-04-17 14:23:01 --> 404 Page Not Found: Payment/nffr_mplpmnt
ERROR - 2020-04-17 14:23:08 --> Severity: Notice --> Undefined offset: 1 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 52
ERROR - 2020-04-17 14:23:08 --> Severity: Notice --> Undefined offset: 0 /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 86
ERROR - 2020-04-17 14:23:08 --> Query error: You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near 'and om.je_mesiac_uzatvoreny=false' at line 1 - Invalid query: select o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where o.podmienky_pracovneho_vztahu= and om.je_mesiac_uzatvoreny=false
ERROR - 2020-04-17 14:23:08 --> Severity: error --> Exception: Call to a member function result_array() on bool /opt/lampp/htdocs/bp_rest_api/application/models/PaymentMod.php 88
