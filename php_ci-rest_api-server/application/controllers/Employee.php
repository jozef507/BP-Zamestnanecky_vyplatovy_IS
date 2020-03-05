<?php


class Employee extends CI_Controller
{

	public function index()
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET'){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_all_employees();
		json_output(200,$resp);
	}

	public function detail($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_employee_detail($id);
		json_output(200,$resp);
	}

	public function relations_places_details($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_relations_places_details($id);
		json_output(200,$resp);

	}

	public function emp_usr_imp($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_emp_usr_imp($id);
		json_output(200,$resp);
	}

	public function emp_rel_ov($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_emp_rel_ov($id);
		json_output(200,$resp);
	}

	public function count_imp_pay($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_count_imp_pay($id);
		json_output(200,$resp);
	}

	public function all_imp($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->get_all_imp($id);
		json_output(200,$resp);
	}

	public function update()
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'POST'){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->update_employee();
		json_output($resp['status'],$resp);
	}

	public function update_imp()
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'POST'){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->update_employee_important();
		json_output($resp['status'],$resp);
	}

	public function delete_imp($id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'DELETE' || $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
			return;
		}

		$check_auth_client = $this->AuthMod->check_auth_client();
		if($check_auth_client != true) {
			json_output($check_auth_client['status'], $check_auth_client);
			return;
		}
		$response = $this->AuthMod->auth();
		if($response['status'] != 200){
			json_output($response['status'], $response);
			return;
		}
		$usertype = $this->AuthMod->get_user_type();
		if(!($usertype=="riaditeľ" || $usertype=="učtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->EmployeeMod->delete_employee_important($id);
		json_output($resp['status'],$resp);
	}

}
