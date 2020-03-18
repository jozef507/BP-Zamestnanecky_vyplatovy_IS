<?php


class LevyMod extends CI_Model
{
	public function get_levies()
	{
		$query = $this->db->query("select t.*,  DATE_FORMAT(t.platnost_od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(t.platnost_do,'%d.%m.%Y') as nice_date2 from typ_odvodu t order by t.platnost_od, t.nazov");
		return $query->result();
	}

	public function create_levy()
	{
		$params = $_REQUEST;
		$levy_id = null;
		$levy_name = $params['name'];
		$levy_part_employee = $params['part_employee'];
		$levy_part_employer = $params['part_employer'];
		$levy_from = $params['from'];
		$levy_to = null;

		$this->db->trans_start();

			$data = array(
				'platnost_od' => $levy_from,
				'platnost_do' => $levy_to,
				'nazov' => $levy_name,
				'percentualna_cast_zamestnanec' => $levy_part_employee,
				'percentualna_cast_zamestnavatel' => $levy_part_employer
			);
			$this->db->insert('typ_odvodu', $data);
			$levy_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'levy_id' => $levy_id);
		}
	}

	public function delete_levy($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('typ_odvodu');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function update_levy()
	{
		$params = $_REQUEST;

		$minwage_id =  $params['id'];
		$minwage_to =  $params['to'];

		$this->db->trans_start();

		$data = array(
			'platnost_do' => $minwage_to
		);
		$this->db->where('id', $minwage_id)->update('typ_odvodu', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}
}
