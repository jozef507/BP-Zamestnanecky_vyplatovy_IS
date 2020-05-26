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

$route['auth/login']['post']           = 'Auth/login';
$route['auth/logout']['post']           = 'Auth/logout';

$route['employee']['get']           = 'Employee';
$route['employee/emp_usr/(:num)']['get']           = 'Employee/emp_usr/$1';
$route['employee/emps_usrs']['get']           = 'Employee/emps_usrs';
$route['employee/update']['post']           = 'Employee/update';
$route['employee/update_imp']['post']           = 'Employee/update_imp';
$route['employee/crt_emp']['post']           = 'Employee/crt_emp';
$route['employee/crt_usr']['post']           = 'Employee/crt_usr';
$route['employee/upd_usr']['post']           = 'Employee/upd_usr';
$route['employee/upd_usr_pas']['post']           = 'Employee/upd_usr_pas';
$route['employee/detail/(:num)']['get']           = 'Employee/detail/$1';
$route['employee/relations_places_details/(:num)']['get']		 = 'Employee/relations_places_details/$1';
$route['employee/emp_usr_imp/(:num)']['get'] 		= 'Employee/emp_usr_imp/$1';
$route['employee/emp_rel_ov/(:num)']['get'] 		= 'Employee/emp_rel_ov/$1';
$route['employee/count_imp_pay/(:num)']['get']		 = 'Employee/count_imp_pay/$1';
$route['employee/all_imp/(:num)']['get'] 		= 'Employee/all_imp/$1';
$route['employee/delete_imp/(:num)']['delete'] 			= 'Employee/delete_imp/$1';
$route['employee/del_emp/(:num)']['delete'] 			= 'Employee/del_emp/$1';

$route['employee/nf/(:num)']['get']           = 'Employee/info/$1';


$route['relation/detail/(:num)']['get']           = 'Relation/detail/$1';
$route['relation/nxt_dmnds_d/(:num)']['get']           = 'Relation/nxt_dmnds_d/$1';
$route['relation/con_ncon_po_pl/(:num)']['get']           = 'Relation/con_ncon_po_pl/$1';
$route['relation/rel_by_cons/(:num)']['get']           = 'Relation/rel_by_cons/$1';
$route['relation/emp_rel_cons_po_pl']['get']           = 'Relation/emp_rel_cons_po_pl';
$route['relation/crt_rel']['post']           = 'Relation/crt_rel';
$route['relation/upd_rel']['post']           = 'Relation/upd_rel';
$route['relation/upd_rel_to']['post']           = 'Relation/upd_rel_to';
$route['relation/del_rel/(:num)']['delete']           = 'Relation/del_rel/$1';
$route['relation/del_cons/(:num)']['delete']           = 'Relation/del_cons/$1';
$route['relation/del_lst_cons/(:num)/(:num)']['delete']           = 'Relation/del_lst_cons/$1/$2';

$route['relation/emp_rel_cons_po_pl_of_emp/(:num)']['get']           = 'Relation/emp_rel_cons_po_pl_of_emp/$1';
$route['relation/wgs_of_rltn/(:num)']['get']           = 'Relation/wages_of_relation/$1';


$route['place']['get']           = 'Place';
$route['place/del_place/(:num)']['delete']           = 'Place/del_place/$1';
$route['place/crt_place']['post']           = 'Place/crt_place';

$route['position']['get']           = 'Position';
$route['position/pos_lvl']['get']           = 'Position/pos_lvl';
$route['position/lvls']['get']           = 'Position/lvls';
$route['position/crt_pos']['post']           = 'Position/crt_pos';
$route['position/upd_pos']['post']           = 'Position/upd_pos';
$route['position/del_pos/(:num)/(:num)']['delete']           = 'Position/del_pos/$1/$2';

