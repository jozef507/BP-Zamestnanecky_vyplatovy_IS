<?php


class Level extends CI_Controller
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
		if(!($usertype=="riaditeľ" || $usertype=="účtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->LevelMod->get_levels();
		json_output(200,$resp);
	}

	public function crt_lev()
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
		if(!($usertype=="riaditeľ" || $usertype=="účtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->LevelMod->create_level();
		json_output($resp['status'],$resp);
	}

	public function upd_lev()
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
		if(!($usertype=="riaditeľ" || $usertype=="účtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->LevelMod->update_level();
		json_output($resp['status'],$resp);
	}

	public function del_lev($lvl_id)
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
		if(!($usertype=="riaditeľ" || $usertype=="účtovník"))		{
			json_output(403,array('status' => 403,'message' => 'Forbidden'));
			return;
		}

		$resp = $this->LevelMod->delete_level($lvl_id);
		json_output($resp['status'],$resp);
	}



}
