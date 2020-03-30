<?php
defined('BASEPATH') OR exit('No direct script access allowed');

/*
| -------------------------------------------------------------------------
| URI ROUTING
| -------------------------------------------------------------------------
| This file lets you re-map URI requests to specific controller functions.
|
| Typically there is a one-to-one relationship between a URL string
| and its corresponding controller class/method. The segments in a
| URL normally follow this pattern:
|
|	example.com/class/method/id/
|
| In some instances, however, you may want to remap this relationship
| so that a different class/function is called than the one
| corresponding to the URL.
|
| Please see the user guide for complete details:
|
|	https://codeigniter.com/user_guide/general/routing.html
|
| -------------------------------------------------------------------------
| RESERVED ROUTES
| -------------------------------------------------------------------------
|
| There are three reserved routes:
|
|	$route['default_controller'] = 'welcome';
|
| This route indicates which controller class should be loaded if the
| URI contains no data. In the above example, the "welcome" class
| would be loaded.
|
|	$route['404_override'] = 'errors/page_missing';
|
| This route will tell the Router which controller/method to use if those
| provided in the URL cannot be matched to a valid route.
|
|	$route['translate_uri_dashes'] = FALSE;
|
| This is not exactly a route, but allows you to automatically route
| controller and method names that contain dashes. '-' isn't a valid
| class or method name character, so it requires translation.
| When you set this option to TRUE, it will replace ALL dashes in the
| controller and method URI segments.
|
| Examples:	my-controller/index	-> my_controller/index
|		my-controller/my-method	-> my_controller/my_method
*/

$route['auth/login']['post']           = 'auth/login';

$route['employee']['get']           = 'employee';
$route['employee/emp_usr/(:num)']['get']           = 'employee/emp_usr/$1';
$route['employee/emps_usrs']['get']           = 'employee/emps_usrs';
$route['employee/update']['post']           = 'employee/update';
$route['employee/update_imp']['post']           = 'employee/update_imp';
$route['employee/crt_emp']['post']           = 'employee/crt_emp';
$route['employee/crt_usr']['post']           = 'employee/crt_usr';
$route['employee/upd_usr']['post']           = 'employee/upd_usr';
$route['employee/upd_usr_pas']['post']           = 'employee/upd_usr_pas';
$route['employee/detail/(:num)']['get']           = 'employee/detail/$1';
$route['employee/relations_places_details/(:num)']['get']		 = 'employee/relations_places_details/$1';
$route['employee/emp_usr_imp/(:num)']['get'] 		= 'employee/emp_usr_imp/$1';
$route['employee/emp_rel_ov/(:num)']['get'] 		= 'employee/emp_rel_ov/$1';
$route['employee/count_imp_pay/(:num)']['get']		 = 'employee/count_imp_pay/$1';
$route['employee/all_imp/(:num)']['get'] 		= 'employee/all_imp/$1';
$route['employee/delete_imp/(:num)']['delete'] 			= 'employee/delete_imp/$1';
$route['employee/del_emp/(:num)']['delete'] 			= 'employee/del_emp/$1';

$route['relation/detail/(:num)']['get']           = 'relation/detail/$1';
$route['relation/nxt_dmnds_d/(:num)']['get']           = 'relation/nxt_dmnds_d/$1';
$route['relation/con_ncon_po_pl/(:num)']['get']           = 'relation/con_ncon_po_pl/$1';
$route['relation/rel_by_cons/(:num)']['get']           = 'relation/rel_by_cons/$1';
$route['relation/emp_rel_cons_po_pl']['get']           = 'relation/emp_rel_cons_po_pl';
$route['relation/crt_rel']['post']           = 'relation/crt_rel';
$route['relation/upd_rel']['post']           = 'relation/upd_rel';
$route['relation/del_rel/(:num)']['delete']           = 'relation/del_rel/$1';
$route['relation/del_cons/(:num)']['delete']           = 'relation/del_cons/$1';
$route['relation/del_lst_cons/(:num)/(:num)']['delete']           = 'relation/del_lst_cons/$1/$2';

$route['place']['get']           = 'place';
$route['place/del_place/(:num)']['delete']           = 'place/del_place/$1';
$route['place/crt_place']['post']           = 'place/crt_place';

$route['position']['get']           = 'position';
$route['position/pos_lvl']['get']           = 'position/pos_lvl';
$route['position/lvls']['get']           = 'position/lvls';
$route['position/crt_pos']['post']           = 'position/crt_pos';
$route['position/upd_pos']['post']           = 'position/upd_pos';
$route['position/del_pos/(:num)/(:num)']['delete']           = 'position/del_pos/$1/$2';

$route['wage/forms']['get']           = 'wage/forms';
$route['wage/del_form/(:num)']['delete']           = 'wage/del_form/$1';
$route['wage/wage_of_con/(:num)']['get']           = 'wage/wage_of_con/$1';
$route['wage/wage_form_of_con/(:num)']['get']           = 'wage/wage_form_of_con/$1';
$route['wage/wage_form/(:num)']['get']           = 'wage/wage_form/$1';
$route['wage/crt_wage']['post']           = 'wage/crt_wage';
$route['wage/crt_form']['post']           = 'wage/crt_form';

$route['minwage']['get']           = 'minwage';
$route['minwage/del_minwg/(:num)']['delete']           = 'minwage/del_minwg/$1';
$route['minwage/crt_minwg']['post']           = 'minwage/crt_minwg';
$route['minwage/upd_minwg']['post']           = 'minwage/upd_minwg';

$route['level']['get']           = 'level';
$route['level/crt_lev']['post']           = 'level/crt_lev';
$route['level/upd_lev']['post']           = 'level/upd_lev';
$route['level/del_lev/(:num)']['delete']           = 'level/del_lev/$1';

$route['surcharge']['get']           = 'surcharge';
$route['surcharge/del_sur/(:num)']['delete']           = 'surcharge/del_sur/$1';
$route['surcharge/crt_sur']['post']           = 'surcharge/crt_sur';
$route['surcharge/upd_sur']['post']           = 'surcharge/upd_sur';

$route['hours/hrs/(:any)']['get']           = 'hours/hrs/$1';
$route['hours/crt_hrs']['post']           = 'hours/crt_hrs';
$route['hours/upd_hrs']['post']           = 'hours/upd_hrs';
$route['hours/del_hrs/(:num)']['delete']           = 'hours/del_hrs/$1';

$route['absence/bsnc/(:any)']['get']           = 'absence/bsnc/$1';
$route['absence/crt_bsnc']['post']           = 'absence/crt_bsnc';
$route['absence/crt_fst']['post']           = 'absence/crt_fst';
$route['absence/del_bsnc/(:num)']['delete']           = 'absence/del_bsnc/$1';

$route['default_controller'] = 'Employee';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
