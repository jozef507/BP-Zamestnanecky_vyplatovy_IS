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
$route['employee/update']['post']           = 'employee/update';
$route['employee/update_imp']['post']           = 'employee/update_imp';
$route['employee/detail/(:num)']['get']           = 'employee/detail/$1';
$route['employee/relations_places_details/(:num)']['get']		 = 'employee/relations_places_details/$1';
$route['employee/emp_usr_imp/(:num)']['get'] 		= 'employee/emp_usr_imp/$1';
$route['employee/emp_rel_ov/(:num)']['get'] 		= 'employee/emp_rel_ov/$1';
$route['employee/count_imp_pay/(:num)']['get']		 = 'employee/count_imp_pay/$1';
$route['employee/all_imp/(:num)']['get'] 		= 'employee/all_imp/$1';
$route['employee/delete_imp/(:num)']['delete'] 			= 'employee/delete_imp/$1';

$route['relation/detail/(:num)']['get']           = 'relation/detail/$1';
$route['relation/nxt_dmnds_d/(:num)']['get']           = 'relation/nxt_dmnds_d/$1';
$route['relation/con_ncon_po_pl/(:num)']['get']           = 'relation/con_ncon_po_pl/$1';
$route['relation/crt_rel']['post']           = 'relation/crt_rel';
$route['relation/upd_rel']['post']           = 'relation/upd_rel';
$route['relation/del_rel/(:num)']['delete']           = 'relation/del_rel/$1';
$route['relation/del_cons/(:num)']['delete']           = 'relation/del_cons/$1';

$route['place']['get']           = 'place';

$route['position']['get']           = 'position';

$route['wage/forms']['get']           = 'wage/forms';
$route['wage/wage_of_con/(:num)']['get']           = 'wage/wage_of_con/$1';
$route['wage/crt_wage']['post']           = 'wage/crt_wage';

$route['default_controller'] = 'Employee';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
