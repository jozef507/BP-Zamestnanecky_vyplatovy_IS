<?php


class PlaceMod extends CI_Model
{
	public function get_places_names()
	{
		$query = $this->db->query("select * from pracovisko");
		return $query->result();
	}

	public function create_place()
	{
		$params = $_REQUEST;

		$place_id = null;
		$place_name = $params['name'];
		$place_town = $params['town'];
		$place_street = $params['street'];
		$place_num = $params['num'];

		$this->db->trans_start();

			$data = array(
				'nazov' => $place_name,
				'mesto' => $place_town,
				'ulica' => $place_street,
				'cislo' => $place_num
			);
			$this->db->insert('pracovisko', $data);
			$place_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'place_id' => $place_id);
		}
	}

	public function delete_place($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('pracovisko');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

}
