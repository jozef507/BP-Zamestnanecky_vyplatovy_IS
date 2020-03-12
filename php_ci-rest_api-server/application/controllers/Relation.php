<?php


class Relation extends CI_Controller
{
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

		$resp = $this->RelationMod->get_relation($id);
		json_output(200,$resp);
	}

	public function con_ncon_po_pl($id)
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

		$resp = $this->RelationMod->get_con_ncon_po_pl($id);
		json_output(200,$resp);
	}

	public function nxt_dmnds_d($id)
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

		$resp = $this->RelationMod->get_next_demands_detail($id);
		json_output(200,$resp);
	}

	public function crt_rel()
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

		$resp = $this->RelationMod->create_relation();
		json_output($resp['status'],$resp);
	}

	public function upd_rel()
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

		$resp = $this->RelationMod->update_relation();
		json_output($resp['status'],$resp);
	}

	public function del_rel($id)
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

		$resp = $this->RelationMod->delete_relation($id);
		json_output($resp['status'],$resp);
	}

	public function del_cons($id)
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

		$resp = $this->RelationMod->delete_conditions($id);
		json_output($resp['status'],$resp);
	}


}
