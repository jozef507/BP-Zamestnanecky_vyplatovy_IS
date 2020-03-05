<?php


class Auth extends CI_Controller
{
	public function login()
	{
		$method = $_SERVER['REQUEST_METHOD'];

		if($method != 'POST'){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
		} else {

			$check_auth_client = $this->AuthMod->check_auth_client();

			if($check_auth_client == true){
				$params = $_REQUEST;

				$username = $params['email'];
				$password = $params['password'];

				$response = $this->AuthMod->login($username,$password);
				json_output($response['status'],$response);
			}
		}
	}

	public function logout()
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'POST'){
			json_output(400,array('status' => 400,'message' => 'Bad request.'));
		} else {
			$check_auth_client = $this->AuthMod->check_auth_client();
			if($check_auth_client == true){
				$response = $this->AuthMod->logout();
				json_output($response['status'],$response);
			}
		}
	}
}
