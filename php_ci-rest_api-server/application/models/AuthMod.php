<?php


class AuthMod extends CI_Model
{
	var $client_service_jfx_desktop = "jfx-desktop-frontend-client";
	var $client_service_android_phone = "android-phone-frontend-client";
	var $auth_key       = "simplerestapi";


	public function check_auth_client(){
		$client_service = $this->input->get_request_header('Client-Service', TRUE);
		$auth_key  = $this->input->get_request_header('Auth-Key', TRUE);

		if($client_service == $this->client_service_jfx_desktop && $auth_key == $this->auth_key){
			return true;
		} else if ($client_service == $this->client_service_android_phone && $auth_key == $this->auth_key) {
			return true;
		} else {
			return array('status' => 401, 'message' => 'Unauthorized.');
		}
	}

	public function auth()
	{
		$users_id  = $this->input->get_request_header('User-ID', TRUE);
		$token     = $this->input->get_request_header('Authorization', TRUE);
		$client_service = $this->input->get_request_header('Client-Service', TRUE);
		$q  = $this->db->select('vyprsana_v')->from('autorizacia_uzivatela')->where('prihlasovacie_konto',$users_id)->where('token',$token)->where('klientska_sluzba',$client_service)->get()->row();
		if($q == ""){
			return array('status' => 401,'message' => 'Unauthorized.');
		} else {
			if($q->vyprsana_v < date('Y-m-d H:i:s')){
				return array('status' => 401,'message' => 'Your session has been expired.');
			} else {
				$updated_at = date('Y-m-d H:i:s');
				$expired_at = date("Y-m-d H:i:s", strtotime('+12 hours'));
				$this->db->where('prihlasovacie_konto',$users_id)->where('token',$token)->update('autorizacia_uzivatela',array('vyprsana_v' => $expired_at,'aktualizovana_v' => $updated_at));
				return array('status' => 200,'message' => 'Authorized.');
			}
		}
	}

	public function get_user_type()
	{
		$users_id  = $this->input->get_request_header('User-ID', TRUE);
		$q  = $this->db->select('typ_prav')->from('prihlasovacie_konto')->where('id',$users_id)->get()->row();
		return $q->typ_prav;
	}

	public function login($username,$password)
	{
		$client_service = $this->input->get_request_header('Client-Service', TRUE);
		$q  = $this->db->select('heslo,id,typ_prav,aktualne')->from('prihlasovacie_konto')->where('email',$username)->get()->row();

		if($q == ""){
			return array('status' => 403,'message' => 'Zadany nespravny prihlasovaci e-mail!');
		} else {
			$usertype = $q->typ_prav;
			$hashed_password = $q->heslo;
			$id              = $q->id;
			$is_current = $q->aktualne;
			if (hash_equals($hashed_password, $password)) {

				if($is_current==0)
					return array('status' => 403,'message' => 'Vase prihlasovacie konto je neaktualne!');

				$flag = false;
				if($usertype=="zamestnanec" && $client_service==$this->client_service_android_phone)
					$flag=true;
				elseif($usertype=="admin" && ($client_service==$this->client_service_jfx_desktop || $client_service==$this->client_service_android_phone))
					$flag=true;
				elseif($usertype=="riaditeľ" && ($client_service==$this->client_service_jfx_desktop || $client_service==$this->client_service_android_phone))
					$flag=true;
				elseif($usertype=="účtovník" && ($client_service==$this->client_service_jfx_desktop || $client_service==$this->client_service_android_phone))
					$flag=true;
				if($flag==false)
					return array('status' => 403,'message' => 'Nemate pristup do tejto aplikacie!');


				$last_login = date('Y-m-d H:i:s');
				$token = crypt(substr( md5(rand()), 0, 7), "");
				$expired_at = date("Y-m-d H:i:s", strtotime('+12 hours'));
				$this->db->trans_start();
				$this->db->where('id',$id)->update('prihlasovacie_konto',array('posledne_prihlasenie' => $last_login));
				$this->db->insert('autorizacia_uzivatela',array('prihlasovacie_konto' => $id,'token' => $token, 'klientska_sluzba' => $client_service,'vyprsana_v' => $expired_at));
				if ($this->db->trans_status() === FALSE){
					$this->db->trans_rollback();
					return array('status' => 500,'message' => 'Internal server error.');
				} else {
					$this->db->trans_commit();
					return array('status' => 200, 'message' => 'Successfully login.', 'id' => $id, 'token' => $token, 'role' => $usertype);
				}
			} else {
				return array('status' => 403,'message' => 'Zadane nespravne heslo!');
			}
		}
	}

	public function logout()
	{
		$users_id  = $this->input->get_request_header('User-ID', TRUE);
		$token     = $this->input->get_request_header('Authorization', TRUE);
		$this->db->where('prihlasovacie_konto',$users_id)->where('token',$token)->delete('autorizacia_uzivatela');
		return array('status' => 200,'message' => 'Successfully logout.');
	}




}
