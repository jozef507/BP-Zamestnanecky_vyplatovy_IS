<?php


class SurchargeMod extends CI_Model
{
	public function get_surcharges()
	{
		$query = $this->db->query("select tp.*,  DATE_FORMAT(tp.platnost_od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(tp.platnost_do,'%d.%m.%Y') as nice_date2 from typ_priplatku tp order by tp.platnost_od, tp.nazov");
		return $query->result();
	}

	public function create_surcharge()
	{
		$params = $_REQUEST;
		$surcharge_id = null;
		$surcharge_name = $params['name'];
		$surcharge_part = $params['part'];
		$surcharge_base = $params['base'];
		$surcharge_from = $params['from'];
		$surcharge_to = null;


		$this->db->trans_start();

			$data = array(
				'platnost_od' => $surcharge_from,
				'platnost_do' => $surcharge_to,
				'nazov' => $surcharge_name,
				'percentualna_cast' => $surcharge_part,
				'pocitany_zo' => $surcharge_base
			);
			$this->db->insert('typ_priplatku', $data);
		$surcharge_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'surcharge_id' => $surcharge_id);
		}
	}

	public function delete_surcharge($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('typ_priplatku');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function update_surcharge()
	{
		$params = $_REQUEST;

		$minwage_id =  $params['id'];
		$minwage_to =  $params['to'];

		$this->db->trans_start();

		$data = array(
			'platnost_do' => $minwage_to
		);
		$this->db->where('id', $minwage_id)->update('typ_priplatku', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}
}
