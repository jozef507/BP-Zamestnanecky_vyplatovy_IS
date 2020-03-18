<?php


class WageMod extends CI_Model
{
	public function get_wage_forms()
	{
		$query = $this->db->query("select * from forma_mzdy");
		return $query->result();
	}

	public function get_wage_by_conditions($id)
	{
		$query = $this->db->query("select * from zakladna_mzda where podmienky_pracovneho_vztahu=".$id);
		return $query->result_array();
	}

	public function get_wage_of_conditions($id)
	{
		$query = $this->db->query("select * from zakladna_mzda zm where zm.podmienky_pracovneho_vztahu = ".$id);
		return $query->result_array();
	}

	public function delete_form($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('forma_mzdy');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function create_wage()
	{
		$params = $_REQUEST;
		$wage_id = null;
		$wage_label = $params['label'];
		$wage_employee = $params['employee'];
		$wage_time = $params['time'];
		$wage_tarif = $params['tarif'];
		$wage_way = $params['way'];
		if($params['paydate']=='NULL')
			$wage_paydate = null;
		else
			$wage_paydate = $params['paydate'];
		$wage_form_id = $params['formid'];
		$wage_con_id = $params['conid'];

		$rel_id = $params['relid'];
		$con_id = $params['conid'];
		$nextcon_id = $params['nextconid'];

		$this->db->trans_start();

			$data = array(
				'popis' => $wage_label,
				'vykon_eviduje_zamestnanec' => $wage_employee,
				'nutne_evidovanie_casu' => $wage_time,
				'tarifa_za_jednotku_mzdy' => $wage_tarif,
				'sposob_vyplacania' => $wage_way,
				'datum_vyplatenia' => $wage_paydate,
				'forma_mzdy' => $wage_form_id,
				'podmienky_pracovneho_vztahu' => $wage_con_id
			);
			$this->db->insert('zakladna_mzda', $data);
			$wage_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();

			$wage_array = $this->get_wage_by_conditions($con_id);
			foreach ($wage_array as $row)
			{
				$this->db->where('id',$row['id'])->delete('zakladna_mzda');
			}

			$this->db->where('id',$con_id)->delete('podmienky_pracovneho_vztahu');
			if($nextcon_id!='NULL')
				$this->db->where('id',$nextcon_id)->delete('dalsie_podmienky');
			if($rel_id!="NULL")
				$this->db->where('id',$rel_id)->delete('pracovny_vztah');

			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'wage_id' => $wage_id);
		}
	}

	public function create_form()
	{
		$params = $_REQUEST;

		$form_id = null;
		$form_name = $params['name'];
		$form_unit = $params['unit'];
		$form_unitshort = $params['unitshort'];

		$this->db->trans_start();

			$data = array(
				'nazov' => $form_name,
				'jednotka_vykonu' => $form_unit,
				'skratka_jednotky' => $form_unitshort
			);
			$this->db->insert('forma_mzdy', $data);
			$form_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'form_id' => $form_id);
		}
	}

}