$route['wage/forms']['get']           = 'Wage/forms';
$route['wage/del_form/(:num)']['delete']           = 'Wage/del_form/$1';
$route['wage/wage_of_con/(:num)']['get']           = 'Wage/wage_of_con/$1';
$route['wage/wage_form_of_con/(:num)']['get']           = 'Wage/wage_form_of_con/$1';
$route['wage/wage_form/(:num)']['get']           = 'Wage/wage_form/$1';
$route['wage/crt_wage']['post']           = 'Wage/crt_wage';
$route['wage/crt_form']['post']           = 'Wage/crt_form';

$route['level']['get']           = 'Level';
$route['level/crt_lev']['post']           = 'Level/crt_lev';
$route['level/upd_lev']['post']           = 'Level/upd_lev';
$route['level/del_lev/(:num)']['delete']           = 'Level/del_lev/$1';

$route['surcharge']['get']           = 'SurchargeType';
$route['surcharge/stbm/(:num)']['get']           = 'SurchargeType/sur_by_monthid/$1';
$route['surcharge/del_sur/(:num)']['delete']           = 'SurchargeType/del_sur/$1';
$route['surcharge/crt_sur']['post']           = 'SurchargeType/crt_sur';
$route['surcharge/upd_sur']['post']           = 'SurchargeType/upd_sur';

$route['minwage']['get']           = 'MinWage';
$route['minwage/del_minwg/(:num)']['delete']           = 'MinWage/del_minwg/$1';
$route['minwage/crt_minwg']['post']           = 'MinWage/crt_minwg';
$route['minwage/upd_minwg']['post']           = 'MinWage/upd_minwg';

$route['year/yrs/(:num)/(:num)']['get']           = 'Year/all_years_of_this_yearnumber/$1/$2';

$route['hours/hrs/(:any)']['get']           = 'Hours/hrs/$1';
$route['hours/crt_hrs']['post']           = 'Hours/crt_hrs';
$route['hours/upd_hrs']['post']           = 'Hours/upd_hrs';
$route['hours/del_hrs/(:num)']['delete']           = 'Hours/del_hrs/$1';
$route['hours/emp-hrs-mnth/(:any)']['get']           = 'Hours/employee_hours_of_month/$1';

$route['hours/crt_hrs_by_emp']['post']           = 'Hours/crt_hrs_by_emp';

$route['absence/bsnc/(:any)']['get']           = 'Absence/bsnc/$1';
$route['absence/bsnc-by-mnthid/(:num)']['get']           = 'Absence/absences_by_monthid/$1';
$route['absence/crt_bsnc']['post']           = 'Absence/crt_bsnc';
$route['absence/crt_fst']['post']           = 'Absence/crt_fst';
$route['absence/del_bsnc/(:num)']['delete']           = 'Absence/del_bsnc/$1';

$route['payment/crt_pmnt']['post']           = 'Payment/crt_pmnt';
$route['payment/st_vrg_wg']['post']           = 'Payment/st_vrg_wg';
$route['payment/del_pmnt/(:num)']['delete']           = 'Payment/del_pmnt/$1';
$route['payment/lst_thr_mnths/(:num)/(:num)/(:num)']['get']           = 'Payment/lst_thr_mnths/$1/$2/$3';
$route['payment/mnth/(:num)/(:num)']['get']           = 'Payment/payments_month/$1/$2';
$route['payment/lstmnth']['get']           = 'Payment/payments_last_month';
$route['payment/nffr_mplpmnt/(:num)']['get']           = 'Payment/info_for_emplyee_payment/$1';
$route['payment/pmnt/(:num)']['get']           = 'Payment/pmnt/$1';

$route['wageconstants']['get']           = 'WageConstants';
$route['wageconstants/del_cons/(:num)']['delete']           = 'WageConstants/del_cons/$1';
$route['wageconstants/crt_cons']['post']           = 'WageConstants/crt_cons';
$route['wageconstants/upd_cons']['post']           = 'WageConstants/upd_cons';

$route['default_controller'] = 'Employee';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
