<?php

class PositionMod extends CI_Model
{
	public function get_positions()
	{
		$query = $this->db->query("select po.*, pr.nazov as nazov2 from pozicia po join pracovisko pr on po.pracovisko=pr.id");
		return $query->result();
	}

	public function get_positions_level()
	{
		$query = $this->db->query("select po.*, pr.nazov as pr_nazov, sn.id as sn_id, sn.cislo_stupna from pracovisko pr join pozicia po on pr.id = po.pracovisko join pozicia_stupen_narocnosti psn on po.id = psn.pozicia join stupen_narocnosti sn on psn.stupen_narocnosti = sn.id where sn.platnost_od <= now() and sn.platnost_do>= now() or sn.platnost_do is null");
		return $query->result();
	}

	public function get_levels()
	{

		$query = $this->db->query("select sn.* from stupen_narocnosti sn where sn.platnost_do>= now() or sn.platnost_do is null");
		return $query->result();
	}

	public function create_position()
	{
		$params = $_REQUEST;

		$position_id = null;
		$position_name = $params['name'];
		$position_caracteristic = $params['caracteristic'];
		$place_id = $params['placeid'];
		$level_id = $params['levelid'];

		$this->db->trans_start();

			$data = array(
				'nazov' => $position_name,
				'charakteristika' => $position_caracteristic,
				'pracovisko' => $place_id
			);
			$this->db->insert('pozicia', $data);
			$position_id = $this->db->insert_id();

			$data = array(
				'pozicia' => $position_id,
				'stupen_narocnosti' => $level_id
			);
			$this->db->insert('pozicia_stupen_narocnosti', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

	public function update_position()
	{
		$params = $_REQUEST;

		$position_id = $params['positionid'];
		$level_id = $params['levelid'];

		$this->db->trans_start();

			$data = array(
				'pozicia' => $position_id,
				'stupen_narocnosti' => $level_id
			);
			$this->db->insert('pozicia_stupen_narocnosti', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

	public function delete_position($pos_id, $lvl_id)
	{
		$this->db->trans_start();

			$this->db->where('pozicia', $pos_id)->where('stupen_narocnosti', $lvl_id)->delete('pozicia_stupen_narocnosti');
			$this->db->where('id', $pos_id)->delete('pozicia');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

}
