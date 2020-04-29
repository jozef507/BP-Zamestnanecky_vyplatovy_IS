<?php


class Payment extends CI_Controller
{
	public function payments_last_month()
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

		$resp = $this->PaymentMod->get_payments_last_month();
		json_output(200,$resp);
	}

	public function payments_month($year, $month)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET'|| $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE
			|| $this->uri->segment(4) == '' || is_numeric($this->uri->segment(4)) == FALSE){
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

		$resp = $this->PaymentMod->get_payments_month($year, $month);
		json_output(200,$resp);
	}

	public function info_for_emplyee_payment($month_id)
	{
		$method = $_SERVER['REQUEST_METHOD'];
		if($method != 'GET'|| $this->uri->segment(3) == '' || is_numeric($this->uri->segment(3)) == FALSE){
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

		$resp = $this->PaymentMod->get_info_for_emplyee_payment($month_id);
		json_output(200,$resp);
	}
}
